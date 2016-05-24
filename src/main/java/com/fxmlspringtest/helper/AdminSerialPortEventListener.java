package com.fxmlspringtest.helper;

import com.fxmlspringtest.Controller.AdminController;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by seb on 2016-04-12.
 */

@Component
public class AdminSerialPortEventListener implements gnu.io.SerialPortEventListener {


    private SerialPort serialPort;
    private BufferedReader input;
    private AdminController controller;
    private int number;

    /**
     * This method will take care of an serialportevent and then return it to the controller
     *
     * @param oEvent serialportevent
     */
    @Override
    public void serialEvent(SerialPortEvent oEvent) {
        System.out.println("Got request from Arduino");
        if(oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE){
         try {
             String inputLine = input.readLine();
             System.out.println(inputLine);
             // 1 = addUser 2 = updateUser
             if(inputLine.length() == 7 || inputLine.length() == 8){
                 serialPort.close();
                 controller.createNewRfid(inputLine,number);
             }else{
                 controller.scanStatus("Scan incomplete please try again", number);
             }
         }catch (Exception e){
             System.out.println(e.toString());
         }
        }
    }

    /**
     * This method will set the local serialport to a destinated one
     *
     * @param serialPort the port we need to use
     */
    public void setSerialPort(SerialPort serialPort) {
        this.serialPort = serialPort;
        try {
            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method will connect the controller object to this class
     *
     * @param controller the controller
     */
    public void setController(AdminController controller) {
        this.controller = controller;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
