package com.example.root.switchscreens;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by root on 25/01/18.
 */

public class Server implements Runnable {

    int port;
    ServerSocket serverSocket;
    Socket socket;
    ArrayList<String> users = new ArrayList<String>();

    public Server(int port) {
        serverSocket = null;
        socket = null;
        this.port = port;

        try {
            serverSocket = new ServerSocket(port); //Can use just the one port for all clients.
            //Socket We have is what we use to receive info from user, port is just how to get to it.
            //Threads needed to distinguish different users data.
        } catch (IOException e) {

        }
    }

    public void start() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                socket = serverSocket.accept();
//                System.out.println("\nAccepted connection " + socket.getRemoteSocketAddress());
//                System.out.print("\n");
                users.add(String.valueOf(socket.getRemoteSocketAddress()));
            } catch (IOException e) {
//                System.out.println("I/O error: " + e);
            }
            // new thread for a client
            new EchoThread(socket).start();
        }
    }

    public void stop() throws IOException {
        serverSocket.close();
        socket.close();
    }

    public String listCurrentConnections () {
        if (users.isEmpty())
            return "No active connections...";
        String toReturn = "";
        for (String user : users) {
            toReturn += user + "\n";
        }
        return toReturn;
    }

    public int getPort() {
        return port;
    }

    private class EchoThread extends Thread {
        protected Socket socket;

        public EchoThread(Socket clientSocket) {
            this.socket = clientSocket;
        }

        public void run() {
            InputStream inp = null;
            BufferedReader brinp = null;
            DataOutputStream out = null;
            try {
                inp = socket.getInputStream();
                brinp = new BufferedReader(new InputStreamReader(inp));
                out = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                return;
            }
            String line;
            while (true) {
                try {
                    line = brinp.readLine();
                    if ((line == null) || line.equalsIgnoreCase("QUIT")) {
                        users.remove(String.valueOf(socket.getRemoteSocketAddress()));
                        socket.close();
                        System.out.println("Removed a user \n");
                        for (String user : users) {
                            System.out.print(user + "    ");
                        }
                        return;
                    } else {
                        out.writeBytes(line + "\n\r");
                        out.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }
}
