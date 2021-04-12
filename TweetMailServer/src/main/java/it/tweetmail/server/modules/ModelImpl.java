package it.tweetmail.server.modules;

import it.tweetmail.server.modules.config.ConfigManager;
import it.tweetmail.server.modules.config.ConfigManagerFactory;
import it.tweetmail.server.modules.database.DatabaseManager;
import it.tweetmail.server.modules.database.DatabaseManagerFactory;
import it.tweetmail.server.modules.obervables.ObservablesManager;
import it.tweetmail.server.modules.obervables.ObservablesManagerFactory;
import it.tweetmail.server.modules.synchronize.SynchronizeManager;
import it.tweetmail.server.modules.synchronize.SynchronizeManagerFactory;

class ModelImpl implements Model {

  private final ConfigManager configManager;
  private final DatabaseManager databaseManager;
  private final ObservablesManager<String> observablesManager;
  private final SynchronizeManager<String> synchronizeManager;

  ModelImpl() {
    observablesManager = ObservablesManagerFactory.newInstance();
    synchronizeManager = SynchronizeManagerFactory.newInstance();
    configManager = ConfigManagerFactory.newInstance();
    databaseManager = DatabaseManagerFactory.newInstance(configManager.read("db.path"));
  }

  @Override
  public ConfigManager configManager() {
    return configManager;
  }

  @Override
  public DatabaseManager databaseManager() { return databaseManager; }

  @Override
  public ObservablesManager<String> observablesManager() {
    return observablesManager;
  }

  @Override
  public SynchronizeManager<String> synchronizeManager() {
    return synchronizeManager;
  }
}
