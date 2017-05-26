package com.example.informatica2.aguascomayagua.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Informatica 2 on 23/5/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context, String name,
                          SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        createTable(database); // Crear la tabla "facturacion"
    }

    /**
     * Crear tabla en la base de datos
     *
     * @param database Instancia de la base de datos
     */
        private void createTable(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ctfacturacion.FACTURACION + " (" +
                ctfacturacion.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ctfacturacion.Columnas.fecha + " TEXT, " +
                ctfacturacion.Columnas.valor + " TEXT, " +
                ctfacturacion.Columnas.nombre + " TEXT, ";
        database.execSQL(cmd);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try { db.execSQL("drop table " + ctfacturacion.FACTURACION); }
        catch (SQLiteException e) { }
        onCreate(db);
    }
}