package com.example.restaurante;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    //declaración de variables locales
    EditText edt1, edt2;
    Button btn1, btn2, btn3;

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

    }
}