package ro.clientfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ro.networking.ServicesImpl;
import ro.networking.rpcprotocol.ServicesRpcProxy;
import ro.persistence.*;
import ro.services.Services;

import java.io.IOException;
import java.util.Properties;

public class StartRpcClientFx extends Application {

    private static int defaultChatPort = 55555;
    private static String defaultServer = "localhost";

    private Services server;

    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartRpcClientFx.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatclient.properties " + e);
            return;
        }

        String serverIP = clientProps.getProperty("server.host", defaultServer);
        int serverPort = defaultChatPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        server = new ServicesRpcProxy(serverIP, serverPort);

        System.out.println("okkkkk");


        FXMLLoader loginLoader = new FXMLLoader(getClass().getClassLoader().getResource("hello-view.fxml"));
        Parent loginRoot = loginLoader.load();
        LoginController loginController = loginLoader.getController();
        loginController.setService(server);

        primaryStage.setTitle("Log In");
        primaryStage.setScene(new Scene(loginRoot, 300, 130));
        primaryStage.show();


    }

    @Override
    public void stop() {
        if (server instanceof ServicesRpcProxy) {
            ((ServicesRpcProxy) server).close();
        }
    }
}