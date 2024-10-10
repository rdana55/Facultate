package com.example.socialnetworkmap.domain;

public class FriendshipRequest extends Entity<Long>{
    private User sender;
    private User receiver;
    private FriendshipStatus status;

    public FriendshipRequest(User sender, User receiver, FriendshipStatus status) {
        this.sender = sender;
        this.receiver = receiver;
        this.status = status;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver(){
        return receiver;
    }

    public FriendshipStatus getStatus() {
        return status;
    }

    public void setStatus(FriendshipStatus status) {
        this.status = status;
    }
}

