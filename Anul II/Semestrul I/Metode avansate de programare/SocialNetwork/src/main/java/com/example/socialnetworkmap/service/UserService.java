package com.example.socialnetworkmap.service;

import com.example.socialnetworkmap.domain.Friendship;
import com.example.socialnetworkmap.domain.FriendshipRequest;
import com.example.socialnetworkmap.domain.FriendshipStatus;
import com.example.socialnetworkmap.domain.User;
import com.example.socialnetworkmap.events.EventType;
import com.example.socialnetworkmap.events.UserChangeEvent;
import com.example.socialnetworkmap.observer.Observable;
import com.example.socialnetworkmap.observer.Observer;
import com.example.socialnetworkmap.paging.Page;
import com.example.socialnetworkmap.paging.Pageable;
import com.example.socialnetworkmap.repository.InMemoryRepo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Predicate;

public class UserService implements Observable<UserChangeEvent> {
    private InMemoryRepo repoU;
    private FriendshipService serviceF;

    private FriendshipRequestsService friendshipRequestsService;

    public UserService(InMemoryRepo repoU, FriendshipService serviceF, FriendshipRequestsService friendshipRequestsService) {
        this.repoU = repoU;
        this.serviceF = serviceF;
        this.friendshipRequestsService = friendshipRequestsService;
    }

    private <T> Iterable<T> filter(Iterable<T> list, Predicate<T> cond) {
        List<T> rez = new ArrayList<>();
        list.forEach((T x) -> {
            if (cond.test(x)) rez.add(x);
        });
        return rez;
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] digest = md.digest();

            return Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User createUser(String firstN, String lastN, String ps) throws NoSuchAlgorithmException {
        String password=hashPassword(ps);
        User user = new User(firstN, lastN, password);
        repoU.save(user);
        if (user != null) {
            notifyObserver(new UserChangeEvent(EventType.ADD, Optional.ofNullable(user)));
        }
        return user;
    }

    public Optional<User> update(User newUser) {
        Optional<User> oldUser = repoU.findOne(newUser.getId());
        if (oldUser.isPresent()) {
            repoU.update(newUser);
            notifyObserver(new UserChangeEvent(EventType.UPDATE, Optional.of(newUser), oldUser));
        }
        return oldUser;
    }

    public Optional<User> deleteUser(Long userId) {
        Optional<User> user = repoU.delete(userId);

        if (user.isPresent()) {
            notifyObserver(new UserChangeEvent(EventType.DELETE, user));
        }
        return user;
    }

    public Optional<User> findUserById(Long userId) {
        return repoU.findOne(userId);
    }

    public Iterable<User> findAll() {
        Iterable<User> allUsers = repoU.findAll();

        for (User user : allUsers) {
            populateFriends(user);
        }

        return allUsers;
    }

    private void populateFriends(User user) {
        Set<Friendship> friendships = repoU.findAllF();

        for (Friendship f : friendships) {
            if (f.containsUser(user.getId())) {
                Long friendId;
                if(f.getId().getLeft().equals(user.getId())) {
                    friendId = f.getId().getRight();
                } else {
                    friendId = f.getId().getLeft();
                }
                Optional<User> friend = findUserById(friendId);
                friend.ifPresent(user::addFriend);
            }
        }
    }

    public void sendFriendshipRequest(User sender, User receiver) {
        if (friendshipRequestsService != null) {
            FriendshipRequest request = new FriendshipRequest(sender, receiver, FriendshipStatus.PENDING);
            friendshipRequestsService.sendFriendshipRequest(sender, receiver);
            receiver.addReceivedFriendshipRequest(request);
            sender.addSentFriendshipRequest(request);
        }
    }

    public void acceptFriendshipRequest(User receiver, User sender) {
        FriendshipRequest request = findFriendshipRequest(receiver.getReceivedFriendshipRequests(), sender);
        if (request != null) {
            request.setStatus(FriendshipStatus.ACCEPTED);
        }
        friendshipRequestsService.acceptFriendshipRequest(sender, receiver);
    }

    public void rejectFriendshipRequest(User receiver, User sender) {
        FriendshipRequest request = findFriendshipRequest(receiver.getReceivedFriendshipRequests(), sender);
        if (request != null) {
            request.setStatus(FriendshipStatus.REJECTED);
        }
        friendshipRequestsService.rejectFriendshipRequest(sender,receiver);
    }

    private FriendshipRequest findFriendshipRequest(List<FriendshipRequest> requests, User sender) {
        return requests.stream()
                .filter(request -> request.getSender().equals(sender))
                .findFirst()
                .orElse(null);
    }

    private final List<Observer<UserChangeEvent>> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer<UserChangeEvent> e) {
        observers.add(e);

    }

    @Override
    public void removeObserver(Observer<UserChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObserver(UserChangeEvent event) {
        observers.stream().forEach(x -> x.update(event));
    }

    public Page<User> findAllOnePage(Pageable pageable) {
        Iterable<User> allUsers = repoU.findAll();

        List<User> usersOnPage = new ArrayList<>();
        int pageSize = pageable.getPageSize();
        int pageNr = pageable.getPageNr();
        int startIdx = pageNr * pageSize;
        int endIdx = Math.min((pageNr + 1) * pageSize, totalNumberOfUsers());

        int currentIndex = 0;
        for (User user : allUsers) {
            if (currentIndex >= startIdx && currentIndex < endIdx) {
                populateFriends(user);
                usersOnPage.add(user);
            }
            currentIndex++;
        }

        return new Page<>(usersOnPage, totalNumberOfUsers());
    }

    private int totalNumberOfUsers() {
       Iterable<User> users= repoU.findAll();
       List<User> userList=new ArrayList<>();
       users.forEach(userList::add);
       return userList.size();
    }

    public Set<User> getUsersOnPage(int page) {
        Iterable<User> allUsers = repoU.findAll();
        int pageSize = 3;
        int startIdx = page * pageSize;
        int endIdx = Math.min((page + 1) * pageSize, totalNumberOfUsers());

        Set<User> usersOnPage = new HashSet<>();
        int currentIndex = 0;
        for (User user : allUsers) {
            if (currentIndex >= startIdx && currentIndex < endIdx) {
                populateFriends(user);
                usersOnPage.add(user);
            }
            currentIndex++;
        }

        return usersOnPage;
    }

}