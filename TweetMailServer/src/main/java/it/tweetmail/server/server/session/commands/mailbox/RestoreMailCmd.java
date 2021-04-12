package it.tweetmail.server.server.session.commands.mailbox;

import it.tweetmail.lib.enums.MailFolder;
import it.tweetmail.lib.enums.ServerResponse;
import it.tweetmail.lib.objects.Email;
import it.tweetmail.lib.objects.User;
import it.tweetmail.server.Main;
import it.tweetmail.server.server.session.Session;
import it.tweetmail.server.server.session.commands.CommandExecutor;

import java.util.List;
import java.util.concurrent.locks.Lock;

public class RestoreMailCmd implements CommandExecutor {
    @Override
    public void handle(Session session, List<String> params) {
        if (!session.isLoggedIn()) {
            session.sendResponse(ServerResponse.ANONYMOUS_SESSION);
            return;
        }

        User loggedUser = session.getUser();
        Lock readLock = Main.appModel().synchronizeManager().getReadLock(loggedUser.getEmail());

        Email emailToMove;
        try {
            readLock.lock();
            emailToMove = Main.appModel().databaseManager().readMail(loggedUser.getEmail(), MailFolder.TRASH, params.get(0));
        } finally {
            readLock.unlock();
        }

        if (emailToMove == null) {
            session.sendResponse(ServerResponse.RESOURCE_NOT_FOUND);
            return;
        }

        MailFolder folderTo = (emailToMove.getSender().equals(loggedUser.getEmail())) ? MailFolder.OUTBOX : MailFolder.INBOX;

        boolean moved;
        try {
            readLock.lock();
            moved = Main.appModel().databaseManager().moveMail(loggedUser.getEmail(), MailFolder.TRASH, folderTo, emailToMove.getUUID());
        } finally {
            readLock.unlock();
        }

        if (moved) session.sendResponse(ServerResponse.OK);
        else session.sendResponse(ServerResponse.IO_ERROR);
    }
}
