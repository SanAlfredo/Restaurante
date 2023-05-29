package com.example.restaurante;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistroUser extends AppCompatActivity {
    //declaracion de variables locales
    EditText edt1, edt2, edt3, edt4;
    Button btn1;
    //funcion de ayuda ventana
    FuncionesHelper funcionesHelper = new FuncionesHelper();

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
        //necesario instanciar la clase DBHelper que contiene la conexion a la base de datos
        DBHelper helper = new DBHelper(RegistroUser.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            //se colocan los valores en el contenedor
            ContentValues c = new ContentValues();
            c.put("nombre", nombre);
            c.put("usuario", email);
            c.put("contra", pass);
            c.put("tipo", tipo);
            //se insertan en la base de datos
            db.insert("usuarios", null, c);
            //se cierra la base de datos
            db.close();
            // un pequeño mensaje de registrado con exito
            Toast.makeText(getApplication(), "Registro exitoso", Toast.LENGTH_SHORT).show();
            //y volver todas las casillas a vacio
            limpiarRegUser();
        } catch (Exception e) {
            Toast.makeText(getApplication(), "ERROR " + e, Toast.LENGTH_SHORT).show();
        }
    }

    //para limpiar los campos
    public void limpiarRegUser() {
        edt1.setText("");
        edt2.setText("");
        edt3.setText("");
        edt4.setText("");
    }
}