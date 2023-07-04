package com.example.restaurante;

import static com.example.restaurante.MainActivity.ip;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FacturaPago extends AppCompatActivity {
    //iniciar variables localet s
    ListView list1;
    ImageView img1;
    TextView tv1;
    Button btn1, btn2, btn3;
    ArrayList<String> listado;
    //peticiones en internet
    RequestQueue requestQueue;
    String[] recibido1;
    String[] recibido2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura_pago);
        list1 = findViewById(R.id.list_total);
        img1 = findViewById(R.id.iv_qr_generado);
        tv1 = findViewById(R.id.tv_total);
        btn1 = findViewById(R.id.btn_pagar_efectivo);
        btn2 = findViewById(R.id.btn_pagar_qr);
        btn3 = findViewById(R.id.btn_pagar_verificar);
        btn3.setVisibility(View.GONE);
        //datos recibidos del anterior activity Menu User
        recibido1 = getIntent().getStringArrayExtra("datos1");
        recibido2 = getIntent().getStringArrayExtra("datos2");
        //usamos los recibidos de datos 1 para cargar la lista
        cargarLista();
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogoDeAlerta(1);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogoDeAlerta(2);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificarPago();
            }
        });
    }

    //cargar los datos recibidos en la lista:
    public void cargarLista() {
        Float resultado = 0.0f;
        ArrayList<String> datos = new ArrayList<>();
        if (recibido1.length > 1) {
            for (int i = 0; i < recibido1.length; i++) {
                //añadir los datos recibidos en el arrayList
                datos.add(recibido1[i]);
                //separar lo recibido para calcular el monto total adeudato
                String[] separado = recibido1[i].split(":");
                resultado = resultado + Float.parseFloat(separado[4].trim());
            }
        } else {
            datos.add(recibido1[0]);
            String[] separado = recibido1[0].split(":");
            resultado = resultado + Float.parseFloat(separado[4].trim());
        }
        //mostrar el monto total en el text View
        tv1.setText("Total a pagar: " + resultado);
        listado = datos;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(FacturaPago.this,
                android.R.layout.simple_list_item_1, listado);
        //mostrar la lista de comidas compradas en el list View
        list1.setAdapter(adapter);
    }

    //dialogo de alerta
    public void dialogoDeAlerta(int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FacturaPago.this);
        if (i == 1) {
            builder.setTitle("Método de pago").setMessage("¿Desea pagar en efectivo?");
            builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    Intent intent = new Intent(FacturaPago.this, MenuUser.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            builder.setTitle("Método de pago").setMessage("¿Desea generar un QR?");
            builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    cargarQR();
                }
            });
            builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    //cargar el QR
    public void cargarQR() {
        int cod = Integer.parseInt(recibido2[0]);
        //generar la URL que conecta al local host
        String url = "http:" + ip + "/ConexionBDRestaurante/modVenta.php?codigo=" + cod + "&nombre=" +
                recibido2[1] + "";
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        try {
            Bitmap bitmap = barcodeEncoder.encodeBitmap(url, BarcodeFormat.QR_CODE, 500, 500);
            img1.setImageBitmap(bitmap);
            btn3.setVisibility(View.VISIBLE);
        } catch (WriterException e) {
            Toast.makeText(FacturaPago.this, "ERROR " + e, Toast.LENGTH_SHORT).show();
        }

    }

    //funcion que verifica el pago
    public void verificarPago() {
        int cod = Integer.parseInt(recibido2[0]);
        //generar la URL que conecta al local host
        String url = "http:" + ip + "/ConexionBDRestaurante/buscarVenta.php?codigo=" + cod + "";
        //crear progres dialog por si demora la respuesta
        final ProgressDialog progressDialog = new ProgressDialog(FacturaPago.this);
        progressDialog.setMessage("Verificando el pago...");
        //mostrar la barra de progreso
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //guardar la respuesta de la BD un objeto de tipo JSON
                    JSONObject jsonObject = new JSONObject(response);
                    String exito = jsonObject.getString("exito");
                    JSONArray jsonArray = jsonObject.getJSONArray("datos");
                    //si exito es 1
                    if (exito.equals("1")) {
                        progressDialog.dismiss();
                        //recorrer el vector
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            //obtener valores
                            String pagado = object.getString("pago");
                            if (pagado.equals("1")) {
                                Toast.makeText(FacturaPago.this, "Ya ha pagado", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(FacturaPago.this, MenuUser.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(FacturaPago.this, "Aún no se ha pagado", Toast.LENGTH_SHORT).show();
                            }
                        }
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
                Toast.makeText(FacturaPago.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue = Volley.newRequestQueue(FacturaPago.this);
        requestQueue.add(request);

    }
}