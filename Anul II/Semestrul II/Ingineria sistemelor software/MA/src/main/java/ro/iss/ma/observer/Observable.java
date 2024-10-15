package ro.iss.ma.observer;

import ro.iss.ma.event.Event;

public interface Observable<E extends Event>{
    void addObserver(Observer<E> e);
    void removeObserver(Observer<E> e);
    void notifyObserver(E t);
}
