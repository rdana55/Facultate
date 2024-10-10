package com.example.socialnetworkmap.controller;

import com.example.socialnetworkmap.domain.Message;
import com.example.socialnetworkmap.domain.User;
import com.example.socialnetworkmap.events.MessageChangeEvent;
import com.example.socialnetworkmap.events.MessaggesEventType;
import com.example.socialnetworkmap.observer.Observer;
import com.example.socialnetworkmap.paging.Page;
import com.example.socialnetworkmap.paging.Pageable;
import com.example.socialnetworkmap.service.FriendshipRequestsService;
import com.example.socialnetworkmap.service.MessageService;
import com.example.socialnetworkmap.service.UserService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

public class MessageController implements Observer<MessageChangeEvent> {

    @FXML
    private TableView<User> friendsTableView;

    @FXML
    private TableColumn<User, String> friendNameColumn;

    @FXML
    private ListView<Message> textListView;

    @FXML
    private TextField messageTextField;

    @FXML
    private TextField searchTextField;

    @FXML
    private Button sendButton;

    @FXML
    private Button replyButton;

    @FXML
    private Button forwardButton;

    @FXML
    private Button searchButton;
    @FXML
    ScrollBar pageMsg;

    private int pageSizeMsg=3;
    private int currentPageMsg=0;
    private int totalNrOfElemsMsg=0;

    private User currentUser;
    private MessageService messageService;
    private UserService userService;

    private FriendshipRequestsService friendshipRequestsService;

    private ObservableList<User> usersModel = FXCollections.observableArrayList();
    private ObservableList<Message> messagesModel = FXCollections.observableArrayList();

    public void setServices(UserService userService, MessageService messageService, FriendshipRequestsService friendshipRequestsService, User currentUser) {
        this.userService = userService;
        this.messageService = messageService;
        this.friendshipRequestsService=friendshipRequestsService;
        this.currentUser = currentUser;
        this.messageService.addObserver(this);
        List<User> friends =currentUser.getFriends();
        usersModel.setAll(friends);
        initModels();
    }

    private void initModels() {
        initFriendsTable();
        friendsTableView.setItems(usersModel);
    }

    @FXML
    public void initialize() {

        friendNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName().toString()));
        sendButton.setDisable(false);
        replyButton.setDisable(false);
        forwardButton.setDisable(false);
        messageTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                User selectedFriend = friendsTableView.getSelectionModel().getSelectedItem();
                if (selectedFriend != null) {
                    friendsTableView.getSelectionModel().select(selectedFriend);
                }
            }
        });

        textListView.setCellFactory(param -> new ListCell<Message>() {
            @Override
            protected void updateItem(Message item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getSender()+": "+item.getText());
                }
            }
        });

        pageMsg.valueProperty().addListener((observable, oldValue, newValue) -> {
            int newPage = (int) Math.floor(newValue.doubleValue());
            Set<Message> usersOnNewPage = messageService.getMessagesOnPage(newPage);
            updateModel(usersOnNewPage);
            currentPageMsg = newPage;
        });

        pageMsg.valueProperty().addListener((observable, oldValue, newValue) -> {
            handlePageChangeMsg ();
        });
    }

    private void updateModel(Set<Message> msg) {
        messagesModel.clear();
        messagesModel.addAll(msg);
    }

    private void initFriendsTable() {
        friendsTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                initMessageModel(newValue);
            }
        });
        friendsTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }

