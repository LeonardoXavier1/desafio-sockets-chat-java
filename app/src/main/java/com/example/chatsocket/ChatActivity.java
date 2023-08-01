package com.example.chatsocket;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ChatActivity extends AppCompatActivity {
    private final String TAG = "ÇÇÇ";

    String ip;
    int port;

    TextView textStream;
    EditText editMsg;
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent chatIntent = getIntent();

        ip = chatIntent.getStringExtra("ip");
        port = chatIntent.getIntExtra("port", 0);

        textStream = findViewById(R.id.textStream);
        editMsg = findViewById(R.id.editMsg);
        btnSend = findViewById(R.id.btnSend);

        Client client = new Client(ip, port);

        btnSend.setOnClickListener(view -> client.sendMessage(editMsg.getText().toString()));
    }

    protected class Client extends Thread implements Runnable {
        private String ip;
        private int port;
        private Socket socket;
        private BufferedReader bufferedReader;
        private BufferedWriter bufferedWriter;

        public Client(String ip, int port) {
            this.ip = ip;
            this.port = port;
            Log.e(TAG, ip + " " + port);
            start();
        }

        @Override
        public void run() {
            try {
                this.socket = new Socket(ip, port);
                this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                listenForMessage();
            } catch (IOException e) {
                e.printStackTrace();
                closeEverything();
            }
        }

        public void sendMessage(String messageToSend) {
            new Thread(() -> {
                try {
                    if (socket.isConnected()) {
                        bufferedWriter.write(messageToSend);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                        Log.e(TAG, "MANDOU MENSAGEM: " + messageToSend);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    closeEverything();
                }
            }).start();
        }


        public void listenForMessage() {
            new Thread(() -> {
                String msgFromGroupChat;

                while (socket.isConnected()) {
                    try {
                        Log.e(TAG, "Está escutando!");
                        msgFromGroupChat = bufferedReader.readLine();
                        textStream.append("\n" + msgFromGroupChat);
                        Log.e(TAG, "PEGOU A MENSAGEM: " + msgFromGroupChat);
                    } catch (Exception e) {
                        closeEverything();
                    }
                }
            }).start();
        }

        public void closeEverything() {
            try {
                Log.e(TAG, "DEU ALGUMA COISA ERRADA!");
                if (bufferedReader != null) {
                    bufferedReader.close();
                }

                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }

                if (socket != null) {
                    socket.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
