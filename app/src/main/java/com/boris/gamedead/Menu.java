package com.boris.gamedead;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.SQLTransactionRollbackException;


public class Menu extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference JUGADORES;

    TextView MiPuntuaciontxt,uid,correo,nombre,Menu;
    TextView Zombies;
    Button JugarBtn,PuntuacionesBtn,AcercaDeBtn,CerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        JUGADORES = firebaseDatabase.getReference("MI DATA BASE JUGADORES");

        String ubicacion = "fuentes/zombie.TTF";
        Typeface Tf = Typeface.createFromAsset(Menu.this.getAssets(),ubicacion);

        MiPuntuaciontxt = findViewById(R.id.MiPuntuaciontxt);

        Zombies = findViewById(R.id.Zombies);
        uid = findViewById(R.id.uid);
        correo = findViewById(R.id.correo);
        nombre = findViewById(R.id.nombre);
        Menu = findViewById(R.id.Menutxt);

        JugarBtn = findViewById(R.id.JugarBtn);
        PuntuacionesBtn = findViewById(R.id.PuntuacionesBtn);
        AcercaDeBtn = findViewById(R.id.AcercaDeBtn);
        CerrarSesion = findViewById(R.id.CerrarSesionBtn);

        MiPuntuaciontxt.setTypeface(Tf);
        uid.setTypeface(Tf);
        correo.setTypeface(Tf);
        nombre.setTypeface(Tf);
        Menu.setTypeface(Tf);

        JugarBtn.setTypeface(Tf);
        PuntuacionesBtn.setTypeface(Tf);
        AcercaDeBtn.setTypeface(Tf);
        CerrarSesion.setTypeface(Tf);


        JugarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Menu.this, "JUGAR", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Menu.this, EscenarioJuego.class);

                String UidS = uid.getText().toString();
                String NombreS = nombre.getText().toString();
                String ZombieS = Zombies.getText().toString();

                intent.putExtra("UID",UidS);
                intent.putExtra("NOMBRE",NombreS);
                intent.putExtra("ZOMBIE",ZombieS);

                startActivity(intent);
                Toast.makeText(Menu.this, "ENVIANDO PARAMETROS", Toast.LENGTH_SHORT).show();
            }
        });

        PuntuacionesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Menu.this, "PUNTUACIONES", Toast.LENGTH_SHORT).show();
            }
        });

        AcercaDeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Menu.this, "ACERCA DE", Toast.LENGTH_SHORT).show();
            }
        });

        CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CerrarSesion();
            }
        });
    }

    @Override
    protected void onStart() {
        UsuarioLogueado();
        super.onStart();
    }

    private void  UsuarioLogueado(){

        if (user != null){
            Consulta();
            Toast.makeText(this, "Jugador en linea", Toast.LENGTH_SHORT).show();
        }
        else {
            startActivity(new Intent(Menu.this,MainActivity.class));
            finish();
        }
    }



    private void CerrarSesion(){
        auth.signOut();
        startActivity(new Intent(Menu.this,MainActivity.class));
        Toast.makeText(this, "Cerrado sesion exitosamemte", Toast.LENGTH_SHORT).show();
    }

    private void Consulta(){
        Query query = JUGADORES.orderByChild("Email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){

                    String zombiesString = ""+ds.child("Zombies").getValue();
                    String uidString = ""+ds.child("Uid").getValue();
                    String emailString = ""+ds.child("Email").getValue();
                    String nombreString = ""+ds.child("Nombres").getValue();

                    Zombies.setText(zombiesString);
                    uid.setText(uidString);
                    correo.setText(emailString);
                    nombre.setText(nombreString);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}