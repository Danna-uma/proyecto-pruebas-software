
package com.ostraverde.model;

import java.time.LocalDate;

public class Siembra {

    private final String codigo;
    private final LocalDate fecha;
    private final String codigoSeccion;
    private final int cantidadSustratos;
    private final String responsable;
    private final String observaciones;

    public Siembra(
            String codigo,
            LocalDate fecha,
            String codigoSeccion,
            int cantidadSustratos,
            String responsable,
            String observaciones) {

        this.codigo = codigo;
        this.fecha = fecha;
        this.codigoSeccion = codigoSeccion;
        this.cantidadSustratos = cantidadSustratos;
        this.responsable = responsable;
        this.observaciones = observaciones;
    }

    public String getCodigo() {
        return codigo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getCodigoSeccion() {
        return codigoSeccion;
    }

    public int getCantidadSustratos() {
        return cantidadSustratos;
    }

    public String getResponsable() {
        return responsable;
    }

    public String getObservaciones() {
        return observaciones;
    }
}
