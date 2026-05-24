package com.roselli208carucci301.usermanagement.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import java.util.List;

@Dao
public interface ContactoDao {
    @Insert
    public void insertar(Contacto contacto);

    @Query("SELECT * FROM contactos")
    List<Contacto> getAllContacts();

    @Update
    void actualizar(Contacto contacto);

    @Delete
    void eliminar(Contacto contacto);
}
