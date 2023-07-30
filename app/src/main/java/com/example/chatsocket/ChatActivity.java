package com.example.chatsocket;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatsocket.serverstuff.Client;

public class ChatActivity extends AppCompatActivity {
    Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent chatIntent = getIntent();

        client = (Client) chatIntent.getSerializableExtra("client");

        client.listenForMessage();
        client.sendMessage();
    }
}
