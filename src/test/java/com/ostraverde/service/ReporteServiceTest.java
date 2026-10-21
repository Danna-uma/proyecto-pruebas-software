
package com.ostraverde.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import java.util.List;
import com.ostraverde.model.Aplicacion;

public class ReporteServiceTest {

    // CP-026: Reporte de producción total por período
    @Test
    void calcularProduccionTotalPorPeriodo() {

        // ARRANGE: Preparar los datos
        SeccionService seccionService =
                new SeccionService();

        TrabajadorService trabajadorService =
                new TrabajadorService();

        trabajadorService.registrar(
                "101110111",
                "Carlos Rodríguez Mora",
                "88881234",
                "carlos@email.com",
                "Encargado de producción"
        );

        SiembraService siembraService =
                new SiembraService(
                        seccionService,
                        trabajadorService
                );

        CosechaService cosechaService =
                new CosechaService(
                        siembraService,
                        trabajadorService
                );

        // Registrar tres secciones disponibles
        seccionService.registrar(
                "SEC-026-A",
                "Sección Norte",
                500,
                "Disponible"
        );

        seccionService.registrar(
                "SEC-026-B",
                "Sección Sur",
                500,
                "Disponible"
        );

        seccionService.registrar(
                "SEC-026-C",
                "Sección Este",
                500,
                "Disponible"
        );

        // Registrar tres siembras
        LocalDate fechaSiembra =
                LocalDate.of(2026, 10, 1);

        siembraService.registrar(
                "SIEM-026-A",
                fechaSiembra,
                "SEC-026-A",
                100,
                "101110111",
                "Primera siembra"
        );

        siembraService.registrar(
                "SIEM-026-B",
                fechaSiembra,
                "SEC-026-B",
                100,
                "101110111",
                "Segunda siembra"
        );

        siembraService.registrar(
                "SIEM-026-C",
                fechaSiembra,
                "SEC-026-C",
                100,
                "101110111",
                "Tercera siembra"
        );

        // Registrar las tres cosechas
        cosechaService.registrar(
                "COS-026-A",
                "SIEM-026-A",
                LocalDate.of(2026, 10, 10),
                50.0,
                20,
                "101110111"
        );

        cosechaService.registrar(
                "COS-026-B",
                "SIEM-026-B",
                LocalDate.of(2026, 10, 12),
                30.0,
                15,
                "101110111"
        );

        cosechaService.registrar(
                "COS-026-C",
                "SIEM-026-C",
                LocalDate.of(2026, 10, 20),
                20.0,
                10,
                "101110111"
        );

        ReporteService servicio =
                new ReporteService(cosechaService);

        LocalDate fechaInicio =
                LocalDate.of(2026, 10, 10);

        LocalDate fechaFin =
                LocalDate.of(2026, 10, 15);

        // ACT: Calcular la producción del período
        double total = servicio.calcularProduccionTotal(
                fechaInicio,
                fechaFin
        );

        // ASSERT: Verificar el total esperado
        assertEquals(80.0, total, 0.001);
    }

    // CP-027: Reporte de producción por sección
    @Test
    void calcularProduccionPorSeccion() {

        // ARRANGE: Preparar los datos
        SeccionService seccionService =
                new SeccionService();

        TrabajadorService trabajadorService =
                new TrabajadorService();

        trabajadorService.registrar(
                "101110111",
                "Carlos Rodríguez Mora",
                "88881234",
                "carlos@email.com",
                "Encargado de producción"
        );

        SiembraService siembraService =
                new SiembraService(
                        seccionService,
                        trabajadorService
                );

        CosechaService cosechaService =
                new CosechaService(
                        siembraService,
                        trabajadorService
                );

        // Registrar dos secciones disponibles
        seccionService.registrar(
                "SEC-027-A",
                "Sección Norte",
                500,
                "Disponible"
        );

        seccionService.registrar(
                "SEC-027-B",
                "Sección Sur",
                500,
                "Disponible"
        );

        // Registrar las siembras
        LocalDate fechaSiembra =
                LocalDate.of(2026, 10, 1);

        siembraService.registrar(
                "SIEM-027-A",
                fechaSiembra,
                "SEC-027-A",
                100,
                "101110111",
                "Siembra sección Norte"
        );

        siembraService.registrar(
                "SIEM-027-B",
                fechaSiembra,
                "SEC-027-B",
                100,
                "101110111",
                "Siembra sección Sur"
        );

        // Registrar cosechas en diferentes secciones
        cosechaService.registrar(
                "COS-027-A",
                "SIEM-027-A",
                LocalDate.of(2026, 10, 10),
                70.0,
                25,
                "101110111"
        );

        cosechaService.registrar(
                "COS-027-B",
                "SIEM-027-B",
                LocalDate.of(2026, 10, 12),
                40.0,
                15,
                "101110111"
        );

        ReporteService servicio =
                new ReporteService(
                        cosechaService,
                        siembraService
                );

        // ACT: Consultar producción de la sección Norte
        double total = servicio.calcularProduccionPorSeccion(
                "SEC-027-A"
        );

        // ASSERT: Verificar que solo incluya esa sección
        assertEquals(70.0, total, 0.001);
    }

