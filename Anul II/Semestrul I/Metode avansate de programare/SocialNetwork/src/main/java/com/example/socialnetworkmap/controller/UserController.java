package com.example.socialnetworkmap.controller;

import com.example.socialnetworkmap.domain.User;
import com.example.socialnetworkmap.events.UserChangeEvent;
import com.example.socialnetworkmap.observer.Observer;
import com.example.socialnetworkmap.paging.Page;
import com.example.socialnetworkmap.paging.Pageable;
import com.example.socialnetworkmap.service.FriendshipRequestsService;
import com.example.socialnetworkmap.service.MessageService;
import com.example.socialnetworkmap.service.UserService;
import javafx.beans.property.SimpleObjectProperty;
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
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserController implements Observer<UserChangeEvent> {
    UserService service;
    FriendshipRequestsService friendshipRequestsService;

    MessageService messageService;
    ObservableList<User> model = FXCollections.observableArrayList();

    @FXML
    TableView<User> tableView;
    @FXML
    TableColumn<User, Long> idColumn;
    @FXML
    TableColumn<User,String> fnColumn;
    @FXML
    TableColumn<User,String> snColumn;
    @FXML
    TableColumn<User,String> friends;
    @FXML
    ScrollBar pageScrollBar;

    private int pageSizeUser=3;
    private int currentPageUser=0;
    private int totalNrOfElemsUser=0;

    public void setUserService(UserService userService,FriendshipRequestsService fService,MessageService msgService) {
        service = userService;
        friendshipRequestsService=fService;
        messageService=msgService;
        service.addObserver(this);
        initModel();
    }

    @FXML
    public void initialize(){
        idColumn.setCellValueFactory(item -> new SimpleObjectProperty<>(item.getValue().getId()));
        fnColumn.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getFirstName()));
        snColumn.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getLastName()));
        friends.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getFriendsAsString()));

        tableView.setItems(model);

        pageScrollBar.valueProperty().addListener((observable, oldValue, newValue) -> {
            int newPage = (int) Math.floor(newValue.doubleValue());
            Set<User> usersOnNewPage = service.getUsersOnPage(newPage);
            updateModel(usersOnNewPage);
            currentPageUser = newPage;
        });

        pageScrollBar.valueProperty().addListener((observable, oldValue, newValue) -> {
            handlePageChange();
        });
    }

    private void updateModel(Set<User> users) {
        model.clear();
        model.addAll(users);
    }

    /*
    private void initModel(){
         Iterable<User> users = service.findAll();
         List<User> usersList= StreamSupport.stream(users.spliterator(),false)
                 .collect(Collectors.toList());
         model.setAll(usersList);
    }
     */

    private void initModel(){
        Page<User> pageUsers=service.findAllOnePage(new Pageable(currentPageUser,pageSizeUser));
        int maxPageUser=(int) Math.ceil((double) pageUsers.getTotalNrOfElems()/pageSizeUser)-1;
        if (currentPageUser > maxPageUser){
            currentPageUser=maxPageUser;
            pageUsers = service.findAllOnePage(new Pageable(currentPageUser,pageSizeUser));
        }
        model.setAll(StreamSupport.stream(pageUsers.getElementsOnPage().spliterator(),false).collect(Collectors.toList()));
        totalNrOfElemsUser=pageUsers.getTotalNrOfElems();

        pageScrollBar.setMin(0);
        pageScrollBar.setMax(maxPageUser);
        pageScrollBar.setValue(currentPageUser);
    }

    private void handlePageChange() {
        int newPage = (int) Math.floor(pageScrollBar.getValue());
        Set<User> usersOnNewPage = service.getUsersOnPage(newPage);
        updateModel(usersOnNewPage);
        currentPageUser = newPage;
    }



    @FXML
    public void handleDeleteUser(ActionEvent actionEvent){
        User selected=tableView.getSelectionModel().getSelectedItem();
        if (selected!=null){
            service.deleteUser(selected.getId());
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Delete","Userul a fost sters cu succes.");
        }
        else MessageAlert.showErrorMessage(null,"Nu ati selectat niciun user!");

    }


    @Override
    public void update(UserChangeEvent userChangeEvent){
        initModel();
    }

    @FXML
    public void handleUpdateUser(ActionEvent actionEvent){
        User selected = tableView.getSelectionModel().getSelectedItem();
        if(selected!=null){
            showUserEditDialog(selected);
        }
        else MessageAlert.showErrorMessage(null, "Nu ati selectat niciun user!");
    }

    @FXML
    public void handleAddUser(ActionEvent actionEvent){

        showUserEditDialog(null);
    }

    public void showUserEditDialog(User user){
        try{
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/editUser.fxml"));

            AnchorPane root=loader.load();

            Stage dialogStage=new Stage();
            dialogStage.setTitle("Edit User");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene =new Scene(root);
            dialogStage.setScene(scene);

            EditUserController editUserController=loader.getController();
            editUserController.setService(service,dialogStage,user);

            dialogStage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void handleRequests(ActionEvent actionEvent){
        User selected = tableView.getSelectionModel().getSelectedItem();
        showFriendshipRequests(selected);
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
            friendshipRequestsController.setUserService(service,friendshipRequestsService,requestStage,user);

            requestStage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSendRequest(ActionEvent event) {
        User selected = tableView.getSelectionModel().getSelectedItem();
        showSendRequestDialog(selected);
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
            sendRequestController.setUserService(service, sendRequestStage, user);

            sendRequestStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMessages(ActionEvent event) {
        User selected = tableView.getSelectionModel().getSelectedItem();
        showMessages(selected);
        //initModel();

    }

    private void showMessages(User user) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/messages.fxml"));

            AnchorPane root = loader.load();

            Stage sendRequestStage = new Stage();
            sendRequestStage.setTitle(user.toString());
            sendRequestStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            sendRequestStage.setScene(scene);

            MessageController messageController = loader.getController();
            messageController.setServices(service,messageService,friendshipRequestsService,user);

            sendRequestStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
