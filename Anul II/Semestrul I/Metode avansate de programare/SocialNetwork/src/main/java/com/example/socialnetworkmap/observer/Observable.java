package com.example.socialnetworkmap.observer;

import com.example.socialnetworkmap.events.Event;
public interface Observable<E extends Event>{
    void addObserver(Observer<E> e);
    void removeObserver(Observer<E> e);
    void notifyObserver(E t);
}
