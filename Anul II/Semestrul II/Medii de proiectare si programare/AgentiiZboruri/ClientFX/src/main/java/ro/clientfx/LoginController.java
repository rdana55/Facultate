package ro.clientfx;

import javafx.concurrent.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ro.model.Angajat;
import ro.networking.ServicesImpl;
import ro.services.Services;

import java.io.IOException;
import java.util.Optional;

public class LoginController {
    @FXML
    private TextField unTextField;
    @FXML
    private PasswordField psTextField;

    private Services service;
    private Angajat angajat;
    private Parent parent; // added field
    private ZboruriController zboruriController; // added field

    private Stage primaryStage; // added field

    public void setStage(Stage primaryStage) { // added method
        this.primaryStage = primaryStage;
    }

    @FXML
    private void initialize() {
    }

    public void setService(Services service) {
        this.service = service;
    }

    public void setParent(Parent parent) { // added method
        this.parent = parent;
    }

    public void setZboruriController(ZboruriController zboruriController) { // added method
        this.zboruriController = zboruriController;
    }

    private void clearFields() {
        unTextField.setText("");
        psTextField.setText("");
    }

    @FXML
    public void handleLogin() {
        String username = unTextField.getText();
        String password = psTextField.getText();

        if (service.login(username, password)) {
            Optional<Angajat> userOptional = service.findOneU(username);
            if (userOptional.isPresent()) {
                Angajat user = userOptional.get();
                switchToAngajatView(user);
            } else {
                MessageAlert.showMessage( Alert.AlertType.INFORMATION, "Invalid", "Utilizatorul sau parola sunt incorecte.");
            }
        }
    }

    private void switchToAngajatView(Angajat user) {
        FXMLLoader zboruriLoader = new FXMLLoader(getClass().getClassLoader().getResource("angajat.fxml"));
        Parent zboruriRoot;
        try {
            zboruriRoot = zboruriLoader.load();
            ZboruriController zboruriController = zboruriLoader.getController();
            zboruriController.setService(service, primaryStage);
            zboruriController.setAngajat(user);
            Scene scene = new Scene(zboruriRoot);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}