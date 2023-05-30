package com.example.restaurante;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class Producto extends AppCompatActivity {
    //declaracion de variables locales
    EditText edt1, edt2, edt3, edt4, edt5;
    Button btn1, btn2, btn3;
    //funcion que contiene la ventana
    FuncionesHelper funcionesHelper = new FuncionesHelper();

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);
        //enlazar variables
        edt1 = findViewById(R.id.et_prod_cod);
        edt2 = findViewById(R.id.et_prod_nombre);
        edt3 = findViewById(R.id.et_prod_descripcion);
        edt4 = findViewById(R.id.et_prod_categoria);
        edt5 = findViewById(R.id.et_prod_precio);
        btn1 = findViewById(R.id.btn_prod_buscar);
        btn2 = findViewById(R.id.btn_prod_add);
        btn3 = findViewById(R.id.btn_prod_mod);

        //buscar producto por codigo
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //se verifica que el campo no esté vacío, por que si esta vacio no se podra buscar el producto
                if (edt1.getText().toString().isEmpty()) {
                    //si el campo Codigo producto esta vacio se llama una alerta con un mensaje
                    funcionesHelper.ventanaMensaje(Producto.this, "Debe llenar el código de producto para poder buscarlo");

                } else {
                    //si el campo codigo tiene un elemento entonces lo guardamos en la variable cod
                    String[] cod = {edt1.getText().toString()};
                    //usamos la funcion creada para buscar por id
                    buscaID(cod);
                }
            }
        });

        //boton para registrar un producto
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = edt2.getText().toString();
                String descripcion = edt3.getText().toString();
                String categoria = edt4.getText().toString();
                String precio1 = edt5.getText().toString();
                //verificar campos vacios
                if (nombre.isEmpty() || descripcion.isEmpty() || categoria.isEmpty() || precio1.isEmpty()) {
                    //usando la alerta mensaje
                    funcionesHelper.ventanaMensaje(Producto.this, "Debe llenar los campos:\n" +
                            "\t\t* Nombre del producto\n\t\t* Descripción\n\t\t* Categoria\n\t\t* Precio");
                } else {
                    //convertir a flotante
                    Float precio = Float.parseFloat(precio1);
                    //usar funcion para guardar
                    guardarProducto(nombre, descripcion, categoria, precio);
                }
            }
        });
        //boton para modificar el producto
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = edt2.getText().toString();
                String descripcion = edt3.getText().toString();
                String categoria = edt4.getText().toString();
                String precio1 = edt5.getText().toString();
                //verificar campos vacios
                if (nombre.isEmpty() || descripcion.isEmpty() || categoria.isEmpty() || precio1.isEmpty()) {
                    //usando la alerta mensaje
                    funcionesHelper.ventanaMensaje(Producto.this, "Debe llenar los campos:\n" +
                            "\t\t* Nombre del producto\n\t\t* Descripción\n\t\t* Categoria\n\t\t* Precio");

                } else {
                    //mandar el codigo del producto
                    String[] cod = {edt1.getText().toString()};
                    //convertir a flotante
                    Float precio = Float.parseFloat(precio1);
                    //llamar a la funcion que modifica
                    modificarProducto(cod, nombre, descripcion, categoria, precio);
                }

            }
        });
    }

    //función que guarda los datos de productos en la base de datos
    public void guardarProducto(String nombre, String descripcion, String categoria, Float precio) {
        //necesario instanciar la clase DBHelper que contiene la conexion a la base de datos
        DBHelper helper = new DBHelper(Producto.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            //se colocan los valores en el contenedor
            ContentValues c = new ContentValues();
            c.put("nombre", nombre);
            c.put("descripcion", descripcion);
            c.put("categoria", categoria);
            c.put("precio", precio);
            //se insertan en la base de datos
            db.insert("productos", null, c);
            //se cierra la base de datos
            db.close();
            // un pequeño mensaje de registrado con exito
            Toast.makeText(getApplication(), "Registro exitoso", Toast.LENGTH_SHORT).show();
            //y volver todas las casillas a vacio
            limpiarProducto();
        } catch (Exception e) {
            Toast.makeText(getApplication(), "ERROR " + e, Toast.LENGTH_SHORT).show();
        }
    }

    //funcion que busca por codigo de producto
    public void buscaID(String[] cod) {
        //instanciamos data base helper
        DBHelper helper = new DBHelper(Producto.this);
        //instanciamos la libreria de sqlite
        SQLiteDatabase db = helper.getReadableDatabase();
        //generamos la consulta
        String consulta = "select * from productos where codigo =?";
        try {
            //ejecutamos la consulta dentro un try catch por si hubiera algun error
            Cursor c = db.rawQuery(consulta, cod);
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                //llenar los campos con los valores encontrados
                edt2.setText(c.getString(1));
                edt3.setText(c.getString(2));
                edt4.setText(c.getString(3));
                edt5.setText(c.getString(4));
            } else {
                funcionesHelper.ventanaMensaje(Producto.this, "No existen datos para mostrar");
                limpiarProducto();
            }
        } catch (Exception e) {
            Toast.makeText(getApplication(), "ERROR " + e,
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void modificarProducto(String[] cod, String nombre, String descripcion, String categoria, Float precio) {
        //necesario instanciar la clase DBHelper que contiene la conexion a la base de datos
        DBHelper helper = new DBHelper(Producto.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            //se colocan los valores en el contenedor
            ContentValues c = new ContentValues();
            c.put("nombre", nombre);
            c.put("descripcion", descripcion);
            c.put("categoria", categoria);
            c.put("precio", precio);
            //actualizamos la tabla
            db.update("productos", c, "codigo =?", cod);
            //se cierra la base de datos
            db.close();
            // un pequeño mensaje de modificado con exito
            Toast.makeText(getApplication(), "Modificado con exito", Toast.LENGTH_SHORT).show();
            //y volver todas las casillas a vacio
            limpiarProducto();
        } catch (Exception e) {
            Toast.makeText(getApplication(), "ERROR " + e, Toast.LENGTH_SHORT).show();
        }
    }

    //para limpiar los campos
    public void limpiarProducto() {
        edt1.setText("");
        edt2.setText("");
        edt3.setText("");
        edt4.setText("");
        edt5.setText("");
    }
}