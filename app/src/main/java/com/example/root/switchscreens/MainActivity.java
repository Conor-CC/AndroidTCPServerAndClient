package com.example.root.switchscreens;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button goToScreenOne;
    Button getGoToScreenTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialiseButtons();
    }

    protected void initialiseButtons() {
        goToScreenOne = (Button) findViewById(R.id.button1);
        getGoToScreenTwo = (Button) findViewById(R.id.button2);
//        unoGoBackToHome = (Button) findViewById(R.id.button3);
//        dosGoBackToHome = (Button) findViewById(R.id.button4);


        goToScreenOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MultiClientServer.class);
                startActivity(intent);
            }
        });

        getGoToScreenTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TCPClient.class);
                startActivity(intent);
            }
        });

    }
}
