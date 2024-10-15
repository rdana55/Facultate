package ro.iss.ma.controller;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ro.iss.ma.domain.Angajat;
import ro.iss.ma.domain.Sarcina;
import ro.iss.ma.domain.Sef;
import ro.iss.ma.event.AngajatChangeEvent;
import ro.iss.ma.event.EventType;
import ro.iss.ma.observer.Observer;
import ro.iss.ma.service.Service;
import ro.iss.ma.domain.Stare;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SefController implements Observer<AngajatChangeEvent> {

    @FXML
    private ListView<Angajat> angajatiListView;

    @FXML
    private TextArea sarcinaTextArea;

    @FXML
    private Button trimiteSarcinaButton;

    @FXML
    private TableView<Sarcina> sarciniTableView;

    @FXML
    private TextArea descriereNouaTextArea;

    @FXML
    private Button modificaSarcinaButton;

    @FXML
    private Button stergeSarcinaButton;

    private Service service;
    private Stage stage;
    private Sef sef;

    private ObservableList<Angajat> angajati = FXCollections.observableArrayList();

    private ObservableList<Sarcina> sarciniO = FXCollections.observableArrayList();

    public void setService(Service service, Stage stage) {
        this.service = service;
        this.stage = stage;
        this.service.addObserver(this);
        loadAngajati();
        loadSarcini();

    }

    public void setSef(Sef sef) {
        this.sef = sef;
    }

    @FXML
    private void initialize() {
        trimiteSarcinaButton.setOnAction(event -> handleTrimiteSarcina());
        modificaSarcinaButton.setOnAction(event -> handleModificaSarcina());
        stergeSarcinaButton.setOnAction(event -> handleStergeSarcina());
    }

    private void loadAngajati() {
        List<Angajat> angajati = service.getAllAngajati();
        LocalDate currentDate = LocalDate.now();

        List<Angajat> filteredAngajati = angajati.stream()
                .filter(angajat -> angajat.getIntrare().toLocalDate().isEqual(currentDate)
                        && angajat.getIesire() != null
                        && !angajat.getIesire().toLocalDate().isEqual(currentDate))
                .collect(Collectors.toList());

        angajatiListView.setCellFactory(param -> new ListCell<Angajat>() {
            @Override
            protected void updateItem(Angajat angajat, boolean empty) {
                super.updateItem(angajat, empty);

                if (empty || angajat == null) {
                    setText(null);
                } else {
                    setText(angajat.getNume() + " " + angajat.getPrenume());
                }
            }
        });
        this.angajati.setAll(filteredAngajati);
        angajatiListView.setItems(this.angajati);
    }

    private void loadSarcini() {
        List<Sarcina> sarcini = service.getAllSarcini();
        sarciniO.setAll(sarcini);

        TableColumn<Sarcina, String> descriereColumn = new TableColumn<>("Descriere");
        descriereColumn.setCellValueFactory(new PropertyValueFactory<>("descriere"));

        TableColumn<Sarcina, String> angajatColumn = new TableColumn<>("Angajat");
        angajatColumn.setCellValueFactory(sarcina -> {
            Angajat angajat = sarcina.getValue().getAngajat();
            return new SimpleStringProperty(angajat != null ? angajat.getNume() + " " + angajat.getPrenume() : "");
        });

        TableColumn<Sarcina, Stare> stareColumn = new TableColumn<>("Stare");
        stareColumn.setCellValueFactory(new PropertyValueFactory<>("stare"));

        sarciniTableView.getColumns().setAll(descriereColumn, angajatColumn, stareColumn);
        sarciniTableView.setItems(sarciniO);
    }

    @FXML
    private void handleTrimiteSarcina() {
        Angajat selectedAngajat = angajatiListView.getSelectionModel().getSelectedItem();
        String descriere = sarcinaTextArea.getText();
        if (selectedAngajat != null && descriere != null && !descriere.isEmpty()) {
            Sarcina newSarcina = new Sarcina(selectedAngajat, descriere, Stare.PRIMITA);
            //System.out.println(newSarcina.getAngajat().getId());
            service.addSarcina(newSarcina);
            service.notifyObserver(new AngajatChangeEvent(EventType.SARCINA, Optional.ofNullable(newSarcina.getAngajat()), newSarcina));
            sarciniTableView.getItems().add(newSarcina);
        }
    }

    @FXML
    private void handleModificaSarcina() {
        Sarcina selectedSarcina = sarciniTableView.getSelectionModel().getSelectedItem();
        String newDescriere = descriereNouaTextArea.getText();
        if (selectedSarcina != null && newDescriere != null && !newDescriere.isEmpty()) {
            selectedSarcina.setDescriere(newDescriere);
            selectedSarcina.setStare(Stare.ACTUALIZATA);
            service.updateSarcina(selectedSarcina);
            service.notifyObserver(new AngajatChangeEvent(EventType.SARCINA, Optional.ofNullable(selectedSarcina.getAngajat()), selectedSarcina));
            sarciniTableView.refresh();
        }
    }

    @FXML
    private void handleStergeSarcina() {
        Sarcina selectedSarcina = sarciniTableView.getSelectionModel().getSelectedItem();
        System.out.println(selectedSarcina.getId());
        if (selectedSarcina != null) {
            service.deleteSarcina(selectedSarcina);
            service.notifyObserver(new AngajatChangeEvent(EventType.SARCINAD, Optional.ofNullable(selectedSarcina.getAngajat()), selectedSarcina));
            sarciniTableView.getItems().remove(selectedSarcina);
            sarciniTableView.refresh();
        }
    }

    @Override
    public void update(AngajatChangeEvent event) {
        if (event.getAngajat().isPresent()) {
            String angajatName = event.getAngajat().get().getNume();
            if (event.getType() == EventType.LOGOUT) {
                angajatiListView.getItems().removeIf(angajat -> angajat.getNume().equals(angajatName));
                // Create an alert
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informare");
                alert.setHeaderText(null);
                alert.setContentText("Angajatul " + angajatName + " s-a deconectat.");
                // Show the alert
                alert.showAndWait();
            } else if (event.getType() == EventType.LOGIN) {
                // Add the logged in employee to the list
                Angajat loggedInAngajat = event.getAngajat().get();
                angajatiListView.getItems().add(loggedInAngajat);
            } else if (event.getType()==EventType.SARCINA){
                if(event.getSarcina() != null) {
                    Sarcina sarcina = event.getSarcina();
                    if (event.getSarcina().getStare() != Stare.PRIMITA) {
                        sarciniTableView.getItems().removeIf(sarcina1 -> sarcina1.getId().equals(sarcina.getId()));
                        sarciniTableView.getItems().add(sarcina);

                    }
                }
            }
        }
        loadAngajati();
        loadSarcini();
    }
}