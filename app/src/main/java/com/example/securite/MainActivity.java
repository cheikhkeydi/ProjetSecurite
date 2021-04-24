package com.example.securite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button vigenere,hill,des,transpR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vigenere =(Button) findViewById(R.id.vigenere);
        hill =(Button) findViewById(R.id.hill);
        des =(Button) findViewById(R.id.des);
        transpR =(Button) findViewById(R.id.transpR);

        //Redirection vers activity  Vigenere
        vigenere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent page = new Intent(getApplicationContext(),vigenere.class);
                startActivity(page);
            }
        });

        //Redirection vers activity  hill
        hill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent page = new Intent(getApplicationContext(),Hill.class);
                startActivity(page);
            }
        });

        //Redirection vers activity  des
        des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent page = new Intent(getApplicationContext(),DES.class);
                startActivity(page);
            }
        });

        //Redirection vers activity  transpR
        transpR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent page = new Intent(getApplicationContext(),TranspositionRect.class);
                startActivity(page);
            }
        });

    }
}