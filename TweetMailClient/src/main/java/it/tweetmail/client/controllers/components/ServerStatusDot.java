package it.tweetmail.client.controllers.components;

import it.tweetmail.client.Main;
import it.tweetmail.client.model.modules.observables.Observables;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ServerStatusDot {
    public static void bind(Circle serverStatusCircle) {
        SimpleObjectProperty<Boolean> serverStatusDot = Main.appModel().observablesManager().getObject(Observables.SERVER_STATUS);
        serverStatusDot.addListener((observable, oldValue, newValue) -> setStatusCircle(serverStatusCircle, newValue));
        setStatusCircle(serverStatusCircle, serverStatusDot.getValue());
    }

    private static void setStatusCircle(Circle serverStatusCircle, boolean value) {
        serverStatusCircle.setFill(value ? Color.GREEN : Color.RED);
        String toolTipText = "Server " + (value ? "ONLINE" : "OFFLINE");
        Tooltip.install(serverStatusCircle, new Tooltip(toolTipText));
    }

}
