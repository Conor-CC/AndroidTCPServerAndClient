package com.example.root.switchscreens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by root on 25/01/18.
 */

public class TCPClient extends AppCompatActivity {

    Button goBackHome;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_numero_uno);
        goBackHome = (Button) findViewById(R.id.button3);

        goBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
