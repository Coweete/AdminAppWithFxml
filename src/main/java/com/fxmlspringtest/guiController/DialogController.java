package com.fxmlspringtest.guiController;

import com.fxmlspringtest.Controller.AdminController;

/**
 * Created by Jonatan Fridsten on 2016-05-08.
 */
public interface DialogController {

    /**
     * Makes the connection to admin controller avible
     * @param controller program controller
     */
    void setController(AdminController controller);

}
