package Sync;

/**
 * Created by Informatica 2 on 24/5/2017.
 */
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import utils.utilidades;
import utils.constantes;
import com.example.informatica2.aguascomayagua.R;
import provider.facturacion;
import web.Facturacion;
import web.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = SyncAdapter.class.getSimpleName();

    ContentResolver resolver;

   private Gson gson = new Gson();

    /**
     * Proyección para las consultas
     */
    private static final String[] PROJECTION = new String[]{
            facturacion.Columnas.id,
            facturacion.Columnas.ID_REMOTA,
            facturacion.Columnas.recibo,
            facturacion.Columnas.fecha,
            facturacion.Columnas.vence,
            facturacion.Columnas.nombre,
            facturacion.Columnas.valor,
            facturacion.Columnas.tipofa,
            facturacion.Columnas.clave,
            facturacion.Columnas.abonado,
            facturacion.Columnas.direccion,
            facturacion.Columnas.ciclo
    };

    // Indices para las columnas indicadas en la proyección
    public static final int COLUMNA_ID_REMOTA = 1;
    public static final int COLUMNA_ID = 2;
    public static final int COLUMNA_RECIBO = 3;
    public static final int COLUMNA_FECHA = 4;
    public static final int COLUMNA_NOMBRE = 5;
    public static final int COLUMNA_VALOR = 6;
    public static final int COLUMNA_TIPOFA = 7;
    public static final int COLUMNA_CLAVE = 8;
    public static final int COLUMNA_ABONADO = 9;
    public static final int COLUMNA_DIRECCION = 10;
    public static final int COLUMNA_CLICLO = 11;


    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        resolver = context.getContentResolver();
    }

    /**
     * Constructor para mantener compatibilidad en versiones inferiores a 3.0
     */
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        resolver = context.getContentResolver();
    }

    public static void inicializarSyncAdapter(Context context) {
        obtenerCuentaASincronizar(context);
    }

    @Override
    public void onPerformSync(Account account,
                              Bundle extras,

                              String authority,
                              ContentProviderClient provider,
                              final SyncResult syncResult) {

        Log.i(TAG, "onPerformSync()...");

        boolean soloSubida = extras.getBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, false);

        if (!soloSubida) {
            realizarSincronizacionLocal(syncResult);
        } else {
            realizarSincronizacionRemota();
        }
    }

    private void realizarSincronizacionLocal(final SyncResult syncResult) {
        Log.i(TAG, "Actualizando el cliente.");

        VolleySingleton.getInstance(getContext()).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.GET,
                        constantes.GET_URL,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                procesarRespuestaGet(response, syncResult);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, error.networkResponse.toString());
                            }
                        }
                )
        );
    }

    /**
     * Procesa la respuesta del servidor al pedir que se retornen todos los gastos.
     *
     * @param response   Respuesta en formato Json
     * @param syncResult Registro de resultados de sincronización
     */
    private void procesarRespuestaGet(JSONObject response, SyncResult syncResult) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString(constantes.ESTADO);

            switch (estado) {
                case constantes.SUCCESS: // EXITO
                    actualizarDatosLocales(response, syncResult);
                    break;
                case constantes.FAILED: // FALLIDO
                    String mensaje = response.getString(constantes.MENSAJE);
                    Log.i(TAG, mensaje);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void realizarSincronizacionRemota() {
        Log.i(TAG, "Actualizando el servidor...");

        iniciarActualizacion();

        Cursor c = obtenerRegistrosSucios();

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros sucios.");

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(COLUMNA_ID);

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                constantes.INSERT_URL,
                                utilidades.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                      procesarRespuestaInsert(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d(TAG, "Error Volley: " + error.getMessage());
                                    }
                                }

                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8" + getParamsEncoding();
                            }
                        }
                );
            }

        } else {
            Log.i(TAG, "No se requiere sincronización");
        }
        c.close();
    }

    /**
     * Obtiene el registro que se acaba de marcar como "pendiente por sincronizar" y
     * con "estado de sincronización"
     *
     * @return Cursor con el registro.
     */
   private Cursor obtenerRegistrosSucios() {
        Uri uri = facturacion.CONTENT_URI;
        String selection = facturacion.Columnas.PENDIENTE_INSERCION + "=? AND "
                + facturacion.Columnas.ESTADO + "=?";
        String[] selectionArgs = new String[]{"1", facturacion.ESTADO_SYNC + ""};

        return resolver.query(uri, PROJECTION, selection, selectionArgs, null);
    }
