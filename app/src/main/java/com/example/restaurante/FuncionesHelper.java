package com.example.restaurante;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public class FuncionesHelper {
    // función que llama a una ventana de dialogo con el mensaje que se le pone
    public void ventanaMensaje(Context context, String mensaje) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(context);
        dialogo.setMessage(mensaje)
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        dialogo.show();
    }
}
