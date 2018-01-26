package com.example.root.switchscreens;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by root on 25/01/18.
 */

public class MultiClientServer extends AppCompatActivity {

    Button goBackHome;
    TextView addressView;
    TextView connectedHostsView;
    Server multiClientTCPServer;
    String address;
    Handler addressHandler;
    Handler connectedHostsHandler;
    int port = 8070;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_numero_uno);
        instantiateThreads();
    }

    protected void instantiateThreads() {

        addressView = (TextView) findViewById(R.id.AddressView);
        connectedHostsView = (TextView) findViewById(R.id.ConnectedHostsView);
        addressView.setText("CUNT");

        multiClientTCPServer = new Server(port);
        multiClientTCPServer.start();

        address = getIpAddress() + ":" + port;
        addressView.setText(address);

        addressHandler = new Handler();
        addressHandler.post(updateAddressView);

        connectedHostsHandler = new Handler();
        connectedHostsHandler.post(updateConnectedHostsView);


        goBackHome = (Button) findViewById(R.id.button3);

        goBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    multiClientTCPServer.stop();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
    }

    protected String getIpAddress() {
        WifiManager wifiMan = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        int ipAddress = wifiInf.getIpAddress();
        return String.format("%d.%d.%d.%d", (ipAddress & 0xff),(ipAddress >> 8 & 0xff),(ipAddress >> 16 & 0xff),(ipAddress >> 24 & 0xff));
    }

    protected Runnable updateAddressView = new Runnable() {

        @Override
        public void run() {
            String toUpdate = getIpAddress() + ":" + port;
            addressView.setText(toUpdate);
            addressHandler.postDelayed(this, 1000);
        }
    };

    protected Runnable updateConnectedHostsView = new Runnable() {

        @Override
        public void run() {
            String toUpdate = "Connections List:\n" + multiClientTCPServer.listCurrentConnections();
            connectedHostsView.setText(toUpdate);
            connectedHostsHandler.postDelayed(this, 1000);
        }
    };
}
