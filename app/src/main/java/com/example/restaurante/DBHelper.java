package com.example.restaurante;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    //creacion de la base de datos
    private static final String DB_NOMBRE = "restaurante.db";
    private static final int DB_VERSION = 1;
    public static final String TABLA_USUARIOS = "usuarios";

    //constructor
    public DBHelper(@Nullable Context context) {
        super(context, DB_NOMBRE, null, DB_VERSION);
    }


    //metodo para crear las tablas e insertar datos
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + TABLA_USUARIOS + "(" +
                "id integer primary key autoincrement," +
                "nombre text,usuario text,contra text,tipo integer)");
        db.execSQL("INSERT INTO " + TABLA_USUARIOS + " (nombre,usuario,contra) VALUES ('Alisson Bellot Cuba'," +
                "'admin@gmail.com','123456')");
    }

    //metodo para borrar tablas y crear de nuevo si se cambia la DB VERSION
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table " + TABLA_USUARIOS);
        onCreate(db);
    }
}
