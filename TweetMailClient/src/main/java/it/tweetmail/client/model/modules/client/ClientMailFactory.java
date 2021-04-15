package it.tweetmail.client.model.modules.client;

import it.tweetmail.client.model.modules.config.ConfigManager;

public final class ClientMailFactory {
    public static ClientMail newInstance(ConfigManager configManager) {
        return new ClientMailImpl(configManager);
    }
}

