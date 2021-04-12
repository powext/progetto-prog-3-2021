package it.tweetmail.server.modules;

import it.tweetmail.server.modules.config.ConfigManager;
import it.tweetmail.server.modules.database.DatabaseManager;
import it.tweetmail.server.modules.obervables.ObservablesManager;
import it.tweetmail.server.modules.synchronize.SynchronizeManager;

public interface Model {
  ConfigManager configManager();

  DatabaseManager databaseManager();

  ObservablesManager<String> observablesManager();

  SynchronizeManager<String> synchronizeManager();
}
