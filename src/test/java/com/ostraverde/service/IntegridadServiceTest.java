
package com.ostraverde.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.ostraverde.model.SeccionProduccion;

import java.time.LocalDate;

public class IntegridadServiceTest {

    // CP-034: Escenario A - Integridad ante código duplicado
    @Test
    void verificarIntegridadConSeccionDuplicada() {

        // ARRANGE: Preparar el servicio
        SeccionService servicio = new SeccionService();

        servicio.registrar(
                "SEC-034",
                "Sección Original",
                500,
                "Disponible"
        );

        // ACT: Intentar registrar otra sección
        // utilizando el mismo código
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> servicio.registrar(
                        "SEC-034",
                        "Sección Duplicada",
                        300,
                        "Disponible"
                )
        );

        // ASSERT: Verificar el mensaje de error
        assertEquals(
                "El código de la sección ya existe",
                error.getMessage()
        );

        // Comprobar que se conserva el registro original
        SeccionProduccion seccion =
                servicio.consultarPorCodigo("SEC-034");

        assertNotNull(seccion);

        assertEquals(
                "Sección Original",
                seccion.getNombre()
        );

        assertEquals(
                500,
                seccion.getCapacidadMaxima()
        );

        assertEquals(
                "Disponible",
                seccion.getEstado()
        );
    }

    // CP-034: Escenario B - Integridad ante producto inexistente
    @Test
    void verificarIntegridadConProductoInexistente() {

        // ARRANGE: Preparar los servicios
        ProductoService productoService =
                new ProductoService();

        // Registrar un producto válido
        productoService.registrar(
                "PROD-034",
                "Fertilizante Orgánico",
                "Fertilizante",
                "Producto utilizado para el cultivo",
                "Litros"
        );

        AplicacionService servicio =
                new AplicacionService(productoService);

        // ACT: Intentar registrar una aplicación
        // asociada a un producto inexistente
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> servicio.registrar(
                        "APL-034",
                        "PROD-999",
                        LocalDate.of(2026, 10, 10),
                        2.5
                )
        );

        // ASSERT: Verificar el mensaje de error
        assertEquals(
                "El producto no existe",
                error.getMessage()
        );

        // Comprobar que la aplicación inválida
        // no quedó almacenada
        assertThrows(
                IllegalArgumentException.class,
                () -> servicio.consultar("APL-034")
        );

        // Verificar que el producto válido
        // permanece registrado
        assertNotNull(
                productoService.consultar("PROD-034")
        );

        // Comprobar que el producto válido
        // no fue marcado como utilizado
        assertFalse(
                servicio.tieneAplicaciones("PROD-034")
        );
    }

}
