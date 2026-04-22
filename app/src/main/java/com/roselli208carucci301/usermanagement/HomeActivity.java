package com.roselli208carucci301.usermanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
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
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.Collections;
import android.view.View;



import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    /*Defino un arraylist para acumular los contactos*/
    private final ArrayList<Contacto> contactos = new ArrayList<>();
    private RecyclerView recyclerContactos;
    private TextView txtEmpty;
    private ContactoAdapter adapter;
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

        recyclerContactos = findViewById(R.id.recyclerContactos);
        txtEmpty = findViewById(R.id.txtEmpty);
        recyclerContactos.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ContactoAdapter(contactos);
        recyclerContactos.setAdapter(adapter);
        start = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Contacto nuevo = (Contacto) result.getData().getSerializableExtra("contactoNuevo");
                if (nuevo != null) {
                    contactos.add(nuevo);
                    //Ordenar por apellido
                    Collections.sort(contactos, (c1, c2) -> {
                        String apellido1 = c1.getApellido().toLowerCase();
                        String apellido2 = c2.getApellido().toLowerCase();
                        return apellido1.compareTo(apellido2);
                    });
                    adapter.actualizarLista(contactos);
                    actualizarEstado();
                }
            }else{
                Toast.makeText(HomeActivity.this,"Carga de nuevo usuario cancelada",Toast.LENGTH_SHORT).show();
            }
        });
        btnAdd.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, AgregarContactoActivity.class);
            start.launch(intent);
        });

        //Toolbar del buscador
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actualizarEstado();
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
                adapter.filtrar(newText);
                actualizarEstado();

                return true;
            }
        });

        return true;
    }
    //Metodo que muestra si no hay contactos el textView de No hay contactos agregados
    //Si busca contactos pero no hay coincidencia muesta Sin Resultados
    //Y si hay, muestra la lista
    private void actualizarEstado() {
        if (contactos.isEmpty()) {
            txtEmpty.setText("Aún no tenés contactos");
            txtEmpty.setVisibility(View.VISIBLE);
            recyclerContactos.setVisibility(View.GONE);
        } else if (adapter.estaVacia()) {
            txtEmpty.setText("Sin resultados");
            txtEmpty.setVisibility(View.VISIBLE);
            recyclerContactos.setVisibility(View.GONE);
        } else {
            txtEmpty.setVisibility(View.GONE);
            recyclerContactos.setVisibility(View.VISIBLE);
        }
    }
}