package com.boris.gamedead;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class Menu extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference JUGADORES;

    TextView MiPuntuaciontxt,uid,correo,nombre,edad,pais,Menu;
    TextView Zombies;
    Button JugarBtn,EditarBtn,CambiarPassBtn,PuntuacionesBtn,AcercaDeBtn,CerrarSesion;
    CircleImageView imagenPerfil;

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
        //PERFIL
        MiPuntuaciontxt = findViewById(R.id.MiPuntuaciontxt);
        imagenPerfil = findViewById(R.id.imagenPerfil);
        Zombies = findViewById(R.id.Zombies);
        uid = findViewById(R.id.uid);
        correo = findViewById(R.id.correo);
        nombre = findViewById(R.id.nombre);
        edad = findViewById(R.id.edad);
        pais = findViewById(R.id.pais);
        Menu = findViewById(R.id.Menutxt);

        //OPCIONES DEL JUEGO
        JugarBtn = findViewById(R.id.JugarBtn);
        EditarBtn = findViewById(R.id.EditarBtn);
        CambiarPassBtn = findViewById(R.id.CambiarPassBtn);
        PuntuacionesBtn = findViewById(R.id.PuntuacionesBtn);
        AcercaDeBtn = findViewById(R.id.AcercaDeBtn);
        CerrarSesion = findViewById(R.id.CerrarSesionBtn);

        MiPuntuaciontxt.setTypeface(Tf);
        uid.setTypeface(Tf);
        correo.setTypeface(Tf);
        nombre.setTypeface(Tf);
        edad.setTypeface(Tf);
        pais.setTypeface(Tf);
        Zombies.setTypeface(Tf);
        Menu.setTypeface(Tf);

        /*CAMBIO DE FUENTE DE LETRA*/

        JugarBtn.setTypeface(Tf);
        EditarBtn.setTypeface(Tf);
        CambiarPassBtn.setTypeface(Tf);
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
        EditarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Menu.this, "Editar", Toast.LENGTH_SHORT).show();
                EditarDatos();
            }
        });
        CambiarPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Menu.this, "Cambiar Contraseña", Toast.LENGTH_SHORT).show();
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

    /*METODO PARA CAMBIAR DATOS*/

    private void EditarDatos() {
        //DEFINIDO EL ARREGLO CON LAS OPCIONES QUE PODREMOS ELEGIR
        String [] Opciones = {"Foto de perfil","Cambiar edad","Cambiar pais"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(Opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (i == 0){
                    ActualizarFotoPerfil();
                }

                if (i == 1){
                    ActualizarEdad("Edad");
                }

                if (i == 2){
                    ActualizarPais("Pais");
                }
            }
        });
        builder.create().show();
    }

    private void ActualizarEdad(final String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cambiar:"+key);
        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(this);
        linearLayoutCompat.setOrientation(LinearLayoutCompat.VERTICAL);
        linearLayoutCompat.setPadding(10,10,10,10);
        final EditText editText = new EditText(this);
        editText.setHint("Ingrese"+key);
        linearLayoutCompat.addView(editText);
        builder.setView(linearLayoutCompat);

        //SI EL USUARIO HACE CLIK EN ACTUALIZAR
        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String value = editText.getText().toString().trim();
                HashMap<String,Object> result = new HashMap<>();
                result.put(key,value);
                JUGADORES.child(user.getUid()).updateChildren(result)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Menu.this, "DATO ACTUALIZADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Menu.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(Menu.this, "CANCELADO POR EL USUARIO", Toast.LENGTH_SHORT).show();

            }
        });
        builder.create().show();

    }

    private void ActualizarPais(final String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cambiar:"+key);
        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(this);
        linearLayoutCompat.setOrientation(LinearLayoutCompat.VERTICAL);
        linearLayoutCompat.setPadding(10,10,10,10);
        final EditText editText = new EditText(this);
        editText.setHint("Ingrese"+key);
        linearLayoutCompat.addView(editText);
        builder.setView(linearLayoutCompat);

        //SI EL USUARIO HACE CLIK EN ACTUALIZAR
        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String value = editText.getText().toString().trim();
                HashMap<String,Object> result = new HashMap<>();
                result.put(key,value);
                JUGADORES.child(user.getUid()).updateChildren(result)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Menu.this, "DATO ACTUALIZADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Menu.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(Menu.this, "CANCELADO POR EL USUARIO", Toast.LENGTH_SHORT).show();

            }
        });
        builder.create().show();
    }

    /*CAMBIO DE VIDEO*/
    private void ActualizarFotoPerfil() {
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

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String zombiesString = "" + ds.child("Zombies").getValue();
                    String uidString = "" + ds.child("Uid").getValue();
                    String emailString = "" + ds.child("Email").getValue();
                    String edadString = "" + ds.child("Edad").getValue();
                    String paisString = "" + ds.child("Pais").getValue();
                    String imagen = "" + ds.child("Imagen").getValue();
                    String nombreString = "" + ds.child("Nombres").getValue();

                    Zombies.setText(zombiesString);
                    uid.setText(uidString);
                    correo.setText("Correo:"+emailString);
                    nombre.setText("Nombre:"+nombreString);
                    edad.setText("Edad:"+edadString);
                    pais.setText("Pais:"+paisString);
                    try {
                        Picasso.get().load(imagen).into(imagenPerfil);
                    } catch (Exception e) {
                        Picasso.get().load(R.drawable.soldado).into(imagenPerfil);
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}