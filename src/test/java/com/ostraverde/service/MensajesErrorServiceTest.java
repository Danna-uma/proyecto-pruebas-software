
package com.ostraverde.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

public class MensajesErrorServiceTest {

    // CP-035: Escenario A - Mensaje por código duplicado
    @Test
    void verificarMensajeSeccionDuplicada() {

        // ARRANGE: Preparar el servicio
        SeccionService servicio = new SeccionService();

        servicio.registrar(
                "SEC-035",
                "Sección Original",
                500,
                "Disponible"
        );

        // ACT: Intentar registrar una sección duplicada
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> servicio.registrar(
                        "SEC-035",
                        "Sección Duplicada",
                        300,
                        "Disponible"
                )
        );

        // ASSERT: Verificar que el mensaje sea específico
        assertEquals(
                "El código de la sección ya existe",
                error.getMessage()
        );

        // Mostrar el mensaje en Console
        System.out.println(
                "CP-035 | Escenario A | Mensaje: "
                + error.getMessage()
        );
    }

    // CP-035: Escenario B - Mensaje por cero sustratos
    @Test
    void verificarMensajeSiembraConCeroSustratos() {

        // ARRANGE: Preparar la sección
        SeccionService seccionService =
                new SeccionService();

        seccionService.registrar(
                "SEC-035-B",
                "Sección para prueba de siembra",
                500,
                "Disponible"
        );

        // Preparar el trabajador
        TrabajadorService trabajadorService =
                new TrabajadorService();

        trabajadorService.registrar(
                "101110111",
                "Carlos Rodríguez Mora",
                "88881234",
                "carlos@email.com",
                "Encargado de siembra"
        );

        // Preparar el servicio de siembras
        SiembraService servicio =
                new SiembraService(
                        seccionService,
                        trabajadorService
                );

        // ACT: Intentar registrar cero sustratos
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> servicio.registrar(
                        "SIEM-035",
                        LocalDate.of(2026, 10, 10),
                        "SEC-035-B",
                        0,
                        "101110111",
                        "Siembra con cantidad inválida"
                )
        );

        // ASSERT: Verificar que el mensaje sea claro
        assertEquals(
                "La cantidad de sustratos debe ser mayor que cero",
                error.getMessage()
        );

        // Mostrar el mensaje en Console
        System.out.println(
                "CP-035 | Escenario B | Mensaje: "
                + error.getMessage()
        );
    }

}
