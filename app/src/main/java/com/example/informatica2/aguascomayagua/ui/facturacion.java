package com.example.informatica2.aguascomayagua.ui;

/**
 * Created by Informatica 2 on 26/5/2017.
 */
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.informatica2.aguascomayagua.utils.utilidades;
import com.example.informatica2.aguascomayagua.R;
import com.example.informatica2.aguascomayagua.provider.providerFacturacion;
import com.example.informatica2.aguascomayagua.sync.SyncAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class facturacion extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText valor;
    Spinner nombre;
    TextView fecha;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facturacion);

        setToolbar();

        valor = (EditText) findViewById(R.id.valor);
        nombre = (Spinner) findViewById(R.id.nombre);
        fecha = (TextView) findViewById(R.id.fecha);


        fecha.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DateDialog().show(getSupportFragmentManager(), "DatePicker");
                    }
                }
        );
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void alClickearBoton(View v) {
        String valorText = valor.getText().toString();
        String etiquetaText = nombre.getSelectedItem().toString();
        String fechaText = fecha.getText().toString();


        ContentValues values = new ContentValues();
        values.put(facturacion.Columnas.VALOR, valorText);
        values.put(facturacion.Columnas.NOMBRE, nombreText);
        values.put(facturacion.Columnas.FECHA, fechaText);
        values.put(facturacion.Columnas.PENDIENTE_INSERCION, 1);

        getContentResolver().insert(facturacion.CONTENT_URI, values);
        SyncAdapter.sincronizarAhora(this, true);

        if (utilidades.materialDesign())
            finishAfterTransition();
        else finish();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(year + "-" + monthOfYear + "-" + dayOfMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outDate = dateFormat.format(date);

        fecha.setText(outDate);
    }

}
