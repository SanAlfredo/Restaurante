package com.example.restaurante;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

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
    //funcion de encriptacion de contraseñas
    public String hash_Mac (String secret){
        //generamos una contraseña vacia
        String hash ="";
        try{
            //convertimos a bytes la palabra secreta o llave "key"
            byte[] key="Udabol".getBytes();
            //con esa llave generamos un algoritmo de encriptacion
            HmacUtils hm256= new HmacUtils(HmacAlgorithms.HMAC_SHA_256,key);
            //encriptamos la contraseña
            hash = hm256.hmacHex(secret);
        }catch (Exception e){
            e.printStackTrace();
        }
        return hash;
    }
}
