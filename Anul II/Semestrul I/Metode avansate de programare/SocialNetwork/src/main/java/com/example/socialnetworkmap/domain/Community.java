package com.example.socialnetworkmap.domain;

import java.util.*;

import com.example.socialnetworkmap.domain.Friendship;

public class Community {
    private Set<User> users = new HashSet<>();
    private Set<Friendship> friendships = new HashSet<>();

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public void addFriendship(Friendship friendship) {
        friendships.add(friendship);
    }

    public void removeFriendship(Friendship friendship) {
        friendships.remove(friendship);
    }

}