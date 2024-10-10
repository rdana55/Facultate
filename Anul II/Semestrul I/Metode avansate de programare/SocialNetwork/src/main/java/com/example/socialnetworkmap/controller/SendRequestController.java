package com.example.socialnetworkmap.controller;

import com.example.socialnetworkmap.domain.User;
import com.example.socialnetworkmap.service.UserService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SendRequestController {
    @FXML
    private TableView<User> usersTableView;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private Button sendRequestButton;

    @FXML
    private TextField filter;

    private UserService userService;
    private Stage sendRequestStage;
    private User currentUser;

    ObservableList<User> model = FXCollections.observableArrayList();

    public void setUserService(UserService userService, Stage sendRequestStage, User currentUser) {
        this.userService = userService;
        this.sendRequestStage = sendRequestStage;
        this.currentUser = currentUser;
        initModel();
    }

    @FXML
    private void initialize() {
        usernameColumn.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().toString()));
        model.remove(currentUser);
        usersTableView.setItems(model);
        filter.textProperty().addListener(o -> handleFilter());
    }

    private void initModel() {
        /*
        List<User> friends = currentUser.getFriends();
        friends.remove(currentUser);
        if (model == null) {
            model = FXCollections.observableArrayList();
        }
        model.setAll(friends);

         */

        Iterable<User> users = userService.findAll();
        List <User> good = new ArrayList<>();
        users.forEach(x->
                good.add(x));

        good.remove(currentUser);
        model.setAll(good);
    }

    @FXML
    private void handleSendRequest(ActionEvent event) {
        User selectedUser = usersTableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            userService.sendFriendshipRequest(currentUser, selectedUser);
            sendRequestStage.close();
        }
    }

    @FXML
    private void handleFilter() {
        String filterTextLower = filter.getText().toLowerCase();

        Task<List<User>> filterTask = new Task<List<User>>() {
            @Override
            protected List<User> call() throws Exception {
                return StreamSupport.stream(userService.findAll().spliterator(), false)
                        .filter(o -> {
                            String userFullNameLower = o.toString().toLowerCase();
                            return userFullNameLower.contains(filterTextLower);
                        })
                        .collect(Collectors.toList());
            }
        };

        filterTask.setOnSucceeded(event -> {
            List<User> filteredUsers = filterTask.getValue();
            model.setAll(filteredUsers);
        });

        new Thread(filterTask).start();
    }
}
