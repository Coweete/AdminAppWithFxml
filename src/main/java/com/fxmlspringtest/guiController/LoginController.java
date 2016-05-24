package com.fxmlspringtest.guiController;

import com.fxmlspringtest.Controller.AdminController;
import com.fxmlspringtest.services.ScreenConfiguration;
import com.sun.org.apache.bcel.internal.util.ClassLoader;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ParameterList;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;

/**
 * Created by jonatan on 2016-05-08.
 */
public class LoginController implements DialogController{

    private AdminController controller;
    private FXMLDialog dialog;

    @FXML
    Label header;

    @FXML
    TextField username;

    @FXML
    TextField password;

    @FXML
    Text txtError;

    @FXML
    Button btnSignin;

    public LoginController(AdminController controller) {;
        this.controller = controller;
    }

    public void actionLogin(ActionEvent actionEvent) {
       login();
    }



    @Override
    public void setController(AdminController controller) {
        this.controller = controller;
    }

    public void setFocus() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                btnSignin.requestFocus();
            }
        });

    }

    public void setErrorText(String errorText){
        txtError.setText(errorText);
    }

    public void clearError(Event event) {
        if(txtError.getText() != null){
            txtError.setText("");
        }
    }

    public void enterLogin(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER)){
            login();
        }
    }

    private void login() {
        if(username.getText() != null && password.getText() != null){
            controller.login(username.getText(),password.getText());
        }
    }
}
