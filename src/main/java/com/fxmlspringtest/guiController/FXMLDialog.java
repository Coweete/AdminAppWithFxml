package com.fxmlspringtest.guiController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Callback;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

/**
 * This class returns an fxml file linked with the controller as an fxmldialog
 * Created by Jonatan Fridsten on 2016-05-08.
 */
public class FXMLDialog extends Stage {
    /**
     * Contstructor to the fxmldialog, that creats an window with an selected fxml file and
     * links to the controller
     *
     * @param controller the gui controller for the class
     * @param fxml       Gui layout
     * @param owner      the window where the fxml file should be put into
     */
    public FXMLDialog(DialogController controller, URL fxml, Window owner) {
        this(controller, fxml, owner, StageStyle.DECORATED, Modality.WINDOW_MODAL);
    }

    /**
     * Creates a new window that han connection to an fxml file and an controller class,
     *
     * @param controller the gui controller for the class
     * @param fxml       gui layout
     * @param owner      the window where the fxml file should be put into
     * @param style      sets window style
     * @param modality   defines how the window should react to other windows
     */
    public FXMLDialog(final DialogController controller, URL fxml, Window owner, StageStyle style, Modality modality) {
        super(style);
        initOwner(owner);
        initModality(modality);
        FXMLLoader loader = new FXMLLoader(fxml);
        try {
            loader.setControllerFactory(new Callback<Class<?>, Object>() {
                @Override
                public Object call(Class<?> param) {
                    return controller;
                }
            });
            if (controller instanceof ErrorController) {
                ((ErrorController) controller).setDialog(this);
            }
            setScene(new Scene((Parent) loader.load()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
