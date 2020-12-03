package com.boris.gamedead;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
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

    private StorageReference ReferenciaDeAlmacenamiento;
    private String RutaAlmacenamiento ="FotosDePerfil/*";
    /*PERMISOS */
    private static final int CODIGO_DE_SOLICITUD_DE_ALMACENAMIENTO =200;
    private static final int CODIGO_PARA_LA_SELECCION_DE_LA_IMAGEN =300;
    /*MATRICES */
    private String [] PermisosDeAlmacenamiento;
    private Uri imagen_uri;
    private String perfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        JUGADORES = firebaseDatabase.getReference("MI DATA BASE JUGADORES");

        ReferenciaDeAlmacenamiento = FirebaseStorage.getInstance().getReference();
        PermisosDeAlmacenamiento = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


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
                    perfil = "Imagen";
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
    /*CAMBIO DE VIDEO*/
    private void ActualizarFotoPerfil() {
    String  [] opciones = {"Galeria"};
    AlertDialog.Builder builder =  new AlertDialog.Builder(this);
    builder.setTitle("Seleccionar imagen de: ");
    builder.setItems(opciones, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if( i == 0){
                //SELECCIONO GALERIA
                if(!ComprobarPermisosAlmacenamiento()){
                    SolicitarPermisoAlmacenamiento();
                    //SI NO SE HABILITO EL PERMISO
                }else {
                    ElegirImagenGaleria();
                    //SI SE HABILITO EL PERMISO

                }
            }
        }
    });
            builder.create().show();
    }



    //´PERMISO DE ALMACENAMIENTO EN TIEMPO DE EJECUCION
    private void SolicitarPermisoAlmacenamiento() {
        requestPermissions(PermisosDeAlmacenamiento, CODIGO_DE_SOLICITUD_DE_ALMACENAMIENTO);
    }
    //COMPRUEBA SI LOS PERMISOS DE ALMACENAMIENTO ESTAN HABILITADOS
    private boolean ComprobarPermisosAlmacenamiento() {
        boolean resultado = ContextCompat.checkSelfPermission(Menu.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);
        return resultado;
    }

    //SE LLAMA CUANDO EL USUARIO  PRESIONA PERMITIR O DENEGAR EL CUADRO DE DIALOGO
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CODIGO_DE_SOLICITUD_DE_ALMACENAMIENTO:{
            //SELECCION DE LA GALERIA
            if(grantResults.length>0){
                boolean EscrituraDeAlmacenamientoAceptado =  grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if(EscrituraDeAlmacenamientoAceptado){
                    //FUE HABILITADO
                    ElegirImagenGaleria();
                }else{
                    Toast.makeText(this, "HABILITE EL PERMISO DE LA GALERIA", Toast.LENGTH_SHORT).show();
                }
            }
        }
            break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //SE LLAMA CUANDO EL USUARIO YA HA ELEGIDO LA IMAGEN DE LA GALERIA
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            //deLA IMAGEN VAMOS A OBTENER LA URI
            if (requestCode == CODIGO_PARA_LA_SELECCION_DE_LA_IMAGEN) {
                imagen_uri= data.getData();
                SubirFoTo(imagen_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //ESTE METODO CAMBIA LA FOTO DE PERFIL DEL JUGADOR Y ACTUALIZA LA INFORMACION EN LA BASE DE DATOS
    private void SubirFoTo(Uri imagen_uri) {
        String RutaDeArchivoYNombre = RutaAlmacenamiento + ""+ perfil+""+user.getUid();
        StorageReference storageReference = ReferenciaDeAlmacenamiento.child(RutaDeArchivoYNombre);
        storageReference.putFile(imagen_uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful());
                            Uri downloaduri = uriTask.getResult();
                        if(uriTask.isSuccessful()){
                                HashMap<String, Object> resultado= new HashMap<>();
                                resultado.put(perfil,downloaduri.toString());
                                JUGADORES.child(user.getUid()).updateChildren(resultado)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Menu.this, "LA IMAGEN HA SIDO CAMBIADA CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Menu.this, "HA OCURRIDO UN ERROR", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                Toast.makeText(Menu.this, "ALGO HA SALIDO MAL", Toast.LENGTH_SHORT).show();
                            }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Menu.this, "ALGO HA SALIDO MAL", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //ESTE METODO ABRE LA GALERIA
    private void ElegirImagenGaleria() {
        Intent IntentGaleria = new Intent(Intent.ACTION_PICK);
        IntentGaleria.setType("image/*");
        startActivityForResult(IntentGaleria, CODIGO_PARA_LA_SELECCION_DE_LA_IMAGEN);
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