package com.bytemike.services;

import com.bytemike.models.Log;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.*;

public class FirebaseService {
    public int currentSecurityPin;

    public FirebaseService() throws IOException {
        this.initialize();
    }

    private void initialize() throws IOException {
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(
                        GoogleCredentials.fromStream(getCredentialsAsStream())
                )
                .setDatabaseUrl(getDatabaseURL())
                .build();

        FirebaseApp.initializeApp(options);


        this.onSecurityPinChange(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentSecurityPin = dataSnapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private FileInputStream getCredentialsAsStream() throws FileNotFoundException {
        File file = new File(getClass().getClassLoader().getResource("firebase-credentials.json").getFile());
        return new FileInputStream(file);
    }

    public FirebaseDatabase getConnection() {
        return FirebaseDatabase.getInstance();
    }

    public void onDoorChange(ValueEventListener $callback) {
        var isOpenedRef = this.getConnection().getReference("door/isOpened");
        isOpenedRef.addValueEventListener($callback);
    }

    public void sendLog(Log log) {
        var temp = this.getConnection().getReference("logs").push();
        temp.setValue(log, (databaseError, databaseReference) -> {});
    }

    public void setDoorClosed() {
        this.getConnection().getReference("door/isOpened").setValue(false, (databaseError, databaseReference) -> {});
        this.sendLog(new Log("Zatvaranje vrata"));
    }

    public void setDoorOpened() {
        this.getConnection().getReference("door/isOpened").setValue(true, (databaseError, databaseReference) -> {});
        this.sendLog(new Log("Otvaranje vrata"));
    }

    public void onSecurityPinChange(ValueEventListener $callback) {
        var isOpenedRef = this.getConnection().getReference("door/securityPin");
        isOpenedRef.addValueEventListener($callback);
    }

    public String getDatabaseURL() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream("/firebase-credentials.json");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(inputStream);

        String databaseUrl = jsonNode.get("database_url").asText();

        return databaseUrl;
    }
}
