package com.example.restaurante;

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

public class MainActivity extends AppCompatActivity {
    //declaración de variables locales
    EditText edt1, edt2;
    Button btn1, btn2, btn3;
    //funcion de ayuda ventana
    FuncionesHelper funcionesHelper = new FuncionesHelper();
    //peticiones en internet
    RequestQueue requestQueue;
    final static String ip = "192.168.100.90";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //enlazar botones con variables locales
        edt1 = findViewById(R.id.et_log_user);
        edt2 = findViewById(R.id.et_log_pass);
        btn1 = findViewById(R.id.btn_log_recu);
        btn2 = findViewById(R.id.btn_log_new);
        btn3 = findViewById(R.id.btn_log_log);

        //ventana de registro de usuarios
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //se dirije a la ventana de recuperacion de contraseña
                Intent intent = new Intent(MainActivity.this, RecuperarContra.class);
                startActivity(intent);
            }
        });
        //boton para registro de usuario
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt1.setText("");
                edt2.setText("");
                Intent intent = new Intent(MainActivity.this, RegistroUser.class);
                startActivity(intent);
            }
        });
        //inicio o logeo
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = edt1.getText().toString();
                String pass = edt2.getText().toString();
                //comprobar campos vacios y logear
                if (user.isEmpty() || pass.isEmpty()) {
                    funcionesHelper.ventanaMensaje(MainActivity.this, "Debe llenar los campos de: " +
                            "\n\t* Usuario\n\t* Contraseña");
                } else {
                    buscarUser(user, pass);
                }
            }
        });
    }

    //metodo para buscar usuario en la base de datos
    public void buscarUser(String user, String pass1) {
        //generar la URL que conecta al local host
        String url = "http:" + ip + "/ConexionBDRestaurante/buscarUser.php?usuario=" + user + "";
        //crear un progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Obteniendo datos del servidor...");
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
                        //cerrar el progress dialog
                        progressDialog.dismiss();
                        //recorrer el resultado
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            //obtener los valores
                            String id = object.getString("id");
                            String nombre = object.getString("nombre");
                            String usuario = object.getString("usuario");
                            String contra = object.getString("contra");
                            int tipo = object.getInt("tipo");
                            //verificar si la contraseña encriptada es igual a la proporcionada
                            if (funcionesHelper.hash_Mac(pass1).equals(contra)) {
                                //definir mensaje para el Toast
                                String mensaje = "Bienvenido/a: " + nombre;
                                Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG).show();
                                edt1.setText("");
                                edt2.setText("");
                                //si es administrador
                                if (tipo == 1) {
                                    Intent intent = new Intent(MainActivity.this, MenuAdmin.class);
                                    startActivity(intent);
                                } else {
                                    //si no es administrador
                                    String []datos=new String[2];
                                    datos[0]=id;
                                    datos[1]=usuario;
                                    Intent intent = new Intent(MainActivity.this, MenuUser.class);
                                    intent.putExtra("datos",datos);
                                    startActivity(intent);
                                }
                            } else {
                                //definir mensaje para la ventana
                                String mensaje = "Contraseña incorrecta";
                                funcionesHelper.ventanaMensaje(MainActivity.this, mensaje);
                            }
                        }
                    } else {
                        progressDialog.dismiss();
                        funcionesHelper.ventanaMensaje(MainActivity.this, "El usuario y/o contraseña son incorrectas");
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
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(request);
    }
}