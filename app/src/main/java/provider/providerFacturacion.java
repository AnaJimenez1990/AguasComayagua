package provider;

/**
 * Created by Informatica 2 on 23/5/2017.
 */

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

public class providerFacturacion extends ContentProvider {
    /**
     * Nombre de la base de datos
     */
    private static final String DATABASE_NAME = "AguasComayagua";
    /**
     * Versión actual de la base de datos
     */
    private static final int DATABASE_VERSION = 1;
    /**
     * Instancia global del Content Resolver
     */
    private ContentResolver resolver;
    /**
     * Instancia del administrador de BD
     */
    private DatabaseHelper databaseHelper;

    @Override
    public boolean onCreate() {
        // Inicializando gestor BD
        databaseHelper = new DatabaseHelper(getContext(), DATABASE_NAME, null,
                DATABASE_VERSION
        );

        resolver = getContext().getContentResolver();

        return true;
    }

    @Override
    public Cursor query(
            Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder) {

        // Obtener base de datos
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        // Comparar Uri
        int match = facturacion.uriMatcher.match(uri);

        Cursor c;

        switch (match) {
            case facturacion.ALLROWS:
                // Consultando todos los registros
                c = db.query(facturacion.facturacion, projection,
                        selection, selectionArgs,
                        null, null, sortOrder);
                c.setNotificationUri(
                        resolver,
                        facturacion.CONTENT_URI);
                break;
            case facturacion.SINGLE_ROW:
                // Consultando un solo registro basado en el Id del Uri
                long idfacturacion = ContentUris.parseId(uri);
                c = db.query(facturacion.facturacion, projection,
                        facturacion.Columnas._ID + " = " + idfacturacion,
                        selectionArgs, null, null, sortOrder);
                c.setNotificationUri(
                        resolver,
                        facturacion.CONTENT_URI);
                break;
            default:
                throw new IllegalArgumentException("URI no soportada: " + uri);
        }
        return c;

    }

    @Override
    public String getType(Uri uri) {
        switch (facturacion.uriMatcher.match(uri)) {
            case facturacion.ALLROWS:
                return facturacion.MULTIPLE_MIME;
            case facturacion.SINGLE_ROW:
                return facturacion.SINGLE_MIME;
            default:
                throw new IllegalArgumentException("Tipo de gasto desconocido: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // Validar la uri
        if (facturacion.uriMatcher.match(uri) != facturacion.ALLROWS) {
            throw new IllegalArgumentException("URI desconocida : " + uri);
        }
        ContentValues contentValues;
        if (values != null) {
            contentValues = new ContentValues(values);
        } else {
            contentValues = new ContentValues();
        }

        // Inserción de nueva fila
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long rowId = db.insert(facturacion.facturacion, null, contentValues);
        if (rowId > 0) {
            Uri uri_facturacion = ContentUris.withAppendedId(
                    facturacion.CONTENT_URI, rowId);
            resolver.notifyChange(uri_facturacion, null, false);
            return uri_facturacion;
        }
        throw new SQLException("Falla al insertar fila en : " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        int match = facturacion.uriMatcher.match(uri);
        int affected;

        switch (match) {
            case facturacion.ALLROWS:
                affected = db.delete(facturacion.facturacion,
                        selection,
                        selectionArgs);
                break;
            case facturacion.SINGLE_ROW:
                long idfacturacion = ContentUris.parseId(uri);
                affected = db.delete(facturacion.facturacion,
                        facturacion.Columnas.id+ "=" + idfacturacion
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                // Notificar cambio asociado a la uri
                resolver.
                        notifyChange(uri, null, false);
                break;
            default:
                throw new IllegalArgumentException("Elemento gasto desconocido: " +
                        uri);
        }
        return affected;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int affected;
        switch (facturacion.uriMatcher.match(uri)) {
            case facturacion.ALLROWS:
                affected = db.update(facturacion.facturacion, values,
                        selection, selectionArgs);
                break;
            case facturacion.SINGLE_ROW:
                String idfacturacion = uri.getPathSegments().get(1);
                affected = db.update(facturacion.facturacion, values,
                        facturacion.Columnas.id + "=" + idfacturacion
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("URI desconocida: " + uri);
        }
        resolver.notifyChange(uri, null, false);
        return affected;
    }


}
