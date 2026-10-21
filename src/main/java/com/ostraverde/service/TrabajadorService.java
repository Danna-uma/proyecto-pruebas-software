
package com.ostraverde.service;

import java.util.ArrayList;
import java.util.List;

import com.ostraverde.model.Trabajador;

public class TrabajadorService {

    private final List<Trabajador> trabajadores =
            new ArrayList<>();

    // CP-005 y CP-006: Registrar trabajadores
    public Trabajador registrar(
            String identificacion,
            String nombre,
            String telefono,
            String correo,
            String cargo) {

        // Validar que la identificación sea única
        if (existeIdentificacion(identificacion)) {
            throw new IllegalArgumentException(
                    "La identificación del trabajador ya existe"
            );
        }

        Trabajador nuevoTrabajador = new Trabajador(
                identificacion,
                nombre,
                telefono,
                correo,
                cargo
        );

        trabajadores.add(nuevoTrabajador);

        return nuevoTrabajador;
    }

    // CP-007: Consultar trabajador
    public Trabajador consultar(String identificacion) {

        int indice = buscarIndice(identificacion);

        if (indice == -1) {
            throw new IllegalArgumentException(
                    "El trabajador no existe"
            );
        }

        return trabajadores.get(indice);
    }

    // CP-007: Actualizar teléfono
    public Trabajador actualizarTelefono(
            String identificacion,
            String nuevoTelefono) {

        int indice = buscarIndice(identificacion);

        if (indice == -1) {
            throw new IllegalArgumentException(
                    "El trabajador no existe"
            );
        }

        Trabajador trabajador = trabajadores.get(indice);

        Trabajador actualizado = new Trabajador(
                trabajador.getIdentificacion(),
                trabajador.getNombre(),
                nuevoTelefono,
                trabajador.getCorreo(),
                trabajador.getCargo()
        );

        trabajadores.set(indice, actualizado);

        return actualizado;
    }

    // Buscar trabajador por identificación
    private int buscarIndice(String identificacion) {

        for (int i = 0; i < trabajadores.size(); i++) {

            if (trabajadores.get(i).getIdentificacion()
                    .equals(identificacion)) {
                return i;
            }
        }

        return -1;
    }

    // Comprobar si existe una identificación
    private boolean existeIdentificacion(
            String identificacion) {

        return buscarIndice(identificacion) != -1;
    }
}
