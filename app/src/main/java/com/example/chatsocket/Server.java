package com.example.chatsocket;

import android.widget.Toast;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread implements Runnable {
    private ServerSocket serverSocket;
    private int port;

    public Server(int port) {
        this.port = port;
        start();
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);

            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
