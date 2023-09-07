package com.bytemike;

import com.bytemike.models.Log;
import com.bytemike.services.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.awt.*;

public class Main {
    public static void main(String[] args) throws Exception {
        FirebaseService firebaseService = new FirebaseService();
        ArduinoService arduinoService = new ArduinoService();

        firebaseService.onDoorChange(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean isDoorOpened = dataSnapshot.getValue(Boolean.class);

                if (isDoorOpened) {
                    arduinoService.sendCommandToArduino(ArduinoCommand.DOOR_OPEN);

                    WindowsNotificationService.send(
                            "Vrata su otvorena",
                            null,
                            TrayIcon.MessageType.INFO
                    );

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    firebaseService.setDoorClosed();

                } else {
                    arduinoService.sendCommandToArduino(ArduinoCommand.DOOR_CLOSED);
                    WindowsNotificationService.send("Vrata su zatvorena", "", TrayIcon.MessageType.INFO);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        IArduinoRequest callback = pin -> {
            if (pin == firebaseService.currentSecurityPin) {
                firebaseService.setDoorOpened();
            } else {
                arduinoService.sendCommandToArduino(ArduinoCommand.PIN_FAILED);
                firebaseService.sendLog(new Log(
                        String.format("Pogrešan PIN (%s)", pin)
                ));
                WindowsNotificationService.send("Pogrešan PIN", String.format("PIN: %s", pin), TrayIcon.MessageType.WARNING);
            }
        };

        arduinoService.setCallback(callback);

        while (true) {
        }
    }
}