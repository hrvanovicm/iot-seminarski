package com.bytemike.services;

public enum ArduinoCommand {
    CONNECTED(1),
    DOOR_OPEN(2),
    DOOR_CLOSED(3),
    PIN_FAILED(4),
    INPUT_RECEIVED(5);

    int value;

    ArduinoCommand(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
