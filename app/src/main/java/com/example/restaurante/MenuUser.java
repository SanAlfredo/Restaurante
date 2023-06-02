package com.example.restaurante;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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

public class MenuUser extends AppCompatActivity {
    //declaracion de variables locales
    Button btn1, btn2, btn3, btn4, btn5, btn6;
    ListView list1;
    ArrayList<String> listado;
    //ventana
    FuncionesHelper funcionesHelper = new FuncionesHelper();
    //peticiones en internet
    private RequestQueue requestQueue;
    //variable de tipo array List
    private ArrayList<String> datos = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_user);
        //enlazar variables locales
        btn1 = findViewById(R.id.btn_user_combo);
        btn2 = findViewById(R.id.btn_user_pollo);
        btn3 = findViewById(R.id.btn_user_soda);
        btn4 = findViewById(R.id.btn_user_comp);
        btn5 = findViewById(R.id.btn_user_sand);
        btn6 = findViewById(R.id.btn_user_postre);
        list1 = findViewById(R.id.list_user_pedir);

        //boton Combos
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //llama a la funcion parseCombo
                parseCombo(new Callback() {
                    @Override
                    public void onSuccess(ArrayList<String> datos) {
                        //obtiene los datos y los pone en el array adapter
                        listado = datos;
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MenuUser.this,
                                android.R.layout.simple_list_item_1, listado);
                        //adaptar los datos al List View
                        list1.setAdapter(adapter);
                    }
                    @Override
                    public void onFail(String msg) {

                    }
                });
            }
        });
    }
    //funcion parseCombo que permite llamarse a si misma
    public void parseCombo(final Callback onCallback){
        String busq = "combo";
        //generar la URL que conecta al local host
        String url = "http:192.168.100.76/ConexionBDRestaurante/buscarProductoNombre.php?nombre=" + busq + "";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //guardar la respuesta de la BD un objeto de tipo JSON
                    JSONObject jsonObject = new JSONObject(response);
                    String exito = jsonObject.getString("exito");
                    JSONArray jsonArray = jsonObject.getJSONArray("datos");
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
                            String linea = "Nombre del producto: " + nombre + "\nDescripcion: " +
                                    descripcion + "\nCategoria: " + categoria + "\nPrecio: " + precio2;
                            //añadir la linea en datos
                            datos.add(linea);
                        }
                        onCallback.onSuccess(datos);
                    } else {
                        funcionesHelper.ventanaMensaje(MenuUser.this, "No se encontraron combos");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MenuUser.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue = Volley.newRequestQueue(MenuUser.this);
        requestQueue.add(request);
    }
    public interface Callback {
        void onSuccess(ArrayList<String> datos);

        void onFail(String msg);
    }
}