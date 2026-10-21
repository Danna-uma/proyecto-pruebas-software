
package com.ostraverde.service;

import java.util.ArrayList;
import java.util.List;

import com.ostraverde.model.SeccionProduccion;

public class SeccionService {

    private final List<SeccionProduccion> secciones =
            new ArrayList<>();

    // CP-001: Registrar una sección
    public SeccionProduccion registrar(
            String codigo,
            String nombre,
            int capacidad,
            String estado) {

        validarNombre(nombre);

        if (existeCodigo(codigo)) {
            throw new IllegalArgumentException(
                    "El código de la sección ya existe"
            );
        }

        SeccionProduccion nuevaSeccion =
                new SeccionProduccion(
                        codigo,
                        nombre,
                        capacidad,
                        estado
                );

        secciones.add(nuevaSeccion);

        return nuevaSeccion;
    }

    // CP-003: Modificar una sección existente
    public SeccionProduccion modificar(
            String codigo,
            String nombre,
            int capacidad,
            String estado) {

        int indice = buscarIndice(codigo);

        if (indice == -1) {
            throw new IllegalArgumentException(
                    "La sección no existe"
            );
        }

        validarNombre(nombre);

        SeccionProduccion seccionActualizada =
                new SeccionProduccion(
                        codigo,
                        nombre,
                        capacidad,
                        estado
                );

        secciones.set(indice, seccionActualizada);

        return seccionActualizada;
    }

    // Validar que el nombre no esté vacío
    private void validarNombre(String nombre) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre de la sección es obligatorio"
            );
        }
    }

    // Buscar el índice de una sección por su código
    private int buscarIndice(String codigo) {

        for (int i = 0; i < secciones.size(); i++) {
            if (secciones.get(i).getCodigo().equals(codigo)) {
                return i;
            }
        }

        return -1;
    }

    // Verificar si existe un código registrado
    private boolean existeCodigo(String codigo) {
        return buscarIndice(codigo) != -1;
    }
    



    // CP-004: Consultar secciones disponibles (REFACTOR)
    public List<SeccionProduccion> consultarDisponibles() {

        return secciones.stream()
                .filter(seccion ->
                        "Disponible".equals(seccion.getEstado()))
                .toList();
    }

    // Consultar una sección por su código
    public SeccionProduccion consultarPorCodigo(String codigo) {

        int indice = buscarIndice(codigo);

        if (indice == -1) {
            throw new IllegalArgumentException(
                    "La sección no existe"
            );
        }

        return secciones.get(indice);
    }




}
