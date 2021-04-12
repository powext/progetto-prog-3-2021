package it.tweetmail.server.server.session.commands.mailbox;

import it.tweetmail.lib.enums.MailFolder;
import it.tweetmail.lib.enums.ServerResponse;
import it.tweetmail.lib.objects.User;
import it.tweetmail.server.Main;
import it.tweetmail.server.server.session.Session;
import it.tweetmail.server.server.session.commands.CommandExecutor;

import java.util.List;
import java.util.concurrent.locks.Lock;

public class DeleteMailCmd implements CommandExecutor {
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

        boolean opResult;

        User loggedUser = session.getUser();
        Lock writeLock = Main.appModel().synchronizeManager().getWriteLock(loggedUser.getEmail());

        try {
            writeLock.lock();

            if (folder == MailFolder.TRASH) {
                opResult = Main.appModel().databaseManager().deleteMail(loggedUser.getEmail(), folder, params.get(1));
            } else {
                opResult = Main.appModel().databaseManager().moveMail(loggedUser.getEmail(), folder, MailFolder.TRASH, params.get(1));
            }
        } finally {
            writeLock.unlock();
        }

        session.sendResponse(opResult ? ServerResponse.OK : ServerResponse.IO_ERROR);
    }
}
