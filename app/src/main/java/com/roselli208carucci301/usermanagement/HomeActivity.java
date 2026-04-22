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
import android.view.Menu;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import androidx.appcompat.widget.SearchView;

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
            start.launch(intent);

        });

        //Toolbar del buscador
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    //Funcion para la toolbar de busqueda
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Carga el archivo menu_home.xml en la toolbar
        getMenuInflater().inflate(R.menu.menu_home, menu);

        //Busca dentro del menú la lupa (action_search)
        MenuItem searchItem = menu.findItem(R.id.action_search);

        //Obtiene el componente visual del item, que es un SearchView
        SearchView searchView = (SearchView) searchItem.getActionView();

        //Lee lo que el usuario escribe en el buscador
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                //Se ejecuta cuando el usuario presiona enter o buscar
                //En este caso no hacemos nada porque filtramos al escribir
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Se ejecuta cada vez que se escribe o borra algo
                //Llamamos a nuestro metodo para filtrar contactos (estaá mas abajo)
                filtrarContactos(newText);

                return true;
            }
        });

        return true;
    }
    private void filtrarContactos(String texto) {
        //Si no hay contactos mostramos mensaje vacío
        if (contacts.isEmpty()) {
            contactState.setText(R.string.empty_message);
            return;
        }

        StringBuilder sb = new StringBuilder("Lista de Contactos:\n");
        boolean encontrado = false;

        //Recorremos todos los contactos
        for (Contacto c : contacts) {

            //Armamos nombre completo en minúscula para comparar
            String nombreCompleto = (c.getApellido() + " " + c.getNombre()).toLowerCase();

            //Si contiene el texto buscado
            if (nombreCompleto.contains(texto.toLowerCase())) {

                sb.append(c.getApellido())
                        .append(", ")
                        .append(c.getNombre())
                        .append("\n")
                        .append("Telefono: ")
                        .append(c.getTelefono())
                        .append("\n\n");

                encontrado = true;
            }
        }

        // Si encontro coincidencias
        if (encontrado) {
            contactState.setText(sb.toString());
        } else {
            contactState.setText("No se encontraron contactos");
        }
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