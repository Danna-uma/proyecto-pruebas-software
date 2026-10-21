
package com.ostraverde.service;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import com.ostraverde.model.Aplicacion;

public class AplicacionService {

    private final ProductoService productoService;
    private final SiembraService siembraService;
    private final CosechaService cosechaService;

    private final List<Aplicacion> aplicaciones =
            new ArrayList<>();

    // Constructor original para conservar pruebas anteriores
    public AplicacionService(ProductoService productoService) {
        this.productoService = productoService;
        this.siembraService = null;
        this.cosechaService = null;
    }

    // Constructor con los servicios relacionados
    public AplicacionService(
            ProductoService productoService,
            SiembraService siembraService,
            CosechaService cosechaService) {

        this.productoService = productoService;
        this.siembraService = siembraService;
        this.cosechaService = cosechaService;
    }

    // Registro original de aplicaciones
    public Aplicacion registrar(
            String codigo,
            String codigoProducto,
            LocalDate fecha,
            double cantidad) {

        // CP-019: Validar la cantidad aplicada
        validarCantidad(cantidad);

        // Verificar que el producto exista
        productoService.consultar(codigoProducto);

        // Crear la aplicación
        Aplicacion nuevaAplicacion = new Aplicacion(
                codigo,
                codigoProducto,
                fecha,
                cantidad
        );

        // Guardar la aplicación
        aplicaciones.add(nuevaAplicacion);

        // Marcar el producto como utilizado
        productoService.registrarUsoEnAplicacion(codigoProducto);

        return nuevaAplicacion;
    }

    // CP-020: Registrar aplicación asociada a una siembra
    public Aplicacion registrar(
            String codigo,
            LocalDate fecha,
            String codigoSiembra,
            String codigoProducto,
            double cantidad,
            String responsable,
            String observaciones) {

        // Comprobar que los servicios estén configurados
        if (siembraService == null || cosechaService == null) {
            throw new IllegalStateException(
                    "Se requieren los servicios de siembra y cosecha"
            );
        }

        // Verificar que la siembra exista
        siembraService.consultar(codigoSiembra);


        // CP-020: Verificar que el cultivo no esté cosechado
        validarCultivoNoCosechado(codigoSiembra);


        // Validar la cantidad aplicada
        validarCantidad(cantidad);

        // Verificar que el producto exista
        productoService.consultar(codigoProducto);

        // Crear la aplicación con todos sus datos
        Aplicacion nuevaAplicacion = new Aplicacion(
                codigo,
                fecha,
                codigoSiembra,
                codigoProducto,
                cantidad,
                responsable,
                observaciones
        );

        // Almacenar la aplicación
        aplicaciones.add(nuevaAplicacion);

        // Marcar el producto como utilizado
        productoService.registrarUsoEnAplicacion(codigoProducto);

        return nuevaAplicacion;
    }

    // Verificar si un producto tiene aplicaciones
    public boolean tieneAplicaciones(String codigoProducto) {

        for (Aplicacion aplicacion : aplicaciones) {

            if (aplicacion.getCodigoProducto()
                    .equals(codigoProducto)) {
                return true;
            }
        }

        return false;
    }

    // CP-017: Consultar aplicación agrícola
    public Aplicacion consultar(String codigo) {

        return buscarPorCodigo(codigo);
    }

    // Buscar una aplicación por su código
    private Aplicacion buscarPorCodigo(String codigo) {

        for (Aplicacion aplicacion : aplicaciones) {

            if (aplicacion.getCodigo().equals(codigo)) {
                return aplicacion;
            }
        }

        throw new IllegalArgumentException(
                "La aplicación no existe"
        );
    }

    // CP-019: Validar la cantidad aplicada
    private void validarCantidad(double cantidad) {

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad aplicada debe ser mayor que cero"
            );
        }
    }

    // CP-020: Validar que la siembra no tenga cosechas
    private void validarCultivoNoCosechado(String codigoSiembra) {

        if (cosechaService.tieneCosecha(codigoSiembra)) {
            throw new IllegalArgumentException(
                    "No se permiten aplicaciones en cultivos cosechados"
            );
        }
    }


 // CP-029: Consultar historial de aplicaciones (REFACTOR)
    public List<Aplicacion> consultarHistorialPorSiembra(
            String codigoSiembra) {

        // Crear la lista de resultados
        List<Aplicacion> historial = new ArrayList<>();

        // Recorrer las aplicaciones registradas
        for (Aplicacion aplicacion : aplicaciones) {

        	// Verificar si pertenece a la siembra solicitada
        	if (perteneceASiembra(aplicacion, codigoSiembra)) {
        	    historial.add(aplicacion);
        	}
        }

        return historial;
    }


    // CP-029: Verificar si una aplicación pertenece a una siembra
    private boolean perteneceASiembra(
            Aplicacion aplicacion,
            String codigoSiembra) {

        return codigoSiembra.equals(
                aplicacion.getCodigoSiembra()
        );
    }





}
