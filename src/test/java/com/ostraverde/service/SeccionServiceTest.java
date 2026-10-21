package com.ostraverde.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.ostraverde.model.SeccionProduccion;

public class SeccionServiceTest {

    // CP-001: Registro de sección con datos válidos
    @Test
    void registrarSeccionConDatosValidos() {

        // ARRANGE: Preparar los datos
        SeccionService servicio = new SeccionService();

        String codigo = "SEC-001";
        String nombre = "Sección Norte";
        int capacidad = 500;
        String estado = "Disponible";

        // ACT: Ejecutar la funcionalidad
        SeccionProduccion seccion = servicio.registrar(
                codigo, nombre, capacidad, estado
        );

        // ASSERT: Verificar los resultados
        assertNotNull(seccion);
        assertEquals("SEC-001", seccion.getCodigo());
        assertEquals("Sección Norte", seccion.getNombre());
        assertEquals(500, seccion.getCapacidadMaxima());
        assertEquals("Disponible", seccion.getEstado());
    }

    // CP-002: Registro de sección con código duplicado
    @Test
    void registrarSeccionConCodigoDuplicado() {

        // ARRANGE: Preparar los datos
        SeccionService servicio = new SeccionService();

        servicio.registrar(
                "SEC-001",
                "Sección Norte",
                500,
                "Disponible"
        );

        // ACT: Intentar registrar una sección duplicada
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> servicio.registrar(
                        "SEC-001",
                        "Sección Sur",
                        300,
                        "Disponible"
                )
        );

        // ASSERT: Verificar el mensaje de error
        assertEquals(
                "El código de la sección ya existe",
                error.getMessage()
        );
    }

    // CP-002: Escenario B - Registro con nombre vacío
    @Test
    void registrarSeccionConNombreVacio() {

        // ARRANGE: Preparar los datos
        SeccionService servicio = new SeccionService();

        String codigo = "SEC-002";
        String nombre = "";
        int capacidad = 300;
        String estado = "Disponible";

        // ACT: Intentar registrar sin nombre
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> servicio.registrar(
                        codigo, nombre, capacidad, estado
                )
        );

        // ASSERT: Verificar el mensaje
        assertEquals(
                "El nombre de la sección es obligatorio",
                error.getMessage()
        );
    }

    // CP-003: Modificación de información de una sección
    @Test
    void modificarInformacionDeSeccion() {

        // ARRANGE: Preparar los datos
        SeccionService servicio = new SeccionService();

        servicio.registrar(
                "SEC-001",
                "Sección Norte",
                500,
                "Disponible"
        );

        // ACT: Modificar la sección existente
        SeccionProduccion seccion = servicio.modificar(
                "SEC-001",
                "Sección Principal",
                1,
                "Disponible"
        );

        // ASSERT: Verificar los resultados
        assertNotNull(seccion);
        assertEquals("SEC-001", seccion.getCodigo());
        assertEquals("Sección Principal", seccion.getNombre());
        assertEquals(1, seccion.getCapacidadMaxima());
        assertEquals("Disponible", seccion.getEstado());
    }
    

    // CP-004: Escenario A - Consulta de secciones disponibles
    @Test
    void consultarSeccionesDisponibles() {

        // ARRANGE: Preparar las secciones
        SeccionService servicio = new SeccionService();

        servicio.registrar(
                "SEC-001", "Sección Norte", 500, "Disponible"
        );

        servicio.registrar(
                "SEC-002", "Sección Sur", 300, "Ocupada"
        );

        servicio.registrar(
                "SEC-003", "Sección Este", 200, "En mantenimiento"
        );

        // ACT: Consultar secciones disponibles
        List<SeccionProduccion> disponibles =
                servicio.consultarDisponibles();

        // ASSERT: Verificar los resultados
        assertNotNull(disponibles);
        assertEquals(1, disponibles.size());
        assertEquals("SEC-001", disponibles.get(0).getCodigo());
        assertEquals("Disponible", disponibles.get(0).getEstado());
    }

    // CP-004: Escenario B - Sin secciones disponibles
    @Test
    void consultarSinSeccionesDisponibles() {

        // ARRANGE: Preparar las secciones
        SeccionService servicio = new SeccionService();

        servicio.registrar(
                "SEC-001", "Sección Norte", 500, "Ocupada"
        );

        servicio.registrar(
                "SEC-002", "Sección Sur", 300, "En mantenimiento"
        );

        // ACT: Consultar secciones disponibles
        List<SeccionProduccion> disponibles =
                servicio.consultarDisponibles();

        // ASSERT: Verificar que la lista esté vacía
        assertNotNull(disponibles);
        assertTrue(disponibles.isEmpty());
    }

}