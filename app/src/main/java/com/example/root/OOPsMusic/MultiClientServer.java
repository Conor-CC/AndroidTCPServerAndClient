package com.example.root.OOPsMusic;

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

public class MultiClientServer implements Runnable {

    int port;
    ServerSocket serverSocket;
    Socket socket;
    ArrayList<String> users;
    ArrayList<EchoThread> threads;
    boolean online;

    public MultiClientServer(int port) {
        serverSocket = null;
        socket = null;
        this.port = port;
        online = false;
        users = new ArrayList<String>();
        threads = new ArrayList<EchoThread>();

    }

    public void start() {
        online = true;
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port); //Can use just the one port for all clients.
            //Socket We have is what we use to receive info from user, port is just how to get to it.
            //Threads needed to distinguish different users data.
        } catch (IOException e) {
            System.out.println("FUCCCCKKK: " + e );
        }

        while (true) {
            if (serverSocket != null) {
                try {
                    socket = serverSocket.accept();
//                System.out.println("\nAccepted connection " + socket.getRemoteSocketAddress());
//                System.out.print("\n");
                    users.add(String.valueOf(socket.getRemoteSocketAddress()));
                    EchoThread t = new EchoThread(socket);
                    threads.add(t);
                    threads.get(threads.indexOf(t)).start();
                } catch (IOException e) {
//                System.out.println("I/O error: " + e);
                }
            }
            // new thread for a client

        }
    }

    public void closeServerSocket() {
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.serverSocket = null;
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
                            threads.remove(threads.get(threads.indexOf(this)));
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
//            if (serverSocket.isClosed()) {
//                try {
//                    this.socket.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                if (!threads.isEmpty()) {
//                    EchoThread t = threads.get(threads.indexOf(this));
//                    threads.remove(t);
//                    t = null;
//                }
////                try {
////                    t.socket.close();
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////                t = null;
//
//            }
        }
}

