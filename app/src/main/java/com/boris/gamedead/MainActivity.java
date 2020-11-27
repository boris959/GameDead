package com.boris.gamedead;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button BTNLOGIN, BTNREGISTRAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BTNLOGIN = findViewById(R.id.BTNLOGIN);
        BTNREGISTRAR = findViewById(R.id.BTNREGISTRAR);
        BTNLOGIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Mensaje
                Toast.makeText(MainActivity.this, "Haz entrado al Login", Toast.LENGTH_SHORT).show();
            }
        });
        BTNREGISTRAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Registro.class);
                startActivity(intent);
            }
        });
    }
}

