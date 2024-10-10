package com.example.socialnetworkmap.controller;

import com.example.socialnetworkmap.domain.User;
import com.example.socialnetworkmap.service.FriendshipRequestsService;
import com.example.socialnetworkmap.service.MessageService;
import com.example.socialnetworkmap.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginUserController {
    @FXML
    private TextField fnTextField;
    @FXML
    private TextField snTextField;
    @FXML
    private TextField psTextField;
    @FXML
    Button login;
    private UserService service;

    private MessageService messageService;

    private FriendshipRequestsService friendshipRequestsService;

    Stage dialogStage;
    User user;

    @FXML
    private void initialize(){}

    public void setService(UserService service, MessageService messageService, FriendshipRequestsService friendshipRequestsService, Stage stage){
        this.service=service;
        this.messageService=messageService;
        this.friendshipRequestsService=friendshipRequestsService;
        this.dialogStage=stage;
        //this.user=u;
        /*
        if (null!=u){
            setFields(u);
            //textFieldId.setEditable(false);
        }

         */
    }

    /*
    private void setFields(User u){
        fnTextField.setText(u.getFirstName());
        snTextField.setText(u.getLastName());
        psTextField.setText(u.getPassword());
        //friends.setText(u.getFriends().toString());
    }

     */

    private void clearFields(){
        fnTextField.setText("");
        snTextField.setText("");
        psTextField.setText("");
    }

    @FXML
    public void handleLogin() {
        String firstName = fnTextField.getText();
        String lastName = snTextField.getText();
        String password = psTextField.getText();

        String hashedPassword = UserService.hashPassword(password);

        Iterable<User> users = service.findAll();
        for (User user : users) {
            if (user.getFirstName().equals(firstName) && user.getLastName().equals(lastName) && Objects.equals(user.getPassword(), hashedPassword)) {
                showMessagePage(user);
                dialogStage.close();
                return;
            }
        }
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Invalid", "Utilizatorul sau parola sunt incorecte.");
    }


    private void showMessagePage(User u){
        try{
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/messages.fxml"));

            AnchorPane root=loader.load();

            Stage requestStage=new Stage();
            requestStage.setTitle(u.toString());
            requestStage.initModality(Modality.WINDOW_MODAL);
            Scene scene =new Scene(root);
            requestStage.setScene(scene);

            MessageController messageController=loader.getController();
            messageController.setServices(service,messageService,friendshipRequestsService,u);

            requestStage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}