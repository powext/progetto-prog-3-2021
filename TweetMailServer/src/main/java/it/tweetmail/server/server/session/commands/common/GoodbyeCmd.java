package it.tweetmail.server.server.session.commands.common;

import it.tweetmail.lib.enums.ServerResponse;
import it.tweetmail.server.server.session.Session;
import it.tweetmail.server.server.session.commands.CommandExecutor;

import java.util.List;

public class GoodbyeCmd implements CommandExecutor {
    @Override
    public void handle(Session session, List<String> params) {
        session.sendResponse(ServerResponse.OK, "ByeBye");
        session.close();
    }
}

