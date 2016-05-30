package com.fxmlspringtest.guiController;

import com.fxmlspringtest.Controller.AdminController;
import com.fxmlspringtest.model.Account;
import com.fxmlspringtest.model.AuthoritiesConstants;
import com.fxmlspringtest.model.RfidKey;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.security.core.authority.AuthorityUtils;

/**
 * The controller for the add User dialog
 * Created by Jonatan Fridsten on 2016-05-16.
 */
public class AddUserController implements DialogController {

    private AdminController adminController;
    private boolean scanCardActive = false;
    private RfidKey key;

    @FXML
    Button btnScanCard;
    @FXML
    Button btnAddUser;
    @FXML
    Button btnCancel;
    @FXML
    PasswordField passwordField1;
    @FXML
    PasswordField passwordField2;
    @FXML
    TextField firstname;
    @FXML
    TextField lastname;
    @FXML
    TextField rfidkey;
    @FXML
    Label txtError;

    /**
     * Creates an object of the addUserController
     *
     * @param controller
     */
    public AddUserController(AdminController controller) {
        this.adminController = controller;
    }


    @Override
    /**
     * Sets the connection to admincontroller
     */
    public void setController(AdminController controller) {
        this.adminController = controller;
    }

    /**
     * Start the listener to the ardunio card and turns of the scancard button
     *
     * @param actionEvent listen to the scanCardButton
     */
    public void scanCard(ActionEvent actionEvent) {
        adminController.scanCard(1);
        btnScanCard.setDisable(true);
        System.out.println("Button state" + btnScanCard.isDisable());
        scanCardActive = true;
    }

    /**
     * Returns to mainview and close currentview and if
     * the scan is active the portlistener is closed
     *
     * @param actionEvent listen to cancelbutton
     */
    public void cancel(ActionEvent actionEvent) {
        //checks if the scan is active and shuts it down
        if (scanCardActive == true) {
            adminController.closeRxTx();
        }
        adminController.cancel();
    }

    /**
     * Method will start when adduser button is getting pressed,
     * and then does some check before it send an request to the server
     *
     * @param actionEvent listen to adduserbutton
     */
    public void addUser(ActionEvent actionEvent) {
        Account acc = new Account();
        System.out.println("Fristname: " + firstname.getText() + "/");
        //checks if all fields are filled
        if (firstname.getText().equals("") || lastname.getText().equals("") || passwordField1.getText().equals("") ||
                passwordField2.getText().equals("") || rfidkey.getText().equals("")) {
            txtError.setText("Missing fields");
        } else {
            acc.setFirstName(firstname.getText());
            acc.setLastName(lastname.getText());
            //if the passwords match
            if (passwordField1.getText().equals(passwordField2.getText())) {
                acc.setPassword(passwordField1.getText());
                //if we have an rfidkey
                if (rfidkey != null) {
                    RfidKey key = new RfidKey(rfidkey.getText());
                    key.setEnabled(true);
                    acc.setRfidKey(key);
                }
                acc.setAuthorities(AuthorityUtils.createAuthorityList(AuthoritiesConstants.USER));
                adminController.addUser(acc);
            } else {
                txtError.setText("Your password and confirmation password do not match");
            }
        }
    }

    /**
     * Method will save the rfidcard,
     * then write it on the userinterface and turn on the scanbutton again
     *
     * @param rfidKey the new rfidkey
     */
    public void writeRfidkey(RfidKey rfidKey) {
        this.key = rfidKey;
        rfidkey.setText(key.getId());
        if (btnScanCard.isDisabled() == true) {
            btnScanCard.setDisable(false);
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                txtError.setText("");
            }
        });
    }

    /**
     * Method will get a message with the status of the scan
     * and then write it out on the screen
     *
     * @param scanStatus that should be delivered
     */
    public void scanStatus(String scanStatus) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                txtError.setText(scanStatus);
            }
        });
    }

    /**
     * Updates the scanButton if the connection too the arduino is lost
     */
    public void updatScanButon() {
        System.out.println("Button status" + btnScanCard.isDisable());
        if (btnScanCard.isDisabled() == true) {
            btnScanCard.setDisable(false);
        }
    }
}
