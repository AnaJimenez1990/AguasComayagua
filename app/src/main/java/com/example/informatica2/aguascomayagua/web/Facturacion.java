package com.example.informatica2.aguascomayagua.web;

import com.example.informatica2.aguascomayagua.provider.facturacion;

/**
 * Created by Informatica 2 on 24/5/2017.
 */

public class Facturacion extends com.example.informatica2.aguascomayagua.provider.facturacion {
    public String idfacturacion ;
    public int valor ;
    public int codigo ;
    public String fecha;
    public String vence;
    public String nombre;
    public String decripcion ;
    public String usuario;
    public String periodo;
    public String tipofa ;
    public int clave ;
    public String abonado;
    public String direccion;

    public Facturacion(String idfacturacion, int  valor, int codigo, String fecha, String vence, String nombre,
                      String descripcion, String usuario, String periodo, String tipofa, int clave, String abonado, String direccion) {
        this.idfacturacion = idfacturacion;
        this.valor = valor;
        this.codigo = codigo;
        this.fecha = fecha;
        this.vence = vence;
        this.nombre = nombre;
        this.decripcion = descripcion;
        this.usuario = usuario;
        this.periodo = periodo;
        this.tipofa= tipofa;
        this.clave= clave;
        this.abonado= abonado;
        this.direccion=direccion;

    }


}

