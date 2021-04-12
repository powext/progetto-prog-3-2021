package it.tweetmail.server.modules.config;

public final class ConfigManagerFactory {
  public static ConfigManager newInstance() { return new ConfigManagerImpl(); }
}
