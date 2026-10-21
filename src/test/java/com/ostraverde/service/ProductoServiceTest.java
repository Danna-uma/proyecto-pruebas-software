
package com.ostraverde.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.ostraverde.model.ProductoAgricola;

import java.time.LocalDate;

public class ProductoServiceTest {

    // CP-014: Registro y consulta de productos agrícolas
    @Test
    void registrarYConsultarProductoAgricola() {

        // ARRANGE: Preparar los datos
        ProductoService servicio = new ProductoService();

        String codigo = "PROD-001";
        String nombre = "Fertilizante Orgánico";
        String tipo = "Fertilizante";
        String descripcion =
                "Producto utilizado para el mantenimiento del cultivo";
        String unidadMedida = "Litros";

        // ACT: Registrar el producto
        ProductoAgricola producto = servicio.registrar(
                codigo,
                nombre,
                tipo,
                descripcion,
                unidadMedida
        );

        // ASSERT: Verificar los datos registrados
        assertNotNull(producto);
        assertEquals("PROD-001", producto.getCodigo());
        assertEquals("Fertilizante Orgánico", producto.getNombre());
        assertEquals("Fertilizante", producto.getTipo());
        assertEquals(descripcion, producto.getDescripcion());
        assertEquals("Litros", producto.getUnidadMedida());

        // ACT: Consultar el producto registrado
        ProductoAgricola consultado =
                servicio.consultar("PROD-001");

        // ASSERT: Verificar la consulta
        assertNotNull(consultado);
        assertEquals("PROD-001", consultado.getCodigo());
        assertEquals("Fertilizante Orgánico", consultado.getNombre());
        assertEquals("Fertilizante", consultado.getTipo());
        assertEquals(descripcion, consultado.getDescripcion());
        assertEquals("Litros", consultado.getUnidadMedida());
    }
    

    // CP-015: Modificación de producto agrícola
    @Test
    void modificarProductoAgricola() {

        // ARRANGE: Preparar los datos
        ProductoService servicio = new ProductoService();

        servicio.registrar(
                "PROD-001",
                "Fertilizante Orgánico",
                "Fertilizante",
                "Producto utilizado para el mantenimiento del cultivo",
                "Litros"
        );

        // ACT: Modificar el producto
        ProductoAgricola actualizado = servicio.modificar(
                "PROD-001",
                "Fertilizante Orgánico Mejorado",
                "Fertilizante",
                "Producto mejorado para el mantenimiento del cultivo",
                "Litros"
        );

        // ASSERT: Verificar los datos actualizados
        assertNotNull(actualizado);
        assertEquals("PROD-001", actualizado.getCodigo());
        assertEquals(
                "Fertilizante Orgánico Mejorado",
                actualizado.getNombre()
        );
        assertEquals("Fertilizante", actualizado.getTipo());
        assertEquals(
                "Producto mejorado para el mantenimiento del cultivo",
                actualizado.getDescripcion()
        );
        assertEquals("Litros", actualizado.getUnidadMedida());

        // ACT: Consultar nuevamente el producto
        ProductoAgricola consultado =
                servicio.consultar("PROD-001");

        // ASSERT: Verificar que los cambios se conservaron
        assertNotNull(consultado);
        assertEquals("PROD-001", consultado.getCodigo());
        assertEquals(
                "Fertilizante Orgánico Mejorado",
                consultado.getNombre()
        );
        assertEquals(
                "Producto mejorado para el mantenimiento del cultivo",
                consultado.getDescripcion()
        );
        assertEquals("Fertilizante", consultado.getTipo());
        assertEquals("Litros", consultado.getUnidadMedida());
    }
    

    // CP-016: Escenario A - Eliminar producto sin aplicaciones
    @Test
    void eliminarProductoSinAplicaciones() {

        // ARRANGE: Preparar los datos
        ProductoService servicio = new ProductoService();

        servicio.registrar(
                "PROD-002",
                "Fungicida Biológico",
                "Fungicida",
                "Producto para controlar hongos no deseados",
                "Litros"
        );

        // ACT: Eliminar el producto registrado
        servicio.eliminar("PROD-002");

        // ASSERT: Verificar que el producto ya no existe
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> servicio.consultar("PROD-002")
        );

        assertEquals(
                "El producto no existe",
                error.getMessage()
        );
    }
    

    // CP-016: Escenario B - Producto con aplicaciones
    @Test
    void impedirEliminarProductoConAplicaciones() {

        // ARRANGE: Preparar los datos
        ProductoService productoService =
                new ProductoService();

        productoService.registrar(
                "PROD-003",
                "Fertilizante Líquido",
                "Fertilizante",
                "Producto para el mantenimiento del cultivo",
                "Litros"
        );

        AplicacionService aplicacionService =
                new AplicacionService(productoService);

        aplicacionService.registrar(
                "APL-001",
                "PROD-003",
                LocalDate.now(),
                2.5
        );

        // ACT: Intentar eliminar el producto utilizado
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> productoService.eliminar("PROD-003")
        );

        // ASSERT: Verificar el mensaje
        assertEquals(
                "No se puede eliminar un producto con aplicaciones",
                error.getMessage()
        );

        // Comprobar que el producto continúa registrado
        ProductoAgricola producto =
                productoService.consultar("PROD-003");

        assertNotNull(producto);
        assertEquals("PROD-003", producto.getCodigo());
        assertEquals(
                "Fertilizante Líquido",
                producto.getNombre()
        );
    }



}
