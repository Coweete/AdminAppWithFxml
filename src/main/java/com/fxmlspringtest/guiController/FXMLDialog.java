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
 * Created by jonatan on 2016-05-08.
 */
public class  FXMLDialog extends Stage {

    public FXMLDialog(DialogController controller, URL fxml, Window owner){
        this(controller,fxml,owner, StageStyle.DECORATED,Modality.WINDOW_MODAL);
    }

    public FXMLDialog(final DialogController controller,URL fxml,Window owner,StageStyle style,Modality modality){
        super(style);
        initOwner(owner);
        initModality(modality);
        FXMLLoader loader = new FXMLLoader(fxml);
        try{
            loader.setControllerFactory(new Callback<Class<?>, Object>() {
                @Override
                public Object call(Class<?> param) {
                    return controller;
                }
            });
            if(controller instanceof ErrorController){
                ((ErrorController) controller).setDialog(this);
            }
            setScene(new Scene((Parent) loader.load()));
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

}
