package com.example.root.switchscreens;

/**
 * Created by root on 26/01/18.
 */

import android.app.Activity;

import java.io.*;
import java.net.*;

class TCPIPClient extends Activity implements Runnable {

    String sentence;
    String modifiedSentence = "";
    String ip;
    String mostRecentResponse = "POOOOOOOOOOOOO";
    int port;
    private boolean isConnected;
    Socket clientSocket;

    public void setIPandPortNum(String ip, int portNum) {
        this.ip = ip;
        this.port = portNum;
    }

    public void start() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            clientSocket = new Socket(ip, port);
            System.out.println("Connected to " + ip + ":" + port);
            isConnected = true;
            boolean run = true;
            while (run) {
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                modifiedSentence = inFromServer.readLine();
                mostRecentResponse = modifiedSentence;
                if (modifiedSentence == null) {
                    run = false;
                }
                System.out.println("FROM SERVER: " + modifiedSentence);
            }
            clientSocket.close();
        } catch (java.io.IOException e) {
            System.out.println("FAILURE: " + e);
        }
        isConnected = false;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void sendTrans(String trans) throws IOException {
        if (isConnected) {
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            sentence = trans;
            outToServer.writeBytes(sentence + '\n');
        }
    }

    public String getMostRecentResponse() {
        return  mostRecentResponse;
    }

}