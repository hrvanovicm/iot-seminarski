package com.bytemike.services;

public interface IArduinoRequest {
    void pinReceived(int pin) throws InterruptedException;
}
