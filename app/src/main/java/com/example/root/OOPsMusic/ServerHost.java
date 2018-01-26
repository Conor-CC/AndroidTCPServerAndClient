package com.example.root.OOPsMusic;

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

public class ServerHost extends AppCompatActivity {

    Button goBackHome;
    Button toggleServer;
    TextView addressView;
    TextView connectedHostsView;
    Handler addressHandler;
    Handler connectedHostsHandler;
    int port = 8070;
    MultiClientServer multiClientTCPServer = new MultiClientServer(port);
    boolean serverOnline;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_a_server_screen);
        serverOnline = false;
        instantiateThreads();
    }

    protected void instantiateThreads() {
        initialiseViews();
        initialiseHandlers();
    }

    protected void initialiseServer() {
        //multiClientTCPServer = new MultiClientServer(port);
        multiClientTCPServer.start();
    }

    protected void closeServer() {
        multiClientTCPServer.closeServerSocket();
        multiClientTCPServer = null;
        multiClientTCPServer = new MultiClientServer(port);
    }

    protected void initialiseViews() {
        addressView = (TextView) findViewById(R.id.AddressView);
        String address = getIpAddress() + ":" + port;
        addressView.setText(address);

        connectedHostsView = (TextView) findViewById(R.id.ConnectedHostsView);

        goBackHome = (Button) findViewById(R.id.ReturnToHomeScreen);
        goBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        toggleServer = (Button) findViewById(R.id.ToggleServer);
        toggleServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!serverOnline) {
                    initialiseServer();
                    serverOnline = true;
                    toggleServer.setText("Close Server");
                }
                else if (serverOnline) {
                    closeServer();
                    serverOnline = false;
                    toggleServer.setText("Create Server");
                }
            }
        });
    }

    protected void initialiseHandlers() {
        addressHandler = new Handler();
        addressHandler.post(updateAddressView);

        connectedHostsHandler = new Handler();
        connectedHostsHandler.post(updateConnectedHostsView);
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
