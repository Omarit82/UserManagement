package com.roselli208carucci301.usermanagement.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ContactoDao {
    @Insert
    void insertar(Contacto contacto);

    @Query("SELECT * FROM contactos")
    List<Contacto> getAllContacts();
}
