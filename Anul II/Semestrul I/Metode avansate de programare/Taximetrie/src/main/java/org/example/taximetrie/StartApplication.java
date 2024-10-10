package org.example.taximetrie;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.taximetrie.controller.LoginController;
import org.example.taximetrie.domain.Sofer;
import org.example.taximetrie.repository.ComandaRepo;
import org.example.taximetrie.repository.PersoanaRepo;
import org.example.taximetrie.repository.SoferRepo;
import org.example.taximetrie.service.ComandaService;
import org.example.taximetrie.service.PersoanaService;
import org.example.taximetrie.service.SoferService;

import java.io.IOException;

public class StartApplication extends Application {

    PersoanaService persoanaService;
    SoferService soferService;

    ComandaService comandaService;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        String url="jdbc:postgresql://localhost:5432/Taximetrie";
        String username = "postgres";
        String password = "postgres";

        PersoanaRepo persoanaRepo = new PersoanaRepo(url, username, password);
        SoferRepo soferRepo = new SoferRepo(url, username, password);
        ComandaRepo comandaRepo = new ComandaRepo(url, username, password);

        persoanaService = new PersoanaService(persoanaRepo);
        soferService = new SoferService(soferRepo);
        comandaService = new ComandaService(comandaRepo);

        initView(primaryStage);
        primaryStage.setWidth(800);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    private void initView(Stage primaryStage) throws IOException{
        FXMLLoader userLoader=new FXMLLoader();
        userLoader.setLocation(getClass().getResource("login.fxml"));
        AnchorPane userLayout=userLoader.load();

        LoginController loginController=userLoader.getController();
        loginController.setService(persoanaService, soferService, comandaService,primaryStage);

        primaryStage.setScene(new Scene(userLayout));
    }
}

