package com.boris.gamedead;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Registro extends AppCompatActivity {
    //declaracion variable
    EditText correoET, passET, nombreET;
    TextView fechaTxt;
    Button registrarBt;
    FirebaseAuth auth; //Autentificacion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        //conexion
        correoET = findViewById(R.id.correoET);
        passET = findViewById(R.id.passET);
        nombreET = findViewById(R.id.nombreET);
        fechaTxt = findViewById(R.id.fechaTxt);
        registrarBt = findViewById(R.id.registrarBt);

        auth = FirebaseAuth.getInstance();

        Date date = new Date();
        SimpleDateFormat fecha = new SimpleDateFormat("d 'de' MMMM 'del' yyyyy");
        String StringFecha = fecha.format(date);
        fechaTxt.setText(StringFecha);
    }
}