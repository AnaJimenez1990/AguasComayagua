package com.example.informatica2.aguascomayagua.utils;

/**
 * Created by Informatica 2 on 24/5/2017.
 */

public class constantes {
    /**
     * Puerto que utilizas para la conexión.
     * Dejalo en blanco si no has configurado esta característica.
     */
    private static final String PUERTO_HOST = ":8080";

    /**
     * Dirección IP de genymotion o AVD
     */
    private static final String IP = "http://10.0.3.2";

    /**
     * URLs del Web Service
     */
    public static final String GET_URL = IP + PUERTO_HOST + "aguascomayagua&table=facturacion&token=080efa529ae4a6cf5980370bf18c9b93&pos=0/web/obter_facturacion.php";
    public static final String INSERT_URL = IP + PUERTO_HOST + "/aguascomayagua&table=facturacion&token=080efa529ae4a6cf5980370bf18c9b93&pos=0s/web/insertar_facturacion.php";

    /**
     * Campos de las respuestas Json
     */
    public static final String ID_FACTURACION = "idfacturacion";
    public static final String ESTADO = "estado";
    public static final String VALOR = "valor";
    public static final String MENSAJE = "mensaje";

    /**
     * Códigos del campo {@link }
     */
    public static final String SUCCESS = "1";
    public static final String FAILED = "2";

    /**
     * Tipo de cuenta para la sincronización
     */
    public static final String ACCOUNT_TYPE = "com.example.informatica2.aguascomayagua";

}
