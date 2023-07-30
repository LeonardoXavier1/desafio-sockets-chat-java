package com.example.chatsocket;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText editIp, editPort;
    Button btnEnterServer, btnCreateServer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editIp = findViewById(R.id.editIp);
        editPort = findViewById(R.id.editPort);
        btnEnterServer = findViewById(R.id.btnEnterServer);
        btnCreateServer = findViewById(R.id.btnCreateServer);

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        editIp.setText(Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress()));

        btnCreateServer.setOnClickListener(view -> {
            int port = Integer.parseInt(editPort.getText().toString());
            Server server = new Server(port);
            connectToServer(
                    editIp.getText().toString(),
                    port
            );
        });

        btnEnterServer.setOnClickListener(view -> {
            connectToServer(
                editIp.getText().toString(),
                Integer.parseInt(editPort.getText().toString())
            );
        });
    }

    protected void connectToServer(String ip, int port) {
        Intent chatIntent = new Intent(
                MainActivity.this, ChatActivity.class
        );

        chatIntent.putExtra("ip", ip);
        chatIntent.putExtra("port", port);

        startActivity(chatIntent);
    }
}