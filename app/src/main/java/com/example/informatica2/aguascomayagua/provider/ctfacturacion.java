package com.example.informatica2.aguascomayagua.provider;

/**
 * Created by Informatica 2 on 23/5/2017.
 */
import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;


public class ctfacturacion {

    /**
     * Autoridad del Content Provider
     */
    public final static String AUTHORITY
            = "com.example.informatica2.aguascomayagua";
    /**
     * Representación de la tabla a consultar
     */
    public static final String FACTURACION = "facturacion";
    /**
     * Tipo MIME que retorna la consulta de una sola fila
     */
    public final static String SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + AUTHORITY + FACTURACION;
    /**
     * Tipo MIME que retorna la consulta de {@link }
     */
    public final static String MULTIPLE_MIME =
            "vnd.android.cursor.dir/vnd." + AUTHORITY + FACTURACION;
    /**
     * URI de contenido principal
     */
    public final static Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + FACTURACION);
    /**
     * Comparador de URIs de contenido
     */
    public static final UriMatcher uriMatcher;
    /**
     * Código para URIs de multiples registros
     */
    public static final int ALLROWS = 1;
    /**
     * Código para URIS de un solo registro
     */
    public static final int SINGLE_ROW = 2;


    // Asignación de URIs
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, FACTURACION, ALLROWS);
        uriMatcher.addURI(AUTHORITY, FACTURACION + "/#", SINGLE_ROW);
    }

    // Valores para la columna ESTADO
    public static final int ESTADO_OK = 0;
    public static final int ESTADO_SYNC = 1;



    /**
     * Estructura de la tabla
     */
    public static class Columnas implements BaseColumns {
        private Columnas() {

        }

            // Sin instancias
            public static final String id = "id";
            public final static String nombre = "nombre";
            public final static String valor = "valor";
        public final static String fecha = "fecha";

        public static String PENDIENTE_INSERCION = "pendiente_insercion";;
        public static final String ESTADO = "estado";
        public static final String ID_REMOTA = "idRemota";
    }
    }








