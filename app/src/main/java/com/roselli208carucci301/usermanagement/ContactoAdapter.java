package com.roselli208carucci301.usermanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//Esta clase conecta la lista de contactos con el RecyclerView (Que sirve para mostrar los contactos en android)
public class ContactoAdapter extends RecyclerView.Adapter<ContactoAdapter.ViewHolder> {

    //Lista de contactos que vamos a mostrar
    private ArrayList<Contacto> lista;
    private ArrayList<Contacto> listaOriginal;

    //Constructor: recibe la lista desde HomeActivity y una copia
    public ContactoAdapter(ArrayList<Contacto> lista) {
        this.lista = new ArrayList<>(lista);
        this.listaOriginal = new ArrayList<>(lista);
    }

    //Metodo para actualizar lista
    public void actualizarLista(ArrayList<Contacto> nuevaLista) {
        lista.clear();
        lista.addAll(nuevaLista);

        listaOriginal.clear();
        listaOriginal.addAll(nuevaLista);

        notifyDataSetChanged();
    }

    //Metodo para filtrar
    public void filtrar(String texto) {
        lista.clear();

        if (texto.isEmpty()) {
            lista.addAll(listaOriginal);
        } else {
            for (Contacto c : listaOriginal) {
                String nombreCompleto = (c.getApellido() + " " + c.getNombre()).toLowerCase();

                if (nombreCompleto.contains(texto.toLowerCase())) {
                    lista.add(c);
                }
            }
        }

        notifyDataSetChanged();
    }

    //Clase interna ViewHolder
    //Representa 1 item de la lista (El contacto a mostrar)
    public static class ViewHolder extends RecyclerView.ViewHolder {

        //Referencias a los elementos del layout item_contacto.xml
        TextView nombreCompleto, telefono;
        ImageView avatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Vinculamos los elementos del XML con el código
            nombreCompleto = itemView.findViewById(R.id.txtNombreCompleto);
            telefono = itemView.findViewById(R.id.txtTelefono);
            avatar = itemView.findViewById(R.id.imgAvatar);
        }
    }

    //Se ejecuta cuando RecyclerView necesita crear un nuevo item
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Muestra el layout item_contacto.xml (lo convierte en vista)
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contacto, parent, false);

        //Devuelve un nuevo ViewHolder con esa vista
        return new ViewHolder(vista);
    }

    //Se ejecuta para cargar datos en cada item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //Obtenemos el contacto en la posición actual
        Contacto c = lista.get(position);

        //Mostramos los datos en el item
        holder.nombreCompleto.setText(c.getApellido() + ", " + c.getNombre());
        holder.telefono.setText(c.getTelefono());
    }

    //Indica cuántos elementos tiene la lista
    @Override
    public int getItemCount() {
        return lista.size();
    }

    //Si la lista esta vacia va a mostrar un mensaje
    public boolean estaVacia() {
        return lista.isEmpty();
    }
}
