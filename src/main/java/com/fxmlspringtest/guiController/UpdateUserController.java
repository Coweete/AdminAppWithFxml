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

    /**
     * Creats an controller too the updateuser dialog
     *
     * @param controller
     */
    public UpdateUserController(AdminController controller) {
        adminController = controller;
    }

    @Override
    public void setController(AdminController controller) {
        this.adminController = controller;
    }

    /**
     * Sets the information about the selected account on the gui
     *
     * @param account that is going to be updated
     */
    public void setUserDetails(Account account) {
        this.account = account;
        System.out.println("In updatecontroller " + account.toString());
        txfFirstname.setText(account.getFirstName());
        txfLastname.setText(account.getLastName());
        txfUsername.setText(account.getUsername());
        txfRfid.setText(account.getRfidKey().getId());
    }

    /**
     * Cancles the operation
     *
     * @param actionEvent button cancel
     */
    public void cancel(ActionEvent actionEvent) {
        //checks if the scan is active
        if (scanCardActive == true) {
            adminController.closeRxTx();
        }
        adminController.cancel();
    }

    /**
     * Calls for the scan card operation on the admin controller
     *
     * @param actionEvent button scan card
     */
    public void scanCard(ActionEvent actionEvent) {
        scanCardActive = true;
        btnScanCard.setDisable(true);
        adminController.scanCard(2);
    }

    /**
     * Checks if there is any leagal changes on the gui and then sends it to the admin controller, then the server
     *
     * @param actionEvent button Update
     */
    public void updateUser(ActionEvent actionEvent) {
        log.info("Update User");
        // checks if there is an empty line
        if (txfFirstname.getText().equals("") || txfLastname.getText().equals("") || txfRfid.getText().equals("") ||
                txfUsername.getText().equals("")) {
            log.info("Missing fields");
            scanAndErrorMessage.setText("Missing some fields");
        } else {
            //if its not empty
            if (!passwordField1.getText().equals("")) {
                //checks if the passwords match
                if (passwordField1.getText().equals(passwordField2.getText())) {
                    //checks if the password has an correct length
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

    /**
     * Stores the rfid then it starts scanbutton again and
     * writes the rfid on the gui
     *
     * @param rfidKey the new rfidkey
     */
    public void writeRfidkey(RfidKey rfidKey) {
        account.setRfidKey(rfidKey);
        txfRfid.setText(rfidKey.getId());
        if (btnScanCard.isDisabled() == true) {
            btnScanCard.setDisable(false);
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                scanAndErrorMessage.setText("");
            }
        });
    }

    /**
     * Shows the error message to the user
     *
     * @param message the error message
     */
    public void writeScanAndErrorMessage(String message) {
        scanAndErrorMessage.setText(message);
    }

    /**
     * Checks if the scanbutton is on, otherwise turns it on
     */
    public void updateScanButton() {
        if (btnScanCard.isDisabled() == true) {
            btnScanCard.setDisable(false);
        }
    }
}
