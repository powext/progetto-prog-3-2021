package it.tweetmail.client.model.modules.observables;

public final class ObservablesManagerFactory {
    public static <T> ObservablesManager<T> newInstance() {
        return new ObservablesManagerImpl<>();
    }
}
