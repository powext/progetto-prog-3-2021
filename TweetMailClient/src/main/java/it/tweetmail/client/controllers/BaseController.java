package it.tweetmail.client.controllers;

import it.tweetmail.client.Main;
import it.tweetmail.client.model.modules.client.ClientMail;
import it.tweetmail.client.model.modules.observables.Observables;
import it.tweetmail.client.model.modules.observables.ObservablesManager;
import it.tweetmail.client.scene.SceneSwitcher;
import it.tweetmail.lib.enums.MailFolder;
import javafx.fxml.Initializable;

public abstract class BaseController implements Initializable {
    protected static String waitingForInput = "Waiting for input...";
    private static String currentUser = null;

    protected static String getCurrentUser() {
        if (currentUser == null)
            currentUser = Main.appModel().configManager().read("user.email");
        return currentUser;
    }

    protected static MailFolder getCurrentFolder() {
        return (MailFolder) observablesManager().getObject(Observables.CURRENT_FOLDER).getValue();
    }

    protected static SceneSwitcher sceneSwitcher() {
        return SceneSwitcher.getInstance();
    }

    protected static ObservablesManager<Observables> observablesManager() {
        return Main.appModel().observablesManager();
    }

    protected static ClientMail clientMail() {
        return Main.appModel().clientMail();
    }

    protected static void setStatusLabel(String string) {
        observablesManager().setObjectValue(Observables.STATUS_LABEL, string);
    }
}
