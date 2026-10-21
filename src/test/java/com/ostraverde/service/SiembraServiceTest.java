
package com.ostraverde.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.ostraverde.model.SeccionProduccion;
import com.ostraverde.model.Siembra;

public class SiembraServiceTest {

    // CP-008: Registro exitoso de una siembra
    @Test
    void registrarSiembraEnSeccionDisponible() {

        // ARRANGE: Preparar los datos
        SeccionService seccionService = new SeccionService();

        seccionService.registrar(
                "SEC-001",
                "Sección Norte",
                500,
                "Disponible"
        );

        TrabajadorService trabajadorService =
                new TrabajadorService();

        trabajadorService.registrar(
                "101110111",
                "Carlos Rodríguez Mora",
                "88881234",
                "carlos@email.com",
                "Encargado de siembra"
        );

        SiembraService servicio = new SiembraService(
                seccionService,
                trabajadorService
        );

        LocalDate fecha = LocalDate.of(2026, 10, 10);

        // ACT: Registrar la siembra
        Siembra siembra = servicio.registrar(
                "SIEM-001",
                fecha,
                "SEC-001",
                100,
                "101110111",
                "Siembra inicial"
        );

        // ASSERT: Verificar los datos
        assertNotNull(siembra);
        assertEquals("SIEM-001", siembra.getCodigo());
        assertEquals(fecha, siembra.getFecha());
        assertEquals("SEC-001", siembra.getCodigoSeccion());
        assertEquals(100, siembra.getCantidadSustratos());
        assertEquals("101110111", siembra.getResponsable());
        assertEquals("Siembra inicial", siembra.getObservaciones());

        // Verificar que la sección quedó ocupada
        SeccionProduccion seccion =
                seccionService.consultarPorCodigo("SEC-001");

        assertEquals("Ocupada", seccion.getEstado());
    }

    // CP-009: Registro de siembra en sección ocupada
    @Test
    void registrarSiembraEnSeccionOcupada() {

        // ARRANGE: Preparar los datos
        SeccionService seccionService = new SeccionService();

        seccionService.registrar(
                "SEC-002",
                "Sección Sur",
                300,
                "Ocupada"
        );

        TrabajadorService trabajadorService =
                new TrabajadorService();

        trabajadorService.registrar(
                "101110111",
                "Carlos Rodríguez Mora",
                "88881234",
                "carlos@email.com",
                "Encargado de siembra"
        );

        SiembraService servicio = new SiembraService(
                seccionService,
                trabajadorService
        );

        LocalDate fecha = LocalDate.of(2026, 10, 10);

        // ACT: Intentar registrar en sección ocupada
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> servicio.registrar(
                        "SIEM-002",
                        fecha,
                        "SEC-002",
                        150,
                        "101110111",
                        "Intento de siembra en sección ocupada"
                )
        );

        // ASSERT: Verificar el mensaje de error
        assertEquals(
                "La sección no está disponible",
                error.getMessage()
        );

        // Verificar que la sección continúa ocupada
        SeccionProduccion seccion =
                seccionService.consultarPorCodigo("SEC-002");

