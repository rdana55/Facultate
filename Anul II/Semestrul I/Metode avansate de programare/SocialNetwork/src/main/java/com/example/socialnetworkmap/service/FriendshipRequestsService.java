package com.example.socialnetworkmap.service;

import com.example.socialnetworkmap.domain.FriendshipRequest;
import com.example.socialnetworkmap.domain.FriendshipStatus;
import com.example.socialnetworkmap.domain.User;
import com.example.socialnetworkmap.events.EventType;
import com.example.socialnetworkmap.events.RequestChangeEvent;
import com.example.socialnetworkmap.events.UserChangeEvent;
import com.example.socialnetworkmap.observer.Observable;
import com.example.socialnetworkmap.observer.Observer;
import com.example.socialnetworkmap.repository.FriendshipRequestsRepo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FriendshipRequestsService implements Observable<RequestChangeEvent> {
    private final FriendshipRequestsRepo friendshipRequestsRepo;
    private final FriendshipService friendshipService;

    public FriendshipRequestsService(FriendshipRequestsRepo friendshipRequestsRepo, FriendshipService friendshipService) {
        this.friendshipRequestsRepo = friendshipRequestsRepo;
        this.friendshipService = friendshipService;
    }

    private final List<Observer<RequestChangeEvent>> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer<RequestChangeEvent> e) {
        observers.add(e);

    }

    @Override
    public void removeObserver(Observer<RequestChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObserver(RequestChangeEvent event) {
//        if (observers != null) {
//            observers.forEach(x -> x.update(t));
//        }
        observers.stream().forEach(x -> x.update(event));
    }

    public void sendFriendshipRequest(User sender, User receiver){
        FriendshipRequest request = new FriendshipRequest(sender, receiver, FriendshipStatus.PENDING);
        friendshipRequestsRepo.save(request);
    }

    public void acceptFriendshipRequest(User sender, User receiver){
        friendshipRequestsRepo.findOne(sender.getId(),receiver.getId())
                .ifPresent(request -> {
                    request.setStatus(FriendshipStatus.ACCEPTED);
                    friendshipRequestsRepo.update(request);
                    friendshipRequestsRepo.delete(request.getId());
                    friendshipService.addFriendship(sender.getId(), receiver.getId(), LocalDateTime.now());

                    notifyObserver(new RequestChangeEvent(RequestChangeEvent.RequestChangeType.REQUEST_ACCEPTED, Optional.of(request)));
                });
    }

    public void rejectFriendshipRequest(User sender,User receiver){
        friendshipRequestsRepo.findOne(sender.getId(),receiver.getId())
                .ifPresent(request -> {
                    request.setStatus(FriendshipStatus.REJECTED);
                    friendshipRequestsRepo.update(request);
                    friendshipRequestsRepo.delete(request.getId());

                    notifyObserver(new RequestChangeEvent(RequestChangeEvent.RequestChangeType.REQUEST_REJECTED, Optional.of(request)));
                });
    }

    public List<FriendshipRequest> getFriendshipRequestsForUser(User user) {
        return (List<FriendshipRequest>) friendshipRequestsRepo.findAll(user.getId());
    }
}
