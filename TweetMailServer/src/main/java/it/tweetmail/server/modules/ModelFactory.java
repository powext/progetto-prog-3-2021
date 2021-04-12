package it.tweetmail.server.modules;

public final class ModelFactory {

  public static Model newInstance() {
    return new ModelImpl();
  }
}
