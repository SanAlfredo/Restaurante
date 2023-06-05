package com.example.restaurante;

import static com.example.restaurante.MainActivity.ip;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VerProductos extends AppCompatActivity {
    //Variable local list view
    ListView list1;
    ArrayList<String> listado;
    //llamar a la ventana
    FuncionesHelper funcionesHelper = new FuncionesHelper();
    //peticiones en internet
    RequestQueue requestQueue;
    //variable de tipo array List
    private ArrayList<String> datos = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_productos);
        //enlazar la variable local lista
        list1 = findViewById(R.id.list_prod_total);
        //llamar a la funcion cargar Lista
        ListaProductos(new Callback() {
            @Override
            public void onSuccess(ArrayList<String> datos) {
                cargarLista(datos);
            }

            @Override
            public void onFail(String msg) {
                Toast.makeText(VerProductos.this, "No hay Productos para mostrar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //se necesita una lista para poder almacenar los datos que provienden de la base de datos
    public void ListaProductos(final Callback onCallback) {
        //generar la URL que conecta al local host
        String url = "http:" + ip + "/ConexionBDRestaurante/buscarProductos.php";
        //crear progres dialog por si demora la respuesta
        final ProgressDialog progressDialog = new ProgressDialog(VerProductos.this);
        progressDialog.setMessage("Cargando...");
        //mostrar la barra de progreso
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //guardar la respuesta de la BD un objeto de tipo JSON
                    JSONObject jsonObject = new JSONObject(response);
                    String exito = jsonObject.getString("exito");
                    JSONArray jsonArray = jsonObject.getJSONArray("datos");
                    datos.clear();
                    //si exito es 1
                    if (exito.equals("1")) {
                        //recorrer el vector
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            //obtener valores
                            String id = object.getString("id");
                            String nombre = object.getString("nombre");
                            String descripcion = object.getString("descripcion");
                            String categoria = object.getString("categoria");
                            float precio = object.getInt("precio");
                            String precio2 = Float.toString(precio);
                            //guardar en la linea
                            String linea = "Codigo producto: " + id + "\nNombre del producto: " + nombre +
                                    "\nDescripcion: " + descripcion + "\nCategoria: " + categoria + "\nPrecio: " + precio2;
                            //añadir la linea en datos
                            datos.add(linea);
                        }
                        progressDialog.dismiss();
                        onCallback.onSuccess(datos);
                    } else {
                        progressDialog.dismiss();
                        funcionesHelper.ventanaMensaje(VerProductos.this, "No se encontraron productos");
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(VerProductos.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue = Volley.newRequestQueue(VerProductos.this);
        requestQueue.add(request);
    }

    //se crea una funcion que se llame a si misma
    public interface Callback {
        void onSuccess(ArrayList<String> datos);

        void onFail(String msg);
    }

    //permite cargar la lista en un adaptador para usar el arraylist
    private void cargarLista(ArrayList<String> datos) {
        listado = datos;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(VerProductos.this,
                android.R.layout.simple_list_item_1, listado);
        list1.setAdapter(adapter);
    }
}