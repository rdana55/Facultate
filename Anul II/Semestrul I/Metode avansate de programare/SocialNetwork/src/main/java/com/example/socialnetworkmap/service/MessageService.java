package com.example.socialnetworkmap.service;

import com.example.socialnetworkmap.domain.Message;
import com.example.socialnetworkmap.domain.User;
import com.example.socialnetworkmap.events.MessageChangeEvent;
import com.example.socialnetworkmap.events.MessaggesEventType;
import com.example.socialnetworkmap.events.UserChangeEvent;
import com.example.socialnetworkmap.observer.Observable;
import com.example.socialnetworkmap.observer.Observer;
import com.example.socialnetworkmap.paging.Page;
import com.example.socialnetworkmap.paging.Pageable;
import com.example.socialnetworkmap.repository.MessageDbRepo;

import java.time.LocalDateTime;
import java.util.*;

public class MessageService implements Observable<MessageChangeEvent>, Observer<UserChangeEvent> {

    private MessageDbRepo messageDbRepo;
    private UserService userService;

    private final List<Observer<MessageChangeEvent>> observers = new ArrayList<>();

    public MessageService(MessageDbRepo messageDbRepo, UserService userService) {
        this.messageDbRepo = messageDbRepo;
        this.userService = userService;
        userService.addObserver(this);
    }

    public Message sendMessage(User sender, User receiver, String text) {
        Message message = new Message(sender, receiver, text, LocalDateTime.now(), null);
        messageDbRepo.save(message);

        notifyObserver(new MessageChangeEvent(MessaggesEventType.SEND, Optional.of(message)));
        return message;
    }

    public Message replyToMessage(User sender, User receiver, String text, Long mId) {
        Message replyMessage = new Message(sender, receiver, text, LocalDateTime.now(), mId);
        messageDbRepo.save(replyMessage);

        notifyObserver(new MessageChangeEvent(MessaggesEventType.REPLY, Optional.of(replyMessage)));
        return replyMessage;
    }

    public Set<Message> getMessagesBetweenUsers(User user1, User user2) {
        Set<Message> messages = new HashSet<>();
        messageDbRepo.findAll().forEach(message -> {
            User sender = message.getSender();
            User receiver = message.getReceiver();
            System.out.println(message.getText());
            if ((sender.toString().equals(user1.toString()) && receiver.toString().equals(user2.toString())) ||
                    (sender.toString().equals(user2.toString()) && receiver.toString().equals(user1.toString()))) {
                //System.out.println(message.getText());
                messages.add(message);
            }
        });

        return messages;
    }


    @Override
    public void addObserver(Observer<MessageChangeEvent> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<MessageChangeEvent> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObserver(MessageChangeEvent event) {
        observers.forEach(observer -> observer.update(event));
    }

    @Override
    public void update(UserChangeEvent event) {
        // Aici poți reacționa la evenimentele legate de utilizatori, de exemplu, să actualizezi lista de mesaje
        // sau să faci altă acțiune relevantă în contextul serviciului de mesaje
    }

    public Page<Message> findAllOnePage(Pageable pageable, User user1, User user2) {
        Iterable<Message> allMessages = getMessagesBetweenUsers(user1, user2);

        List<Message> usersOnPage = new ArrayList<>();
        int pageSize = pageable.getPageSize();
        int pageNr = pageable.getPageNr();
        int startIdx = pageNr * pageSize;
        int endIdx = Math.min((pageNr + 1) * pageSize, totalNumberOfMessages());

        int currentIndex = 0;
        for (Message message : allMessages) {
            if (currentIndex >= startIdx && currentIndex < endIdx) {
                //populateFriends(user);
                usersOnPage.add(message);
            }
            currentIndex++;
        }

        return new Page<>(usersOnPage, totalNumberOfMessages());
    }

    private int totalNumberOfMessages() {
        Iterable<Message> messages= messageDbRepo.findAll();
        List<Message> messageList=new ArrayList<>();
        messages.forEach(messageList::add);
        return messageList.size();
    }

    public Set<Message> getMessagesOnPage(int page) {
        Iterable<Message> allMessages = messageDbRepo.findAll();
        int pageSize = 3;
        int startIdx = page * pageSize;
        int endIdx = Math.min((page + 1) * pageSize, totalNumberOfMessages());

        Set<Message> usersOnPage = new HashSet<>();
        int currentIndex = 0;
        for (Message message : allMessages) {
            if (currentIndex >= startIdx && currentIndex < endIdx) {
                //populateFriends(user);
                usersOnPage.add(message);
            }
            currentIndex++;
        }

        return usersOnPage;
    }
}


