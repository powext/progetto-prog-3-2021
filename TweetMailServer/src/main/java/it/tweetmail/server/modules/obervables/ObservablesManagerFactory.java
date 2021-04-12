package it.tweetmail.server.modules.obervables;

public final class ObservablesManagerFactory {
    public static <T> ObservablesManager<T> newInstance() {
        return new ObservablesManagerImpl<>();
    }
}
