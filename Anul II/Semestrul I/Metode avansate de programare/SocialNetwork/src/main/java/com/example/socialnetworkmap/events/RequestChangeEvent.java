package com.example.socialnetworkmap.events;

import com.example.socialnetworkmap.domain.FriendshipRequest;
import com.example.socialnetworkmap.domain.User;

import java.util.Optional;

public class RequestChangeEvent implements Event {
    public enum RequestChangeType {
        REQUEST_SENT, REQUEST_ACCEPTED, REQUEST_REJECTED
    }

    private final RequestChangeType type;
    private final Optional<FriendshipRequest> request;
    //private final Optional<FriendshipRequest> old = null;

    public RequestChangeEvent(RequestChangeType type, Optional<FriendshipRequest> request) {
        this.type = type;
        this.request = request;
    }

//    public RequestChangeEvent(RequestChangeType type, Optional<FriendshipRequest> request, Optional<FriendshipRequest> old) {
//        this.type = type;
//        this.request = request;
//        this.old = old;
//    }

    public RequestChangeType getType() {
        return type;
    }

    public Optional<FriendshipRequest> getRequest() {
        return request;
    }

//    public Optional<FriendshipRequest> getOld() {
//        return old;
//    }
}