import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import provider.usuarios_net;

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
    public void onCreate(SQLiteDatabase db) {
        createTable(); // Crear la tabla "gasto"
    }

    /**
     * Crear tabla en la base de datos
     *
     * @param database Instancia de la base de datos
     */
    private void createTable(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + usuarios_net.usuarios_net + " (" +
                usuarios_net.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                usuarios_net.Columnas.usuario + " TEXT, " +
                usuarios_net.Columnas.password + " TEXT, " ;
        database.execSQL(cmd);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try { db.execSQL("drop table " + usuarios_net.usuarios_net); }
        catch (SQLiteException e) { }
        onCreate(db);
    }
}