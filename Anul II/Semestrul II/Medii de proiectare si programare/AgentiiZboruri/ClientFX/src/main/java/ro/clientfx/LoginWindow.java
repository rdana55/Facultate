package ro.clientfx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ro.model.Angajat;
import ro.networking.ServicesImpl;
import ro.services.Services;

import java.io.IOException;

public class LoginWindow {
    public static void showPage(Angajat u, Services service) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(LoginWindow.class.getResource("/ro/clientfx/clientfx/angajat.fxml"));

            AnchorPane root = loader.load();

            Stage requestStage = new Stage();
            requestStage.setTitle(u.getNume()+" "+u.getPrenume());
            requestStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            requestStage.setScene(scene);

            ZboruriController angajatController = loader.getController();
            angajatController.setService(service, requestStage);
            angajatController.setAngajat(u);

            requestStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}