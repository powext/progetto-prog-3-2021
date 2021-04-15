package it.tweetmail.client.scene;

public enum AppScene {
    MAIN,
    READ,
    COMPOSE;

    @Override
    public String toString() {
        return super.name().toLowerCase();
    }
}

