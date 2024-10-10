package com.example.socialnetworkmap.service;

import com.example.socialnetworkmap.domain.Friendship;
import com.example.socialnetworkmap.domain.User;
import com.example.socialnetworkmap.repository.FriendshipDbRepo;
import com.example.socialnetworkmap.repository.UserDbRepo;

import java.util.Optional;

public class dbService {
    private UserDbRepo userDbRepo;
    private FriendshipDbRepo friendshipDbRepo;
    private UserService serviceU;
    public dbService(UserDbRepo userDbRepo, FriendshipDbRepo friendshipDbRepo, UserService serviceU) {
        this.userDbRepo = userDbRepo;
        this.friendshipDbRepo = friendshipDbRepo;
        this.serviceU = serviceU;

        loadUsersAndFriendsFromDB();
    }

    private void loadUsersAndFriendsFromDB(){
        Iterable<User> users=userDbRepo.findAll();
        users.forEach(user->{
            addUser(user);
        });

        Iterable<Friendship> friendships=friendshipDbRepo.findAll();
        friendships.forEach(friendship -> {
            addFriendship(friendship);
            serviceU.findUserById(friendship.getId().getLeft()).get().addFriend(serviceU.findUserById(friendship.getId().getRight()).get());
            serviceU.findUserById(friendship.getId().getRight()).get().addFriend(serviceU.findUserById(friendship.getId().getLeft()).get());
        });
    }

    public void addUser(User user) {
        Optional<User> existingUser = userDbRepo.findOne(user.getId());
        if (existingUser.isPresent()) {
            return;
        }

        userDbRepo.save(user);
    }

    public void addFriendship(Friendship friendship) {
        //Set<Friendship> friendship1=dbrepo.findAllF();
        friendshipDbRepo.save(friendship);
    }
}