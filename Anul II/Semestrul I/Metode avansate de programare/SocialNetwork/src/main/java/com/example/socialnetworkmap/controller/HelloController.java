package com.example.socialnetworkmap.controller;

import com.example.socialnetworkmap.domain.User;
import com.example.socialnetworkmap.service.FriendshipRequestsService;
import com.example.socialnetworkmap.service.MessageService;
import com.example.socialnetworkmap.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    @FXML
    Button login;
    @FXML
    Button create;

    UserService service;
    FriendshipRequestsService friendshipRequestsService;

    MessageService messageService;

    public void setService(UserService userService, FriendshipRequestsService fService, MessageService msgService) {
        service = userService;
        friendshipRequestsService=fService;
        messageService=msgService;
    }

    @FXML
    public void handleLogin(ActionEvent actionEvent){
        showUserLoginDialog(null);
    }

    @FXML
    public void handleCreate(ActionEvent actionEvent){
        showUserEditDialog(null);
    }

    public void showUserEditDialog(User user){
        try{
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/editUser.fxml"));

            AnchorPane root=loader.load();

            Stage dialogStage=new Stage();
            dialogStage.setTitle("Create account");
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

    public void showUserLoginDialog(User user){
        try{
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/login.fxml"));

            AnchorPane root=loader.load();

            Stage dialogStage=new Stage();
            dialogStage.setTitle("Log into your account");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene =new Scene(root);
            dialogStage.setScene(scene);

            LoginUserController loginUserController=loader.getController();
            loginUserController.setService(service,messageService,friendshipRequestsService,dialogStage);

            dialogStage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
