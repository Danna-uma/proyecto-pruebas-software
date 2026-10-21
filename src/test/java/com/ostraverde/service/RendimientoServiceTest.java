
package com.ostraverde.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class RendimientoServiceTest {

    // CP-033: Tiempo de respuesta al consultar secciones
    @Test
    void medirTiempoConsultaSeccionesDisponibles() {

        // ARRANGE: Preparar los datos
        SeccionService servicio = new SeccionService();

        servicio.registrar(
                "SEC-033",
                "Sección de rendimiento",
                500,
                "Disponible"
        );

        long limiteNanosegundos = 3_000_000_000L;

        // ACT: Ejecutar la consulta diez veces
        for (int i = 1; i <= 10; i++) {

            long inicio = System.nanoTime();

            servicio.consultarDisponibles();

            long fin = System.nanoTime();

            long tiempo = fin - inicio;

            double milisegundos = tiempo / 1_000_000.0;

            // Mostrar el resultado de cada ejecución
            System.out.println(
                    "CP-033 | Consulta de secciones | "
                    + "Ejecución " + i
                    + " | Tiempo: " + milisegundos + " ms"
            );

            // ASSERT: Verificar el límite de 3 segundos
            assertTrue(
                    tiempo <= limiteNanosegundos,
                    "La consulta superó el límite de 3 segundos"
            );
        }
    }

    // CP-033: Tiempo de respuesta al consultar trabajadores
    @Test
    void medirTiempoConsultaTrabajadores() {

        // ARRANGE: Preparar los datos
        TrabajadorService servicio =
                new TrabajadorService();

        servicio.registrar(
                "101110111",
                "Carlos Rodríguez Mora",
                "88881234",
                "carlos@email.com",
                "Encargado de producción"
        );

        long limiteNanosegundos = 3_000_000_000L;

        // ACT: Ejecutar la consulta diez veces
        for (int i = 1; i <= 10; i++) {

            long inicio = System.nanoTime();

            servicio.consultar("101110111");

            long fin = System.nanoTime();

            long tiempo = fin - inicio;

            double milisegundos =
                    tiempo / 1_000_000.0;

            // Mostrar el tiempo de cada ejecución
            System.out.println(
                    "CP-033 | Consulta de trabajadores | "
                    + "Ejecución " + i
                    + " | Tiempo: " + milisegundos + " ms"
            );

            // ASSERT: Verificar el límite de 3 segundos
            assertTrue(
                    tiempo <= limiteNanosegundos,
                    "La consulta de trabajadores superó "
                    + "el límite de 3 segundos"
            );
        }
    }

    // CP-033: Tiempo de respuesta al consultar productos
    @Test
    void medirTiempoConsultaProductosAgricolas() {

        // ARRANGE: Preparar los datos
        ProductoService servicio =
                new ProductoService();

        servicio.registrar(
                "PROD-033",
                "Fertilizante Orgánico",
                "Fertilizante",
                "Producto para el cultivo",
                "Litros"
        );

        long limiteNanosegundos = 3_000_000_000L;

        // ACT: Ejecutar la consulta diez veces
        for (int i = 1; i <= 10; i++) {

            long inicio = System.nanoTime();

            servicio.consultar("PROD-033");

            long fin = System.nanoTime();

            long tiempo = fin - inicio;

            double milisegundos =
                    tiempo / 1_000_000.0;

            // Mostrar el tiempo de cada ejecución
            System.out.println(
                    "CP-033 | Consulta de productos | "
                    + "Ejecución " + i
                    + " | Tiempo: " + milisegundos + " ms"
            );

            // ASSERT: Verificar el límite de 3 segundos
            assertTrue(
                    tiempo <= limiteNanosegundos,
                    "La consulta de productos superó "
                    + "el límite de 3 segundos"
            );
        }
    }


}
