package com.example.chatsocket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread implements Runnable {
    private static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        start();
    }

    @Override
    public void run() {
        String messageFromClient;

        try {
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            clientHandlers.add(this);

            broadcastMessage("SERVER: Anonymous has entered the chat!");

            while (socket.isConnected()) {
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);
            }
        } catch (Exception e) {
            e.printStackTrace();
            closeEverything();
        }
    }

    public void broadcastMessage(String messageToSend) {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                clientHandler.bufferedWriter.write(messageToSend);
                clientHandler.bufferedWriter.newLine();
                clientHandler.bufferedWriter.flush();
            } catch (Exception e) {
                e.printStackTrace();
                closeEverything();
            }
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage("SERVER: Anonymous has left the chat!");
    }

    public void closeEverything() {
        removeClientHandler();
        try {
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
