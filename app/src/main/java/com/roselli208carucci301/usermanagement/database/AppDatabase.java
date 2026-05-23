package com.roselli208carucci301.usermanagement.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Contacto.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ContactoDao contactoDao();
}
