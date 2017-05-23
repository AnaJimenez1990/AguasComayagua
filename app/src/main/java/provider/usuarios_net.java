package provider;

/**
 * Created by Informatica 2 on 23/5/2017.
 */
import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;


public class usuarios_net {

        public final static String AUTHORITY
                = "com.example.informatica2.aguascomayagua;";

        /**
         * Representación de la tabla a consultar
         */
        public static final String usuarios_net = "usuarios_net";
        /**
         * Tipo MIME que retorna la consulta de una sola fila
         */
        public final static String SINGLE_MIME =
                "vnd.android.cursor.item/vnd." + AUTHORITY + usuarios_net;
        /**
         * Tipo MIME que retorna la consulta de {@link }
         */
        public final static String MULTIPLE_MIME =
                "vnd.android.cursor.dir/vnd." + AUTHORITY + usuarios_net;
        /**
         * URI de contenido principal
         */
        public final static Uri CONTENT_URI =
                Uri.parse("content://" + AUTHORITY + "/" + usuarios_net);
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
            uriMatcher.addURI(AUTHORITY, usuarios_net ,ALLROWS);
            uriMatcher.addURI(AUTHORITY, usuarios_net + "/#", SINGLE_ROW);
        }

        // Valores para la columna ESTADO
        public static final int ESTADO_OK = 0;
        public static final int ESTADO_SYNC = 1;


        /**
         * Estructura de la tabla
         */
        public static class Columnas implements BaseColumns {

            private Columnas() {
                // Sin instancias
            }

            public final static String usuario = "usu";
            public final static String password = "pass";

        }
}
