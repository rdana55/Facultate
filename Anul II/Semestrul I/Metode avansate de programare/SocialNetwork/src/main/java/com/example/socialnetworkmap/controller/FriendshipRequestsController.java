package com.example.socialnetworkmap.controller;

import com.example.socialnetworkmap.domain.FriendshipRequest;
import com.example.socialnetworkmap.domain.FriendshipStatus;
import com.example.socialnetworkmap.domain.User;
import com.example.socialnetworkmap.events.RequestChangeEvent;
import com.example.socialnetworkmap.observer.Observer;
import com.example.socialnetworkmap.service.FriendshipRequestsService;
import com.example.socialnetworkmap.service.UserService;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;
public class FriendshipRequestsController implements Observer<RequestChangeEvent> {
    @FXML
    private TableView<FriendshipRequest> requestsTableView;

    @FXML
    private TableColumn<FriendshipRequest, String> senderColumn;

    @FXML
    private TableColumn<FriendshipRequest, FriendshipStatus> statusColumn;

    private UserService userService;

    private FriendshipRequestsService friendshipRequestsService;

    ObservableList<FriendshipRequest> model = FXCollections.observableArrayList();

    User user;

    Stage requestStage;
    public void setUserService(UserService userService, FriendshipRequestsService friendshipRequestsService, Stage requestStage, User user) {
        this.userService = userService;
        this.friendshipRequestsService=friendshipRequestsService;
        this.requestStage = requestStage;
        this.user = user;
        friendshipRequestsService.addObserver(this);
        initModel();
    }

    @FXML
    private void initialize() {
        if (senderColumn != null && statusColumn != null) {
            senderColumn.setCellValueFactory(item -> new SimpleObjectProperty(item.getValue().getSender().toString()));
            statusColumn.setCellValueFactory(item -> new SimpleObjectProperty(item.getValue().getStatus()));

            requestsTableView.setItems(model);
        }
    }

    private void initModel(){
        List<FriendshipRequest> requests = friendshipRequestsService.getFriendshipRequestsForUser(user);
        model.setAll(requests);
    }

    @FXML
    private void handleAcceptRequest(ActionEvent event) {
        FriendshipRequest selectedRequest = requestsTableView.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            Task<Void> acceptTask = new Task<>() {
                @Override
                protected Void call() {
                    userService.acceptFriendshipRequest(selectedRequest.getReceiver(), selectedRequest.getSender());
                    return null;
                }
            };

            acceptTask.setOnSucceeded(taskEvent -> {
                Platform.runLater(() -> refreshTable());
            });
            new Thread(acceptTask).start();
        }
    }

    @FXML
    private void handleRejectRequest(ActionEvent event) {
        FriendshipRequest selectedRequest = requestsTableView.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            Task<Void> rejectTask = new Task<>() {
                @Override
                protected Void call() {
                    userService.rejectFriendshipRequest(selectedRequest.getReceiver(), selectedRequest.getSender());
                    return null;
                }
            };

            rejectTask.setOnSucceeded(taskEvent -> {
                Platform.runLater(() -> refreshTable());
            });
            new Thread(rejectTask).start();
        }
    }

    private void refreshTable() {
        if (user != null) {
            List<FriendshipRequest> receivedRequests = user.getReceivedFriendshipRequests();
            model.setAll(receivedRequests);
            requestsTableView.setItems(model);
        }
    }

    @Override
    public void update(RequestChangeEvent requestChangeEvent) {
        Platform.runLater(() -> handleRequestChangeEvent(requestChangeEvent));
    }

    private void handleRequestChangeEvent(RequestChangeEvent event) {
        switch (event.getType()) {
            case REQUEST_SENT:
                break;
            case REQUEST_ACCEPTED:
                refreshTable();
                break;
            case REQUEST_REJECTED:
                refreshTable();
                break;
        }
        refreshTable();
    }
}
