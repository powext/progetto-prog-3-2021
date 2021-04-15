package it.tweetmail.client.model;

import it.tweetmail.client.model.modules.client.ClientMail;
import it.tweetmail.client.model.modules.client.ClientMailFactory;
import it.tweetmail.client.model.modules.config.ConfigManager;
import it.tweetmail.client.model.modules.config.ConfigManagerFactory;
import it.tweetmail.client.model.modules.observables.Observables;
import it.tweetmail.client.model.modules.observables.ObservablesManager;
import it.tweetmail.client.model.modules.observables.ObservablesManagerFactory;

class ModelImpl implements Model {
    private final ObservablesManager<Observables> observablesManager;
    private final ConfigManager configManager;
    private final ClientMail clientMail;

    public ModelImpl() {
        observablesManager = ObservablesManagerFactory.newInstance();
        configManager = ConfigManagerFactory.newInstance();
        clientMail = ClientMailFactory.newInstance(configManager);
    }

    @Override
    public ConfigManager configManager() {
        return configManager;
    }

    @Override
    public ObservablesManager<Observables> observablesManager() {
        return observablesManager;
    }

    @Override
    public ClientMail clientMail() {
        return clientMail;
    }
}