    // CP-028: Reporte de producción por trabajador
    @Test
    void calcularProduccionPorTrabajador() {

        // ARRANGE: Preparar los datos
        SeccionService seccionService =
                new SeccionService();

        TrabajadorService trabajadorService =
                new TrabajadorService();

        // Registrar dos trabajadores
        trabajadorService.registrar(
                "101110111",
                "Carlos Rodríguez Mora",
                "88881234",
                "carlos@email.com",
                "Encargado de cosecha"
        );

        trabajadorService.registrar(
                "202220222",
                "María Fernández López",
                "88885678",
                "maria@email.com",
                "Encargada de cosecha"
        );

        SiembraService siembraService =
                new SiembraService(
                        seccionService,
                        trabajadorService
                );

        CosechaService cosechaService =
                new CosechaService(
                        siembraService,
                        trabajadorService
                );

        // Registrar tres secciones
        seccionService.registrar(
                "SEC-028-A",
                "Sección Norte",
                500,
                "Disponible"
        );

        seccionService.registrar(
                "SEC-028-B",
                "Sección Sur",
                500,
                "Disponible"
        );

        seccionService.registrar(
                "SEC-028-C",
                "Sección Este",
                500,
                "Disponible"
        );

        LocalDate fechaSiembra =
                LocalDate.of(2026, 10, 1);

        // Registrar tres siembras
        siembraService.registrar(
                "SIEM-028-A",
                fechaSiembra,
                "SEC-028-A",
                100,
                "101110111",
                "Primera siembra"
        );

        siembraService.registrar(
                "SIEM-028-B",
                fechaSiembra,
                "SEC-028-B",
                100,
                "101110111",
                "Segunda siembra"
        );

        siembraService.registrar(
                "SIEM-028-C",
                fechaSiembra,
                "SEC-028-C",
                100,
                "202220222",
                "Tercera siembra"
        );

        // Dos cosechas del primer trabajador
        cosechaService.registrar(
                "COS-028-A",
                "SIEM-028-A",
                LocalDate.of(2026, 10, 10),
                60.0,
                20,
                "101110111"
        );

        cosechaService.registrar(
                "COS-028-B",
                "SIEM-028-B",
                LocalDate.of(2026, 10, 12),
                40.0,
                15,
                "101110111"
        );

        // Una cosecha del segundo trabajador
        cosechaService.registrar(
                "COS-028-C",
                "SIEM-028-C",
                LocalDate.of(2026, 10, 14),
                30.0,
                10,
                "202220222"
        );

        ReporteService servicio =
                new ReporteService(
                        cosechaService,
                        siembraService
                );

        // ACT: Consultar producción del primer trabajador
        double total = servicio.calcularProduccionPorTrabajador(
                "101110111"
        );

        // ASSERT: Verificar el total esperado
        assertEquals(100.0, total, 0.001);
    }

