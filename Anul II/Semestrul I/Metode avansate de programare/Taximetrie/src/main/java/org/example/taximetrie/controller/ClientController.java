package org.example.taximetrie.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.taximetrie.domain.Persoana;
import org.example.taximetrie.events.ClientChangeEvent;
import org.example.taximetrie.events.Event;
import org.example.taximetrie.events.TaximetristChangeEvent;
import org.example.taximetrie.observer.Observer;
import org.example.taximetrie.service.ComandaService;
import org.example.taximetrie.service.PersoanaService;
import org.example.taximetrie.service.SoferService;

import java.time.LocalDateTime;
import java.util.Objects;

public class ClientController implements Observer<Event> {

    PersoanaService clientService;
    SoferService taxiService;

    ComandaService comandaService;
    Persoana currentClient;

    Long soferId;

    @FXML
    TextField locatie;

    @FXML
    Button cauta;

    @FXML
    Label timp;

    @FXML
    Label indicativ;

    @FXML
    Button accept;
    @FXML
    Button cancel;

    public void setServices(PersoanaService clientService, SoferService taxiService, ComandaService comandaService, Persoana currentClient) {
        this.clientService = clientService;
        this.taxiService = taxiService;
        this.comandaService=comandaService;
        this.currentClient = currentClient;
        clientService.addObserver(this);
        taxiService.addObserver(this);
    }

    @FXML
    public void handleCautaButtonAction() {
        String locatieText = locatie.getText();
        clientService.notifyObserver(new TaximetristChangeEvent(currentClient.getId(), currentClient.getNume(), locatieText));
    }

    @FXML
    public void handleAcceptButton() {
        clientService.notifyObserver(new TaximetristChangeEvent(currentClient.getId(), soferId, "ACCEPT"));
        //comandaService.saveComanda(currentClient.getId(), soferId, LocalDateTime.now());
    }

    @FXML
    public void handleCancelButton() {
        clientService.notifyObserver(new TaximetristChangeEvent(currentClient.getId(), soferId, "CANCEL"));
    }


    @Override
    public void update(Event event) {
        if (event instanceof ClientChangeEvent) {
            ClientChangeEvent clientChangeEvent = (ClientChangeEvent) event;
            if (clientChangeEvent.getTimpMaxim() != null && clientChangeEvent.getIndicativMasina() != null && Objects.equals(clientChangeEvent.getClientId(), currentClient.getId())) {
                timp.setText(clientChangeEvent.getTimpMaxim());
                indicativ.setText(clientChangeEvent.getIndicativMasina());
                soferId=clientChangeEvent.getSoferId();
            }
        }
    }


}
