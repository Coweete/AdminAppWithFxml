package com.fxmlspringtest.Controller;

import com.fxmlspringtest.helper.AdminSerialPortEventListener;
import com.fxmlspringtest.model.Account;
import com.fxmlspringtest.model.RfidKey;
import com.fxmlspringtest.services.RxTxService;
import com.fxmlspringtest.services.ScreenConfiguration;
import com.fxmlspringtest.services.ServerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by jonatan on 2016-05-08.
 */
@Component
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);
    private ServerService serverService;
    private Account currentUser;
    private ScreenConfiguration screens;
    private RxTxService rxTxService;
    private AdminSerialPortEventListener eventListener = new AdminSerialPortEventListener();

    public void login(String username,String password) {
        if(serverService.login(username,password)){
            screens.setMainScene(currentUser);
        }
    }

    public void setServerService(ServerService serverService) {
        this.serverService = serverService;
    }

    public Account getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Account currentUser) {
        this.currentUser = currentUser;
    }

    public void setScreens(ScreenConfiguration screens) {
        this.screens = screens;
    }

    public Account[] getUserlist() {
        return serverService.getAllUsers();
    }


    public void addUser(Account account) {
        serverService.addUser(account);
        cancel();
    }

    public void showAddUserDialog() {
        screens.setAddScene();
    }

    public void cancel() {
        screens.setMainScene(currentUser);
    }

    public void scanCard(int number) {
        // 1 = addUser 2 = updateUser
        eventListener.setNumber(number);
        log.info("Före tråd");
        rxTxService.initialize(number);
    }

    public void setRxTxService(RxTxService rxTxService) {
        this.rxTxService = rxTxService;
    }

    public void deleteUser(Account account) {
        serverService.deleteUser(account);
    }

    public void showUpdateUserDialog(Account account) {
        screens.setUpdateScene(account);
        log.info("Updateacc: " + account.toString());
    }

    public void updateUser(Account account) {
        serverService.updateUser(account);
        cancel();
    }

    /**
     * This method will send an error message to the screen viewer
     * @param error the error message
     */
    public void showError(String error) {
        screens.erroHappend(error);
    }

    public void logout() {
        currentUser = null;
        screens.setLoginScene();
    }

    public void showLoginError(String erorText) {
        screens.showLoginError(erorText);
    }

    /**
     *  Writes out the scan status on two view for when we scan a card and
     *  uses an interger for knowing witch scene is active
     * @param status the output message
     * @param scene number to wich scene that is in action
     */
    public void scanStatus(String status,int scene) {
        screens.scenStatus(status, scene);
    }

    public void createNewRfid(String inputLine,int scene) {
        // 1 = addUser 2 = updateUser
        if(scene == 1){
            screens.newRfidAdd(new RfidKey(inputLine));
        }else if(scene == 2){
            screens.newRfidUpdate(new RfidKey(inputLine));
        }
    }
    /**
     * Will be called to initialize the rxtx components in our program
     */
    public void initializeRxTx(){
        rxTxService.setCtrl(this);
        rxTxService.setEventHandler(eventListener);
        //// TODO: 2016-05-19 gör så att man kan ändra denna
        rxTxService.setCompPort("COM5");
        eventListener.setController(this);
    }

    /**
     * If we want to swap scene and the scan is active
     * this method will shut down the event
     */
    public void closeRxTx(){
        rxTxService.close();
    }

    public void changeArduinoPort(String text) {
        String realText = text.toUpperCase();
        rxTxService.setCompPort(realText);
    }
}
