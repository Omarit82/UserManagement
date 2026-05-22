package com.roselli208carucci301.usermanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.SharedPreferences; //Import para SharedPreferences

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        //Al abrir hay que leer la clave del "sesion" para ingresar directo o loguearse
        SharedPreferences preferences = getSharedPreferences("sesion", MODE_PRIVATE); //Creamos (O lo abrimos si ya existe) un archivo de preferencias que llamamos "sesion"
        boolean autenticado = preferences.getBoolean("usuarioAutenticado", false); //Leemos la clave "usuarioAutenticado", si no existe = false y nos lleva al login.
        //Si existe entonces = true y pasamos directo al HomeActivity.
        if (autenticado) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button init = findViewById(R.id.init);

        init.setOnClickListener(v -> {
            EditText user = findViewById(R.id.email);
            EditText pass = findViewById(R.id.password);
            if(user.getText().toString().equals("admin@admin.com") && pass.getText().toString().equals("1234")){

                //Guardar sesion
                SharedPreferences.Editor editor = preferences.edit(); //Creamos "editor" para poder modificar ese archivo
                editor.putBoolean("usuarioAutenticado", true);  //Guardamos la clave "usuarioAutenticado" de valor true
                editor.apply(); //Guardamos los cambios

                Toast.makeText(LoginActivity.this,"Logged in",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(LoginActivity.this,"Error",Toast.LENGTH_SHORT).show();
            }
        });

    }
}