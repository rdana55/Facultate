package com.example.socialnetworkmap.domain;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

import java.time.LocalDateTime;
import java.util.Set;

public class Message extends Entity<Long> {
    private User sender;
    private User receiver;
    private String text;

    LocalDateTime date;

    private Long reply;

    public Message(User sender, User receiver, String text, LocalDateTime date, Long reply) {
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.date=date;
        this.reply=reply;
    }

    public LocalDateTime getDate() {
        return date;
    }
    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public String getText() {
        return text;
    }

    public Long getReply(){
        return reply;
    }

}
