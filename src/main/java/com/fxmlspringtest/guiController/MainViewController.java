package com.fxmlspringtest.guiController;

import com.fxmlspringtest.Controller.AdminController;
import com.fxmlspringtest.model.Account;
import com.fxmlspringtest.services.ScreenConfiguration;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

/**
 * Handles the main view
 *
 * Created by Jonatan Fridsten on 2016-05-09.
 */
public class MainViewController implements DialogController {

    private AdminController controller;
    private Account[] userArray;
    private Account currentUser;

    @FXML
    ListView<String> listView;
    @FXML
    Button btnUpdateList;
    @FXML
    Button btnUpdateUser;
    @FXML
    Button btnAddUser;
    @FXML
    Button btnLogout;
    @FXML
    Button btnDeleteUser;
    @FXML
    Label lblFirstname;
    @FXML
    Label lblLastname;
    @FXML
    TextField txtArduino;

    /**
     * Constructor for the mainview controller
     * @param controller
     */
    public MainViewController(AdminController controller) {
        this.controller = controller;
    }

    @Override
    public void setController(AdminController controller) {
        this.controller = controller;
    }

    /**
     * Sets up the listview with all the accounts that is avible on the server
     */
    public void setListView() {
        userArray = controller.getUserlist();
        if (userArray != null) {
            listView.getItems().clear();
            for (int i = 0; i < userArray.length; i++) {
                listView.getItems().add(i, userArray[i].getFirstName() + " " + userArray[i].getLastName());
            }
        }
    }

    /**
     * Checks if the selected account is avible for an update,
     * if its possible for an update then sends a request to swap scene to the admincontroller
     *
     * @param actionEvent update button
     */
    public void updateUser(ActionEvent actionEvent) {
        int res = listView.getSelectionModel().getSelectedIndex();
        System.out.println("Result" + res);
        System.out.println("CurrentUser " + currentUser.getUsername());
        if (res == -1) {
            controller.showError("Error you need to choose an account");
        } else {
            if (currentUser.getUsername().equals(userArray[res].getUsername())) {
                controller.showError("Cant update current user");
            } else {
                for (int i = 0; i < userArray[res].getAuthorities().size(); i++) {
                    if (userArray[res].getAuthorities().get(i).toString().equals("ROLE_PI")) {
                        System.out.println("hejdå");
                        controller.showError("Cant update an pi user");
                    } else {
                        System.out.println("hello");
                        controller.showUpdateUserDialog(userArray[res]);
                    }
                }
            }
        }
    }

    /**
     * Updates the listview then sets the text on the current user
     *
     * @param adminUser currentuser
     */
    public void onStart(Account adminUser) {
        currentUser = adminUser;
        lblFirstname.setText(adminUser.getFirstName());
        lblLastname.setText(adminUser.getLastName());
        listView.getFocusModel().focus(0);
        System.out.println(listView.getFocusModel().getFocusedIndex());
        setListView();
    }

    /**
     * Calls to the controller for swap scene to the add view
     *
     * @param actionEvent button adduser
     */
    public void addUser(ActionEvent actionEvent) {
        controller.showAddUserDialog();
    }

    /**
     * Checks if the account is avible for delete then sends the selected accouont to
     * the controller with an request for deleting the account
     *
     * @param actionEvent button delete
     */
    public void deleteUser(ActionEvent actionEvent) {
        int res = listView.getSelectionModel().getSelectedIndex();
        Account selectedAccount = userArray[res];
        System.out.println("SelectedAccount" + selectedAccount.toString());
        if (res == -1) {
            controller.showError("Error you need to choose an account");
        } else {
            if (currentUser.getUsername().equals(selectedAccount.getUsername())) {
                controller.showError("Cant delete the selected user");
            } else {
                for (int i = 0; i < selectedAccount.getAuthorities().size(); i++) {
                    if (selectedAccount.getAuthorities().get(i).toString().equals("ROLE_PI") ||
                            selectedAccount.getAuthorities().get(i).toString().equals("ROLE_ADMIN")) {
                        System.out.println("hejdå");
                        controller.showError("Cant delete pi user");
                        return;
                    } else {
                        controller.deleteUser(selectedAccount);
                        setListView();
                    }
                }
            }
        }
    }

    /**
     * Calls the logout function in the main controller
     * @param actionEvent button logout
     */
    public void logout(ActionEvent actionEvent) {
        controller.logout();
    }

    /**
     * Changes the arduino port, need to press enter for it to happen
     * @param event enter pressed
     */
    public void changePort(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            controller.changeArduinoPort(txtArduino.getText());
        }
    }
}
