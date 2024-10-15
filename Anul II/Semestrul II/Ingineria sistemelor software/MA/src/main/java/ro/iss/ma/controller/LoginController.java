package ro.iss.ma.controller;

import ro.iss.ma.domain.Angajat;
import ro.iss.ma.domain.Sef;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ro.iss.ma.service.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

public class LoginController {

    @FXML
    private TextField unTextField;
    @FXML
    private PasswordField psTextField;

    private Service service;
    private Stage stage;

    @FXML
    private void initialize() {
    }

    public LoginController( ) {
    }

    public void setService(Service service) {

        this.service = service;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    private void clearFields() {
        unTextField.setText("");
        psTextField.setText("");

        unTextField.setPromptText("username");
        psTextField.setPromptText("password");
    }

    @FXML
    public void handleLogin() {
        String username = unTextField.getText();
        String password = psTextField.getText();

        if (service.loginAngajat(username, password)){
            Optional<Angajat> userOptional = service.findOneA(username);
            Optional<Sef> userOptional2 = service.findOneS(username);
            if (userOptional.isPresent()) {
                Angajat user = userOptional.get();
                showAngajatWindow(user, service);
            }
            else if (userOptional2.isPresent()){
                Sef user = userOptional2.get();
                showSefWindow(user, service);
            }
            else{
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Invalid", "Utilizatorul sau parola sunt incorecte.");
            }
        }
    }

    public static void showSefWindow(Sef u, Service service) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(LoginController.class.getResource("/sef.fxml"));

            AnchorPane root = loader.load();

            Stage requestStage = new Stage();
            requestStage.setTitle(u.getNume()+" "+u.getPrenume());
            requestStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            requestStage.setScene(scene);

            SefController angajatController = loader.getController();
            angajatController.setService(service, requestStage);
            angajatController.setSef(u);

            requestStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showAngajatWindow(Angajat u, Service service) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(LoginController.class.getResource("/angajat.fxml"));

            AnchorPane root = loader.load();

            Stage requestStage = new Stage();
            requestStage.setTitle(u.getNume()+" "+u.getPrenume());
            requestStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            requestStage.setScene(scene);

            AngajatController angajatController = loader.getController();
            angajatController.setService(service, requestStage);
            u.setIntrare(LocalDateTime.now());
            service.updateAngajatI(u);
            angajatController.setAngajat(u);
            requestStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}