
package com.ostraverde.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.ostraverde.model.Trabajador;

public class TrabajadorServiceTest {

    // CP-005: Registrar trabajador con identificación única
    @Test
    void registrarTrabajadorConDatosValidos() {

        // ARRANGE: Preparar los datos
        TrabajadorService servicio = new TrabajadorService();

        String identificacion = "101110111";
        String nombre = "Carlos Rodríguez Mora";
        String telefono = "88881234";
        String correo = "carlos@email.com";
        String cargo = "Encargado de siembra";

        // ACT: Registrar el trabajador
        Trabajador trabajador = servicio.registrar(
                identificacion,
                nombre,
                telefono,
                correo,
                cargo
        );

        // ASSERT: Verificar los resultados
        assertNotNull(trabajador);
        assertEquals("101110111", trabajador.getIdentificacion());
        assertEquals("Carlos Rodríguez Mora", trabajador.getNombre());
        assertEquals("88881234", trabajador.getTelefono());
        assertEquals("carlos@email.com", trabajador.getCorreo());
        assertEquals("Encargado de siembra", trabajador.getCargo());
    }
    

    // CP-006: Registro de trabajador con identificación duplicada
    @Test
    void registrarTrabajadorConIdentificacionDuplicada() {

        // ARRANGE: Preparar los datos
        TrabajadorService servicio = new TrabajadorService();

        servicio.registrar(
                "101110111",
                "Carlos Rodríguez Mora",
                "88881234",
                "carlos@email.com",
                "Encargado de siembra"
        );

        // ACT: Intentar registrar un trabajador duplicado
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> servicio.registrar(
                        "101110111",
                        "Pedro Vargas",
                        "88887890",
                        "pedro@email.com",
                        "Supervisor"
                )
        );

        // ASSERT: Verificar el mensaje de error
        assertEquals(
                "La identificación del trabajador ya existe",
                error.getMessage()
        );
    }
    

    // CP-007: Escenario A - Consultar trabajador
    @Test
    void consultarTrabajadorPorIdentificacion() {

        // ARRANGE: Preparar los datos
        TrabajadorService servicio = new TrabajadorService();

        servicio.registrar(
                "101110111",
                "Carlos Rodríguez Mora",
                "88881234",
                "carlos@email.com",
                "Encargado de siembra"
        );

        // ACT: Consultar el trabajador
        Trabajador trabajador = servicio.consultar(
                "101110111"
        );

        // ASSERT: Verificar los resultados
        assertNotNull(trabajador);
        assertEquals("101110111", trabajador.getIdentificacion());
        assertEquals("Carlos Rodríguez Mora", trabajador.getNombre());
        assertEquals("88881234", trabajador.getTelefono());
        assertEquals("carlos@email.com", trabajador.getCorreo());
        assertEquals("Encargado de siembra", trabajador.getCargo());
    }

    // CP-007: Escenario B - Actualizar teléfono
    @Test
    void actualizarTelefonoTrabajador() {

        // ARRANGE: Preparar los datos
        TrabajadorService servicio = new TrabajadorService();

        servicio.registrar(
                "101110111",
                "Carlos Rodríguez Mora",
                "88881234",
                "carlos@email.com",
                "Encargado de siembra"
        );

        // ACT: Actualizar el teléfono
        Trabajador actualizado = servicio.actualizarTelefono(
                "101110111",
                "88885678"
        );

        // ASSERT: Verificar los resultados
        assertNotNull(actualizado);
        assertEquals("101110111", actualizado.getIdentificacion());
        assertEquals("Carlos Rodríguez Mora", actualizado.getNombre());
        assertEquals("88885678", actualizado.getTelefono());
        assertEquals("carlos@email.com", actualizado.getCorreo());
        assertEquals("Encargado de siembra", actualizado.getCargo());

        // ASSERT: Comprobar que los cambios se conservaron
        Trabajador consultado = servicio.consultar("101110111");

        assertNotNull(consultado);
        assertEquals("101110111", consultado.getIdentificacion());
        assertEquals("Carlos Rodríguez Mora", consultado.getNombre());
        assertEquals("88885678", consultado.getTelefono());
        assertEquals("carlos@email.com", consultado.getCorreo());
        assertEquals("Encargado de siembra", consultado.getCargo());

    }
    
    


}
