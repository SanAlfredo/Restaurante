package com.example.restaurante;

import static com.example.restaurante.MainActivity.ip;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.Map;


public class MenuUser extends AppCompatActivity {
    //declaracion de variables locales
    Button btn1, btn2, btn3, btn4, btn5, btn6, btn8;
    ListView list1;

    //ventana
    FuncionesHelper funcionesHelper = new FuncionesHelper();
    //peticiones en internet
    RequestQueue requestQueue;
    //variable de tipo array List
    private ArrayList<String> datos = new ArrayList<String>();
    private ArrayList<String> datos1 = new ArrayList<String>();
    //array list para enviar los datos de este activity al de pago
    String[] enviar1 = new String[100];
    String[] enviar2 = new String[100];
    int id_venta = 0;
    ArrayList<String> listado;
    int cod_cliente = 3;
    SendDeviseDetails sendDeviseDetails = new SendDeviseDetails();
    int n = 0;

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
        btn8 = findViewById(R.id.btn_user_comprar);
        list1 = findViewById(R.id.list_user_pedir);

        //boton Combos
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //llama a la funcion parseCombo
                parseCombo(new Callback() {
                    @Override
                    public void onSuccess(ArrayList<String> datos, ArrayList<String> datos1) {
                        //si la funcion es llamada correctamente se llama a cargar la Lista
                        cargarLista(datos, datos1);
                    }

                    @Override
                    public void onFail(String msg) {
                        //si falla la funcion en devolver datos
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
        //boton de complementos
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
        //boton para sandwiches
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
        //boton para las compras
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regVenta();
            }
        });

    }

    //funcion parseCombo que permite llamarse a si misma
    public void parseCombo(final Callback onCallback) {
        String busq = "combo";
        //generar la URL que conecta al local host
        String url = "http:" + ip + "/ConexionBDRestaurante/buscarProductoNombre.php?nombre=" + busq + "";
        //funcion que se conecta a la api de respuesta
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //guardar la respuesta de la BD un objeto de tipo JSON
                    JSONObject jsonObject = new JSONObject(response);
                    String exito = jsonObject.getString("exito");
                    JSONArray jsonArray = jsonObject.getJSONArray("datos");
                    //limpiar las listas de datos
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
                        //llamar a la funcion callback para guardar los cambios en los datos
                        onCallback.onSuccess(datos, datos1);
                    } else {
                        //en caso de no tener resultados
                        funcionesHelper.ventanaMensaje(MenuUser.this, "No se encontraron combos");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //en caso de tener un error al leer los datos en la base de datos
                Toast.makeText(MenuUser.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //llamar a la libreria Volley
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
        //limpiar los datos que se envian
//        enviar1.clear();
//        enviar2.clear();
        //obtiene los datos y los pone en el array adapter
        listado = datos1;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MenuUser.this,
                android.R.layout.simple_list_item_1, listado);
        //adaptar los datos al List View
        list1.setAdapter(adapter);
        //cuando se da click a algun item de la lista se activa lo siguiente
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //se crea un Alert Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MenuUser.this);
                //en base a un layout propio product_dialog
                View mView = getLayoutInflater().inflate(R.layout.product_dialog, null);
                // se instancia las varibles dentro del layout, referenciado a esta vista
                final TextView tv1 = (TextView) mView.findViewById(R.id.tv_alert_detalle);
                final EditText edt1 = (EditText) mView.findViewById(R.id.et_alert_cantidad);
                Button btn7 = (Button) mView.findViewById(R.id.btn_alert_comprar);
                //se llena el TextView con los datos seleccionados
                tv1.setText(datos1.get(i));
                //se separa el string para obtener los datos de codigo, precio del seleccionado
                String[] separado = datos.get(i).split(":");
                Float precio = Float.parseFloat(separado[5].trim());
                String[] separar1 = separado[1].split("\n");
                int codigo = Integer.parseInt(separar1[0].trim());
                String producto = separar1[1].trim();
                //enlazamos el alert dialog con nuestro diseño
                builder.setView(mView);
                AlertDialog dialog = builder.create();
                //este boton del alert dialog
                btn7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String c = edt1.getText().toString();
                        if (c.isEmpty()) {
                            Toast.makeText(MenuUser.this, "Debe poner una cantidad ", Toast.LENGTH_SHORT).show();
                        } else {
                            Float precio_tot = Float.parseFloat(c) * precio;
                            String envia1 = "Producto: " + producto + "\nCantidad: " + c + "\nPrecio Unidad: " + precio +
                                    "\nPrecio total: " + precio_tot;
                            String envia2 = "::" + codigo + "::" + c + "::" + precio_tot;
                            enviar1[n] = envia1;
                            enviar2[n] = envia2;
                            n = n + 1;
                            Toast.makeText(MenuUser.this, "guardado el pedido", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                    }
                });
                //finalmente mostramos el dialog
                dialog.show();
            }
        });
    }

    //funcion para registrar
    public void regVenta() {
        //generar la URL que conecta al local host
        String url = "http:" + ip + "/ConexionBDRestaurante/regVenta.php?codigo=" + cod_cliente + "";
        final ProgressDialog progressDialog = new ProgressDialog(MenuUser.this);
        progressDialog.setMessage("Cargando su pedido...");
        if (vectorVacio(enviar2) == false) {
            Toast.makeText(MenuUser.this, "esta vacio", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String exito = jsonObject.getString("exito");
                        JSONArray jsonArray = jsonObject.getJSONArray("datos");
                        //si es igual a 1
                        if (exito.equals("1")) {
                            //obtener id resultado
                            JSONObject object = jsonArray.getJSONObject(0);
                            //obtener los valores
                            id_venta = Integer.parseInt(object.getString("id"));
                            progressDialog.dismiss();
                            progressDialog.show();
                            for (int i = 0; i < enviar2.length; i++) {
                                if (enviar2[i] != null) {
                                    Toast.makeText(MenuUser.this, "paso1", Toast.LENGTH_SHORT).show();
                                    String[] separado = enviar2[i].split("::");
                                    int cod = Integer.parseInt(separado[1].trim());
                                    int can = Integer.parseInt(separado[2].trim());
                                    Float pre = Float.parseFloat(separado[3].trim());
                                    try {
                                        //Toast.makeText(MenuUser.this, "paso 2 "+separado[3], Toast.LENGTH_SHORT).show();
                                        String url1 = "http:" + ip + "/ConexionBDRestaurante/regPedido.php?codigo1=" + cod +
                                                "&codigo2=" + id_venta + "&cantidad=" + can + "&precio=" + pre + "";
                                        StringRequest request1 = new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
//
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                progressDialog.dismiss();
                                                Toast.makeText(MenuUser.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                        requestQueue = Volley.newRequestQueue(MenuUser.this);
                                        requestQueue.add(request1);
                                    } catch (Exception e) {
                                        progressDialog.dismiss();
                                        e.printStackTrace();
                                    }
                                }
                            }
                            progressDialog.dismiss();
                            //aqui ir a la otra ventana
                            Intent intent = new Intent(MenuUser.this, FacturaPago.class);
                            startActivity(intent);

                        } else {
                            progressDialog.dismiss();
                            funcionesHelper.ventanaMensaje(MenuUser.this, "no se ingreso");

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
                    Toast.makeText(MenuUser.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue = Volley.newRequestQueue(MenuUser.this);
            requestQueue.add(request);
        }
//        if (id_venta != 0) {
//
//        }

    }

    public boolean vectorVacio(String[] vector) {
        boolean respuesta = false;
        for (int i = 0; i < vector.length; i++) {
            if (vector[i] != null) {
                respuesta = true;
                break;
            } else {
                respuesta = false;
            }
        }
        return respuesta;
    }
    //Intent intent = new Intent(MenuUser.this, FacturaPago.class);
    //intent.putExtra("usuario")
}