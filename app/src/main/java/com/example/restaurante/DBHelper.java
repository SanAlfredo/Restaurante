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
    public static final String TABLA_VENTAS = "ventas";
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
                "email text," +
                "contra text," +
                "tipo integer)");
        db.execSQL("create table if not exists " + TABLA_PRODUCTOS + "(" +
                "id integer primary key autoincrement," +
                "nombre text," +
                "descripcion text," +
                "categoria text," +
                "precio real)");
//        db.execSQL("create table if not exists " + TABLA_VENTAS + "(" +
//                "id integer primary key autoincrement," +
//                "cod_cliente integer," +
//                "fecha text," +
//                "foreign key (cod_cliente) references " + TABLA_USUARIOS + "(id))");
        //tabla de pedidos diseño 1
        db.execSQL("create table if not exists " + TABLA_PEDIDOS + "(" +
                "id integer primary key autoincrement," +
                "cod_producto integer," +
                "cod_cliente integer," +
                "cantidad integer, " +
                "fecha text, " +
                "precio_total real, " +
                "foreign key (cod_producto) references " + TABLA_PRODUCTOS + "(id)," +
                "foreign key (cod_cliente) references " + TABLA_USUARIOS + "(id))");
        //tabla de pedidos diseño 2 de BD
//        db.execSQL("create table if not exists " + TABLA_PEDIDOS + "(" +
//                "id integer primary key autoincrement," +
//                "cod_producto integer," +
//                "cod_venta integer," +
//                "cantidad integer, " +
//                "precio_total real, " +
//                "foreign key (cod_producto) references " + TABLA_PRODUCTOS + "(id)," +
//                "foreign key (cod_venta) references " + TABLA_VENTAS + "(id))");

        db.execSQL("INSERT INTO " + TABLA_USUARIOS +
                " (id,nombre,email,contra,tipo) VALUES " +
                "('Administrador o gerente','admin','123',1)," +
                "('Alisson Bellot Cuba','alisson','1234',2)," +
                "('Mariela Poma Andia','mariela','1234',2)," +
                "('Jimmy Vladimir Mendez Condori','jimmy','1234',2)");
        db.execSQL("INSERT INTO " + TABLA_PRODUCTOS +
                " (nombre,descripcion,categoria,precio) VALUES " +
                "('porcion de papas','porcion de papas fritas tamaño mediano','complementos',5.0)," +
                "('porcion de postre','porcion de platano frito tamaño mediano','complementos',3.0)," +
                "('porcion de arroz','porcion de arroz graneado para una persona','complementos',5.0)," +
                "('coca cola vaso pequeño','gaseosa coca cola en vaso de 250 ml','gaseosas',5.0)," +
                "('coca cola vaso mediano','gaseosa coca cola en vaso de 500 ml','gaseosas',7.0)," +
                "('coca cola vaso grande','gaseosa coca cola en vaso de 750 ml','gaseosas',10.0)," +
                "('coca cola botella','gaseosa coca cola en botella descartable de 2 Lts','gaseosas',14.0)," +
                "('fanta vaso pequeño','gaseosa fanta en vaso de 250 ml','gaseosas',5.0)," +
                "('fanta vaso mediano','gaseosa fanta en vaso de 500 ml','gaseosas',7.0)," +
                "('fanta vaso grande','gaseosa fanta en vaso de 750 ml','gaseosas',10.0)," +
                "('fanta botella','gaseosa fanta en botella descartable de 2 Lts','gaseosas',14.0)," +
                "('porcion pollo a la broaster','1/4 pollo preparado con la reseta especial Don Pepe y cocido a la broaster','pollos',12.0)," +
                "('balde de 4','balde de 4 porciones de 1/4 de pollo preparado con la reseta especial Don Pepe y cocido a la broaster acompañado de papas fritas','pollos',56.0)," +
                "('balde de 8','balde de 8 porciones de 1/4 de pollo preparado con la reseta especial Don Pepe y cocido a la broaster acompañado de papas fritas','pollos',110.0)," +
                "('pollo a la broaster con papas','1/4 pollo preparado con la reseta especial Don Pepe y cocido a la broaster acompañado de papas fritas','pollos',15.0)," +
                "('pollo a la broaster con arroz','1/4 pollo preparado con la reseta especial Don Pepe y cocido a la broaster acompañado de arroz graneado','pollos',15.0)," +
                "('combo pollo 1','1/4 pollo broaster con papas fritas y coca cola vaso pequeña','pollos',19.0)," +
                "('combo pollo 2','1/4 pollo broaster con papas fritas y coca cola vaso mediana','pollos',21.0)," +
                "('combo pollo 3','1/4 pollo broaster con papas fritas y coca cola vaso grande','pollos',23.0)," +
                "('lomito','sandwich de carne de res con jamon, queso, tomate, lechuga y aderesos acompañado de papa frita','sandwiches',18.0)," +
                "('lomito con huevo','sandwich de carne de res con huevo, jamon, queso, tomate, lechuga y aderesos acompañado de papa frita','sandwiches',20.0)," +
                "('hamburguesa','sandwich de carne de res molida con jamon, queso, tomate, lechuga y aderesos acompañado de papa frita','sandwiches',18.0)");

    }

    //metodo para borrar tablas y crear de nuevo si se cambia la DB VERSION
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table " + TABLA_PEDIDOS);
        //db.execSQL("drop table " + TABLA_VENTAS);
        db.execSQL("drop table " + TABLA_PRODUCTOS);
        db.execSQL("drop table " + TABLA_USUARIOS);
        onCreate(db);
    }
}
