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
 * Created by Jonatan Fridsten on 2016-05-08.
 */
@Component
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);
    private ServerService serverService;
    private Account currentUser;
    private ScreenConfiguration screens;
    private RxTxService rxTxService;
    private AdminSerialPortEventListener eventListener = new AdminSerialPortEventListener();

    public void login(String username, String password) {
        if (serverService.login(username, password)) {
            screens.setMainScene(currentUser);
        }
    }

    /**
     * Sets the connection to the serverClass
     *
     * @param serverService Serverclass object
     */
    public void setServerService(ServerService serverService) {
        this.serverService = serverService;
    }

    /**
     * Returns the currentuser
     *
     * @return current user
     */
    public Account getCurrentUser() {
        return currentUser;
    }

    /**
     * Sets the currentAdminUser, the person that is logged in.
     *
     * @param currentUser An adminuser
     */
    public void setCurrentUser(Account currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * Setts the connection to the Screen handler
     *
     * @param screens Screen handler
     */
    public void setScreens(ScreenConfiguration screens) {
        this.screens = screens;
    }

    /**
     * Connects to serverclass and returns the answerfrom the serverclass
     *
     * @return an array with all users
     */
    public Account[] getUserlist() {
        return serverService.getAllUsers();
    }

    /**
     * Connects to the serverclass with an request for add an account then swaps to the mainview
     *
     * @param account the new account
     */
    public void addUser(Account account) {
        serverService.addUser(account);
        cancel();
    }

    /**
     * Sets the view to AddUserDialog
     */
    public void showAddUserDialog() {
        screens.setAddScene();
    }

    /**
     * Sets the view to Mainview
     */
    public void cancel() {
        screens.setMainScene(currentUser);
    }

    /**
     * Connects to the rxtx service and an number of witch the currentview is,
     * Also make it avible to scan the card
     *
     * @param number the current view 1 = addUser 2 = updateUser
     */
    public void scanCard(int number) {
        // 1 = addUser 2 = updateUser
        eventListener.setNumber(number);
        log.info("Före tråd");
        rxTxService.initialize(number);
    }

    /**
     * Sets the connection to rxtxservice
     *
     * @param rxTxService an object of rxtxservice
     */
    public void setRxTxService(RxTxService rxTxService) {
        this.rxTxService = rxTxService;
    }

    /**
     * Sends an request to the server class for deleting the current account
     *
     * @param account the account that will be deleted
     */
    public void deleteUser(Account account) {
        serverService.deleteUser(account);
    }

    /**
     * Shows the update user dialog it will send the choose
     * account and send it to the view.
     *
     * @param account the account that will be updated
     */
    public void showUpdateUserDialog(Account account) {
        screens.setUpdateScene(account);
        log.info("Updateacc: " + account.toString());
    }

    /**
     * Connect to the server class then it will send det updated account to the server
     *
     * @param account the updated account
     */
    public void updateUser(Account account) {
        serverService.updateUser(account);
        cancel();
    }

    /**
     * This method will send an error message to the screen viewer
     *
     * @param error the error message
     */
    public void showError(String error) {
        screens.erroHappend(error);
    }

    /**
     * Calls the server class to do logout method and then set
     * the current user too null and goes back to loginview
     */
    public void logout() {
        serverService.logout();
        currentUser = null;
        screens.setLoginScene();
    }

    /**
     * Shows an error message on an new view an locks the other view.
     *
     * @param erorText the error message
     */
    public void showLoginError(String erorText) {
        screens.showLoginError(erorText);
    }

    /**
     * Writes out the scan status on two view for when we scan a card and
     * uses an interger for knowing witch scene is active
     *
     * @param status the output message
     * @param scene  number to wich scene that is in action
     */
    public void scanStatus(String status, int scene) {
        screens.scenStatus(status, scene);
    }

    public void createNewRfid(String inputLine, int scene) {
        // 1 = addUser 2 = updateUser
        if (scene == 1) {
            screens.newRfidAdd(new RfidKey(inputLine));
        } else if (scene == 2) {
            screens.newRfidUpdate(new RfidKey(inputLine));
        }
    }

    /**
     * Will be called to initialize the rxtx components in our program
     */
    public void initializeRxTx() {
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
    public void closeRxTx() {
        rxTxService.close();
    }

    /**
     * Changes the port that the program listenes to
     *
     * @param text new port
     */
    public void changeArduinoPort(String text) {
        String realText = text.toUpperCase();
        rxTxService.setCompPort(realText);
    }

    public void scanButton(int sceneNumber) {
        screens.updateScanNumber(sceneNumber);
    }
}
