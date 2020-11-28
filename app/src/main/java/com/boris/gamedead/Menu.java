package com.boris.gamedead;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Menu extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    @Override
    protected void onStart() {
        UsuarioLogueado();
        super.onStart();
    }

    private void  UsuarioLogueado(){

        if (user != null){
            Toast.makeText(this, "Jugador en linea", Toast.LENGTH_SHORT).show();
        }
        else {
            startActivity(new Intent(Menu.this,MainActivity.class));
            finish();
        }
    }


}