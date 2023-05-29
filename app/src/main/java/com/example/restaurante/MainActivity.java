package com.example.restaurante;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //declaración de variables locales
    EditText edt1, edt2;
    Button btn1, btn2, btn3;
    //funcion de ayuda ventana
    FuncionesHelper funcionesHelper = new FuncionesHelper();

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
                Intent intent = new Intent(MainActivity.this, RegistroUser.class);
                startActivity(intent);
            }
        });
        //inicio o logeo
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user=edt1.getText().toString();
                String pass=edt2.getText().toString();
                //comprobar campos vacios y logear
                if (user.isEmpty() || pass.isEmpty()) {
                    funcionesHelper.ventanaMensaje(MainActivity.this, "Debe llenar los campos de: " +
                            "\n\t* Usuario\n\t* Contraseña");
                } else {
                    String[] usuario = {edt1.getText().toString()};
                    buscarUser(usuario, pass);
                }
            }
        });
    }

    //metodo para buscar usuario en la base de datos
    public void buscarUser(String[] user, String pass1) {
        //instanciamos data base helper
        DBHelper helper = new DBHelper(MainActivity.this);
        //instanciamos la libreria de sqlite
        SQLiteDatabase db = helper.getReadableDatabase();
        //generamos la consulta SQL
        //String consulta = "select * from usuarios where usuario =? and contra =?";
        String consulta = "select * from usuarios where usuario like?";
        try {
            //ejecutamos la consulta dentro un try catch por si hubiera algun error
            Cursor c = db.rawQuery(consulta, user);
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                if (pass1.equals(c.getString(3))){
                    //definir mensaje para el Toast
                    String mensaje = "Bienvenido/a: " + c.getString(1);
                    Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG).show();
                    edt1.setText("");
                    edt2.setText("");
                }else{
                    //definir mensaje para la ventana
                    //String mensaje = "Contraseña incorrecta "+pass1+" base de datos "+c.getString(3);
                    String mensaje = "Contraseña incorrecta";
                    funcionesHelper.ventanaMensaje(MainActivity.this, mensaje);
                }
            } else {
                funcionesHelper.ventanaMensaje(MainActivity.this, "El usuario y/o contraseña son incorrectas");
            }
        } catch (Exception e) {
            Toast.makeText(getApplication(), "ERROR " + e,
                    Toast.LENGTH_SHORT).show();
        }
    }
}