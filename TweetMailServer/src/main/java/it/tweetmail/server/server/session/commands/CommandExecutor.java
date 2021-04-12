package it.tweetmail.server.server.session.commands;

import it.tweetmail.server.server.session.Session;

import java.util.List;

public interface CommandExecutor {
    void handle(Session session, List<String> params);
}
