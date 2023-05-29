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
    public static final String TABLA_PRODUCTOS = "productos";
    public static final String TABLA_FACTURAS = "facturas";
    public static final String TABLA_PEDIDOS = "pedidos";

    //constructor
    public DBHelper(@Nullable Context context) {
        super(context, DB_NOMBRE, null, DB_VERSION);
    }


    //metodo para crear las tablas e insertar datos
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + TABLA_USUARIOS + "(" +
                "id integer primary key autoincrement," +
                "nombre text," +
                "usuario text," +
                "contra text," +
                "tipo integer)");
        db.execSQL("create table if not exists " + TABLA_PRODUCTOS + "(" +
                "id integer primary key autoincrement," +
                "nombre text," +
                "descripcion text," +
                "categoria text," +
                "precio real)");
        db.execSQL("create table if not exists " + TABLA_FACTURAS + "(" +
                "id integer primary key autoincrement," +
                "cod_cliente integer," +
                "fecha text," +
                "foreign key (cod_cliente) references " + TABLA_USUARIOS + "(id))");
        db.execSQL("create table if not exists " + TABLA_PEDIDOS + "(" +
                "id integer primary key autoincrement," +
                "cod_producto integer," +
                "cantidad_prod integer," +
                "cod_factura integer, " +
                "foreign key (cod_producto) references " + TABLA_PRODUCTOS + "(id)," +
                "foreign key (cod_factura) references " + TABLA_FACTURAS + "(id))");
        db.execSQL("INSERT INTO " + TABLA_USUARIOS +
                " (id,nombre,usuario,contra,tipo) VALUES (1,'Alisson Bellot Cuba','admin@gmail.com','1234',1)");
    }

    //metodo para borrar tablas y crear de nuevo si se cambia la DB VERSION
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table " + TABLA_PEDIDOS);
        db.execSQL("drop table " + TABLA_FACTURAS);
        db.execSQL("drop table " + TABLA_PRODUCTOS);
        db.execSQL("drop table " + TABLA_USUARIOS);
        onCreate(db);
    }
}
