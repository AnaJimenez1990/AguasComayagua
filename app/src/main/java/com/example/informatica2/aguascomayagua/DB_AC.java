package com.example.informatica2.aguascomayagua;

/**
 * Created by Informatica 2 on 18/5/2017.
 */

public class DB_AC {

  public  static  final  String TABLE_NAME = "usuario";

    public  static final  String CN_Codigo = "codigo";
    public  static final  String CN_CONTRASENA = "password";
    public  static final  String CN_NOMBRE = "nombre";

// create tabla de usuarios
    //codigo java
    public static final String CREATE_TABLE = "create table" + TABLE_NAME+ "("
            + CN_Codigo + " integer primary key,"
            + CN_NOMBRE + "Tex not null,"
            + CN_CONTRASENA + "Tex not null);";

    public  static  final  String TABLE = "usuarios";

    public  static final  String CN_DIRECCION = "direccion";
    public  static final  String CN_NOM = "nombre";
    public  static final  String CN_LECTURAAC = "lectura actual";
    public  static final  String CN_LECTURAAN = "lectura anterior";



}