    // CP-029: Consultar historial de aplicaciones por siembra
    @Test
    void consultarHistorialAplicacionesPorSiembra() {

        // ARRANGE: Preparar los datos
        SeccionService seccionService =
                new SeccionService();

        TrabajadorService trabajadorService =
                new TrabajadorService();

        trabajadorService.registrar(
                "101110111",
                "Carlos Rodríguez Mora",
                "88881234",
                "carlos@email.com",
                "Encargado de aplicaciones"
        );

        seccionService.registrar(
                "SEC-029-A",
                "Sección Norte",
                500,
                "Disponible"
        );

        seccionService.registrar(
                "SEC-029-B",
                "Sección Sur",
                500,
                "Disponible"
        );

        SiembraService siembraService =
                new SiembraService(
                        seccionService,
                        trabajadorService
                );

        siembraService.registrar(
                "SIEM-029-A",
                LocalDate.of(2026, 10, 1),
                "SEC-029-A",
                100,
                "101110111",
                "Primera siembra"
        );

        siembraService.registrar(
                "SIEM-029-B",
                LocalDate.of(2026, 10, 1),
                "SEC-029-B",
                100,
                "101110111",
                "Segunda siembra"
        );

        CosechaService cosechaService =
                new CosechaService(siembraService);

        ProductoService productoService =
                new ProductoService();

        productoService.registrar(
                "PROD-029",
                "Fertilizante Orgánico",
                "Fertilizante",
                "Producto para mantenimiento",
                "Litros"
        );

        AplicacionService aplicacionService =
                new AplicacionService(
                        productoService,
                        siembraService,
                        cosechaService
                );

        // Registrar dos aplicaciones de la primera siembra
        aplicacionService.registrar(
                "APL-029-A",
                LocalDate.of(2026, 10, 5),
                "SIEM-029-A",
                "PROD-029",
                2.5,
                "101110111",
                "Primera aplicación"
        );

        aplicacionService.registrar(
                "APL-029-B",
                LocalDate.of(2026, 10, 7),
                "SIEM-029-A",
                "PROD-029",
                1.5,
                "101110111",
                "Segunda aplicación"
        );

        // Registrar una aplicación de otra siembra
        aplicacionService.registrar(
                "APL-029-C",
                LocalDate.of(2026, 10, 8),
                "SIEM-029-B",
                "PROD-029",
                3.0,
                "101110111",
                "Aplicación en otra siembra"
        );

        // ACT: Consultar historial de la primera siembra
        List<Aplicacion> historial =
                aplicacionService.consultarHistorialPorSiembra(
                        "SIEM-029-A"
                );

        // ASSERT: Verificar el historial
        assertNotNull(historial);
        assertEquals(2, historial.size());

        assertEquals(
                "APL-029-A",
                historial.get(0).getCodigo()
        );

        assertEquals(
                "APL-029-B",
                historial.get(1).getCodigo()
        );

        // Verificar que ambas pertenecen a la misma siembra
        for (Aplicacion aplicacion : historial) {
            assertEquals(
                    "SIEM-029-A",
                    aplicacion.getCodigoSiembra()
            );
        }
    }

    // CP-030: Reporte de rendimiento de cultivos
    @Test
    void calcularRendimientoCultivo() {

        // ARRANGE: Preparar los datos
        SeccionService seccionService =
                new SeccionService();

        TrabajadorService trabajadorService =
                new TrabajadorService();

        trabajadorService.registrar(
                "101110111",
                "Carlos Rodríguez Mora",
                "88881234",
                "carlos@email.com",
                "Encargado de producción"
        );

        seccionService.registrar(
                "SEC-030",
                "Sección de rendimiento",
                500,
                "Disponible"
        );

        SiembraService siembraService =
                new SiembraService(
                        seccionService,
                        trabajadorService
                );

        // Registrar una siembra con 100 sustratos
        siembraService.registrar(
                "SIEM-030",
                LocalDate.of(2026, 10, 1),
                "SEC-030",
                100,
                "101110111",
                "Siembra para calcular rendimiento"
        );

        CosechaService cosechaService =
                new CosechaService(
                        siembraService,
                        trabajadorService
                );

        // Registrar una cosecha de 50 kg
        cosechaService.registrar(
                "COS-030",
                "SIEM-030",
                LocalDate.of(2026, 10, 10),
                50.0,
                20,
                "101110111"
        );

        ReporteService servicio =
                new ReporteService(
                        cosechaService,
                        siembraService
                );

        // ACT: Calcular el rendimiento del cultivo
        double rendimiento =
                servicio.calcularRendimientoCultivo("SIEM-030");

        // ASSERT: Verificar el resultado esperado
        assertEquals(0.5, rendimiento, 0.001);
    }

    // CP-026: Escenario B - Período de un solo día
    @Test
    void calcularProduccionPeriodoMismoDia() {

        // ARRANGE: Preparar los datos
        SeccionService seccionService =
                new SeccionService();

        TrabajadorService trabajadorService =
                new TrabajadorService();

        trabajadorService.registrar(
                "101110111",
                "Carlos Rodríguez Mora",
                "88881234",
                "carlos@email.com",
                "Encargado de producción"
        );

        seccionService.registrar(
                "SEC-026-D",
                "Sección de prueba",
                500,
                "Disponible"
        );

        SiembraService siembraService =
                new SiembraService(
                        seccionService,
                        trabajadorService
                );

        siembraService.registrar(
                "SIEM-026-D",
                LocalDate.of(2026, 10, 1),
                "SEC-026-D",
                100,
                "101110111",
                "Siembra para reporte diario"
        );

        CosechaService cosechaService =
                new CosechaService(
                        siembraService,
                        trabajadorService
                );

        cosechaService.registrar(
                "COS-026-D",
                "SIEM-026-D",
                LocalDate.of(2026, 10, 10),
                50.0,
                20,
                "101110111"
        );

        ReporteService servicio =
                new ReporteService(cosechaService);

        LocalDate fecha =
                LocalDate.of(2026, 10, 10);

        // ACT: Consultar producción de un único día
        double total = servicio.calcularProduccionTotal(
                fecha,
                fecha
        );

        // ASSERT: Verificar que la fecha sea inclusiva
        assertEquals(50.0, total, 0.001);
    }

