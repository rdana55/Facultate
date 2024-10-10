package org.example.taximetrie.observer;

import org.example.taximetrie.events.Event;

public interface Observer<E extends Event>{
    void update(E e);
}
