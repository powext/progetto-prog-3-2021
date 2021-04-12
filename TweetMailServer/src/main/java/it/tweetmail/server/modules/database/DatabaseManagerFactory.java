package it.tweetmail.server.modules.database;

public final class DatabaseManagerFactory {
    public static DatabaseManager newInstance(String dbPath) {
        return new DatabaseManagerImpl(dbPath);
    }
}
