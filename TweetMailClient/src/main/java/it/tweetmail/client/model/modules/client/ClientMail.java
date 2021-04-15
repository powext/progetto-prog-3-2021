package it.tweetmail.client.model.modules.client;

import it.tweetmail.lib.enums.Command;
import it.tweetmail.lib.enums.ServerResponse;
import javafx.beans.property.SimpleObjectProperty;

import java.util.List;

public interface ClientMail {
    void bindServerStatus(SimpleObjectProperty<Boolean> serverStatus);

    void sendCmd(Command command, ResponseHandler responseHandler, String... params);

    void close();

    @FunctionalInterface
    interface ResponseHandler {
        void run(ServerResponse response, List<String> args);
    }
}
