package ro.clientfx;

import javafx.scene.control.Alert;

public class MessageAlert {
    public static void showMessage(Alert.AlertType alertType, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}