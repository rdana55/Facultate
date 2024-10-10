package com.example.socialnetworkmap.controller;

import com.example.socialnetworkmap.domain.User;
import com.example.socialnetworkmap.exceptions.ValidationException;
import com.example.socialnetworkmap.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class EditUserController {
    @FXML
    private TextField fnTextField;
    @FXML
    private TextField snTextField;
    @FXML
    private TextField psTextField;
    private UserService service;

    Stage dialogStage;
    User user;

    @FXML
    private void initialize(){}

    public void setService(UserService service, Stage stage, User u){
        this.service=service;
        this.dialogStage=stage;
        this.user=u;
        if (null!=u){
            setFields(u);
            //textFieldId.setEditable(false);
        }
    }

    private void setFields(User u){
        fnTextField.setText(u.getFirstName());
        snTextField.setText(u.getLastName());
        psTextField.setText(u.getPassword());
        //friends.setText(u.getFriends().toString());
    }

    private void clearFields(){
        fnTextField.setText("");
        snTextField.setText("");
        psTextField.setText("");
    }

    @FXML
    public void handleSave() throws NoSuchAlgorithmException {
        String fn=fnTextField.getText();
        String sn=snTextField.getText();
        String ps=psTextField.getText();
        User u=new User(fn,sn,ps);
//        if (user.getId()!=null){
//            u.setId(user.getId());
//        }
        if (null==this.user)
            saveUser(u);
        else {
            u.setId(user.getId());
            updateUser(u);
        }
    }

    private void saveUser(User u){
        try{
            User usr=this.service.createUser(u.getFirstName(),u.getLastName(),u.getPassword());
            if(usr==null)
                dialogStage.close();
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Save User","Utilizatorul a fost salvat.");
        }
        catch (ValidationException e){
            MessageAlert.showErrorMessage(null,e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        dialogStage.close();
    }

    private void updateUser(User u){
        try{
            Optional<User> usr=this.service.update(u);
            if(usr.isEmpty())
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Modify User","Utilizator a fost actualizat.");
        }
        catch (ValidationException e){
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
        dialogStage.close();
    }

    @FXML
    public void handleCancel(){
        dialogStage.close();
    }
}