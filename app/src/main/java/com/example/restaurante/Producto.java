package com.example.restaurante;

import static com.example.restaurante.MainActivity.ip;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.database.sqlite.SQLiteDatabase;
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

public class Producto extends AppCompatActivity {
    //declaracion de variables locales
    EditText edt1, edt2, edt3, edt4, edt5;
    Button btn1, btn2, btn3;
    //funcion que contiene la ventana
    FuncionesHelper funcionesHelper = new FuncionesHelper();
    RequestQueue requestQueue;

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
                    int cod = Integer.parseInt(edt1.getText().toString());
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
                if (edt1.getText().toString().isEmpty()||nombre.isEmpty() ||
                        descripcion.isEmpty() || categoria.isEmpty() || precio1.isEmpty()) {
                    //usando la alerta mensaje
                    funcionesHelper.ventanaMensaje(Producto.this, "Debe llenar los campos:\n" +
                            "\t\t* Nombre del producto\n\t\t* Descripción\n\t\t* Categoria\n\t\t* Precio");

                } else {
                    //mandar el codigo del producto
                    int cod = Integer.parseInt(edt1.getText().toString());
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
        //generar la URL que conecta al local host
        String url = "http:" + ip + "/ConexionBDRestaurante/regProducto.php?nombre=" + nombre +
                "&descripcion="+descripcion+"&categoria="+categoria+"&precio="+precio+"";
        //crear progres dialog por si demora la respuesta
        final ProgressDialog progressDialog = new ProgressDialog(Producto.this);
        progressDialog.setMessage("Guardando...");
        //mostrar la barra de progreso
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String exito = jsonObject.getString("exito");
                    //si es igual a 1
                    if (exito.equals("1")) {
                        Toast.makeText(Producto.this,"Registrado con exito",Toast.LENGTH_SHORT).show();
                        limpiarProducto();
                        progressDialog.dismiss();
                    } else {
                        progressDialog.dismiss();
                        funcionesHelper.ventanaMensaje(Producto.this, "No se registro");
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
                Toast.makeText(Producto.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue = Volley.newRequestQueue(Producto.this);
        requestQueue.add(request);
    }

    //funcion que busca por codigo de producto
    public void buscaID(int cod) {
        //generar la URL que conecta al local host
        String url = "http:" + ip + "/ConexionBDRestaurante/buscarProductoID.php?id=" + cod + "";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String exito = jsonObject.getString("exito");
                    JSONArray jsonArray = jsonObject.getJSONArray("datos");
                    //si es igual a 1
                    if (exito.equals("1")) {
                        //recorrer el resultado
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            //obtener los valores
                            String id = object.getString("id");
                            String nombre = object.getString("nombre");
                            String descripcion = object.getString("descripcion");
                            String categoria = object.getString("categoria");
                            String precio = object.getString("precio");
                            edt2.setText(nombre);
                            edt3.setText(descripcion);
                            edt4.setText(categoria);
                            edt5.setText(precio);
                        }
                    } else {
                        funcionesHelper.ventanaMensaje(Producto.this,
                                "No existe ese producto");
                        limpiarProducto();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Producto.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue = Volley.newRequestQueue(Producto.this);
        requestQueue.add(request);
    }

    public void modificarProducto(int cod, String nombre, String descripcion, String categoria, Float precio) {
        //generar la URL que conecta al local host
        String url = "http:" + ip + "/ConexionBDRestaurante/modProducto.php?nombre=" + nombre +
                "&descripcion="+descripcion+"&categoria="+categoria+"&precio="+precio+"&id="+cod+"";
        //crear progres dialog por si demora la respuesta
        final ProgressDialog progressDialog = new ProgressDialog(Producto.this);
        progressDialog.setMessage("Modificando...");
        //mostrar la barra de progreso
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String exito = jsonObject.getString("exito");
                    //si es igual a 1
                    if (exito.equals("1")) {
                        Toast.makeText(Producto.this,"Modificado con exito",Toast.LENGTH_SHORT).show();
                        limpiarProducto();
                        progressDialog.dismiss();
                    } else {
                        progressDialog.dismiss();
                        funcionesHelper.ventanaMensaje(Producto.this, "No se pudo modificar");
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
                Toast.makeText(Producto.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue = Volley.newRequestQueue(Producto.this);
        requestQueue.add(request);
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