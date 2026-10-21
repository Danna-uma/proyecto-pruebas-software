
package com.ostraverde.model;

public class SeccionProduccion {

    private String codigo;
    private String nombre;
    private int capacidadMaxima;
    private String estado;

    public SeccionProduccion(String codigo, String nombre,
                            int capacidadMaxima, String estado) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.capacidadMaxima = capacidadMaxima;
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public String getEstado() {
        return estado;
    }
}