    // CP-027: Escenario complementario - Segunda sección
    @Test
    void calcularProduccionSegundaSeccion() {

        // ARRANGE: Preparar los datos
        SeccionService seccionService =
                new SeccionService();

        TrabajadorService trabajadorService =
                new TrabajadorService();

        trabajadorService.registrar(
                "101110111",
                "Carlos Rodríguez Mora",
                "88881234",
                "carlos@email.com",
                "Encargado de producción"
        );

        SiembraService siembraService =
                new SiembraService(
                        seccionService,
                        trabajadorService
                );

        CosechaService cosechaService =
                new CosechaService(
                        siembraService,
                        trabajadorService
                );

        // Registrar dos secciones
        seccionService.registrar(
                "SEC-027-A",
                "Sección Norte",
                500,
                "Disponible"
        );

        seccionService.registrar(
                "SEC-027-B",
                "Sección Sur",
                500,
                "Disponible"
        );

        LocalDate fechaSiembra =
                LocalDate.of(2026, 10, 1);

        // Registrar las siembras
        siembraService.registrar(
                "SIEM-027-A",
                fechaSiembra,
                "SEC-027-A",
                100,
                "101110111",
                "Primera siembra"
        );

        siembraService.registrar(
                "SIEM-027-B",
                fechaSiembra,
                "SEC-027-B",
                100,
                "101110111",
                "Segunda siembra"
        );

        // Registrar cosechas de ambas secciones
        cosechaService.registrar(
                "COS-027-A",
                "SIEM-027-A",
                LocalDate.of(2026, 10, 10),
                70.0,
                25,
                "101110111"
        );

        cosechaService.registrar(
                "COS-027-B",
                "SIEM-027-B",
                LocalDate.of(2026, 10, 12),
                40.0,
                15,
                "101110111"
        );

        ReporteService servicio =
                new ReporteService(
                        cosechaService,
                        siembraService
                );

        // ACT: Consultar la producción de la segunda sección
        double total = servicio.calcularProduccionPorSeccion(
                "SEC-027-B"
        );

        // ASSERT: Verificar el total esperado
        assertEquals(40.0, total, 0.001);
    }

    // CP-028: Escenario complementario - Segundo trabajador
    @Test
    void calcularProduccionSegundoTrabajador() {

        // ARRANGE: Preparar los datos
        SeccionService seccionService =
                new SeccionService();

        TrabajadorService trabajadorService =
                new TrabajadorService();

        trabajadorService.registrar(
                "101110111",
                "Carlos Rodríguez Mora",
                "88881234",
                "carlos@email.com",
                "Encargado de producción"
        );

        trabajadorService.registrar(
                "202220222",
                "María Fernández López",
                "88885678",
                "maria@email.com",
                "Encargada de producción"
        );

        SiembraService siembraService =
                new SiembraService(
                        seccionService,
                        trabajadorService
                );

        CosechaService cosechaService =
                new CosechaService(
                        siembraService,
                        trabajadorService
                );

        // Registrar las secciones
        seccionService.registrar(
                "SEC-028-D",
                "Sección Norte",
                500,
                "Disponible"
        );

        seccionService.registrar(
                "SEC-028-E",
                "Sección Sur",
                500,
                "Disponible"
        );

        // Registrar las siembras
        LocalDate fechaSiembra =
                LocalDate.of(2026, 10, 1);

        siembraService.registrar(
                "SIEM-028-D",
                fechaSiembra,
                "SEC-028-D",
                100,
                "101110111",
                "Siembra del primer trabajador"
        );

        siembraService.registrar(
                "SIEM-028-E",
                fechaSiembra,
                "SEC-028-E",
                100,
                "202220222",
                "Siembra del segundo trabajador"
        );

        // Registrar cosecha del primer trabajador
        cosechaService.registrar(
                "COS-028-D",
                "SIEM-028-D",
                LocalDate.of(2026, 10, 10),
                100.0,
                30,
                "101110111"
        );

        // Registrar cosecha del segundo trabajador
        cosechaService.registrar(
                "COS-028-E",
                "SIEM-028-E",
                LocalDate.of(2026, 10, 12),
                30.0,
                10,
                "202220222"
        );

        ReporteService servicio =
                new ReporteService(
                        cosechaService,
                        siembraService
                );

        // ACT: Consultar producción del segundo trabajador
        double total = servicio.calcularProduccionPorTrabajador(
                "202220222"
        );

        // ASSERT: Verificar el total esperado
        assertEquals(30.0, total, 0.001);
    }

