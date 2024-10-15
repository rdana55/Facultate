package ro.iss.ma.observer;

import ro.iss.ma.event.Event;

public interface Observer<E extends Event>{
    void update(E e);
}

