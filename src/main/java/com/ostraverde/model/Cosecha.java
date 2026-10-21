
package com.ostraverde.model;

import java.time.LocalDate;

public class Cosecha {

    private final String codigo;
    private final String codigoSiembra;
    private final LocalDate fecha;
    private final double cantidadKilogramos;
    private final int cantidadBandejas;
    private final String responsable;

    // Constructor original para conservar las pruebas anteriores
    public Cosecha(
            String codigo,
            String codigoSiembra,
            LocalDate fecha,
            double cantidadKilogramos) {

        this(
                codigo,
                codigoSiembra,
                fecha,
                cantidadKilogramos,
                0,
                null
        );
    }

    // CP-021: Constructor completo para registrar cosechas
    public Cosecha(
            String codigo,
            String codigoSiembra,
            LocalDate fecha,
            double cantidadKilogramos,
            int cantidadBandejas,
            String responsable) {

        this.codigo = codigo;
        this.codigoSiembra = codigoSiembra;
        this.fecha = fecha;
        this.cantidadKilogramos = cantidadKilogramos;
        this.cantidadBandejas = cantidadBandejas;
        this.responsable = responsable;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getCodigoSiembra() {
        return codigoSiembra;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public double getCantidadKilogramos() {
        return cantidadKilogramos;
    }

    public int getCantidadBandejas() {
        return cantidadBandejas;
    }

    public String getResponsable() {
        return responsable;
    }
}
