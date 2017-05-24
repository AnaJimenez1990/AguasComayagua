package provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import provider.facturacion;


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
        String cmd = "CREATE TABLE " + facturacion.facturacion + " (" +
                facturacion.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                facturacion.Columnas.recibo + " TEXT, " +
                facturacion.Columnas.fecha + " TEXT, " +
        facturacion.Columnas.vence + " TEXT, " +
        facturacion.Columnas.nombre + " TEXT, " +
        facturacion.Columnas.valor + " TEXT, " +
        facturacion.Columnas.tipofa + " TEXT, " +
                facturacion.Columnas.clave + " TEXT, " +
                facturacion.Columnas.abonado + " TEXT, " +
        facturacion.Columnas.direccion + " TEXT, " +
                facturacion.Columnas.ciclo + " TEXT, " ;

        database.execSQL(cmd);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try { db.execSQL("drop table " + facturacion.facturacion); }
        catch (SQLiteException e) { }
        onCreate(db);
    }
}