//
    /**
     * Cambia a estado "de sincronización" el registro que se acaba de insertar localmente
     */
    private void iniciarActualizacion() {
        Uri uri = facturacion.CONTENT_URI;
        String selection = facturacion.Columnas.PENDIENTE_INSERCION + "=? AND "
                + facturacion.Columnas.ESTADO + "=?";
        String[] selectionArgs = new String[]{"1", facturacion.ESTADO_OK + ""};

        ContentValues v = new ContentValues();
        v.put(facturacion.Columnas.ESTADO, facturacion.ESTADO_SYNC);

        int results = resolver.update(uri, v, selection, selectionArgs);
        Log.i(TAG, "Registros puestos en cola de inserción:" + results);
    }

    /**
     * Limpia el registro que se sincronizó y le asigna la nueva id remota proveida
     * por el servidor
     *
     * @param idRemota id remota
     */
   private void finalizarActualizacion(String idRemota, int idLocal) {
        Uri uri = facturacion.CONTENT_URI;
        String selection = facturacion.Columnas._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(idLocal)};

        ContentValues v = new ContentValues();
        v.put(facturacion.Columnas.PENDIENTE_INSERCION, "0");
        v.put(facturacion.Columnas.ESTADO, facturacion.ESTADO_OK);
        v.put(facturacion.Columnas.ID_REMOTA, idRemota);

        resolver.update(uri, v, selection, selectionArgs);
    }

    /**
     * Procesa los diferentes tipos de respuesta obtenidos del servidor
     *
     * @param response Respuesta en formato Json
     */
    public void procesarRespuestaInsert(JSONObject response, int idLocal) {

        try {
            // Obtener estado
            String estado = response.getString(constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(constantes.MENSAJE);
            // Obtener identificador del nuevo registro creado en el servidor
            String idRemota = response.getString(constantes.ID_FACTURACION);

            switch (estado) {
                case constantes.SUCCESS:
                    Log.i(TAG, mensaje);
                    finalizarActualizacion(idRemota, idLocal);
                    break;

                case constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Actualiza los registros locales a través de una comparación con los datos
     * del servidor
     *
     * @param response   Respuesta en formato Json obtenida del servidor
     * @param syncResult Registros de la sincronización
     */
    private void actualizarDatosLocales(JSONObject response, SyncResult syncResult) {

        JSONArray facturacion = null;

        try {
            // Obtener array "gastos"
            facturacion = response.getJSONArray(provider.facturacion.facturacion);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        Facturacion[] res = gson.fromJson(facturacion != null ? facturacion.toString() : null, Facturacion[].class);
        List<Facturacion> data = Arrays.asList(res);

        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        // Tabla hash para recibir las entradas entrantes
        HashMap<String, facturacion> expenseMap = new HashMap<String, facturacion>();
        for (Facturacion e : data) {
            expenseMap.put(e,idfacturacion,e);
        }

        // Consultar registros remotos actuales
        Uri uri = provider.facturacion.CONTENT_URI;
        String select = provider.facturacion.Columnas.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, PROJECTION, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;
        int valor;
        String nombre;
        String fecha;
        String tipofa;
        int clave;
        String abonado;
        String direccion;
        int ciclo;


        while (c.moveToNext()) {
            syncResult.stats.numEntries++;

            id = c.getString(COLUMNA_ID_REMOTA);

            fecha = c.getString(COLUMNA_FECHA);
            nombre = c.getString(COLUMNA_NOMBRE);
            valor = c.getInt(COLUMNA_VALOR);

            facturacion match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = provider.facturacion.CONTENT_URI.buildUpon()
                        .appendPath(id).build();

                // Comprobar si el gasto necesita ser actualizado
               boolean b = match.valor != valor;

                boolean b2 = match.fecha != null && !match.fecha.equals(fecha);
                boolean b3 = match.nombre != null && !match.nombre.equals(nombre);

                if (b || b1 || b2 || b3) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(facturacion.Columnas.ID, match.id)
                            .withValue(facturacion.Columnas.VALOR, match.valor)
                            .withValue(facturacion.Columnas.FECHA, match.fecha)
                            .withValue(facturacion.Columnas.NOMBRE, match.nombre)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = provider.facturacion.CONTENT_URI.buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        Insertar items resultantes
        for (facturacion e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de: " + e.idfacturacion);
            ops.add(ContentProviderOperation.newInsert(facturacion.CONTENT_URI)
                    .withValue(facturacion.Columnas.ID_REMOTA, e.idfacturacion)
                    .withValue(facturacion.Columnas.VALOR, e.valor)
                    .withValue(facturacion.Columnas.ETIQUETA, e.nombre)
                    .withValue(facturacion.Columnas.FECHA, e.fecha)

                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(provider.facturacion.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    provider.facturacion.CONTENT_URI,
                    null,
                    false);
            Log.i(TAG, "Sincronización finalizada.");

        } else {
            Log.i(TAG, "No se requiere sincronización");
        }

    }

    /**
     * Inicia manualmente la sincronización
     *
     * @param context    Contexto para crear la petición de sincronización
     * @param onlyUpload Usa true para sincronizar el servidor o false para sincronizar el cliente
     */
    public static void sincronizarAhora(Context context, boolean onlyUpload) {
        Log.i(TAG, "Realizando petición de sincronización manual.");
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        if (onlyUpload)
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, true);
        ContentResolver.requestSync(obtenerCuentaASincronizar(context),
                context.getString(R.string.provider_authority), bundle);
    }

    /**
     * Crea u obtiene una cuenta existente
     *
     * @param context Contexto para acceder al administrador de cuentas
     * @return cuenta auxiliar.
     */
    public static Account obtenerCuentaASincronizar(Context context) {
        // Obtener instancia del administrador de cuentas
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Crear cuenta por defecto
        Account newAccount = new Account(
                context.getString(R.string.app_name),constantes.ACCOUNT_TYPE);

        // Comprobar existencia de la cuenta
        if (null == accountManager.getPassword(newAccount)) {

            // Añadir la cuenta al account manager sin password y sin datos de usuario
            if (!accountManager.addAccountExplicitly(newAccount, "", null))
                return null;

        }
        Log.i(TAG, "Cuenta de usuario obtenida.");
        return newAccount;
    }
}