    // CP-029: Escenario complementario - Historial sin aplicaciones
    @Test
    void consultarHistorialSinAplicaciones() {

        // ARRANGE: Preparar los servicios
        SeccionService seccionService =
                new SeccionService();

        TrabajadorService trabajadorService =
                new TrabajadorService();

        trabajadorService.registrar(
                "101110111",
                "Carlos Rodríguez Mora",
                "88881234",
                "carlos@email.com",
                "Encargado de aplicaciones"
        );

        // Registrar una sección disponible
        seccionService.registrar(
                "SEC-029-D",
                "Sección sin aplicaciones",
                500,
                "Disponible"
        );

        SiembraService siembraService =
                new SiembraService(
                        seccionService,
                        trabajadorService
                );

        // Registrar una siembra sin aplicaciones
        siembraService.registrar(
                "SIEM-029-D",
                LocalDate.of(2026, 10, 1),
                "SEC-029-D",
                100,
                "101110111",
                "Siembra sin aplicaciones agrícolas"
        );

        ProductoService productoService =
                new ProductoService();

        CosechaService cosechaService =
                new CosechaService(siembraService);

        AplicacionService aplicacionService =
                new AplicacionService(
                        productoService,
                        siembraService,
                        cosechaService
                );

        // ACT: Consultar historial de una siembra
        // que no tiene aplicaciones registradas
        List<Aplicacion> historial =
                aplicacionService.consultarHistorialPorSiembra(
                        "SIEM-029-D"
                );

        // ASSERT: Verificar que la lista esté vacía
        assertNotNull(historial);
        assertTrue(historial.isEmpty());
        assertEquals(0, historial.size());
    }

    // CP-030: Escenario complementario - Segunda siembra
    @Test
    void calcularRendimientoSegundaSiembra() {

        // ARRANGE: Preparar los datos
        SeccionService seccionService =
                new SeccionService();

        TrabajadorService trabajadorService =
                new TrabajadorService();

        trabajadorService.registrar(
                "101110111",
                "Carlos Rodríguez Mora",
                "88881234",
                "carlos@email.com",
                "Encargado de producción"
        );

        // Registrar dos secciones disponibles
        seccionService.registrar(
                "SEC-030-A",
                "Sección Norte",
                500,
                "Disponible"
        );

        seccionService.registrar(
                "SEC-030-B",
                "Sección Sur",
                500,
                "Disponible"
        );

        SiembraService siembraService =
                new SiembraService(
                        seccionService,
                        trabajadorService
                );

        LocalDate fechaSiembra =
                LocalDate.of(2026, 10, 1);

        // Registrar la primera siembra con 100 sustratos
        siembraService.registrar(
                "SIEM-030-A",
                fechaSiembra,
                "SEC-030-A",
                100,
                "101110111",
                "Primera siembra para rendimiento"
        );

        // Registrar la segunda siembra con 200 sustratos
        siembraService.registrar(
                "SIEM-030-B",
                fechaSiembra,
                "SEC-030-B",
                200,
                "101110111",
                "Segunda siembra para rendimiento"
        );

        CosechaService cosechaService =
                new CosechaService(
                        siembraService,
                        trabajadorService
                );

        // Registrar cosecha de la primera siembra
        cosechaService.registrar(
                "COS-030-A",
                "SIEM-030-A",
                LocalDate.of(2026, 10, 10),
                50.0,
                20,
                "101110111"
        );

        // Registrar cosecha de la segunda siembra
        cosechaService.registrar(
                "COS-030-B",
                "SIEM-030-B",
                LocalDate.of(2026, 10, 12),
                80.0,
                30,
                "101110111"
        );

        ReporteService servicio =
                new ReporteService(
                        cosechaService,
                        siembraService
                );

        // ACT: Calcular rendimiento de la segunda siembra
        double rendimiento =
                servicio.calcularRendimientoCultivo("SIEM-030-B");

        // ASSERT: Verificar el rendimiento esperado
        assertEquals(0.4, rendimiento, 0.001);

        // Verificar que el rendimiento de la primera
        // siembra continúa siendo correcto
        double rendimientoPrimera =
                servicio.calcularRendimientoCultivo("SIEM-030-A");

        assertEquals(0.5, rendimientoPrimera, 0.001);
    }








}
