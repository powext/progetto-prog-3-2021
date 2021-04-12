package it.tweetmail.server.modules.database;

import it.tweetmail.lib.enums.MailFolder;
import it.tweetmail.lib.objects.Email;
import it.tweetmail.lib.objects.User;

import java.util.ArrayList;

public interface DatabaseManager {
    boolean saveUser(User user);

    boolean checkUser(String user);

    User readUser(String user);

    boolean saveMail(String user, MailFolder folder, Email email);

    Email readMail(String user, MailFolder folder, String emailUUID);

    boolean moveMail(String user, MailFolder from, MailFolder to, String emailUUID);

    boolean deleteMail(String user, MailFolder folder, String emailUUID);

    ArrayList<Email> listAllEmails(String user, MailFolder folder);

    ArrayList<Email> listEmails(String user, MailFolder folder, int from, int offset);
}
