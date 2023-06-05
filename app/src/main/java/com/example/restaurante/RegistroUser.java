package com.example.restaurante;

import static com.example.restaurante.MainActivity.ip;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

import org.json.JSONException;
import org.json.JSONObject;

public class RegistroUser extends AppCompatActivity {
    //declaracion de variables locales
    EditText edt1, edt2, edt3, edt4;
    Button btn1;
    //funcion de ayuda ventana
    FuncionesHelper funcionesHelper = new FuncionesHelper();
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_user);
        //enlace de variables locales
        edt1 = findViewById(R.id.et_reg_name);
        edt2 = findViewById(R.id.et_reg_mail);
        edt3 = findViewById(R.id.et_reg_pass1);
        edt4 = findViewById(R.id.et_reg_pass2);
        btn1 = findViewById(R.id.btn_reg_user);

        //boton de registro de usuario
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = edt1.getText().toString();
                String email = edt2.getText().toString();
                String contra1 = edt3.getText().toString();
                String contra2 = edt4.getText().toString();
                if (nombre.isEmpty() || email.isEmpty() || contra1.isEmpty() || contra2.isEmpty()) {
                    //usando la alerta mensaje
                    funcionesHelper.ventanaMensaje(RegistroUser.this, "Debe llenar los campos:\n" +
                            "\t\t* Nombre completo\n\t\t* Correo electronico\n\t\t* Contraseña\n\t\t* Repetir contraseña");
                } else {
                    if (contra1.equals(contra2)) {
                        int tipo = 2;
                        guardarUsuario(nombre, email, contra1, tipo);
                    } else {
                        funcionesHelper.ventanaMensaje(RegistroUser.this, "Las contraseñas deben ser iguales");
                    }
                }
            }
        });

    }

    //función que guarda los datos de productos en la base de datos
    public void guardarUsuario(String nombre, String email, String pass, int tipo) {
        //generar la URL que conecta al local host
        String url = "http:" + ip + "/ConexionBDRestaurante/regUser.php?nombre=" + nombre +
                "&usuario="+email+"&contra="+pass+"&tipo="+tipo+"";
        //crear progres dialog por si demora la respuesta
        final ProgressDialog progressDialog = new ProgressDialog(RegistroUser.this);
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
                        Toast.makeText(RegistroUser.this,"Registrado con exito",Toast.LENGTH_SHORT).show();
                        limpiarRegUser();
                        progressDialog.dismiss();
                        Intent intent=new Intent(RegistroUser.this,MainActivity.class);
                        startActivity(intent);
                    } else {
                        progressDialog.dismiss();
                        funcionesHelper.ventanaMensaje(RegistroUser.this, "No se registro");
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
                Toast.makeText(RegistroUser.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue = Volley.newRequestQueue(RegistroUser.this);
        requestQueue.add(request);
    }

    //para limpiar los campos
    public void limpiarRegUser() {
        edt1.setText("");
        edt2.setText("");
        edt3.setText("");
        edt4.setText("");
    }
}