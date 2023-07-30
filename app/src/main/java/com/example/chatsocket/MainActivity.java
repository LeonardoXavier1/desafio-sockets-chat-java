package com.example.chatsocket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.chatsocket.serverstuff.Client;
import com.example.chatsocket.serverstuff.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    EditText editIp;
    EditText editPort;
    Button btnConnect;
    Button btnLaunch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editIp = findViewById(R.id.editIp);
        editPort = findViewById(R.id.editPort);
        btnConnect = findViewById(R.id.btnConnect);
        btnLaunch = findViewById(R.id.btnLaunch);

        btnLaunch.setOnClickListener(view -> {
            int port = Integer.parseInt(editPort.getText().toString());

            try {
                ServerSocket serverSocket = new ServerSocket(port);
                new Server(serverSocket).startServer();
            } catch (IOException exception) {
                Log.e("Server Launch", exception.getLocalizedMessage());
            }
        });

        btnConnect.setOnClickListener(view -> {
            int port = Integer.parseInt(editPort.getText().toString());
            String ip = editIp.getText().toString();

            Client client = new Client(ip, port, Build.MODEL);

            Intent chatIntent = new Intent(
                    MainActivity.this, ChatActivity.class
            );

            chatIntent.putExtra("client", client);

            startActivity(chatIntent);
        });
    }
}