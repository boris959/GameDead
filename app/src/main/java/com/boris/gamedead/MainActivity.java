package com.boris.gamedead;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    Button BTNLOGIN, BTNREGISTRAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BTNLOGIN = findViewById(R.id.BTNLOGIN);
        BTNREGISTRAR = findViewById(R.id.BTNREGISTRAR);

        String ubicacion = "fuentes/zombie.TTF";
        Typeface Tf = Typeface.createFromAsset(MainActivity.this.getAssets(),ubicacion);

        BTNLOGIN.setTypeface(Tf);
        BTNREGISTRAR.setTypeface(Tf);

        BTNLOGIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //NOS LLEVA AL LOGIN
                Intent intent = new Intent(MainActivity.this,Login.class);
                startActivity(intent);
            }
        });

        //NOS LLEVA AL REGISTRO
        BTNREGISTRAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Registro.class);
                startActivity(intent);
            }
        });
    }
}

