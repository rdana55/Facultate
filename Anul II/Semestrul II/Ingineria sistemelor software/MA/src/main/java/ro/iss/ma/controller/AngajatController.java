package ro.iss.ma.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ro.iss.ma.domain.Angajat;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import ro.iss.ma.domain.Sarcina;
import ro.iss.ma.domain.Stare;
import ro.iss.ma.event.AngajatChangeEvent;
import ro.iss.ma.event.EventType;
import ro.iss.ma.observer.Observable;
import ro.iss.ma.observer.Observer;
import  ro.iss.ma.service.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class AngajatController implements Observer<AngajatChangeEvent> {

    private List<Observer<AngajatChangeEvent>> observers = new ArrayList<>();

    public void notifyObserver(AngajatChangeEvent t) {
        observers.forEach(observer -> observer.update(t));
    }


    @FXML
    private TableView<Sarcina> tableView;

    @FXML
    private TableColumn<Sarcina, String> sarcinaColumn;

    @FXML
    private TableColumn<Sarcina, Stare> statusColumn;

    @FXML
    private Button acceptaButton;

    @FXML
    private Button finalizeazaButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Label myLabel;

    private Service service;
    private Stage stage;
    private Angajat angajat;


    public void setService(Service service, Stage stage) {
        this.service = service;
        this.stage = stage;
        this.service.addObserver((Observer<AngajatChangeEvent>) this);
        //tableView.setItems(getSarciniForCurrentAngajat());
    }

    public void setAngajat(Angajat angajat) {
        this.angajat = angajat;
        myLabel.setText(angajat.getIntrare().toString());

        AngajatChangeEvent event = new AngajatChangeEvent(EventType.LOGIN, Optional.ofNullable(this.angajat));
        notifyObserver(event);
        tableView.setItems(getSarciniForCurrentAngajat());
    }

    @FXML
    public void initialize() {
        sarcinaColumn.setCellValueFactory(new PropertyValueFactory<>("descriere"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("stare"));

        acceptaButton.setOnAction(event -> {
            Sarcina selectedSarcina = tableView.getSelectionModel().getSelectedItem();
            if (selectedSarcina != null) {
                selectedSarcina.setStare(Stare.IN_DESFASURARE);
                service.updateSarcina(selectedSarcina);
                tableView.refresh();
                AngajatChangeEvent eventS = new AngajatChangeEvent(EventType.SARCINA, Optional.ofNullable(selectedSarcina.getAngajat()), selectedSarcina);
                notifyObserver(eventS);
                // Update the task in the database
            }
        });

        finalizeazaButton.setOnAction(event -> {
            Sarcina selectedSarcina = tableView.getSelectionModel().getSelectedItem();
            if (selectedSarcina != null) {
                selectedSarcina.setStare(Stare.REALIZATA);
                service.updateSarcina(selectedSarcina);
                tableView.refresh();
                AngajatChangeEvent eventS = new AngajatChangeEvent(EventType.SARCINA, Optional.ofNullable(selectedSarcina.getAngajat()), selectedSarcina);
                notifyObserver(eventS);
                // Update the task in the database
            }
        });

        logoutButton.setOnAction(event -> {
            // Set all non-REALIZATA tasks to NEREALIZATA
            service.getSarciniAngajat(angajat).stream()
                    .filter(sarcina -> sarcina.getStare() != Stare.REALIZATA)
                    .forEach(sarcina -> {
                        sarcina.setStare(Stare.NEREALIZATA);
                        service.updateSarcina(sarcina);
                        AngajatChangeEvent eventS = new AngajatChangeEvent(EventType.SARCINA, Optional.ofNullable(sarcina.getAngajat()), sarcina);
                        notifyObserver(eventS);
                    });
            angajat.setIesire(LocalDateTime.now());
            service.updateAngajatO(angajat);
            // Then navigate to the login view

            AngajatChangeEvent eventA = new AngajatChangeEvent(EventType.LOGOUT, Optional.ofNullable(angajat));
            notifyObserver(eventA);

            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/hello-view.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
                // Close the current window
                ((Node)(event.getSource())).getScene().getWindow().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private ObservableList<Sarcina> getSarciniForCurrentAngajat() {
        if (service != null) {
            if (angajat != null) {
                List<Sarcina> sarcinaList = service.getSarciniAngajat(angajat);
                return FXCollections.observableArrayList(sarcinaList);
            } else {
                // Handle the case where angajat is null
                //System.err.println("Angajat has not been initialized");
                return FXCollections.observableArrayList();
            }
        } else {
            // Handle the case where service is null
            throw new IllegalStateException("Service has not been initialized");
        }
    }


    @Override
    public void update(AngajatChangeEvent event) {
        if (event.getSarcina() != null) {
            Sarcina sarcina = event.getSarcina();
            if (event.getType() == EventType.SARCINA) {
                tableView.getItems().add(sarcina);
                tableView.setItems(getSarciniForCurrentAngajat());
            }
            else if (event.getType() == EventType.SARCINAD) {
                tableView.getItems().remove(sarcina);
                tableView.refresh();
            }
        }
        tableView.setItems(getSarciniForCurrentAngajat());
    }

}