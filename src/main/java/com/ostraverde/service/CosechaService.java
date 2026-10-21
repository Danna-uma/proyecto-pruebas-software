
package com.ostraverde.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.ostraverde.model.Cosecha;
import com.ostraverde.model.Siembra;

public class CosechaService {

    private final SiembraService siembraService;
    private final TrabajadorService trabajadorService;

    // Almacenar las cosechas registradas
    private final List<Cosecha> cosechas =
            new ArrayList<>();

    // Constructor original para conservar las pruebas anteriores
    public CosechaService(SiembraService siembraService) {
        this.siembraService = siembraService;
        this.trabajadorService = null;
    }

    // CP-021: Constructor para el registro completo
    public CosechaService(
            SiembraService siembraService,
            TrabajadorService trabajadorService) {

        this.siembraService = siembraService;
        this.trabajadorService = trabajadorService;
    }

    // CP-021: Registrar una cosecha con datos básicos
    public Cosecha registrar(
            String codigo,
            String codigoSiembra,
            LocalDate fecha,
            double cantidadKilogramos) {

        // CP-023: Validar la cantidad cosechada
        validarCantidad(cantidadKilogramos);

        // Consultar la siembra asociada
        Siembra siembra = siembraService.consultar(codigoSiembra);

        // CP-024: Validar la fecha de cosecha
        validarFechaCosecha(fecha, siembra);

        // CP-022: Impedir una segunda cosecha
        validarSiembraSinCosecha(codigoSiembra);

        // Crear la nueva cosecha
        Cosecha nuevaCosecha = new Cosecha(
                codigo,
                codigoSiembra,
                fecha,
                cantidadKilogramos
        );

        // CP-025: Liberar la sección después de cosechar
        siembraService.liberarSeccion(codigoSiembra);

        // Almacenar la cosecha
        cosechas.add(nuevaCosecha);

        return nuevaCosecha;
    }

    // CP-021: Registrar una cosecha con datos completos
    public Cosecha registrar(
            String codigo,
            String codigoSiembra,
            LocalDate fecha,
            double cantidadKilogramos,
            int cantidadBandejas,
            String responsable) {

        // CP-023: Validar la cantidad cosechada
        validarCantidad(cantidadKilogramos);

        // Consultar la siembra asociada
        Siembra siembra = siembraService.consultar(codigoSiembra);

        // CP-024: Validar la fecha de cosecha
        validarFechaCosecha(fecha, siembra);

        // CP-022: Impedir una segunda cosecha
        validarSiembraSinCosecha(codigoSiembra);

        // CP-021: Verificar que el trabajador exista
        validarResponsable(responsable);

        // Crear la cosecha con todos sus datos
        Cosecha nuevaCosecha = new Cosecha(
                codigo,
                codigoSiembra,
                fecha,
                cantidadKilogramos,
                cantidadBandejas,
                responsable
        );

     // CP-025: Liberar la sección después de cosechar
        liberarSeccionDespuesDeCosecha(codigoSiembra);

        // Almacenar la cosecha
        cosechas.add(nuevaCosecha);

        return nuevaCosecha;
    }

    // CP-021: Consultar una cosecha por su código
    public Cosecha consultar(String codigo) {

        return buscarPorCodigo(codigo);
    }

    // Buscar una cosecha registrada mediante su código
    private Cosecha buscarPorCodigo(String codigo) {

        for (Cosecha cosecha : cosechas) {

            if (cosecha.getCodigo().equals(codigo)) {
                return cosecha;
            }
        }

        throw new IllegalArgumentException(
                "La cosecha no existe"
        );
    }

    // CP-023: Validar que la cantidad sea mayor que cero
    private void validarCantidad(double cantidadKilogramos) {

        if (cantidadKilogramos <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad cosechada debe ser mayor que cero"
            );
        }
    }

    // CP-024: Validar que la fecha de cosecha sea posterior
    private void validarFechaCosecha(
            LocalDate fechaCosecha,
            Siembra siembra) {

        // La cosecha debe realizarse después de la siembra
        LocalDate fechaMinima =
                siembra.getFecha().plusDays(1);

        if (fechaCosecha.isBefore(fechaMinima)) {
            throw new IllegalArgumentException(
                    "La fecha de cosecha debe ser posterior a la siembra"
            );
        }
    }

    // RF-20: Verificar si una siembra ya fue cosechada
    public boolean tieneCosecha(String codigoSiembra) {

        for (Cosecha cosecha : cosechas) {

            if (cosecha.getCodigoSiembra()
                    .equals(codigoSiembra)) {
                return true;
            }
        }

        return false;
    }

    // CP-021: Validar el trabajador responsable
    private void validarResponsable(String responsable) {

        // Verificar que el servicio esté configurado
        if (trabajadorService == null) {
            throw new IllegalStateException(
                    "Se requiere el servicio de trabajadores"
            );
        }

        // Comprobar que el trabajador exista
        trabajadorService.consultar(responsable);
    }

    // CP-022: Impedir más de una cosecha por siembra
    private void validarSiembraSinCosecha(String codigoSiembra) {

        if (tieneCosecha(codigoSiembra)) {
            throw new IllegalArgumentException(
                    "La siembra ya tiene una cosecha registrada"
            );
        }
    }

    // CP-025: Liberar la sección después de registrar una cosecha
    private void liberarSeccionDespuesDeCosecha(
            String codigoSiembra) {

        siembraService.liberarSeccion(codigoSiembra);
    }

    // RF-25: Obtener las cosechas registradas para reportes
    public List<Cosecha> listar() {

        // Devolver una copia para proteger la lista original
        return new ArrayList<>(cosechas);
    }


}
