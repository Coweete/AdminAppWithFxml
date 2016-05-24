package com.fxmlspringtest.guiController;

import com.fxmlspringtest.Controller.AdminController;
import com.fxmlspringtest.model.Account;
import com.fxmlspringtest.model.RfidKey;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jonatan on 2016-05-17.
 */
public class UpdateUserController implements DialogController {

    private AdminController adminController;
    private static final Logger log = LoggerFactory.getLogger(UpdateUserController.class);
    private FXMLDialog dialog;
    private Account account;
    private boolean scanCardActive = false;

    @FXML
    PasswordField passwordField1;
    @FXML
    PasswordField passwordField2;
    @FXML
    TextField txfUsername;
    @FXML
    TextField txfFirstname;
    @FXML
    TextField txfLastname;
    @FXML
    TextField txfRfid;
    @FXML
    Button btnScanCard;
    @FXML
    Button btnCancel;
    @FXML
    Button btnUpdateUser;
    @FXML
    Label scanAndErrorMessage;


    public UpdateUserController(AdminController controller) {
        adminController = controller;
    }

    @Override
    public void setController(AdminController controller) {
        this.adminController = controller;
    }

    public void setUserDetails(Account account) {
        this.account = account;
        System.out.println("In updatecontroller " + account.toString());
        txfFirstname.setText(account.getFirstName());
        txfLastname.setText(account.getLastName());
        txfUsername.setText(account.getUsername());
        txfRfid.setText(account.getRfidKey().getId());
    }

    public void cancel(ActionEvent actionEvent) {
        //checks if the scan is active
        if (scanCardActive == true) {
            adminController.closeRxTx();
        }
        adminController.cancel();
    }


    public void scanCard(ActionEvent actionEvent) {
        scanCardActive = true;
        btnScanCard.setDisable(true);
        adminController.scanCard(2);
    }

    public void updateUser(ActionEvent actionEvent) {
        log.info("Update User");
        if (txfFirstname.getText().equals("") || txfLastname.getText().equals("") || txfRfid.getText().equals("") ||
                txfUsername.getText().equals("")) {
            log.info("Missing fields");
            scanAndErrorMessage.setText("Missing some fields");
        } else {
            if (!passwordField1.getText().equals("")) {
                if (passwordField1.getText().equals(passwordField2.getText())) {
                    if (passwordField1.getText().length() >= 4) {
                        account.setPassword(passwordField1.getText());
                        log.info("Password= " + passwordField1.getText());
                    } else {
                        scanAndErrorMessage.setText("Password need to be atleast 4 characters");
                        return;
                    }
                } else {
                    scanAndErrorMessage.setText("Your password and confirmation password do not match");
                    log.info("Error with password");
                    return;
                }
            }
            log.info("changes");
            account.setRfidKey(new RfidKey(txfRfid.getText()));
            account.setFirstName(txfFirstname.getText());
            account.setLastName(txfLastname.getText());
            account.setUsername(txfUsername.getText());
            log.info("New Account= " + account.toString());
            adminController.updateUser(account);
        }
    }

    public void writeRfidkey(RfidKey rfidKey) {
        account.setRfidKey(rfidKey);
        txfRfid.setText(rfidKey.getId());
        if(btnScanCard.isDisabled() == true){
            btnScanCard.setDisable(false);
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                scanAndErrorMessage.setText("");
            }
        });
    }


    public void writeScanAndErrorMessage(String message) {
        scanAndErrorMessage.setText(message);
    }
}
