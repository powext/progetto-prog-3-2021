package it.tweetmail.server.server;

public class ServerFactory {
    public static Server newInstance() { return new ServerImpl(); }
}
