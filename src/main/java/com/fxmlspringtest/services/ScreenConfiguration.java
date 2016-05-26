package com.fxmlspringtest.services;

import com.fxmlspringtest.Controller.AdminController;
import com.fxmlspringtest.guiController.*;
import com.fxmlspringtest.model.Account;
import com.fxmlspringtest.model.RfidKey;
import com.sun.org.apache.bcel.internal.util.ClassLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;


/**
 * Created by Jonatan Fridsten on 2016-05-08.
 */
public class ScreenConfiguration {

    private Stage primaryStage;
    private AdminController controller;
    private FXMLDialog loginDialog;
    private MainViewController mainViewController;
    private LoginController loginController;
    private AddUserController addUserController;
    private UpdateUserController updateUserController;
    private ErrorController errorController;
    private static ScreenConfiguration instance;

    private ScreenConfiguration(){

    }

    public static ScreenConfiguration getInstance(){
        if (instance == null)
            return (instance = new ScreenConfiguration());
        else
            return instance;
    }

    public void init(Stage primaryStage, AdminController controller) {
        this.primaryStage = primaryStage;
        this.loginController = new LoginController(controller);
        this.mainViewController = new MainViewController(controller);
        this.addUserController = new AddUserController(controller);
        this.updateUserController = new UpdateUserController(controller);
        this.errorController = new ErrorController();
        initStage();
    }


    public void setLoginScene(){
        primaryStage.setTitle("Login");
        swapScene(getSceneView(loginController,ClassLoader.getSystemResource("fxml/Login.fxml")));
        loginController.setFocus();
    }

    public void setMainScene(Account account){
        swapScene(getSceneView(mainViewController,ClassLoader.getSystemResource("fxml/mainLayout.fxml")));
        primaryStage.setTitle("MainView");
        mainViewController.onStart(account);
    }

    public void setAddScene(){
        swapScene(getSceneView(addUserController,ClassLoader.getSystemResource("fxml/addscene.fxml")));
        primaryStage.setTitle("AddView");
    }
    public void setUpdateScene(Account account){
        swapScene(getSceneView(updateUserController,ClassLoader.getSystemResource("fxml/updateScene.fxml")));
        updateUserController.setUserDetails(account);
        primaryStage.setTitle("UpdateView");
    }

    public FXMLDialog errorDialog(){
        return new FXMLDialog(errorController,ClassLoader.getSystemResource("fxml/error.fxml"),
                primaryStage,StageStyle.DECORATED,Modality.APPLICATION_MODAL);
    }

    public void newRfidAdd(RfidKey rfidKey) {
        addUserController.writeRfidkey(rfidKey);
    }

    public void newRfidUpdate(RfidKey rfidKey) {
        updateUserController.writeRfidkey(rfidKey);
    }

    public void erroHappend(String error){
        errorDialog().show();
        errorController.setText(error);
    }
    private void swapScene(Scene newScene){
        primaryStage.setScene(newScene);
        primaryStage.show();
    }
    private Scene getSceneView(final DialogController controller, URL fxml){
        FXMLLoader loader = new FXMLLoader(fxml);
        try{
            loader.setControllerFactory(new Callback<Class<?>, Object>() {
                @Override
                public Object call(Class<?> param) {
                    return controller;
                }
            });
            Scene scene = new Scene((Parent) loader.load());
            return scene;
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private void initStage(){
        primaryStage.getIcons().add(new Image("images/Logo.png"));
        primaryStage.setMinWidth(610);
        primaryStage.setMinHeight(500);
        primaryStage.setResizable(false);
    }

    public void showLoginError(String erorText) {
        loginController.setErrorText(erorText);
    }

    /**
     * Writes out the scan status on the correct screen
     * @param status
     * @param scene
     */
    public void scenStatus(String status, int scene) {
        if(scene == 1){
            addUserController.scanStatus(status);
        }else if(scene == 2){
            updateUserController.writeScanAndErrorMessage(status);
        }
    }

    public void updateScanNumber(int sceneNumber) {
        System.out.println("hello");
        if(sceneNumber== 1){
            addUserController.updatScanButon();
        }else if(sceneNumber == 2){
            updateUserController.updateScanButton();
        }
    }
}
