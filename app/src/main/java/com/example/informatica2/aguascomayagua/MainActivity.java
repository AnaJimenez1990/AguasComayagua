package com.example.informatica2.aguascomayagua;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import OpenHelper.SQLite_OpenHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       SQLite_OpenHelper helper = new SQLite_OpenHelper(this);
        // nos devuelve la base de datos en modo de escritura y lectura
        SQLiteDatabase db = helper.getWritableDatabase();





    }
}
