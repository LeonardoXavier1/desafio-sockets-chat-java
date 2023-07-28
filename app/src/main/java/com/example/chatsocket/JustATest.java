package com.example.chatsocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class JustATest implements Runnable{

    private ArrayList<ConecctionHandler> connections;

    @Override
    public void run() {

        try {
            ServerSocket server = new ServerSocket(9999);
            Socket client = server.accept();
        } catch (IOException e) {
            // TODO: handle
        }

    }

    class ConecctionHandler implements Runnable{

        private Socket client;
        private BufferedReader in;
        private PrintWriter out;
        private String nickname;

        public ConecctionHandler(Socket  client){
            this.client = client;
        }

        @Override
        public void run() {

            try {
                out = new PrintWriter(client.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out.println("Pls enter a nickname: ");
                nickname = in.readLine();
                System.out.println(nickname + "Connected!");
            } catch (IOException e){
                // TODO: handle
            }

        }
    }
}
