package OpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteOutOfMemoryException;

import com.example.informatica2.aguascomayagua.DB_AC;

/**
 * Created by Informatica 2 on 18/5/2017.
 */

public class SQLite_OpenHelper extends SQLiteOpenHelper{
//constantes
    private static final String DB_NAME = "AguasComayagua.sqlite";
    private static final int DB_SHEME_VERSION = 1;

// constructor
    public SQLite_OpenHelper(Context context) {
        super(context,DB_NAME, null, DB_SHEME_VERSION);
    }
// metodo para la ejecucion de sql
    @Override
    public void onCreate(SQLiteDatabase db) {
    db.execSQL(DB_AC.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    // metodo que permite abrir la base de datos





}


