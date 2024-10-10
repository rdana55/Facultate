package com.example.socialnetworkmap.events;

import com.example.socialnetworkmap.domain.User;

public class UserStatusEvent implements Event {
    private ExecutionStatusType type;
    private User user;

    public UserStatusEvent(ExecutionStatusType type, User user) {
        this.type = type;
        this.user = user;
    }

    public User getUser(){return user;}
    public void setUser(User user){this.user=user;}
    public ExecutionStatusType getType(){return type;}
    public void setType(ExecutionStatusType type){this.type=type;}
}
