
package com.ostraverde.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ostraverde.model.ProductoAgricola;

public class ProductoService {

    private final List<ProductoAgricola> productos =
            new ArrayList<>();

    // Códigos de productos utilizados en aplicaciones
    private final Set<String> productosUtilizados =
            new HashSet<>();

    // CP-014: Registrar producto agrícola
    public ProductoAgricola registrar(
            String codigo,
            String nombre,
            String tipo,
            String descripcion,
            String unidadMedida) {

        ProductoAgricola nuevoProducto =
                new ProductoAgricola(
                        codigo,
                        nombre,
                        tipo,
                        descripcion,
                        unidadMedida
                );

        productos.add(nuevoProducto);

        return nuevoProducto;
    }

    // CP-014: Consultar producto
    public ProductoAgricola consultar(String codigo) {

        int indice = obtenerIndiceExistente(codigo);

        return productos.get(indice);
    }

    // CP-015: Modificar producto agrícola
    public ProductoAgricola modificar(
            String codigo,
            String nombre,
            String tipo,
            String descripcion,
            String unidadMedida) {

        int indice = obtenerIndiceExistente(codigo);

        ProductoAgricola actualizado =
                new ProductoAgricola(
                        codigo,
                        nombre,
                        tipo,
                        descripcion,
                        unidadMedida
                );

        productos.set(indice, actualizado);

        return actualizado;
    }


    // CP-016: Eliminar producto agrícola
    public void eliminar(String codigo) {

        // Verificar que el producto exista
        int indice = obtenerIndiceExistente(codigo);

        // Validar que no tenga aplicaciones asociadas
        validarProductoSinAplicaciones(codigo);

        // Eliminar el producto registrado
        productos.remove(indice);
    }


    // Registrar la utilización de un producto
    public void registrarUsoEnAplicacion(String codigo) {

        // Verificar que el producto exista
        obtenerIndiceExistente(codigo);

        productosUtilizados.add(codigo);
    }

    // Localizar un producto existente
    private int obtenerIndiceExistente(String codigo) {

        int indice = buscarIndice(codigo);

        if (indice == -1) {
            throw new IllegalArgumentException(
                    "El producto no existe"
            );
        }

        return indice;
    }

    // Buscar producto por código
    private int buscarIndice(String codigo) {

        for (int i = 0; i < productos.size(); i++) {

            if (productos.get(i).getCodigo().equals(codigo)) {
                return i;
            }
        }

        return -1;
    }
    

    // CP-016: Validar que el producto no tenga aplicaciones
    private void validarProductoSinAplicaciones(String codigo) {

        if (productosUtilizados.contains(codigo)) {
            throw new IllegalArgumentException(
                    "No se puede eliminar un producto con aplicaciones"
            );
        }
    }

}
