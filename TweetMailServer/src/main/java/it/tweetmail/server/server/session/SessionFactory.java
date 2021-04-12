package it.tweetmail.server.server.session;

import java.net.Socket;

public final class SessionFactory {
    public static Session newInstance(Socket connection) {
        return new SessionImpl(connection);
    }
}
