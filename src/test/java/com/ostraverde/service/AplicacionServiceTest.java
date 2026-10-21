
package com.ostraverde.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.ostraverde.model.Aplicacion;

public class AplicacionServiceTest {

    // CP-017: Registro y consulta de aplicación agrícola
    @Test
    void registrarYConsultarAplicacion() {

        // ARRANGE: Preparar los datos
        ProductoService productoService =
                new ProductoService();

        productoService.registrar(
                "PROD-001",
                "Fertilizante Orgánico",
                "Fertilizante",
                "Producto para mantenimiento del cultivo",
                "Litros"
        );

        AplicacionService servicio =
                new AplicacionService(productoService);

        LocalDate fecha = LocalDate.of(2026, 10, 10);

        // ACT: Registrar la aplicación
        Aplicacion aplicacion = servicio.registrar(
                "APL-002",
                "PROD-001",
                fecha,
                2.5
        );

        // ASSERT: Verificar los datos registrados
        assertNotNull(aplicacion);
        assertEquals("APL-002", aplicacion.getCodigo());
        assertEquals("PROD-001", aplicacion.getCodigoProducto());
        assertEquals(fecha, aplicacion.getFecha());
        assertEquals(2.5, aplicacion.getCantidad(), 0.001);

        // ACT: Consultar la aplicación registrada
        Aplicacion consultada = servicio.consultar("APL-002");

        // ASSERT: Verificar los datos consultados
        assertNotNull(consultada);
        assertEquals("APL-002", consultada.getCodigo());
        assertEquals("PROD-001", consultada.getCodigoProducto());
        assertEquals(fecha, consultada.getFecha());
        assertEquals(2.5, consultada.getCantidad(), 0.001);

        // Verificar la asociación con el producto
        assertTrue(servicio.tieneAplicaciones("PROD-001"));
    }

    // CP-018: Registrar aplicación con producto inexistente
    @Test
    void registrarAplicacionConProductoInexistente() {

        // ARRANGE: Preparar los datos
        ProductoService productoService =
                new ProductoService();

        AplicacionService servicio =
                new AplicacionService(productoService);

        LocalDate fecha = LocalDate.now();

        // ACT: Intentar registrar con producto inexistente
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> servicio.registrar(
                        "APL-003",
                        "PROD-999",
                        fecha,
                        2.5
                )
        );

        // ASSERT: Verificar el mensaje de error
        assertEquals(
                "El producto no existe",
                error.getMessage()
        );

        // Comprobar que la aplicación no se registró
        IllegalArgumentException errorConsulta = assertThrows(
                IllegalArgumentException.class,
                () -> servicio.consultar("APL-003")
        );

        assertEquals(
                "La aplicación no existe",
                errorConsulta.getMessage()
        );

