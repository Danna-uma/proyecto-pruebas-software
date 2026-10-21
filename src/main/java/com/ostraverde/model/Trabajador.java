
package com.ostraverde.model;

public class Trabajador {

    private String identificacion;
    private String nombre;
    private String telefono;
    private String correo;
    private String cargo;

    public Trabajador(
            String identificacion,
            String nombre,
            String telefono,
            String correo,
            String cargo) {

        this.identificacion = identificacion;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.cargo = cargo;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public String getCargo() {
        return cargo;
    }
}
