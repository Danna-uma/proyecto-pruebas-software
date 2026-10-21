
package com.ostraverde.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.ostraverde.model.SeccionProduccion;
import com.ostraverde.model.Siembra;

public class SiembraService {

    private final SeccionService seccionService;
    private final TrabajadorService trabajadorService;

    // Almacenar las siembras registradas
    private final List<Siembra> siembras =
            new ArrayList<>();

    public SiembraService(
            SeccionService seccionService,
            TrabajadorService trabajadorService) {

        this.seccionService = seccionService;
        this.trabajadorService = trabajadorService;
    }

    // CP-008: Registrar una nueva siembra
    public Siembra registrar(
            String codigo,
            LocalDate fecha,
            String codigoSeccion,
            int cantidadSustratos,
            String responsable,
            String observaciones) {

        // CP-011: Validar la fecha de la siembra
        validarFecha(fecha);

        // CP-013: Validar la cantidad de sustratos
        validarCantidadSustratos(cantidadSustratos);

        // Consultar la sección de producción
        SeccionProduccion seccion =
                seccionService.consultarPorCodigo(codigoSeccion);

        // CP-009: Verificar que la sección no esté ocupada
        // CP-010: Verificar que no esté en mantenimiento
        validarSeccionDisponible(seccion);

        // Verificar que el trabajador responsable exista
        trabajadorService.consultar(responsable);

        // Crear la nueva siembra
        Siembra nuevaSiembra = new Siembra(
                codigo,
                fecha,
                codigoSeccion,
                cantidadSustratos,
                responsable,
                observaciones
        );

        // Cambiar el estado de la sección a Ocupada
        seccionService.modificar(
                seccion.getCodigo(),
                seccion.getNombre(),
                seccion.getCapacidadMaxima(),
                "Ocupada"
        );

        // Guardar la siembra registrada
        siembras.add(nuevaSiembra);

        return nuevaSiembra;
    }

    // CP-009 y CP-010: Validar disponibilidad de la sección
    private void validarSeccionDisponible(
            SeccionProduccion seccion) {

        // Solamente se permiten secciones disponibles
        if (!"Disponible".equals(seccion.getEstado())) {

            throw new IllegalArgumentException(
                    "La sección no está disponible"
            );
        }
    }

    // CP-011: Validar que la fecha no sea futura
    private void validarFecha(LocalDate fecha) {

        if (fecha.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "No se permiten fechas futuras"
            );
        }
    }

    // CP-013: Validar que la cantidad sea mayor que cero
    private void validarCantidadSustratos(int cantidadSustratos) {

        if (cantidadSustratos <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad de sustratos debe ser mayor que cero"
            );
        }
    }

    // CP-020: Consultar una siembra mediante su código
    public Siembra consultar(String codigo) {

        // Recorrer las siembras registradas
        for (Siembra siembra : siembras) {

            if (siembra.getCodigo().equals(codigo)) {
                return siembra;
            }
        }

        // Informar si la siembra no existe
        throw new IllegalArgumentException(
                "La siembra no existe"
        );
    }

    // CP-025: Liberar la sección después de una cosecha
    public void liberarSeccion(String codigoSiembra) {

        // Consultar la siembra registrada
        Siembra siembra = consultar(codigoSiembra);

        // Consultar la sección asociada
        SeccionProduccion seccion =
                seccionService.consultarPorCodigo(
                        siembra.getCodigoSeccion()
                );

        // Actualizar la sección a Disponible
        seccionService.modificar(
                seccion.getCodigo(),
                seccion.getNombre(),
                seccion.getCapacidadMaxima(),
                "Disponible"
        );
    }

}