        assertFalse(servicio.tieneAplicaciones("PROD-999"));
    }

    // CP-019: Escenario A - Cantidad igual a cero
    @Test
    void registrarAplicacionConCantidadCero() {

        // ARRANGE: Preparar los datos
        ProductoService productoService =
                new ProductoService();

        productoService.registrar(
                "PROD-004",
                "Fertilizante Líquido",
                "Fertilizante",
                "Producto para el cultivo",
                "Litros"
        );

        AplicacionService servicio =
                new AplicacionService(productoService);

        // ACT: Intentar registrar cantidad cero
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> servicio.registrar(
                        "APL-004",
                        "PROD-004",
                        LocalDate.now(),
                        0
                )
        );

        // ASSERT: Verificar el mensaje de error
        assertEquals(
                "La cantidad aplicada debe ser mayor que cero",
                error.getMessage()
        );

        // Comprobar que no se registró la aplicación
        assertThrows(
                IllegalArgumentException.class,
                () -> servicio.consultar("APL-004")
        );

        // El producto no debe marcarse como utilizado
        assertFalse(servicio.tieneAplicaciones("PROD-004"));
    }

    // CP-019: Escenario B - Cantidad positiva válida
    @Test
    void registrarAplicacionConCantidadPositiva() {

        // ARRANGE: Preparar los datos
        ProductoService productoService =
                new ProductoService();

        productoService.registrar(
                "PROD-005",
                "Fertilizante Orgánico",
                "Fertilizante",
                "Producto para el cultivo",
                "Litros"
        );

        AplicacionService servicio =
                new AplicacionService(productoService);

        LocalDate fecha = LocalDate.now();

        // ACT: Registrar aplicación con cantidad positiva
        Aplicacion aplicacion = servicio.registrar(
                "APL-005",
                "PROD-005",
                fecha,
                0.1
        );

        // ASSERT: Verificar el resultado
        assertNotNull(aplicacion);
        assertEquals("APL-005", aplicacion.getCodigo());
        assertEquals(0.1, aplicacion.getCantidad(), 0.001);
        assertTrue(servicio.tieneAplicaciones("PROD-005"));
    }

    // CP-020: Aplicación en un cultivo cosechado
    @Test
    void impedirAplicacionEnCultivoCosechado() {

        // ARRANGE: Preparar los datos
        SeccionService seccionService =
                new SeccionService();

        seccionService.registrar(
                "SEC-020",
                "Sección de prueba",
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

        SiembraService siembraService =
                new SiembraService(
                        seccionService,
                        trabajadorService
                );

        siembraService.registrar(
                "SIEM-020",
                LocalDate.of(2026, 10, 10),
                "SEC-020",
                100,
                "101110111",
                "Siembra inicial"
        );

        CosechaService cosechaService =
                new CosechaService(siembraService);

        cosechaService.registrar(
                "COS-020",
                "SIEM-020",
                LocalDate.of(2026, 10, 12),
                50.0
        );

        ProductoService productoService =
                new ProductoService();

        productoService.registrar(
                "PROD-020",
                "Fertilizante Orgánico",
                "Fertilizante",
                "Producto para mantenimiento",
                "Litros"
        );

        // Utilizar los tres servicios relacionados
        AplicacionService servicio =
                new AplicacionService(
                        productoService,
                        siembraService,
                        cosechaService
                );

        // ACT: Intentar aplicar producto al cultivo cosechado
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> servicio.registrar(
                        "APL-020",
                        LocalDate.of(2026, 10, 14),
                        "SIEM-020",
                        "PROD-020",
                        5.0,
                        "101110111",
                        "Aplicación posterior a cosecha"
                )
        );

        // ASSERT: Verificar el mensaje de error
        assertEquals(
                "No se permiten aplicaciones en cultivos cosechados",
                error.getMessage()
        );

        // Verificar que la aplicación no se registró
        IllegalArgumentException errorConsulta = assertThrows(
                IllegalArgumentException.class,
                () -> servicio.consultar("APL-020")
        );

        assertEquals(
                "La aplicación no existe",
                errorConsulta.getMessage()
        );

        // Verificar que el producto no se marcó como utilizado
        assertFalse(servicio.tieneAplicaciones("PROD-020"));
    }

    // CP-017: Escenario complementario - Registro completo
    @Test
    void registrarAplicacionConDatosCompletos() {

        // ARRANGE: Preparar la sección
        SeccionService seccionService = new SeccionService();

        seccionService.registrar(
                "SEC-017",
                "Sección de aplicaciones",
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
                "Encargado de aplicaciones"
        );

        // Preparar la siembra
        SiembraService siembraService =
                new SiembraService(
                        seccionService,
                        trabajadorService
                );

        siembraService.registrar(
                "SIEM-017",
                LocalDate.of(2026, 10, 1),
                "SEC-017",
                100,
                "101110111",
                "Siembra para registrar aplicaciones"
        );

        // Preparar el producto agrícola
        ProductoService productoService =
                new ProductoService();

        productoService.registrar(
                "PROD-017",
                "Fertilizante Orgánico",
                "Fertilizante",
                "Producto para mantenimiento",
                "Litros"
        );

        CosechaService cosechaService =
                new CosechaService(siembraService);

        AplicacionService servicio =
                new AplicacionService(
                        productoService,
                        siembraService,
                        cosechaService
                );

        LocalDate fechaAplicacion =
                LocalDate.of(2026, 10, 10);

        // ACT: Registrar aplicación con todos los datos
        Aplicacion aplicacion = servicio.registrar(
                "APL-017-C",
                fechaAplicacion,
                "SIEM-017",
                "PROD-017",
                2.5,
                "101110111",
                "Aplicación preventiva"
        );

        // ASSERT: Verificar registro
        assertNotNull(aplicacion);
        assertEquals("APL-017-C", aplicacion.getCodigo());
        assertEquals("SIEM-017", aplicacion.getCodigoSiembra());
        assertEquals("PROD-017", aplicacion.getCodigoProducto());
        assertEquals(fechaAplicacion, aplicacion.getFecha());
        assertEquals(2.5, aplicacion.getCantidad(), 0.001);

        // Verificar consulta
        Aplicacion consultada =
                servicio.consultar("APL-017-C");

        assertNotNull(consultada);
        assertEquals("SIEM-017", consultada.getCodigoSiembra());
        assertEquals("PROD-017", consultada.getCodigoProducto());

        // Verificar asociación del producto
        assertTrue(servicio.tieneAplicaciones("PROD-017"));
    }

    // CP-018: Escenario complementario - Siembra inexistente
    @Test
    void registrarAplicacionConSiembraInexistente() {

        // ARRANGE: Preparar una sección
        SeccionService seccionService =
                new SeccionService();

        seccionService.registrar(
                "SEC-018",
                "Sección de prueba",
                500,
                "Disponible"
        );

        // Preparar el trabajador responsable
        TrabajadorService trabajadorService =
                new TrabajadorService();

        trabajadorService.registrar(
                "101110111",
                "Carlos Rodríguez Mora",
                "88881234",
                "carlos@email.com",
                "Encargado de aplicaciones"
        );

        // Preparar el servicio de siembras,
        // sin registrar la siembra SIEM-999
        SiembraService siembraService =
                new SiembraService(
                        seccionService,
                        trabajadorService
                );

        // Registrar un producto válido
        ProductoService productoService =
                new ProductoService();

        productoService.registrar(
                "PROD-018",
                "Fertilizante Orgánico",
                "Fertilizante",
                "Producto para mantenimiento",
                "Litros"
        );

        CosechaService cosechaService =
                new CosechaService(siembraService);

        AplicacionService servicio =
                new AplicacionService(
                        productoService,
                        siembraService,
                        cosechaService
                );

        // ACT: Intentar registrar una aplicación
        // asociada a una siembra inexistente
        assertThrows(
                IllegalArgumentException.class,
                () -> servicio.registrar(
                        "APL-018-C",
                        LocalDate.of(2026, 10, 10),
                        "SIEM-999",
                        "PROD-018",
                        2.5,
                        "101110111",
                        "Aplicación de prueba"
                )
        );

        // ASSERT: Comprobar que no se registró
        assertThrows(
                IllegalArgumentException.class,
                () -> servicio.consultar("APL-018-C")
        );

        // Verificar que el producto no se marcó como utilizado
        assertFalse(servicio.tieneAplicaciones("PROD-018"));
    }


}
