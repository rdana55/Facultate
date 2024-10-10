package com.example.socialnetworkmap.domain;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends Entity<Long> {
    private String firstName;
    private String lastName;

    private String password;
    private final List<User> friends=new ArrayList<>();

    private List<FriendshipRequest> receivedFriendshipRequests = new ArrayList<>();
    private List<FriendshipRequest> sentFriendshipRequests = new ArrayList<>();

    public User(String firstName, String lastName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password=password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword(){
        return password;
    }

    public String getFriendsAsString() {
        StringBuilder result = new StringBuilder();
        for (User friend : friends) {
            result.append(friend.getLastName()).append(" ").append(friend.getFirstName()).append(", ");
        }
        if (!result.isEmpty()) {
            result.setLength(result.length() - 2);
        }
        return result.toString();
    }

    public List<User> getFriends() {
        return friends;
    }

    public void addFriend(User user) {
        if (!friends.contains(user) && this!=user)
            friends.add(user);
    }

    public List<FriendshipRequest> getReceivedFriendshipRequests() {
        return receivedFriendshipRequests;
    }

    public List<FriendshipRequest> getSentFriendshipRequests() {
        return sentFriendshipRequests;
    }

    public void addReceivedFriendshipRequest(FriendshipRequest request) {
        receivedFriendshipRequests.add(request);
    }

    public void addSentFriendshipRequest(FriendshipRequest request) {
        sentFriendshipRequests.add(request);
    }

    @Override
    public String toString() {
        return getLastName() + " " + getFirstName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User that)) return false;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                Objects.equals(getFriends(),that.getFriends());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getFriends());
    }
    private final StringProperty nameProperty = new SimpleStringProperty();

    public StringProperty getNameProperty() {
        return nameProperty;
    }

    public String getName() {
        return nameProperty.get();
    }

    public void setName(String name) {
        nameProperty.set(name);
    }

/*
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

 */

    /*
    public boolean verifyPassword(String inputPassword) {
        return password.equals(hashPassword(inputPassword));
    }

     */
}
