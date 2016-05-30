package com.fxmlspringtest;

import com.fxmlspringtest.Controller.AdminController;
import com.fxmlspringtest.services.RxTxService;
import com.fxmlspringtest.services.ScreenConfiguration;
import com.fxmlspringtest.services.ServerService;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Jonatan Fridsten
 *
 * This class handles the startup
 */
@SpringBootApplication
public class AdminAppStarter extends Application {

    private AdminController controller;
    private ServerService serverService;
    private RxTxService rxTxService;
    private static ConfigurableApplicationContext applicationContext;
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AdminAppStarter.class);

    /**
     * Starts the gui from the javafx
     *
     * @param primaryStage the main stage
     * @throws Exception exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        log.info("JAVAFX STARTING");
        ApplicationContext context = new AnnotationConfigApplicationContext(AdminAppStarter.class);
        ScreenConfiguration screenConfiguration = ScreenConfiguration.getInstance();
        screenConfiguration.init(primaryStage, controller);
        controller.setScreens(screenConfiguration);
        screenConfiguration.setLoginScene();
    }

    /**
     * Runns before and starts up the springboot application
     *
     * @throws Exception when an error occurs
     */
    public void init() throws Exception {
        log.info("STARTING ADMIN APP");
        SpringApplication app = new SpringApplication(AdminAppStarter.class);
        app.setWebEnvironment(false);
        applicationContext = app.run();
        applicationContext.getAutowireCapableBeanFactory().autowireBean(app);

        rxTxService = new RxTxService();
        serverService = new ServerService();
        controller = new AdminController();
        controller.setServerService(serverService);
        controller.setRxTxService(rxTxService);
        serverService.setController(controller);
        controller.initializeRxTx();
    }

    /**
     * Starts the application
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
