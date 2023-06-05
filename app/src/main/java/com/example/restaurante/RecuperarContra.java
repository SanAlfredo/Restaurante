package com.example.restaurante;

import static com.example.restaurante.MainActivity.ip;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class RecuperarContra extends AppCompatActivity {
    //variables locales
    EditText edt1, edt2, edt3;
    Button btn1, btn2;
    String id;
    String nombre;
    String usuario;
    String contra;
    int tipo;
    //funcion que contiene la ventana
    FuncionesHelper funcionesHelper = new FuncionesHelper();
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contra);
        edt1 = findViewById(R.id.et_recu_mail);
        edt2 = findViewById(R.id.et_recu_pass1);
        edt3 = findViewById(R.id.et_recu_pass2);
        btn1 = findViewById(R.id.btn_recu_recu);
        btn2 = findViewById(R.id.btn_recu_guardar);
        edt2.setVisibility(View.GONE);
        edt3.setVisibility(View.GONE);
        btn2.setVisibility(View.GONE);

        //boton para buscar al usuario
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt1.getText().toString().isEmpty()) {
                    funcionesHelper.ventanaMensaje(RecuperarContra.this,
                            "Debe ingresar un usuario");
                } else {
                    buscarUser(edt1.getText().toString());
                }
            }
        });

        //boton para cambiar de contraseña
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contra1 = edt2.getText().toString();
                String contra2 = edt3.getText().toString();
                if (contra1.isEmpty() || contra2.isEmpty()) {
                    funcionesHelper.ventanaMensaje(RecuperarContra.this,
                            "Debe llenar los campos de contraseña");
                } else {
                    if (contra1.equals(contra2)) {
                        modificarUser(contra1);
                    } else {
                        funcionesHelper.ventanaMensaje(RecuperarContra.this,
                                "Las contraseñas deben ser iguales");
                    }
                }
            }
        });

    }

    //metodo para buscar usuario en la base de datos
    public void buscarUser(String user) {
        //generar la URL que conecta al local host
        String url = "http:" + ip + "/ConexionBDRestaurante/buscarUser.php?usuario=" + user + "";
        //crear progres dialog por si demora la respuesta
        final ProgressDialog progressDialog = new ProgressDialog(RecuperarContra.this);
        progressDialog.setMessage("Guardando...");
        //mostrar la barra de progreso
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
                        JSONObject object = jsonArray.getJSONObject(0);
                        //obtener los valores
                        id = object.getString("id");
                        nombre = object.getString("nombre");
                        usuario = object.getString("usuario");
                        tipo = object.getInt("tipo");
                        edt2.setVisibility(View.VISIBLE);
                        edt3.setVisibility(View.VISIBLE);
                        btn2.setVisibility(View.VISIBLE);
                        btn1.setVisibility(View.GONE);
                        progressDialog.dismiss();
                        Toast.makeText(RecuperarContra.this, "Usuario encontrado", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        funcionesHelper.ventanaMensaje(RecuperarContra.this,
                                "El usuario ingresado no existe");
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
                Toast.makeText(RecuperarContra.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue = Volley.newRequestQueue(RecuperarContra.this);
        requestQueue.add(request);
    }

    //metodo para modificar contraseña
    public void modificarUser(String pass) {
        //generar la URL que conecta al local host
        String url = "http:" + ip + "/ConexionBDRestaurante/modUser.php?nombre=" + nombre +
                "&usuario=" + usuario + "&contra=" + pass + "&tipo=" + tipo + "&id=" + id + "";
        //crear progres dialog por si demora la respuesta
        final ProgressDialog progressDialog = new ProgressDialog(RecuperarContra.this);
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
                        limpiarUser();
                        progressDialog.dismiss();
                        Toast.makeText(RecuperarContra.this, "Modificado con exito", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RecuperarContra.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        progressDialog.dismiss();
                        funcionesHelper.ventanaMensaje(RecuperarContra.this, "No se pudo cambiar la contraseña");
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
                Toast.makeText(RecuperarContra.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue = Volley.newRequestQueue(RecuperarContra.this);
        requestQueue.add(request);
    }

    public void limpiarUser() {
        edt1.setText("");
        edt2.setText("");
        edt3.setText("");
        edt2.setVisibility(View.GONE);
        edt3.setVisibility(View.GONE);
        btn2.setVisibility(View.GONE);
        btn1.setVisibility(View.VISIBLE);
    }
}