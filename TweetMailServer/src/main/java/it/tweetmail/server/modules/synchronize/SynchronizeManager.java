package it.tweetmail.server.modules.synchronize;

import java.util.concurrent.locks.Lock;

public interface SynchronizeManager<T> {
    void createLock(T name);

    Lock getWriteLock(T name);

    Lock getReadLock(T name);

    void deleteLock(T name);
}
