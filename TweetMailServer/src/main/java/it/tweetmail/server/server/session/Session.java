package it.tweetmail.server.server.session;

import it.tweetmail.lib.enums.ServerResponse;
import it.tweetmail.lib.objects.User;
import org.json.JSONObject;

public interface Session extends Runnable {
    boolean setUser(JSONObject auth);

    boolean isLoggedIn();

    User getUser();

    void sendResponse(ServerResponse serverResponse, String... params);

    void close();
}
