package com.example.chatsocket;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    private LinearLayout messageContainer;

    //TextView textStream;
    EditText editMsg;
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent chatIntent = getIntent();

        String apelido = chatIntent.getStringExtra("apelido");
        String ip = chatIntent.getStringExtra("ip");
        int port = chatIntent.getIntExtra("port", 0);

        //textStream = findViewById(R.id.textStream);
        editMsg = findViewById(R.id.editMsg);
        btnSend = findViewById(R.id.btnSend);
        messageContainer = findViewById(R.id.messageContainer);

        Client client = new Client(apelido, ip, port);

        btnSend.setOnClickListener(view -> {
            String message = editMsg.getText().toString().trim();
            if (!message.isEmpty()) {
                // Envia a mensagem e adiciona à lista de mensagens enviadas
                client.sendMessage(message);
                addSentMessage(message);

                // Limpa o campo de edição após o envio
                editMsg.setText("");
            }
        });    }

    private void addSentMessage(String message) {
        // Infla o layout da mensagem enviada
        LinearLayout sentMessageLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.inflatable_user, null);

        // Define o texto da mensagem
        TextView textViewMessage = sentMessageLayout.findViewById(R.id.textViewMessage);
        textViewMessage.setText(message);

        // Adiciona o layout ao container de mensagens
        messageContainer.addView(sentMessageLayout);
    }

    private void addReceivedMessage(String message) {
        // Infla o layout da mensagem recebida
        LinearLayout receivedMessageLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.inflatble_user2, null);

        // Define o texto da mensagem
        TextView textViewMessage = receivedMessageLayout.findViewById(R.id.textViewMessage);
        textViewMessage.setText(message);

        // Adiciona o layout ao container de mensagens
        messageContainer.addView(receivedMessageLayout);
    }

    protected class Client implements Runnable {
        private String username;
        private String ip;
        private int port;
        private Socket socket;
        private BufferedReader bufferedReader;
        private BufferedWriter bufferedWriter;

        public Client(String username, String ip, int port) {
            this.username = username;
            this.ip = ip;
            this.port = port;
            Log.e(TAG, ip + " " + port);
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
                Log.e(TAG, e.getMessage() + "\n" + e.getLocalizedMessage() + "\n" + e.toString() + "\nrun");
                closeEverything();
            }
        }

        public void sendMessage(String messageToSend) {
            //new Thread(() -> {
                try {
                    if (socket.isConnected()) {
                        bufferedWriter.write(messageToSend);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                        Log.e(TAG, "MANDOU MENSAGEM: " + messageToSend);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage() + "\n" + e.getLocalizedMessage() + "\n" + e.toString() + "\nsendMessage");
                    closeEverything();
                }
            //}).start();
        }


        public void listenForMessage() {
            new Thread(() -> {
                String msgFromGroupChat;

                while (socket.isConnected()) {
                    try {

                        /*Log.e(TAG, "Está escutando!");
                        msgFromGroupChat = bufferedReader.readLine();
                        String finalMsgFromGroupChat = msgFromGroupChat;
                        runOnUiThread(() -> textStream.append("\n" + finalMsgFromGroupChat));
                        Log.e(TAG, "PEGOU A MENSAGEM: " + msgFromGroupChat);*/
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage() + "\n" + e.getLocalizedMessage() + "\n" + e.toString() + "\nlistenForMessage");
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
                Log.e(TAG, e.getMessage() + "\n" + e.getLocalizedMessage() + "\n" + e.toString() + "\ncloseEverything");
            }
        }
    }
}
