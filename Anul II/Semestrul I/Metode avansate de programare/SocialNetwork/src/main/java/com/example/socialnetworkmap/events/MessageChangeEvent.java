package com.example.socialnetworkmap.events;

import com.example.socialnetworkmap.domain.Message;
import com.example.socialnetworkmap.domain.User;

import java.util.Optional;

public class MessageChangeEvent implements Event{
    private MessaggesEventType type;
    private Optional<Message> message;

    public MessageChangeEvent(MessaggesEventType type, Optional<Message> message) {
        this.type = type;
        this.message = message;
    }

    public MessaggesEventType getType(){return type;}
    public Optional<Message> getMessage(){return message;}

}
