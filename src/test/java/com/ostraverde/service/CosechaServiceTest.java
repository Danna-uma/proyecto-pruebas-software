
package com.ostraverde.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.ostraverde.model.Cosecha;

public class CosechaServiceTest {

    // CP-021: Registro y consulta de una cosecha
    @Test
    void registrarYConsultarCosecha() {

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

        SiembraService siembraService = new SiembraService(
                seccionService,
                trabajadorService
        );

        siembraService.registrar(
                "SIEM-001",
                LocalDate.of(2026, 10, 10),
                "SEC-001",
                100,
                "101110111",
                "Siembra inicial"
        );

        CosechaService servicio =
                new CosechaService(siembraService);

        LocalDate fechaCosecha =
                LocalDate.of(2026, 10, 15);

        // ACT: Registrar la cosecha
        Cosecha cosecha = servicio.registrar(
                "COS-001",
                "SIEM-001",
                fechaCosecha,
                50.0
        );

        // ASSERT: Verificar los datos registrados
        assertNotNull(cosecha);
        assertEquals("COS-001", cosecha.getCodigo());
        assertEquals("SIEM-001", cosecha.getCodigoSiembra());
        assertEquals(fechaCosecha, cosecha.getFecha());
        assertEquals(
                50.0,
                cosecha.getCantidadKilogramos(),
                0.001
        );

        // ACT: Consultar la cosecha registrada
        Cosecha consultada = servicio.consultar("COS-001");

        // ASSERT: Verificar la consulta
        assertNotNull(consultada);
        assertEquals("COS-001", consultada.getCodigo());
        assertEquals(
                "SIEM-001",
                consultada.getCodigoSiembra()
        );
        assertEquals(fechaCosecha, consultada.getFecha());
        assertEquals(
                50.0,
                consultada.getCantidadKilogramos(),
                0.001
        );
    }

    // Prueba adicional: Cosecha con siembra inexistente
    @Test
    void registrarCosechaConSiembraInexistente() {

        // ARRANGE: Preparar los datos
        SeccionService seccionService =
                new SeccionService();

        TrabajadorService trabajadorService =
                new TrabajadorService();

        SiembraService siembraService =
                new SiembraService(
                        seccionService,
                        trabajadorService
                );

        CosechaService servicio =
                new CosechaService(siembraService);

        LocalDate fecha = LocalDate.now();

        // ACT: Intentar registrar una cosecha
        // asociada a una siembra inexistente
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> servicio.registrar(
                        "COS-002",
                        "SIEM-999",
                        fecha,
                        30.0
                )
        );

        // ASSERT: Verificar el mensaje de error
        assertEquals(
                "La siembra no existe",
                error.getMessage()
        );

        // Comprobar que la cosecha no se registró
        IllegalArgumentException errorConsulta = assertThrows(
                IllegalArgumentException.class,
                () -> servicio.consultar("COS-002")
        );

