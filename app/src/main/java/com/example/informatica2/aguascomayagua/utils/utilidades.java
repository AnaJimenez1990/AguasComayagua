package com.example.informatica2.aguascomayagua.utils;

/**
 * Created by Informatica 2 on 25/5/2017.
 */
import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.example.informatica2.aguascomayagua.provider.ctfacturacion;


import org.json.JSONException;
import org.json.JSONObject;

public class utilidades {
    // Indices para las columnas indicadas en la proyección
    public static final int COLUMNA_ID = 0;
    public static final int COLUMNA_ID_REMOTA = 1;
    public static final int COLUMNA_FECHA = 2;
    public static final int COLUMNA_NOMBRE = 3;
    public static final int COLUMNA_VALOR = 4;

    /**
     * Determina si la aplicación corre en versiones superiores o iguales
     * a Android LOLLIPOP
     *
     * @return booleano de confirmación
     */
    public static boolean materialDesign() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * Copia los datos de un gasto almacenados en un cursor hacia un
     * JSONObject
     *
     * @param c cursor
     * @return objeto jason
     */
    public static JSONObject deCursorAJSONObject(Cursor c) {
        JSONObject jObject = new JSONObject();

        String fecha;
        String nombre;
        String valor;


        fecha = c.getString(COLUMNA_FECHA);
        nombre = c.getString(COLUMNA_NOMBRE);
        valor = c.getString(COLUMNA_VALOR);


        try {
            jObject.put(ctfacturacion.Columnas.fecha, fecha);
            jObject.put(ctfacturacion.Columnas.nombre, nombre);
            jObject.put(ctfacturacion.Columnas.valor, valor);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject", String.valueOf(jObject));

        return jObject;
    }
}