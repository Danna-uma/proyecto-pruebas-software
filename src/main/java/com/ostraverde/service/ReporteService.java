
package com.ostraverde.service;

import java.time.LocalDate;

import com.ostraverde.model.Cosecha;
import com.ostraverde.model.Siembra;

public class ReporteService {

    private final CosechaService cosechaService;
    private final SiembraService siembraService;

    // Constructor original para conservar CP-026
    public ReporteService(CosechaService cosechaService) {
        this.cosechaService = cosechaService;
        this.siembraService = null;
    }

    // Constructor para reportes relacionados con siembras
    public ReporteService(
            CosechaService cosechaService,
            SiembraService siembraService) {

        this.cosechaService = cosechaService;
        this.siembraService = siembraService;
    }

    // CP-026: Calcular producción total por período
    public double calcularProduccionTotal(
            LocalDate fechaInicio,
            LocalDate fechaFin) {

        // Acumular los kilogramos cosechados
        double total = 0.0;

        // Recorrer las cosechas registradas
        for (Cosecha cosecha : cosechaService.listar()) {

            LocalDate fechaCosecha = cosecha.getFecha();

            // Verificar si pertenece al período
            boolean dentroDelPeriodo =
                    estaDentroDelPeriodo(
                            fechaCosecha,
                            fechaInicio,
                            fechaFin
                    );

            // Sumar la producción correspondiente
            if (dentroDelPeriodo) {
                total += cosecha.getCantidadKilogramos();
            }
        }

        return total;
    }

    // CP-027: Calcular producción por sección
    public double calcularProduccionPorSeccion(
            String codigoSeccion) {

        // Verificar que el servicio esté configurado
        if (siembraService == null) {
            throw new IllegalStateException(
                    "Se requiere el servicio de siembras"
            );
        }

        double total = 0.0;

        // Recorrer las cosechas registradas
        for (Cosecha cosecha : cosechaService.listar()) {

            // Verificar si pertenece a la sección solicitada
            if (perteneceASeccion(cosecha, codigoSeccion)) {

                // Sumar los kilogramos cosechados
                total += cosecha.getCantidadKilogramos();
            }
        }

        return total;
    }

    // CP-028: Calcular producción por trabajador
    public double calcularProduccionPorTrabajador(
            String identificacionTrabajador) {

        // Acumular los kilogramos cosechados
        double total = 0.0;

        // Recorrer las cosechas registradas
        for (Cosecha cosecha : cosechaService.listar()) {

            // Verificar si pertenece al trabajador
            if (perteneceATrabajador(
                    cosecha,
                    identificacionTrabajador)) {

                // Sumar la producción del trabajador
                total += cosecha.getCantidadKilogramos();
            }
        }

        return total;
    }

    // CP-030: Calcular rendimiento de un cultivo (REFACTOR)
    public double calcularRendimientoCultivo(
            String codigoSiembra) {

        // Verificar que el servicio de siembras esté configurado
        if (siembraService == null) {
            throw new IllegalStateException(
                    "Se requiere el servicio de siembras"
            );
        }

        // Consultar la siembra registrada
        Siembra siembra = siembraService.consultar(codigoSiembra);

        // Obtener la cantidad de sustratos
        int cantidadSustratos = siembra.getCantidadSustratos();

        // Obtener los kilogramos cosechados de la siembra
        double totalKilogramos =
                obtenerKilogramosPorSiembra(codigoSiembra);

        // Calcular el rendimiento en kg por sustrato
        return totalKilogramos / cantidadSustratos;
    }

    // CP-026: Comprobar si una fecha pertenece al período
    private boolean estaDentroDelPeriodo(
            LocalDate fecha,
            LocalDate fechaInicio,
            LocalDate fechaFin) {

        return !fecha.isBefore(fechaInicio)
                && !fecha.isAfter(fechaFin);
    }

    // CP-027: Verificar si una cosecha pertenece a una sección
    private boolean perteneceASeccion(
            Cosecha cosecha,
            String codigoSeccion) {

        // Consultar la siembra asociada a la cosecha
        Siembra siembra = siembraService.consultar(
                cosecha.getCodigoSiembra()
        );

        // Comparar el código de sección
        return siembra.getCodigoSeccion().equals(codigoSeccion);
    }

    // CP-028: Verificar si una cosecha pertenece al trabajador
    private boolean perteneceATrabajador(
            Cosecha cosecha,
            String identificacionTrabajador) {

        return identificacionTrabajador.equals(
                cosecha.getResponsable()
        );
    }

    // CP-030: Obtener kilogramos cosechados por siembra
    private double obtenerKilogramosPorSiembra(
            String codigoSiembra) {

        double totalKilogramos = 0.0;

        // Recorrer las cosechas registradas
        for (Cosecha cosecha : cosechaService.listar()) {

            // Verificar que pertenezca a la siembra solicitada
            if (codigoSiembra.equals(
                    cosecha.getCodigoSiembra())) {

                // Sumar los kilogramos cosechados
                totalKilogramos += cosecha.getCantidadKilogramos();
            }
        }

        return totalKilogramos;
    }
}
