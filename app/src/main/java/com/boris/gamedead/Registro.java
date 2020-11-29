package com.boris.gamedead;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.ref.Reference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

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
        SimpleDateFormat fecha = new SimpleDateFormat("d 'de' MMMM 'del' yyyy");
        final String StringFecha = fecha.format(date);
        fechaTxt.setText(StringFecha);

        String ubicacion = "fuentes/zombie.TTF";
        Typeface Tf = Typeface.createFromAsset(Registro.this.getAssets(),ubicacion);

        registrarBt.setTypeface(Tf);

        registrarBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = correoET.getText().toString();
                String password = passET.getText().toString();

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    correoET.setError("correo invalido");
                    correoET.setFocusable(true);
                }else if (password.length()<6){
                    passET.setError("Contraseña debe ser mayor a 6");
                    correoET.setFocusable(true);
                }else {
                    RegistrarJugador(email,password);
                }
            }
        });

    }


    private void RegistrarJugador(String email, String password) {
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = auth.getCurrentUser();

                            int contador = 0;

                            assert user != null;
                            String uidString = user.getUid();
                            String correoString = correoET.getText().toString();
                            String passString = passET.getText().toString();
                            String nombreString = nombreET.getText().toString();
                            String fechaString = fechaTxt.getText().toString();

                            HashMap<Object,Object>  DatosJugador = new HashMap<>();

                            DatosJugador.put("Uid",uidString);
                            DatosJugador.put("Email",correoString);
                            DatosJugador.put("Password",passString);
                            DatosJugador.put("Nombres",nombreString);
                            DatosJugador.put("Fecha",fechaString);
                            DatosJugador.put("Zombies",contador);

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("MI DATA BASE JUGADORES");
                            reference.child(uidString).setValue(DatosJugador);
                            startActivity(new Intent(Registro.this,Menu.class));
                            Toast.makeText(Registro.this, "USUARIO REGISTRADO EXITOSAMENTE", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(Registro.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Registro.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}