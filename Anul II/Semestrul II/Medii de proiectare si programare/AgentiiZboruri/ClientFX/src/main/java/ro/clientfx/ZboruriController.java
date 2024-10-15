package ro.clientfx;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ro.model.Angajat;

import ro.model.Zbor;
import ro.networking.ServicesImpl;
import ro.services.Services;

import java.time.LocalDateTime;

public class ZboruriController {
    private Services service;
    private Angajat angajat;
    Stage dialogStage;

    @FXML
    TableView<Zbor> tabelZboruri;
    @FXML
    TableColumn<Zbor, String> columnPlecare;
    @FXML
    TableColumn<Zbor, String> columnSosire;
    @FXML
    TableColumn<Zbor, String> columnLocuri;
    @FXML
    TableColumn<Zbor, String> columnOcupate;
    @FXML
    ComboBox<String> comboFrom;
    @FXML
    ComboBox<String> comboTo;
    @FXML
    DatePicker dataFlight;

    @FXML
    private TextField client;

    @FXML
    Button cumpara;
    @FXML
    Button logoutButton;

    public void setAngajat(Angajat angajat) {
        this.angajat = angajat;
    }

    public void setService(Services service, Stage stage) {
        this.service = service;
        this.dialogStage = stage;
        logoutButton.setOnAction(event -> MainWindow.handleLogout(service, dialogStage));
        initModel();
    }

    @FXML
    public void initialize() {
        columnPlecare.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFrom().toString()));
        columnSosire.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTo().toString()));
        columnLocuri.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDataOra().toString()));
        columnOcupate.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getLocuriDisponibile()- service.getTicketsSoldForFlight(cellData.getValue().getId()))));
    }

    private void initModel() {
        ZboruriListModel model = new ZboruriListModel(service.getFlightList(), service);
        tabelZboruri.setItems(model.getFlightList());
        comboFrom.setItems(model.getUniqueFromList());
        comboTo.setItems(model.getUniqueToList());
    }

    @FXML
    private void handleSearch() {
        LocalDateTime data = dataFlight.getValue().atStartOfDay();
        String plecare = comboFrom.getValue();
        String sosire = comboTo.getValue();

        ZboruriListModel model = new ZboruriListModel(service.getFlightList(), service);
        tabelZboruri.setItems(model.getFilteredFlightList(plecare, sosire, data));
    }

    private void clearFields(){
        client.setText("");
    }

    @FXML
    private void handleCumpara() {
        String clientId = client.getText();
        Zbor flight = tabelZboruri.getSelectionModel().getSelectedItem();
        service.saveTicket(angajat.getId(), flight.getId(), Integer.valueOf(clientId));
        clearFields();
        MessageAlert.showMessage(Alert.AlertType.INFORMATION, "Cumparat!", "Biletul a fost achizitionat.");
    }
}