package it.tweetmail.client.controllers.components;

import it.tweetmail.client.Main;
import it.tweetmail.client.model.modules.observables.Observables;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Label;

public class ClientStatusLabel {
    public static void bind(Label clientStatusLabel) {
        SimpleObjectProperty<String> serverStatusString = Main.appModel().observablesManager().getObject(Observables.STATUS_LABEL);
        clientStatusLabel.textProperty().bind(serverStatusString);
    }
}
