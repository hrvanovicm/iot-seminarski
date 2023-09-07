package com.bytemike.services;

import com.fazecast.jSerialComm.SerialPort;

import java.awt.*;

public class ArduinoService {
    private SerialPort port;

    private IArduinoRequest arduinoRequestCallback;

    public ArduinoService() throws Exception {
        this.initalize();
    }

    private void initalize() throws Exception {
        SerialPort[] serialPorts = SerialPort.getCommPorts();

        if (serialPorts.length == 0) {
            WindowsNotificationService.send("Greska", "Nije moguce pronaci arduino!", TrayIcon.MessageType.ERROR);
            throw new Exception("Nije moguce pristupiti arduinu!");
        }

        for (SerialPort port : serialPorts) {
            if (port.getSystemPortName().contains("Arduino Uno")) {
                this.port = port;
                this.port.openPort();
                this.port.setBaudRate(9600);
                break;
            }
        }

        if (this.port == null) {
            WindowsNotificationService.send("Greska", "Nije moguce pronaci arduino!", TrayIcon.MessageType.ERROR);
            throw new Exception("Nije moguce pristupiti arduinu!");
        }

        Thread.sleep(5000);
        this.sendCommandToArduino(ArduinoCommand.CONNECTED);
        WindowsNotificationService.send("Arduino", "Arduino je uspjesno konektovan!", TrayIcon.MessageType.INFO);

        Thread readThread = new Thread(() -> {
            StringBuilder securityPinBuffer = new StringBuilder();

            while (true) {
                byte[] buffer = new byte[1];
                int bytesRead = this.port.readBytes(buffer, 1);

                if (bytesRead > 0) {
                    char receivedChar = (char) buffer[0];

                    if (receivedChar == '#' && securityPinBuffer.isEmpty()) {
                        this.sendCommandToArduino(ArduinoCommand.PIN_FAILED);
                    } else if (receivedChar == '#') {
                        try {
                            arduinoRequestCallback.pinReceived(Integer.parseInt(securityPinBuffer.toString()));
                        } catch (InterruptedException e) {
                            //
                        }
                        securityPinBuffer.setLength(0);
                    } else {
                        this.sendCommandToArduino(ArduinoCommand.INPUT_RECEIVED);
                        securityPinBuffer.append(receivedChar);
                    }
                }
            }
        });
        readThread.start();
    }

    public void setCallback(IArduinoRequest callback) {
        this.arduinoRequestCallback = callback;
    }

    public void sendCommandToArduino(ArduinoCommand command) {
        byte[] data = {(byte) command.value};
        this.port.writeBytes(data, data.length);
    }
}