        assertEquals("Ocupada", seccion.getEstado());
    }
    

    // CP-010: Registro de siembra en sección en mantenimiento
    @Test
    void registrarSiembraEnSeccionEnMantenimiento() {

        // ARRANGE: Preparar los datos
        SeccionService seccionService = new SeccionService();

        seccionService.registrar(
                "SEC-003",
                "Sección Este",
                200,
                "En mantenimiento"
        );

        TrabajadorService trabajadorService =
                new TrabajadorService();

        trabajadorService.registrar(
                "101110111",
                "Carlos Rodríguez Mora",
                "88881234",
                "carlos@email.com",
                "Encargado de siembra"
        );

        SiembraService servicio = new SiembraService(
                seccionService,
                trabajadorService
        );

        LocalDate fecha = LocalDate.of(2026, 10, 10);

        // ACT: Intentar registrar en sección en mantenimiento
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> servicio.registrar(
                        "SIEM-003",
                        fecha,
                        "SEC-003",
                        100,
                        "101110111",
                        "Intento de siembra en mantenimiento"
                )
        );

        // ASSERT: Verificar el mensaje de error
        assertEquals(
                "La sección no está disponible",
                error.getMessage()
        );

        // Comprobar que la sección sigue en mantenimiento
        SeccionProduccion seccion =
                seccionService.consultarPorCodigo("SEC-003");

        assertEquals("En mantenimiento", seccion.getEstado());
    }
    

    // CP-011: Registro de siembra con fecha futura
    @Test
    void registrarSiembraConFechaFutura() {

        // ARRANGE: Preparar los datos
        SeccionService seccionService = new SeccionService();

        seccionService.registrar(
                "SEC-004",
                "Sección Oeste",
                400,
                "Disponible"
        );

        TrabajadorService trabajadorService =
                new TrabajadorService();

        trabajadorService.registrar(
                "101110111",
                "Carlos Rodríguez Mora",
                "88881234",
                "carlos@email.com",
                "Encargado de siembra"
        );

        SiembraService servicio = new SiembraService(
                seccionService,
                trabajadorService
        );

        LocalDate fechaFutura = LocalDate.now().plusDays(1);

        // ACT: Intentar registrar una fecha futura
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> servicio.registrar(
                        "SIEM-004",
                        fechaFutura,
                        "SEC-004",
                        100,
                        "101110111",
                        "Intento de siembra con fecha futura"
                )
        );

        // ASSERT: Verificar el mensaje de error
        assertEquals(
                "No se permiten fechas futuras",
                error.getMessage()
        );

        // Comprobar que la sección continúa disponible
        SeccionProduccion seccion =
                seccionService.consultarPorCodigo("SEC-004");

        assertEquals("Disponible", seccion.getEstado());
    }
    

    // CP-012: Registro de siembra con fecha actual
    @Test
    void registrarSiembraConFechaActual() {

        // ARRANGE: Preparar los datos
        SeccionService seccionService = new SeccionService();

        seccionService.registrar(
                "SEC-005",
                "Sección Central",
                500,
                "Disponible"
        );

        TrabajadorService trabajadorService =
                new TrabajadorService();

        trabajadorService.registrar(
                "101110111",
                "Carlos Rodríguez Mora",
                "88881234",
                "carlos@email.com",
                "Encargado de siembra"
        );

        SiembraService servicio = new SiembraService(
                seccionService,
                trabajadorService
        );

        LocalDate fechaActual = LocalDate.now();

        // ACT: Registrar la siembra con fecha actual
        Siembra siembra = servicio.registrar(
                "SIEM-005",
                fechaActual,
                "SEC-005",
                100,
                "101110111",
                "Siembra con fecha actual"
        );

        // ASSERT: Verificar los resultados
        assertNotNull(siembra);
        assertEquals("SIEM-005", siembra.getCodigo());
        assertEquals(fechaActual, siembra.getFecha());
        assertEquals(100, siembra.getCantidadSustratos());

        // Comprobar que la sección quedó ocupada
        SeccionProduccion seccion =
                seccionService.consultarPorCodigo("SEC-005");

        assertEquals("Ocupada", seccion.getEstado());
    }
    

    // CP-013: Escenario A - Cantidad de sustratos igual a cero
    @Test
    void registrarSiembraConCeroSustratos() {

        // ARRANGE: Preparar los datos
        SeccionService seccionService = new SeccionService();

        seccionService.registrar(
                "SEC-006",
                "Sección Norte",
                500,
                "Disponible"
        );

        TrabajadorService trabajadorService =
                new TrabajadorService();

        trabajadorService.registrar(
                "101110111",
                "Carlos Rodríguez Mora",
                "88881234",
                "carlos@email.com",
                "Encargado de siembra"
        );

        SiembraService servicio = new SiembraService(
                seccionService,
                trabajadorService
        );

        LocalDate fecha = LocalDate.now();

        // ACT: Intentar registrar con cero sustratos
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> servicio.registrar(
                        "SIEM-006",
                        fecha,
                        "SEC-006",
                        0,
                        "101110111",
                        "Siembra con cantidad inválida"
                )
        );

        // ASSERT: Verificar el mensaje de error
        assertEquals(
                "La cantidad de sustratos debe ser mayor que cero",
                error.getMessage()
        );

        // Verificar que la sección continúa disponible
        SeccionProduccion seccion =
                seccionService.consultarPorCodigo("SEC-006");

        assertEquals("Disponible", seccion.getEstado());
    }

    // CP-013: Escenario B - Cantidad mínima válida
    @Test
    void registrarSiembraConUnSustrato() {

        // ARRANGE: Preparar los datos
        SeccionService seccionService = new SeccionService();

        seccionService.registrar(
                "SEC-007",
                "Sección Sur",
                300,
                "Disponible"
        );

        TrabajadorService trabajadorService =
                new TrabajadorService();

        trabajadorService.registrar(
                "101110111",
                "Carlos Rodríguez Mora",
                "88881234",
                "carlos@email.com",
                "Encargado de siembra"
        );

        SiembraService servicio = new SiembraService(
                seccionService,
                trabajadorService
        );

        LocalDate fecha = LocalDate.now();

        // ACT: Registrar con un sustrato
        Siembra siembra = servicio.registrar(
                "SIEM-007",
                fecha,
                "SEC-007",
                1,
                "101110111",
                "Siembra con cantidad mínima"
        );

        // ASSERT: Verificar el resultado
        assertNotNull(siembra);
        assertEquals(1, siembra.getCantidadSustratos());

        // Verificar que la sección quedó ocupada
        SeccionProduccion seccion =
                seccionService.consultarPorCodigo("SEC-007");

        assertEquals("Ocupada", seccion.getEstado());
    }




}
