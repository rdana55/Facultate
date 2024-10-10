package org.example.taximetrie.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.example.taximetrie.domain.Comanda;
import org.example.taximetrie.domain.Persoana;
import org.example.taximetrie.domain.Sofer;
import org.example.taximetrie.events.Event;
import org.example.taximetrie.events.TaximetristChangeEvent;
import org.example.taximetrie.events.ClientChangeEvent;
import org.example.taximetrie.events.TaximetristChangeEvent;
import org.example.taximetrie.observer.Observer;
import org.example.taximetrie.paging.Page;
import org.example.taximetrie.paging.Pageable;
import org.example.taximetrie.service.ComandaService;
import org.example.taximetrie.service.PersoanaService;
import org.example.taximetrie.service.SoferService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TaximetristController implements Observer<Event>{

    @FXML
    ListView comenzi;

    @FXML
    TableView<Persoana> clientiOnorati;

    @FXML
    TableColumn<Persoana, String> clientiOnoratiColumn;

    @FXML
    TextField timp;

    @FXML
    Button onoreaza;

    @FXML
    DatePicker data;

    @FXML
    Button medieComenzi;

    @FXML
    Button clientFidel;

    Sofer currentSofer;

    PersoanaService clientService;
    SoferService taxiService;

    Long selectedClientId;

    ComandaService comandaService;
    private int pageSize = 5;
    private int currentPage = 0;
    private int totalNrOfElemsFriend = 0;

    @FXML
    private Button btn_prev;
    @FXML
    private Button btn_next;

    @FXML
    private TextField txt_pagina;

    ObservableList<Persoana> observablePersoana = FXCollections.observableArrayList();

    public void setServices(PersoanaService clientService, SoferService taxiService, ComandaService comandaService, Sofer currentSofer) {
        this.clientService = clientService;
        this.taxiService = taxiService;
        this.comandaService=comandaService;
        this.currentSofer = currentSofer;
        clientService.addObserver(this);
        taxiService.addObserver(this);
        initModel();
    }

    @FXML
    public void initialize() {
        clientiOnoratiColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNume()));
        clientiOnorati.setItems(observablePersoana);
        txt_pagina.textProperty().addListener(o -> updatePage());
        //List<Persoana> clienti = service.findOneSofer(id_taxi).get().getListClienti();
        //columnCereri.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Persoana, String>, ObservableValue<String>>) new SimpleStringProperty(service.findOneSofer(id_taxi).get().getListClienti().toString()));
    }

    private void initModel() {
        //initModelCereri();
        Page<Persoana> pageFriend = clientService.findAllPersoanaPage(new Pageable(currentPage, pageSize));
        int maxPageFriend = (int) Math.ceil((double) pageFriend.getTotalNrOfElems() / pageSize) - 1;

        if (currentPage > maxPageFriend) {
            currentPage = maxPageFriend;

            pageFriend = clientService.findAllPersoanaPage(new Pageable(currentPage, pageSize));
        }

        observablePersoana.setAll(StreamSupport.stream(pageFriend.getElementsOnPage().spliterator(),
                false).collect(Collectors.toList()));

        totalNrOfElemsFriend = pageFriend.getTotalNrOfElems();

        btn_prev.setDisable(currentPage == 0);
        btn_next.setDisable((currentPage + 1) * pageSize >= totalNrOfElemsFriend);
    }


    private void updatePage() {
        if (txt_pagina.getText().isEmpty() || Integer.parseInt(txt_pagina.getText()) == 0) {
            initModel();
        } else {
            this.pageSize = Integer.parseInt(txt_pagina.getText());
            this.currentPage = 0;
            initModel();
        }
    }


    @FXML
    public void handlePrec(ActionEvent ev) {
        currentPage--;
        initModel();
    }

    @FXML
    public void handleNext(ActionEvent ev) {
        currentPage++;
        initModel();
    }


    public Long getIdByNume(String nume) {
        Iterable<Persoana> persoane = clientService.findAll();
        for (Persoana pers : persoane) {
            if (pers.getNume().equals(nume)) {
                return pers.getId();
            }
        }
        return null;
    }

    @FXML
    public void handleOnoreazaButtonAction() {
        String timpText = timp.getText();
        selectedClientId=getIdByNume(comenzi.getSelectionModel().getSelectedItem().toString().split(" ")[0]);
        if (selectedClientId != null) {
            ClientChangeEvent event = new ClientChangeEvent(selectedClientId, timpText, currentSofer.getIndicativMasina(), currentSofer.getId());
            if (event.getClientId().equals(selectedClientId)) {
                taxiService.notifyObserver(event);
            }
        }
    }

    @FXML
    public void handleDataButtonAction() {
        LocalDateTime selectedDate = data.getValue().atStartOfDay();
        List<Comanda> comenzi=comandaService.findComenziByDate(selectedDate);
        String comenziText = comenzi.stream()
                .map(Comanda::toString)
                .collect(Collectors.joining("\n"));
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Comenzi");
        alert.setHeaderText(null);
        alert.setContentText(comenziText);
        alert.showAndWait();
    }

    @FXML
    public void handleMedie(){
        int nrComenzi=comandaService.getNrComenziInUltimile3Luni();
        nrComenzi=nrComenzi/84;
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Medie comenzi");
        alert.setHeaderText(null);
        alert.setContentText("Numarul mediu de comenzi in ultimele 3 luni este: "+nrComenzi);
        alert.showAndWait();
    }

    @FXML
    public void handleClientFidel(){
        List<Persoana> clienti=comandaService.findClientiOnorati(currentSofer.getId());
        Map<String, Integer> nameCount = new HashMap<>();
        for (Persoana client : clienti) {
            nameCount.put(client.getNume(), nameCount.getOrDefault(client.getNume(), 0) + 1);
        }

        String mostFrequentClient = Collections.max(nameCount.entrySet(), Map.Entry.comparingByValue()).getKey();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Client fidel");
        alert.setHeaderText(null);
        alert.setContentText("Clientul fidel este: "+mostFrequentClient);
        alert.showAndWait();

    }

    @Override
    public void update(Event event) {
        if (event instanceof TaximetristChangeEvent) {
            TaximetristChangeEvent taximetristChangeEvent = (TaximetristChangeEvent) event;
            if (taximetristChangeEvent.getNumeClient() != null && taximetristChangeEvent.getLocatie() != null) {
                comenzi.getItems().add(taximetristChangeEvent.getNumeClient() + " " + taximetristChangeEvent.getLocatie());
                selectedClientId = taximetristChangeEvent.getClientId();
            } else if (taximetristChangeEvent.getRaspuns() != null) {
                if (taximetristChangeEvent.getRaspuns().equals("ACCEPT") && taximetristChangeEvent.getSoferId().equals(currentSofer.getId())) {
                    Persoana client=clientService.findPersonById(selectedClientId).get();
                    String nume=client.getNume();
                    comandaService.saveComanda(selectedClientId, currentSofer.getId(), LocalDateTime.now());
                    comenzi.getItems().removeIf(item -> ((String)item).startsWith(nume));
                    selectedClientId = null;
                }
                else if (taximetristChangeEvent.getRaspuns().equals("CANCEL") && taximetristChangeEvent.getSoferId().equals(currentSofer.getId())) {
                    Persoana client=clientService.findPersonById(selectedClientId).get();
                    String nume=client.getNume();
                    comenzi.getItems().removeIf(item -> ((String)item).startsWith(nume));
                    selectedClientId = null;
                }
            }
        }
    }
}