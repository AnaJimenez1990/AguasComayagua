package com.example.informatica2.aguascomayagua.utils;

/**
 * Created by Informatica 2 on 25/5/2017.
 */
import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.example.informatica2.aguascomayagua.provider.facturacion;

import org.json.JSONException;
import org.json.JSONObject;

public class utilidades {
    // Indices para las columnas indicadas en la proyección
    public static final int COLUMNA_ID = 1;
    public static final int COLUMNA_RECIBO = 2;
    public static final int COLUMNA_FECHA = 3;
    public static final int COLUMNA_NOMBRE = 4;
    public static final int COLUMNA_VALOR = 5;
    public static final int COLUMNA_TIPOFA = 6;
    public static final int COLUMNA_CLAVE = 7;
    public static final int COLUMNA_ABONADO = 8;
    public static final int COLUMNA_DIRECCION = 9;
    public static final int COLUMNA_CLICLO = 10;
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
        int recibo;
        int id;
        String fecha;
        String nombre;
        int valor;
        String tipofa;
        int clave;
        String abonado;
        String direccion;
        int ciclo;

        id = c.getInt(COLUMNA_ID);
        recibo = c.getInt(COLUMNA_RECIBO);
        fecha = c.getString(COLUMNA_FECHA);
        nombre = c.getString(COLUMNA_NOMBRE);
        valor = c.getInt(COLUMNA_VALOR);
        tipofa = c.getString(COLUMNA_TIPOFA);
        clave = c.getInt(COLUMNA_CLAVE);
        abonado = c.getString(COLUMNA_ABONADO);
        direccion= c.getString(COLUMNA_DIRECCION);
        ciclo = c.getInt(COLUMNA_CLICLO);

        try
        {
            jObject.put(facturacion.Columnas.ID, id);
            jObject.put(facturacion.Columnas.RECIBO, recibo);
            jObject.put(facturacion.Columnas.FECHA, fecha);
            jObject.put(facturacion.Columnas.NOMBRE, nombre);
            jObject.put(facturacion.Columnas.VALOR, valor);
            jObject.put(facturacion.Columnas.TIPOFA, tipofa);
            jObject.put(facturacion.Columnas.CLAVE, clave);
            jObject.put(facturacion.Columnas.ABONADO, abonado);
            jObject.put(facturacion.Columnas.DIRECCION, direccion);
            jObject.put(facturacion.Columnas.CICLO, ciclo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject", String.valueOf(jObject));

        return jObject;
}
