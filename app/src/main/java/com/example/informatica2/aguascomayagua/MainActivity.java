package com.example.informatica2.aguascomayagua;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import  com.example.informatica2.aguascomayagua.utils.utilidades;
import  com.example.informatica2.aguascomayagua.R;
import  com.example.informatica2.aguascomayagua.provider.ExpenseContract;
import  com.example.informatica2.aguascomayagua.sync.SyncAdapter;

public class MainActivity extends AppCompatActivity

    implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private Adaptadorfacturacion adapter;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authenticator);

        setToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.reciclador);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adaptadorfacturacion(this);
        recyclerView.setAdapter(adapter);
        emptyView = (TextView) findViewById(R.id.recyclerview_data_empty);

        getSupportLoaderManager().initLoader(0, null, this);

        SyncAdapter.inicializarSyncAdapter(this);
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void onClickFab(View v) {
        Intent intent = new Intent(this, InsertActivity.class);
        if (Utilidades.materialDesign())
            startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle());

        else startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sync) {
            SyncAdapter.sincronizarAhora(this, false);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        emptyView.setText("Cargando datos...");
        // Consultar todos los registros
        return new CursorLoader(
                this,
                ExpenseContract.CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
        emptyView.setText("");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }


    }

