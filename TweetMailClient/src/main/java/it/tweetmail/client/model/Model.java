package it.tweetmail.client.model;

import it.tweetmail.client.model.modules.client.ClientMail;
import it.tweetmail.client.model.modules.config.ConfigManager;
import it.tweetmail.client.model.modules.observables.Observables;
import it.tweetmail.client.model.modules.observables.ObservablesManager;

public interface Model {
    ConfigManager configManager();

    ObservablesManager<Observables> observablesManager();

    ClientMail clientMail();
}
