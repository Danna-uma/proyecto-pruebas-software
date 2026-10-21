
package com.ostraverde.model;

public class ProductoAgricola {

    private final String codigo;
    private final String nombre;
    private final String tipo;
    private final String descripcion;
    private final String unidadMedida;

    public ProductoAgricola(
            String codigo,
            String nombre,
            String tipo,
            String descripcion,
            String unidadMedida) {

        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.unidadMedida = unidadMedida;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }
}
