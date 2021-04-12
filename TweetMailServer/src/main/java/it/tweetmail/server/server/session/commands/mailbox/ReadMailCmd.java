package it.tweetmail.server.server.session.commands.mailbox;

import it.tweetmail.lib.enums.MailFolder;
import it.tweetmail.lib.enums.ServerResponse;
import it.tweetmail.lib.objects.Email;
import it.tweetmail.lib.objects.User;
import it.tweetmail.lib.utils.Serializer;
import it.tweetmail.server.Main;
import it.tweetmail.server.server.session.Session;
import it.tweetmail.server.server.session.commands.CommandExecutor;

import java.util.List;
import java.util.concurrent.locks.Lock;

public class ReadMailCmd implements CommandExecutor {
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

        User loggedUser = session.getUser();
        Lock readLock = Main.appModel().synchronizeManager().getReadLock(loggedUser.getEmail());

        Email email;
        try {
            readLock.lock();
            email = Main.appModel().databaseManager().readMail(loggedUser.getEmail(), folder, params.get(1));
        } finally {
            readLock.unlock();
        }

        if (email == null) {
            session.sendResponse(ServerResponse.RESOURCE_NOT_FOUND);
            return;
        }

        if (!email.hasBeenRead()) {
            Lock writeLock = Main.appModel().synchronizeManager().getWriteLock(loggedUser.getEmail());
            email.setToRead(false);

            try {
                writeLock.lock();
                Main.appModel().databaseManager().saveMail(loggedUser.getEmail(), folder, email);
            } finally {
                writeLock.unlock();
            }
        }

        session.sendResponse(ServerResponse.OK, Serializer.writeToB64(email));
    }
}
