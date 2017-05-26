package com.example.informatica2.aguascomayagua.provider;

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
    private static final String DATABASE_NAME = "AguasComayagua.db";
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
        databaseHelper = new DatabaseHelper(
                getContext(),
                DATABASE_NAME,
                null,
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
        int match = ctfacturacion.uriMatcher.match(uri);

        Cursor c;

        switch (match) {
            case ctfacturacion.ALLROWS:
                // Consultando todos los registros
                c = db.query(ctfacturacion.FACTURACION, projection,
                        selection, selectionArgs,
                        null, null, sortOrder);
                c.setNotificationUri(
                        resolver,
                        ctfacturacion.CONTENT_URI);
                break;
            case ctfacturacion.SINGLE_ROW:
                // Consultando un solo registro basado en el Id del Uri
                long idfacturacion = ContentUris.parseId(uri);
                c = db.query(ctfacturacion.FACTURACION, projection,
                        ctfacturacion.Columnas._ID + " = " + idfacturacion,
                        selectionArgs, null, null, sortOrder);
                c.setNotificationUri(
                        resolver,
                        ctfacturacion.CONTENT_URI);
                break;
            default:
                throw new IllegalArgumentException("URI no soportada: " + uri);
        }
        return c;

    }

    @Override
    public String getType(Uri uri) {
        switch (ctfacturacion.uriMatcher.match(uri)) {
            case ctfacturacion.ALLROWS:
                return ctfacturacion.MULTIPLE_MIME;
            case ctfacturacion.SINGLE_ROW:
                return ctfacturacion.SINGLE_MIME;
            default:
                throw new IllegalArgumentException("Tipo de gasto desconocido: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // Validar la uri
        if (ctfacturacion.uriMatcher.match(uri) != ctfacturacion.ALLROWS) {
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
        long rowId = db.insert(ctfacturacion.FACTURACION, null, contentValues);
        if (rowId > 0) {
            Uri uri_facturacion = ContentUris.withAppendedId(
                    ctfacturacion.CONTENT_URI, rowId);
            resolver.notifyChange(uri_facturacion, null, false);
            return uri_facturacion;
        }
        throw new SQLException("Falla al insertar fila en : " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        int match = ctfacturacion.uriMatcher.match(uri);
        int affected;

        switch (match) {
            case ctfacturacion.ALLROWS:
                affected = db.delete(ctfacturacion.FACTURACION,
                        selection,
                        selectionArgs);
                break;
            case ctfacturacion.SINGLE_ROW:
                long idFacturacion = ContentUris.parseId(uri);
                affected = db.delete(ctfacturacion.FACTURACION,
                        ctfacturacion.Columnas.ID_REMOTA+ "=" + idFacturacion
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
        switch (ctfacturacion.uriMatcher.match(uri)) {
            case ctfacturacion.ALLROWS:
                affected = db.update(ctfacturacion.FACTURACION, values,
                        selection, selectionArgs);
                break;
            case ctfacturacion.SINGLE_ROW:
                String idfacturacion = uri.getPathSegments().get(1);
                affected = db.update(ctfacturacion.FACTURACION, values,
                        ctfacturacion.Columnas.ID_REMOTA + "=" + idfacturacion
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
