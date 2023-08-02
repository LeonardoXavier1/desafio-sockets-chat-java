package com.example.chatsocket;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editApelido, editIp, editPort;
    Button btnEnterServer, btnCreateServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editApelido = findViewById(R.id.editApelido);
        editIp = findViewById(R.id.editIp);
        editPort = findViewById(R.id.editPort);

        btnCreateServer = findViewById(R.id.btnCreateServer);
        btnEnterServer = findViewById(R.id.btnEnterServer);

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        editIp.setText(Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress()));

        btnCreateServer.setOnClickListener(this);
        btnEnterServer.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String apelido = editApelido.getText().toString();
        String ip = editIp.getText().toString();
        int port = Integer.parseInt(editPort.getText().toString());

        if (view == btnCreateServer) {
            new Thread(() -> new Server(port)).start();
            connectToServer(apelido, ip, port);
        }

        if (view == btnEnterServer) {
            connectToServer(apelido, ip, port);
        }
    }

    protected void connectToServer(String apelido, String ip, int port) {
        Intent chatIntent = new Intent(
                MainActivity.this, ChatActivity.class

        );

        chatIntent.putExtra("apelido", apelido);
        chatIntent.putExtra("ip", ip);
        chatIntent.putExtra("port", port);

        startActivity(chatIntent);
    }
}