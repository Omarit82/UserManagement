package com.roselli208carucci301.usermanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    /*Defino un arraylist para acumular los contactos*/
    private final ArrayList<Contacto> contacts = new ArrayList<>();
    private TextView contactState;
    private ActivityResultLauncher<Intent> start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FloatingActionButton btnAdd = findViewById(R.id.btnIrAgregarContacto);
        contactState= findViewById(R.id.contactList);
        refreshView();
        start = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Contacto nuevo = (Contacto) result.getData().getSerializableExtra("contactoNuevo");
                Log.d("DEBUG_HOME", "Nuevo contacto recibido: " + nuevo.getNombre());
                if (nuevo != null) {
                    contacts.add(nuevo);
                    refreshView();
                }
            }
        });
        btnAdd.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, AgregarContactoActivity.class);
            startActivity(intent);

        });
    }
    private void refreshView() {
        if (contacts.isEmpty()) {
            contactState.setText(R.string.empty_message);
        } else {
            StringBuilder sb = new StringBuilder("Lista de Contactos:\n");
            for (Contacto c : contacts) {
                sb.append(c.getApellido()+", "+c.getNombre()).append("\n").append("Telefono: "+c.getTelefono());
            }
            contactState.setText(sb.toString());
        }
    }
}