        assertEquals(
                "La cosecha no existe",
                errorConsulta.getMessage()
        );
    }

    // CP-023: Escenario A - Peso cosechado igual a cero
    @Test
    void registrarCosechaConCantidadCero() {

        // ARRANGE: Preparar los datos
        SeccionService seccionService = new SeccionService();

        seccionService.registrar(
                "SEC-003",
                "Sección Este",
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
                "SIEM-003",
                LocalDate.now(),
                "SEC-003",
                100,
                "101110111",
                "Siembra para prueba de cantidad"
        );

        CosechaService servicio =
                new CosechaService(siembraService);

        // ACT: Intentar registrar una cosecha con cero kg
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> servicio.registrar(
                        "COS-003",
                        "SIEM-003",
                        LocalDate.now(),
                        0.0
                )
        );

        // ASSERT: Verificar el mensaje
        assertEquals(
                "La cantidad cosechada debe ser mayor que cero",
                error.getMessage()
        );

        // Verificar que la cosecha no se registró
        assertThrows(
                IllegalArgumentException.class,
                () -> servicio.consultar("COS-003")
        );
    }

    // CP-023: Escenario B - Peso positivo válido
    @Test
    void registrarCosechaConCantidadPositiva() {

        // ARRANGE: Preparar los datos
        SeccionService seccionService = new SeccionService();

        seccionService.registrar(
                "SEC-004",
                "Sección Oeste",
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
                "SIEM-004",
                LocalDate.now().minusDays(1),
                "SEC-004",
                100,
                "101110111",
                "Siembra para prueba positiva"
        );

        CosechaService servicio =
                new CosechaService(siembraService);

        LocalDate fecha = LocalDate.now();

        // ACT: Registrar una cosecha de 0.1 kg
        Cosecha cosecha = servicio.registrar(
                "COS-004",
                "SIEM-004",
                fecha,
                0.1
        );

        // ASSERT: Verificar el resultado
        assertNotNull(cosecha);
        assertEquals("COS-004", cosecha.getCodigo());
        assertEquals("SIEM-004", cosecha.getCodigoSiembra());
        assertEquals(
                0.1,
                cosecha.getCantidadKilogramos(),
                0.001
        );

        // Verificar que la cosecha quedó almacenada
        Cosecha consultada = servicio.consultar("COS-004");

        assertNotNull(consultada);
        assertEquals(
                0.1,
                consultada.getCantidadKilogramos(),
                0.001
        );
    }

    // CP-024: Escenario A - Fecha anterior a la siembra
    @Test
    void registrarCosechaConFechaAnteriorASiembra() {

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

        SiembraService siembraService =
                new SiembraService(
                        seccionService,
                        trabajadorService
                );

        LocalDate fechaSiembra =
                LocalDate.of(2026, 10, 10);

        siembraService.registrar(
                "SIEM-005",
                fechaSiembra,
                "SEC-005",
                100,
                "101110111",
                "Siembra para prueba de fechas"
        );

        CosechaService servicio =
                new CosechaService(siembraService);

        LocalDate fechaCosecha =
                fechaSiembra.minusDays(1);

        // ACT: Intentar registrar una cosecha anterior
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> servicio.registrar(
                        "COS-005",
                        "SIEM-005",
                        fechaCosecha,
                        30.0
                )
        );

        // ASSERT: Verificar el mensaje actualizado
        assertEquals(
                "La fecha de cosecha debe ser posterior a la siembra",
                error.getMessage()
        );

        // Verificar que la cosecha no fue registrada
        IllegalArgumentException errorConsulta = assertThrows(
                IllegalArgumentException.class,
                () -> servicio.consultar("COS-005")
        );

        assertEquals(
                "La cosecha no existe",
                errorConsulta.getMessage()
        );
    }

    // CP-021: Registro completo de una cosecha
    @Test
    void registrarCosechaConDatosCompletos() {

        // ARRANGE: Preparar los datos
        SeccionService seccionService =
                new SeccionService();

        seccionService.registrar(
                "SEC-021",
                "Sección de producción",
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
                "Encargado de cosecha"
        );

        SiembraService siembraService =
                new SiembraService(
                        seccionService,
                        trabajadorService
                );

        siembraService.registrar(
                "SIEM-021",
                LocalDate.of(2026, 10, 10),
                "SEC-021",
                100,
                "101110111",
                "Siembra para cosecha"
        );

        CosechaService servicio =
                new CosechaService(
                        siembraService,
                        trabajadorService
                );

        LocalDate fechaCosecha =
                LocalDate.of(2026, 10, 14);

        // ACT: Registrar la cosecha con datos completos
        Cosecha cosecha = servicio.registrar(
                "COS-021",
                "SIEM-021",
                fechaCosecha,
                50.0,
                20,
                "101110111"
        );

        // ASSERT: Verificar los datos registrados
        assertNotNull(cosecha);
        assertEquals("COS-021", cosecha.getCodigo());
        assertEquals("SIEM-021", cosecha.getCodigoSiembra());
        assertEquals(fechaCosecha, cosecha.getFecha());
        assertEquals(
                50.0,
                cosecha.getCantidadKilogramos(),
                0.001
        );
        assertEquals(20, cosecha.getCantidadBandejas());
        assertEquals("101110111", cosecha.getResponsable());

        // ACT: Consultar la cosecha registrada
        Cosecha consultada = servicio.consultar("COS-021");

        // ASSERT: Verificar la información consultada
        assertNotNull(consultada);
        assertEquals("COS-021", consultada.getCodigo());
        assertEquals("SIEM-021", consultada.getCodigoSiembra());
        assertEquals(fechaCosecha, consultada.getFecha());
        assertEquals(
                50.0,
                consultada.getCantidadKilogramos(),
                0.001
        );
        assertEquals(20, consultada.getCantidadBandejas());
        assertEquals("101110111", consultada.getResponsable());
    }

    // CP-022: Impedir una segunda cosecha para la misma siembra
    @Test
    void impedirSegundaCosechaDeUnaMismaSiembra() {

        // ARRANGE: Preparar los datos
        SeccionService seccionService =
                new SeccionService();

        seccionService.registrar(
                "SEC-022",
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
                "Encargado de cosecha"
        );

        SiembraService siembraService =
                new SiembraService(
                        seccionService,
                        trabajadorService
                );

        siembraService.registrar(
                "SIEM-022",
                LocalDate.of(2026, 10, 10),
                "SEC-022",
                100,
                "101110111",
                "Siembra para prueba de cosecha duplicada"
        );

        CosechaService servicio =
                new CosechaService(
                        siembraService,
                        trabajadorService
                );

        LocalDate fecha = LocalDate.of(2026, 10, 14);

        // Registrar la primera cosecha
        Cosecha primeraCosecha = servicio.registrar(
                "COS-022",
                "SIEM-022",
                fecha,
                50.0,
                20,
                "101110111"
        );

        assertNotNull(primeraCosecha);

        // ACT: Intentar registrar una segunda cosecha
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> servicio.registrar(
                        "COS-023",
                        "SIEM-022",
                        fecha.plusDays(1),
                        30.0,
                        10,
                        "101110111"
                )
        );

        // ASSERT: Verificar el mensaje de error
        assertEquals(
                "La siembra ya tiene una cosecha registrada",
                error.getMessage()
        );

        // Verificar que la primera cosecha se conserva
        Cosecha consultada = servicio.consultar("COS-022");

        assertNotNull(consultada);
        assertEquals("SIEM-022", consultada.getCodigoSiembra());
        assertEquals(
                50.0,
                consultada.getCantidadKilogramos(),
                0.001
        );

        // Comprobar que la segunda cosecha no existe
        assertThrows(
                IllegalArgumentException.class,
                () -> servicio.consultar("COS-023")
        );
    }

    // CP-024: Escenario B - Fecha igual a la siembra
    @Test
    void registrarCosechaConFechaIgualASiembra() {

        // ARRANGE: Preparar los datos
        SeccionService seccionService = new SeccionService();

        seccionService.registrar(
                "SEC-024",
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
                "Encargado de cosecha"
        );

        SiembraService siembraService =
                new SiembraService(
                        seccionService,
                        trabajadorService
                );

        LocalDate fechaSiembra =
                LocalDate.of(2026, 10, 10);

        siembraService.registrar(
                "SIEM-024",
                fechaSiembra,
                "SEC-024",
                100,
                "101110111",
                "Siembra para prueba de fechas iguales"
        );

        CosechaService servicio =
                new CosechaService(siembraService);

        // ACT: Intentar registrar una cosecha
        // con la misma fecha de la siembra
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> servicio.registrar(
                        "COS-024",
                        "SIEM-024",
                        fechaSiembra,
                        30.0
                )
        );

        // ASSERT: Verificar el mensaje
        assertEquals(
                "La fecha de cosecha debe ser posterior a la siembra",
                error.getMessage()
        );

        // Verificar que la cosecha no se registró
        assertThrows(
                IllegalArgumentException.class,
                () -> servicio.consultar("COS-024")
        );
    }

    // CP-024: Escenario C - Fecha posterior a la siembra
    @Test
    void registrarCosechaConFechaPosteriorASiembra() {

        // ARRANGE: Preparar los datos
        SeccionService seccionService = new SeccionService();

        seccionService.registrar(
                "SEC-025",
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
                "Encargado de cosecha"
        );

        SiembraService siembraService =
                new SiembraService(
                        seccionService,
                        trabajadorService
                );

        LocalDate fechaSiembra =
                LocalDate.of(2026, 10, 10);

        siembraService.registrar(
                "SIEM-025",
                fechaSiembra,
                "SEC-025",
                100,
                "101110111",
                "Siembra para prueba de fecha posterior"
        );

        CosechaService servicio =
                new CosechaService(siembraService);

        LocalDate fechaCosecha =
                fechaSiembra.plusDays(1);

        // ACT: Registrar una cosecha con fecha posterior
        Cosecha cosecha = servicio.registrar(
                "COS-025",
                "SIEM-025",
                fechaCosecha,
                30.0
        );

        // ASSERT: Verificar el resultado
        assertNotNull(cosecha);
        assertEquals("COS-025", cosecha.getCodigo());
        assertEquals(fechaCosecha, cosecha.getFecha());

        // Verificar que la cosecha quedó registrada
        Cosecha consultada = servicio.consultar("COS-025");

        assertNotNull(consultada);
        assertEquals(fechaCosecha, consultada.getFecha());
    }

    // CP-025: Liberar sección automáticamente después de cosechar
    @Test
    void liberarSeccionDespuesDeRegistrarCosecha() {

        // ARRANGE: Preparar los datos
        SeccionService seccionService =
                new SeccionService();

        seccionService.registrar(
                "SEC-026",
                "Sección de producción",
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
                "Encargado de cosecha"
        );

        SiembraService siembraService =
                new SiembraService(
                        seccionService,
                        trabajadorService
                );

        // Registrar la siembra
        siembraService.registrar(
                "SIEM-026",
                LocalDate.of(2026, 10, 10),
                "SEC-026",
                100,
                "101110111",
                "Siembra para verificar liberación"
        );

        // Verificar que la sección esté ocupada
        assertEquals(
                "Ocupada",
                seccionService.consultarPorCodigo(
                        "SEC-026"
                ).getEstado()
        );

        CosechaService servicio =
                new CosechaService(
                        siembraService,
                        trabajadorService
                );

        // ACT: Registrar la cosecha
        Cosecha cosecha = servicio.registrar(
                "COS-026",
                "SIEM-026",
                LocalDate.of(2026, 10, 14),
                50.0,
                20,
                "101110111"
        );

        // ASSERT: Verificar el registro
        assertNotNull(cosecha);
        assertEquals("COS-026", cosecha.getCodigo());

        // Comprobar que la sección quedó disponible
        assertEquals(
                "Disponible",
                seccionService.consultarPorCodigo(
                        "SEC-026"
                ).getEstado()
        );
    }

}
