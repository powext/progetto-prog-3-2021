package it.tweetmail.server.server.session.commands.mailbox;

import it.tweetmail.lib.enums.MailFolder;
import it.tweetmail.lib.enums.ServerResponse;
import it.tweetmail.lib.objects.Email;
import it.tweetmail.lib.objects.User;
import it.tweetmail.lib.utils.Serializer;
import it.tweetmail.server.Main;
import it.tweetmail.server.server.session.Session;
import it.tweetmail.server.server.session.commands.CommandExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

public class ListMailsCmd implements CommandExecutor {
    @Override
    public void handle(Session session, List<String> params) {
        if (!session.isLoggedIn()) {
            session.sendResponse(ServerResponse.ANONYMOUS_SESSION);
            return;
        }

        MailFolder folder = MailFolder.fromString(params.get(0));
        if (folder == null) {
            session.sendResponse(ServerResponse.UNKNOWN);
            return;
        }

        ArrayList<Email> emails;

        User loggedUser = session.getUser();
        Lock readLock = Main.appModel().synchronizeManager().getReadLock(loggedUser.getEmail());

        try {
            readLock.lock();
            emails = Main.appModel().databaseManager().listAllEmails(loggedUser.getEmail(), folder);
        } finally {
            readLock.unlock();
        }

        session.sendResponse(ServerResponse.OK, Serializer.writeToB64(emails));
    }
}
