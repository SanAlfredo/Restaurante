package com.example.restaurante;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;

public class FacturaPago extends AppCompatActivity {
    //iniciar variables locales
    ListView list1;
    ImageView img1;
    TextView tv1;
    ArrayList<String> listado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura_pago);
        list1=findViewById(R.id.list_total);
        img1=findViewById(R.id.iv_qr_generado);
        tv1=findViewById(R.id.tv_total);
        //datos recibidos del anterior activity Menu User
        String[] recibido=getIntent().getStringArrayExtra("datos");
        cargarLista(recibido);
        cargarQR(recibido);
    }
    //cargar los datos recibidos en la lista:
    public void cargarLista(String[] recibido){
        ArrayList<String> datos=new ArrayList<>();
        if (recibido.length>1){
            for(int i=0;i<recibido.length;i++){
                datos.add(recibido[i]);
            }
        }else{
            datos.add(recibido[0]);
        }
        listado=datos;
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(FacturaPago.this,
                android.R.layout.simple_list_item_1,listado);
        list1.setAdapter(adapter);
    }
    //cargar el QR
    public void cargarQR(String[] recibido){
        Float resultado=0.0f;
        for(int i =0;i<recibido.length;i++){
            String[] separado=recibido[i].split(":");
            resultado=resultado+Float.parseFloat(separado[4].trim());
        }
        tv1.setText("Total a pagar: "+resultado);
        BarcodeEncoder barcodeEncoder=new BarcodeEncoder();
        try {
            Bitmap bitmap=barcodeEncoder.encodeBitmap(Float.toString(resultado), BarcodeFormat.QR_CODE,500,500);
            img1.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Toast.makeText(FacturaPago.this,"ERROR "+e,Toast.LENGTH_SHORT).show();
        }

    }
}