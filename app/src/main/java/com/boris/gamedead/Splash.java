package com.boris.gamedead;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int DURACION_SPLASH = 3500;
        //Handler ejecuta lineas de codigo en un tiempo determinado
        new Handler().postDelayed(new Runnable(){
            public void run(){
                //se ejecuta
                Intent intent = new Intent(Splash.this,Menu.class);
                startActivity(intent);
            };
        },DURACION_SPLASH);
    }
}