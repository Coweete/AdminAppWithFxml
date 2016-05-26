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
 * Created by jonatan on 2016-05-09.
 */
public class MainViewController implements DialogController {

    private AdminController controller;
    private Account[] userArray;

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

    @Override
    public void setController(AdminController controller) {
        this.controller = controller;
    }

    public MainViewController(AdminController controller){
        this.controller = controller;
    }


    public void setListView(){
        userArray =  controller.getUserlist();
        if(userArray != null){
            listView.getItems().clear();
            for (int i = 0; i < userArray.length; i++) {
                listView.getItems().add(i,userArray[i].getFirstName() + " " + userArray[i].getLastName());
            }
        }
    }

    public void updateUser(ActionEvent actionEvent) {
        int res = listView.getSelectionModel().getSelectedIndex();
        if(controller.getCurrentUser().getUsername() == userArray[res].getUsername()){
            controller.showError("Cant update current user");
        }else {
            if(res == -1){
                controller.showError("Error you need to choose an account");
            }else{
                controller.showUpdateUserDialog(userArray[res]);
            }
        }
    }

    public void onStart(Account adminUser){
        lblFirstname.setText(adminUser.getFirstName());
        lblLastname.setText(adminUser.getLastName() );
        listView.getFocusModel().focus(0);
        System.out.println(listView.getFocusModel().getFocusedIndex());
        setListView();
    }


    public void addUser(ActionEvent actionEvent) {
        controller.showAddUserDialog();
    }

    public void deleteUser(ActionEvent actionEvent) {
        int res = listView.getSelectionModel().getSelectedIndex();
        if(res == -1){
            controller.showError("Error you need to choose an account");
        }else{
            controller.deleteUser(userArray[res]);
            setListView();
        }
    }

    public void logout(ActionEvent actionEvent) {
        controller.logout();
    }

    public void changePort(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER)){
            controller.changeArduinoPort(txtArduino.getText());
        }
    }
}
