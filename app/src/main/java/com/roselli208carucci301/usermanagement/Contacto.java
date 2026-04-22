package com.roselli208carucci301.usermanagement;

import java.io.Serializable;
public class Contacto implements Serializable {

    private String nombre;
    private String apellido;
    private String telefono;
    private String domicilio;
    private String genero;

    public Contacto(String nombre, String apellido, String telefono, String domicilio, String genero) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.domicilio = domicilio;
        this.genero = genero;
    }
}