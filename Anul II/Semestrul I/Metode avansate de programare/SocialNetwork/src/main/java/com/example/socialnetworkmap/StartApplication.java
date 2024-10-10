package com.example.socialnetworkmap;

import com.example.socialnetworkmap.controller.HelloController;
import com.example.socialnetworkmap.repository.*;
import com.example.socialnetworkmap.service.FriendshipRequestsService;
import com.example.socialnetworkmap.service.FriendshipService;
import com.example.socialnetworkmap.service.MessageService;
import com.example.socialnetworkmap.service.UserService;
import com.example.socialnetworkmap.validators.UserValidator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class StartApplication extends Application {

    UserService userService;
    FriendshipService friendshipService;

    FriendshipRequestsService friendshipRequestsService;

    MessageService messageService;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        String url="jdbc:postgresql://localhost:5432/SocialNetwork";
        String username = "postgres";
        String password = "postgres";

        UserValidator validator = new UserValidator();
        UserDbRepo userDbRepo = new UserDbRepo(url, username, password);
        FriendshipDbRepo friendshipDbRepo = new FriendshipDbRepo(url, username, password);
        InMemoryRepo repo = new InMemoryRepo(validator, userDbRepo, friendshipDbRepo);
        MessageDbRepo messageDbRepo = new MessageDbRepo(url, username, password, userDbRepo);

        friendshipService = new FriendshipService(repo);
        friendshipRequestsService = new FriendshipRequestsService(new FriendshipRequestsRepo(url, username, password, userDbRepo), friendshipService);
        userService = new UserService(repo, friendshipService, friendshipRequestsService);
        messageService = new MessageService(messageDbRepo, userService);

        initView(primaryStage);
        primaryStage.setWidth(800);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    private void initView(Stage primaryStage) throws IOException{
        FXMLLoader userLoader=new FXMLLoader();
        userLoader.setLocation(getClass().getResource("/view/hello.fxml"));
        AnchorPane userLayout=userLoader.load();

        HelloController helloController=userLoader.getController();
        helloController.setService(userService,friendshipRequestsService,messageService);

        primaryStage.setScene(new Scene(userLayout));
    }
}