/*
    private void initMessageModel(User selectedFriend) {
        Set<Message> messages = messageService.getMessagesBetweenUsers(currentUser, selectedFriend);

        List<Message> sortedMessages = messages.stream()
                .sorted(Comparator.comparing(Message::getDate))
                .collect(Collectors.toList());

        messagesModel.setAll(sortedMessages);
        textListView.setItems(messagesModel);


    }

 */

    /*
    private void initMessageModel(User selectedFriend){
        Page<Message> pageUsers=messageService.findAllOnePage(new Pageable(currentPageMsg,pageSizeMsg), currentUser, selectedFriend);
        int maxPageUser=(int) Math.ceil((double) pageUsers.getTotalNrOfElems()/pageSizeMsg)-1;
        if (currentPageMsg > maxPageUser){
            currentPageMsg=maxPageUser;
            pageUsers = messageService.findAllOnePage(new Pageable(currentPageMsg,pageSizeMsg), currentUser, selectedFriend);
        }
        messagesModel.setAll(StreamSupport.stream(pageUsers.getElementsOnPage().spliterator(),false).collect(Collectors.toList()));
        totalNrOfElemsMsg=pageUsers.getTotalNrOfElems();

        pageMsg.setMin(0);
        pageMsg.setMax(maxPageUser);
        pageMsg.setValue(currentPageMsg);
    }


     */

    private void initMessageModel(User selectedFriend) {
        Page<Message> pageUsers = messageService.findAllOnePage(new Pageable(currentPageMsg, pageSizeMsg), currentUser, selectedFriend);
        int maxPageUser = (int) Math.ceil((double) pageUsers.getTotalNrOfElems() / pageSizeMsg) - 1;

        if (currentPageMsg > maxPageUser) {
            currentPageMsg = maxPageUser;
            pageUsers = messageService.findAllOnePage(new Pageable(currentPageMsg, pageSizeMsg), currentUser, selectedFriend);
        }

        List<Message> sortedMessages = new ArrayList<>(pageUsers.getTotalNrOfElems());
        pageUsers.getElementsOnPage().forEach(sortedMessages::add);
        sortedMessages.sort(Comparator.comparing(Message::getDate));

        messagesModel.setAll(sortedMessages);
        totalNrOfElemsMsg = pageUsers.getTotalNrOfElems();

        pageMsg.setMin(0);
        pageMsg.setMax(maxPageUser);
        pageMsg.setValue(currentPageMsg);

        // Actualizează vizualizarea cu noile mesaje sortate
        textListView.setItems(messagesModel);
    }

    private void handlePageChangeMsg() {
        int newPage = (int) Math.floor(pageMsg.getValue());
        Set<Message> usersOnNewPage = messageService.getMessagesOnPage(newPage);
        updateModel(usersOnNewPage);
        currentPageMsg = newPage;
    }

    @FXML
    private void handleSearch() {
        String filterTextLower = searchTextField.getText().toLowerCase();
        Message selectedMessage = textListView.getSelectionModel().getSelectedItem();

        if (selectedMessage != null) {
            User selectedFriend = selectedMessage.getSender();

            Set<Message> messages = messageService.getMessagesBetweenUsers(currentUser, selectedFriend);
            List<Message> filteredMessages = messages.stream()
                    .filter(message -> message.getText().toLowerCase().contains(filterTextLower))
                    .collect(Collectors.toList());

            messagesModel.setAll(filteredMessages);
        } else {
            System.out.println("Selectați un mesaj înainte de a căuta mesajele.");
        }
    }

    @FXML
    private void handleSentMessage() {
        String messageText = messageTextField.getText();
        User selectedFriend = friendsTableView.getSelectionModel().getSelectedItem();

        if (selectedFriend != null && !messageText.isEmpty()) {
            messageService.sendMessage(currentUser, selectedFriend, messageText);
        } else {
            System.out.println("Selectați un prieten și introduceți un mesaj înainte de a trimite.");
        }
    }

    @FXML
    private void handleReplyMessage(){
        Message selected=textListView.getSelectionModel().getSelectedItem();
        User sender=selected.getSender();
        User receiver=selected.getReceiver();
        String messageText = messageTextField.getText();
        String text="replied to '"+selected.getText()+"': "+messageText;



        if (!messageText.isEmpty()) {
            if(currentUser.toString().equals(sender.toString())){
                messageService.replyToMessage(currentUser,receiver, text, selected.getId());
            }
            else{
                messageService.replyToMessage(currentUser,sender,text, selected.getId());
            }
        } else {
            System.out.println("Selectați un mesaj și introduceți un raspuns înainte de a trimite.");
        }

    }

    @FXML
    private void handleForwardMessage() {
        String messageText = messageTextField.getText();

        if (messageText != null) {
            List<User> selectedFriends = friendsTableView.getSelectionModel().getSelectedItems();
            if (!selectedFriends.isEmpty()) {
                for (User friend : selectedFriends) {
                    messageService.sendMessage(currentUser, friend, messageText);
                }
                System.out.println("Mesajele au fost trimise cu succes!");
            } else {
                System.out.println("Selectați cel puțin un prieten pentru a efectua forward.");
            }

            System.out.println("Mesajele au fost trimise cu succes!");
        } else {
            System.out.println("Scrieti un mesaj pentru a efectua forward.");
        }
    }


    @Override
    public void update(MessageChangeEvent event) {
        if (event.getType() == MessaggesEventType.SEND || event.getType() == MessaggesEventType.REPLY) {
            User sender = event.getMessage().map(Message::getSender).orElse(null);
            User receiver = event.getMessage().map(Message::getReceiver).orElse(null);

            if (sender != null && (currentUser.equals(sender) || currentUser.equals(receiver))) {
                initMessageModel(receiver);
            }
        }
    }

    @FXML
    public void handleRequests(ActionEvent actionEvent){
        showFriendshipRequests(currentUser);
    }

    @FXML
    private void showFriendshipRequests(User user) {
        try{
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/requests.fxml"));

            AnchorPane root=loader.load();

            Stage requestStage=new Stage();
            requestStage.setTitle("Friendship requests");
            requestStage.initModality(Modality.WINDOW_MODAL);
            Scene scene =new Scene(root);
            requestStage.setScene(scene);

            FriendshipRequestsController friendshipRequestsController=loader.getController();
            friendshipRequestsController.setUserService(userService,friendshipRequestsService,requestStage,user);

            requestStage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSendRequest(ActionEvent event) {
        showSendRequestDialog(currentUser);
        //initModel();

    }

    private void showSendRequestDialog(User user) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/sendRequest.fxml"));

            AnchorPane root = loader.load();

            Stage sendRequestStage = new Stage();
            sendRequestStage.setTitle("Send Friendship Request");
            sendRequestStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            sendRequestStage.setScene(scene);

            SendRequestController sendRequestController = loader.getController();
            sendRequestController.setUserService(userService, sendRequestStage, user);

            sendRequestStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
