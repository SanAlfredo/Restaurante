package com.example.restaurante;

import static com.example.restaurante.MainActivity.ip;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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
    private ArrayList<String> datos1 = new ArrayList<String>();
    //private int c;

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
                    public void onSuccess(ArrayList<String> datos, ArrayList<String> datos1) {
                        cargarLista(datos, datos1);
                    }

                    @Override
                    public void onFail(String msg) {
                        funcionesHelper.ventanaMensaje(MenuUser.this, "No hay datos que mostrar");
                    }
                });
            }
        });
        //boton Pollo
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parseCategoria("pollos", new Callback() {
                    @Override
                    public void onSuccess(ArrayList<String> datos, ArrayList<String> datos1) {
                        cargarLista(datos, datos1);
                    }

                    @Override
                    public void onFail(String msg) {
                        funcionesHelper.ventanaMensaje(MenuUser.this, "No hay datos que mostrar");
                    }
                });
            }
        });
        //boton gaseosas
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parseCategoria("gaseosas", new Callback() {
                    @Override
                    public void onSuccess(ArrayList<String> datos, ArrayList<String> datos1) {
                        cargarLista(datos, datos1);
                    }

                    @Override
                    public void onFail(String msg) {
                        funcionesHelper.ventanaMensaje(MenuUser.this, "No hay datos que mostrar");
                    }
                });
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parseCategoria("complementos", new Callback() {
                    @Override
                    public void onSuccess(ArrayList<String> datos, ArrayList<String> datos1) {
                        cargarLista(datos, datos1);
                    }

                    @Override
                    public void onFail(String msg) {
                        funcionesHelper.ventanaMensaje(MenuUser.this, "No hay datos que mostrar");
                    }
                });
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parseCategoria("sandwiches", new Callback() {
                    @Override
                    public void onSuccess(ArrayList<String> datos, ArrayList<String> datos1) {
                        cargarLista(datos, datos1);
                    }

                    @Override
                    public void onFail(String msg) {
                        funcionesHelper.ventanaMensaje(MenuUser.this, "No hay datos que mostrar");
                    }
                });
            }
        });
    }

    //funcion parseCombo que permite llamarse a si misma
    public void parseCombo(final Callback onCallback) {
        String busq = "combo";
        //generar la URL que conecta al local host
        String url = "http:" + ip + "/ConexionBDRestaurante/buscarProductoNombre.php?nombre=" + busq + "";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //guardar la respuesta de la BD un objeto de tipo JSON
                    JSONObject jsonObject = new JSONObject(response);
                    String exito = jsonObject.getString("exito");
                    JSONArray jsonArray = jsonObject.getJSONArray("datos");
                    datos.clear();
                    datos1.clear();
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
                            String linea1 = "Nombre del producto: " + nombre +
                                    "\nDescripcion: " + descripcion + "\nPrecio: " + precio2;
                            //añadir la linea en datos
                            datos.add(linea);
                            datos1.add(linea1);
                        }
                        onCallback.onSuccess(datos, datos1);
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

    //funcion para los pollos
    public void parseCategoria(String busq, final Callback onCallback) {
        //generar la URL que conecta al local host
        String url = "http:" + ip + "/ConexionBDRestaurante/buscarProductoCategoria.php?categoria=" + busq + "";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //guardar la respuesta de la BD un objeto de tipo JSON
                    JSONObject jsonObject = new JSONObject(response);
                    String exito = jsonObject.getString("exito");
                    JSONArray jsonArray = jsonObject.getJSONArray("datos");
                    datos.clear();
                    datos1.clear();
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
                            String linea1 = "Nombre del producto: " + nombre +
                                    "\nDescripcion: " + descripcion + "\nPrecio: " + precio2;
                            //añadir la linea en datos
                            datos.add(linea);
                            datos1.add(linea1);
                        }
                        onCallback.onSuccess(datos, datos1);
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

    //se crea una funcion que se llame a si misma
    public interface Callback {
        void onSuccess(ArrayList<String> datos, ArrayList<String> datos1);

        void onFail(String msg);
    }

    //funcion encargada de llenar el listView
    public void cargarLista(ArrayList<String> datos, ArrayList<String> datos1) {
        //obtiene los datos y los pone en el array adapter
        listado = datos1;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MenuUser.this,
                android.R.layout.simple_list_item_1, listado);
        //adaptar los datos al List View
        list1.setAdapter(adapter);
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MenuUser.this);
                View mView = getLayoutInflater().inflate(R.layout.product_dialog, null);
                final TextView tv1 = (TextView) mView.findViewById(R.id.tv_alert_detalle);
                final EditText edt1 = (EditText) mView.findViewById(R.id.et_alert_cantidad);
                Button btn7 = (Button) mView.findViewById(R.id.btn_alert_comprar);
                tv1.setText(datos1.get(i));
                String[] separado = datos.get(i).split(":");
                Float precio = Float.parseFloat(separado[5].trim());
                String[] separar1 = separado[1].split("\n");
                int codigo = Integer.parseInt(separar1[0].trim());
                btn7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String c = edt1.getText().toString();
                        if (c.isEmpty()) {
                            Toast.makeText(MenuUser.this, "Debe poner una cantidad "+precio, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MenuUser.this, "Cantidad: " + c, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setView(mView);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}