package com.example.socialnetworkmap.events;

import com.example.socialnetworkmap.domain.User;

import java.util.Optional;

public class UserChangeEvent implements Event{
    private EventType type;
    private Optional<User> user;
    private Optional<User> oldUser;

    public UserChangeEvent(EventType type, Optional<User> user) {
        this.type = type;
        this.user = user;
    }

    public UserChangeEvent(EventType type, Optional<User> user, Optional<User> oldUser) {
        this.type = type;
        this.user = user;
        this.oldUser = oldUser;
    }

    public EventType getType(){return type;}
    public Optional<User> getUser(){return user;}
    public Optional<User> getOldUser(){return oldUser;}
}
