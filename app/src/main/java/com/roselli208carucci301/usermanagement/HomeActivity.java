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
import androidx.room.Room;

import java.util.Collections;
import android.view.View;

import android.content.SharedPreferences; //Importamos el SharedPreferences para el storage del login



import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.roselli208carucci301.usermanagement.database.AppDatabase;
import com.roselli208carucci301.usermanagement.database.Contacto;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    /*Defino un arraylist para acumular los contactos*/
    private final ArrayList<Contacto> contactos = new ArrayList<>();
    private RecyclerView recyclerContactos;
    private TextView txtEmpty;
    private ContactoAdapter adapter;
    private ActivityResultLauncher<Intent> start;
    private AppDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        db = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"agenda_db").allowMainThreadQueries().build();

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

        contactos.addAll(db.contactoDao().getAllContacts());

        Collections.sort(contactos, (c1,c2) ->{
            String apellido1 = c1.getApellido().toLowerCase();
            String apellido2 = c2.getApellido().toLowerCase();
            return apellido1.compareTo(apellido2);
        });

        adapter.actualizarLista(contactos);

        start = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Contacto nuevo = (Contacto) result.getData().getSerializableExtra("contactoNuevo");
                if (nuevo != null) {
                    db.contactoDao().insertar(nuevo);
                    contactos.add(nuevo);

                    contactos.clear();
                    contactos.addAll(db.contactoDao().getAllContacts());

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

    //Sobreescribimos el metodo original de Android para el menu de cerrar sesion
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Al tocar "Cerrar sesión"
        if (item.getItemId() == R.id.action_logout) {
            SharedPreferences preferences = getSharedPreferences("sesion", MODE_PRIVATE);//Abrimos SharedPreferences
            SharedPreferences.Editor editor = preferences.edit(); //Creamos editor para modificar preferencias
            editor.putBoolean("usuarioAutenticado", false); //Cambiamos usuarioAutenticado a false para el deslogueo
            editor.apply(); //Guardamos cambios

            //Volvemos al LoginActivity
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);

            //Cerramos HomeActivity
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
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