package com.example.socialnetworkmap.observer;

import com.example.socialnetworkmap.events.Event;

public interface Observer<E extends Event>{
    void update(E e);
}
