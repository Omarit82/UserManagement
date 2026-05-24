package com.roselli208carucci301.usermanagement;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import androidx.room.Room;
import androidx.appcompat.widget.Toolbar;

import com.roselli208carucci301.usermanagement.database.AppDatabase;
import com.roselli208carucci301.usermanagement.database.Contacto;

public class EditContactActivity extends AppCompatActivity {
    private AppDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_contact);

        //Toolbar Editar Contacto
        Toolbar toolbar = findViewById(R.id.toolbarEditar);
        setSupportActionBar(toolbar);

        //Mostrar flecha atrás en la toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "agenda_db"
        ).allowMainThreadQueries().build();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Contacto c= (Contacto) getIntent().getSerializableExtra("contacto");
        TextView txtNombre = findViewById(R.id.txtNombreContacto);
        EditText editTextNombre = findViewById(R.id.editTextNombre);
        EditText editTextApellido = findViewById(R.id.editTextApellido);
        EditText editTextTelefono = findViewById(R.id.editTextTelefono);
        Button btnActualizar = findViewById(R.id.btn_actualizar);
        Button btnEliminar = findViewById(R.id.btn_eliminar);

        if (c != null) {
            txtNombre.setText(c.getApellido() + ", " + c.getNombre());
            editTextNombre.setText(c.getNombre());
            editTextApellido.setText(c.getApellido());
            editTextTelefono.setText(c.getTelefono());
        }

        //Boton Actualizar funcion
        btnActualizar.setOnClickListener(v -> {

            c.setNombre(editTextNombre.getText().toString().trim());
            c.setApellido(editTextApellido.getText().toString().trim());
            c.setTelefono(editTextTelefono.getText().toString().trim());

            db.contactoDao().actualizar(c);

            Toast.makeText(this, "Contacto actualizado", Toast.LENGTH_SHORT).show();

            finish();
        });

        //Boton Eliminar funcion
        btnEliminar.setOnClickListener(v -> {
            if (c != null) {
                db.contactoDao().eliminar(c);
                Toast.makeText(this, "Contacto eliminado", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}