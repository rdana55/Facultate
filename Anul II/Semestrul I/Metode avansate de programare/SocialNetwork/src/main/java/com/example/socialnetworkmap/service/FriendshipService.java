package com.example.socialnetworkmap.service;

import com.example.socialnetworkmap.domain.User;
import com.example.socialnetworkmap.repository.InMemoryRepo;
import com.example.socialnetworkmap.domain.Friendship;
import java.time.LocalDateTime;
import java.util.Optional;

public class FriendshipService {
    private InMemoryRepo repoF;

    public FriendshipService(InMemoryRepo repoF) {
        this.repoF = repoF;
    }

    public void addFriendship(Long userId1, Long userId2, LocalDateTime date) {
        Friendship f = new Friendship(userId1, userId2, date);
        repoF.addFriendship(f);
    }

    public void removeFriendship(Long userId1, Long userId2) {
        Optional<Friendship> f = repoF.findFriendshipByUserIds(userId1, userId2);
        if (f != null) {
            repoF.deleteFriendship(f.get().getId());
        }
    }

    public Iterable<Friendship> findAllF() {
        return repoF.findAll();
    }
}
