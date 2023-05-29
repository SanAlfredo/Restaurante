package com.example.restaurante;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuAdmin extends AppCompatActivity {

    //instanciar variables locales
    Button btn1, btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_admin);
        // enlazar botones
        btn1 = findViewById(R.id.btn_admi_new);
        btn2 = findViewById(R.id.btn_admi_see);

        //boton que lleva al registro y modificacion de productos
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuAdmin.this,Producto.class);
                startActivity(intent);
            }
        });

        // boton que muestra todos los productos ya registrados
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuAdmin.this,VerProductos.class);
                startActivity(intent);
            }
        });
    }
}