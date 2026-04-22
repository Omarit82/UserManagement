package com.roselli208carucci301.usermanagement;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AgregarContactoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_contacto);

        //Leemos el formulario
        EditText editTextNombre = findViewById(R.id.editTextNombre);
        EditText editTextApellido = findViewById(R.id.editTextApellido);
        EditText editTextTelefono = findViewById(R.id.editTextTelefono);
        EditText editTextDomicilio = findViewById(R.id.editTextDomicilio);
        RadioGroup radioGroupGenero = findViewById(R.id.radioGroupGenero);

        //Boton Guardar (Usa la info del formulario y vuelve al home
        ImageButton btnGuardarToolbar = findViewById(R.id.btnGuardarToolbar);
        btnGuardarToolbar.setOnClickListener(view -> {

            //Lee los campos completados
            String nombre = editTextNombre.getText().toString().trim();
            String apellido = editTextApellido.getText().toString().trim();
            String telefono = editTextTelefono.getText().toString().trim();
            String domicilio = editTextDomicilio.getText().toString().trim();
            int idSeleccionado = radioGroupGenero.getCheckedRadioButtonId();

            //Verifica que se haya seleccionado el genero
            if (idSeleccionado == -1) {
                Toast.makeText(this, "Seleccioná un género", Toast.LENGTH_SHORT).show();
                return;
            }

            //Verifica que esten todos completos
            if (nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() || domicilio.isEmpty()) {
                Toast.makeText(this, "Completá todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            //Pone como texto del campo, el nombre del RadioButton seleccionado
            RadioButton radioSeleccionado = findViewById(idSeleccionado);
            String genero = radioSeleccionado.getText().toString();

            //Crea el contacto
            Contacto contacto = new Contacto(nombre, apellido, telefono, domicilio, genero);

            //Muestra el mensaje Contacto guardado y vuelve (solo para probar)
            Intent resultado = new Intent();
            resultado.putExtra("contactoNuevo", contacto);

            setResult(RESULT_OK, resultado);

            Toast.makeText(this, "Contacto guardado", Toast.LENGTH_SHORT).show();
            finish();
        });

        //Boton Atras
        ImageButton btnAtrasToolbar = findViewById(R.id.btnAtrasToolbar);
        btnAtrasToolbar.setOnClickListener(view -> {
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}