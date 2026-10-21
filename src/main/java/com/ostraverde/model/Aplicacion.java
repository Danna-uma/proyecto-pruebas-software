
package com.ostraverde.model;

import java.time.LocalDate;

public class Aplicacion {

    private final String codigo;
    private final String codigoProducto;
    private final LocalDate fecha;
    private final double cantidad;

    private final String codigoSiembra;
    private final String responsable;
    private final String observaciones;

    // Constructor original
    public Aplicacion(
            String codigo,
            String codigoProducto,
            LocalDate fecha,
            double cantidad) {

        this(
                codigo,
                fecha,
                null,
                codigoProducto,
                cantidad,
                null,
                null
        );
    }

    // Constructor completo para aplicaciones agrícolas
    public Aplicacion(
            String codigo,
            LocalDate fecha,
            String codigoSiembra,
            String codigoProducto,
            double cantidad,
            String responsable,
            String observaciones) {

        this.codigo = codigo;
        this.fecha = fecha;
        this.codigoSiembra = codigoSiembra;
        this.codigoProducto = codigoProducto;
        this.cantidad = cantidad;
        this.responsable = responsable;
        this.observaciones = observaciones;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public double getCantidad() {
        return cantidad;
    }

    public String getCodigoSiembra() {
        return codigoSiembra;
    }

    public String getResponsable() {
        return responsable;
    }

    public String getObservaciones() {
        return observaciones;
    }
}
