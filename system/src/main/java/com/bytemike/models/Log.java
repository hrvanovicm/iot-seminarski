package com.bytemike.models;

public class Log {
    private final String note;
    private final String createdAt;

    public Log(String note) {
        this.note = note;
        this.createdAt = Helper.getCurrentDateTime();
    }

    public String getNote() {
        return note;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
