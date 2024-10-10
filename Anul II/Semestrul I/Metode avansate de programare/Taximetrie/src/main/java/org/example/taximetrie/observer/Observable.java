package org.example.taximetrie.observer;

import org.example.taximetrie.events.Event;

public interface Observable<E extends Event>{
    void addObserver(Observer<E> e);
    void removeObserver(Observer<E> e);
    void notifyObserver(E t);
}
