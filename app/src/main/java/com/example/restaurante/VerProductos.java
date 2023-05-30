package com.example.restaurante;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class VerProductos extends AppCompatActivity {
    //Variable local list view
    ListView list1;
    ArrayList<String> listado;
    //llamar a la ventana
    FuncionesHelper funcionesHelper = new FuncionesHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_productos);
        //enlazar la variable local lista
        list1=findViewById(R.id.list_prod_total);
        //llamar a la funcion cargar Lista
        cargarLista();
    }

    //se necesita una lista para poder almacenar los datos que provienden de la base de datos
    private ArrayList<String> ListaProductos() {
        ArrayList<String> datos = new ArrayList<String>();
        DBHelper helper = new DBHelper(VerProductos.this);
        SQLiteDatabase db = helper.getReadableDatabase();
        //SQL que seleciona todos los elementos de la tabla productos
        String consulta = "select * from productos";
        try {
            Cursor c = db.rawQuery(consulta, null);
            if (c.moveToFirst()) {
                //si es que hay datos en la base de datos volveran los resultados
                do {
                    String linea = "Nombre del producto: " + c.getString(1) + "\nDescripcion: " + c.getString(2) +
                            "\nCategoria: " + c.getString(3) +
                            "\nPrecio: " + c.getString(4);
                    datos.add(linea);
                } while (c.moveToNext());
            } else {
                //si no hay datos un mensaje de error
                funcionesHelper.ventanaMensaje(VerProductos.this, "No se encontraron datos para mostrar");
            }
            c.close();
            db.close();
        } catch (Exception e) {
            Toast.makeText(getApplication(), "ERROR " + e, Toast.LENGTH_LONG).show();
        }
        return datos;
    }

    //permite cargar la lista en un adaptador para usar el arraylist
    private void cargarLista() {
        listado = ListaProductos();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(VerProductos.this,
                android.R.layout.simple_list_item_1, listado);
        list1.setAdapter(adapter);
    }
}