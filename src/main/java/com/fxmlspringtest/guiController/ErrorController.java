package com.fxmlspringtest.guiController;

import com.fxmlspringtest.Controller.AdminController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * Controlls the error dialog
 * Created by Jonatan Fridsten on 2016-05-17.
 */
public class ErrorController implements DialogController {

    private FXMLDialog dialog;
    private AdminController controller;

    @FXML
    Button btnOk;
    @FXML
    Text txtError;

    public void setDialog(FXMLDialog dialog){
        dialog.setResizable(false);
        this.dialog = dialog;
    }
    @Override
    public void setController(AdminController controller) {
        this.controller = controller;
    }

    public void cancel(ActionEvent actionEvent) {
        dialog.close();
    }

    public void setText(String text){
        txtError.setText(text);
    }
}
