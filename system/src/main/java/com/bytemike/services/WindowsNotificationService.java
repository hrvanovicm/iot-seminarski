package com.bytemike.services;

import com.bytemike.models.Helper;

import javax.imageio.ImageIO;
import java.awt.*;

public class WindowsNotificationService {
    public static void send(String title, String message, TrayIcon.MessageType type) {
        if (!SystemTray.isSupported())
            return;

        try {
            Image iconImage = ImageIO.read(WindowsNotificationService.class.getResourceAsStream("/door_opened.png"));
            PopupMenu popup = new PopupMenu();

            TrayIcon trayIcon = new TrayIcon(iconImage, "IOT", popup);
            trayIcon.setImageAutoSize(true);
            SystemTray.getSystemTray().add(trayIcon);


            if (message != null) {
                message = String.format("%s | %s", message, Helper.getCurrentDateTime());
            } else {
                message = Helper.getCurrentDateTime();
            }

            trayIcon.displayMessage(title, message, type);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
}
