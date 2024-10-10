package org.example.taximetrie.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.taximetrie.domain.Persoana;
import org.example.taximetrie.domain.Sofer;
import org.example.taximetrie.service.ComandaService;
import org.example.taximetrie.service.PersoanaService;
import org.example.taximetrie.service.SoferService;

import java.io.IOException;
import java.util.Objects;

public class LoginController {
    @FXML
    private TextField usrnmTextField;
    @FXML
    Button login;

    Stage dialogStage;
    PersoanaService serviceP;
    SoferService serviceS;

    ComandaService serviceC;

    @FXML
    private void initialize(){}

    public void setService(PersoanaService serviceP, SoferService serviceS, ComandaService serviceC,Stage stage){
        this.serviceP=serviceP;
        this.serviceS=serviceS;
        this.serviceC=serviceC;
        this.dialogStage=stage;
    }

    @FXML
    public void handleLogin() {
        String username = usrnmTextField.getText();

        Iterable<Persoana> clienti = serviceP.findAll();
        for (Persoana pers : clienti) {
            if (pers.getUsername().equals(username)) {
                showClientsPage(pers);
                //dialogStage.close();
                return;
            }
        }
        //MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Invalid", "Utilizatorul sau parola sunt incorecte.");
        Iterable<Sofer> soferi = serviceS.findAll();
        for (Sofer pers : soferi) {
            if (pers.getUsername().equals(username)) {
                showSoferiPage(pers);
                //dialogStage.close();
                return;
            }
        }
    }

    private void showClientsPage(Persoana u){
        try{
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("/org/example/taximetrie/client.fxml"));

            AnchorPane root=loader.load();

            Stage requestStage=new Stage();
            requestStage.setTitle("Client "+u.getNume().toString());
            requestStage.initModality(Modality.WINDOW_MODAL);
            Scene scene =new Scene(root);
            requestStage.setScene(scene);

            ClientController clientController=loader.getController();
            clientController.setServices(serviceP,serviceS,serviceC,u);

            requestStage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


    private void showSoferiPage(Sofer u){
        try{
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("/org/example/taximetrie/taximetrist.fxml"));

            AnchorPane root=loader.load();

            Stage requestStage=new Stage();
            requestStage.setTitle("Taximetrist "+u.getNume().toString());
            requestStage.initModality(Modality.WINDOW_MODAL);
            Scene scene =new Scene(root);
            requestStage.setScene(scene);

            TaximetristController taximetristController=loader.getController();
            taximetristController.setServices(serviceP,serviceS,serviceC, u);

            requestStage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
