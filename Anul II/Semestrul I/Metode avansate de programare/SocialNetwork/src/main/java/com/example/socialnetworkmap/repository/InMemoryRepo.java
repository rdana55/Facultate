package com.example.socialnetworkmap.repository;

import com.example.socialnetworkmap.domain.Entity;
import com.example.socialnetworkmap.domain.Friendship;
import com.example.socialnetworkmap.domain.Tuple;
import com.example.socialnetworkmap.domain.User;
import com.example.socialnetworkmap.validators.Validator;

import java.util.*;

public class InMemoryRepo<ID, E extends Entity<ID>>/* implements Repository<ID, E> */{
    private final Validator<E> validator;

    private final UserDbRepo userDbRepo;
    private final FriendshipDbRepo friendshipDbRepo;
    Map<ID, E> entities;

    Map<Tuple<Long, Long>, Friendship> friendships;

    public InMemoryRepo(Validator<E> validator, UserDbRepo userDbRepo, FriendshipDbRepo friendshipDbRepo) {
        this.validator = validator;
        this.userDbRepo = userDbRepo;
        this.friendshipDbRepo = friendshipDbRepo;
        entities = new HashMap<>();
        friendships = new HashMap<>();
    }

    //@Override
    public Optional<E> findOne(ID id) {
        if (id == null)
            throw new IllegalArgumentException("The ID can not be null!");

        return (Optional<E>) userDbRepo.findOne((Long) id);
    }


    //@Override
    public Iterable<E> findAll() {
        entities.clear();
        userDbRepo.findAll().forEach(el->{
            entities.put((ID) el.getId(), (E) el);
        });
        return entities.values();
    }


    //@Override
    public Optional<E> save(E entity) {
        if (entity == null)
            throw new IllegalArgumentException("Can not be null");
        validator.validate(entity);
        userDbRepo.save((User) entity);
        return Optional.ofNullable(entities.putIfAbsent(entity.getId(),entity));
    }

    //@Override
    public Optional<E> update(E entity) {
        if(entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);
        if(entities.get(entity.getId()) != null) {
            entities.put(entity.getId(),entity);
            userDbRepo.update((User) entity);
            //return null;
        }
        userDbRepo.update((User) entity);
        return Optional.of(entity);
    }

    //@Override
    public Optional<E> delete(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID-ul nu poate sa fie null.");
        }
        Optional<E> deletedEntity = (Optional<E>) userDbRepo.delete((Long) id);

        return deletedEntity;
    }

    public void addFriendship(Friendship friendship) {
        friendships.put(friendship.getId(), friendship);
        friendshipDbRepo.save(friendship);

        User user1 = (User) findOne((ID) friendship.getId().getLeft()).orElse(null);
        User user2 = (User) findOne((ID) friendship.getId().getRight()).orElse(null);

        if (user1 != null && user2 != null) {
            user1.addFriend(user2);
            user2.addFriend(user1);
        }
    }

    public void deleteFriendship(Tuple<Long, Long> fId) {
        friendships.remove(fId);
        friendshipDbRepo.delete(fId);
        User user1 = (User) findOne((ID) fId.getLeft()).orElse(null);
        User user2 = (User) findOne((ID) fId.getRight()).orElse(null);

        if (user1 != null && user2 != null) {
            user1.getFriends().remove(user2);
            user2.getFriends().remove(user1);
        }
    }


    public Set<Friendship> findAllF() {
        friendships.clear();
        Set<Friendship> friendships = new HashSet<>();

        Iterable<Friendship> allFriendships = friendshipDbRepo.findAll();

        for (Friendship friendship : allFriendships) {
            User user1 = userDbRepo.findOne(friendship.getId().getLeft()).orElse(null);
            User user2 = userDbRepo.findOne(friendship.getId().getRight()).orElse(null);

            if (user1 != null && user2 != null) {
                user1.addFriend(user2);
                user2.addFriend(user1);
            }

            friendships.add(friendship);
        }

        return friendships;
    }



    public Optional<Friendship> findFriendshipByUserIds(Long userId1, Long userId2) {
        Tuple<Long,Long> fId = null;
        fId.setLeft(userId1);
        fId.setRight(userId2);

        return friendshipDbRepo.findOne(fId);
    }
}

/*
    public List<User> findByString(String string){
        List<User> userString=new ArrayList<>();
        dbrepo.findAll().forEach(u->{
            if (u.getFirstName().contains(string) || u.getLastName().contains(string)) {
                userString.add(u);
            }
        });
        return userString;
    }

      public Friendship findFriendshipByUserIds(Long userId1, Long userId2) {
        Optional<Friendship> goodFriendship=findAllF().stream()
                .filter(friendship -> {
                    return friendship.containsUser(userId1) && friendship.containsUser(userId2);
                })
                .findFirst();

        return goodFriendship.orElse(null);
    }
 */