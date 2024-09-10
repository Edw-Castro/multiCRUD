package controlador;

import fabricas.ArchivoRepositorioFabrica;
import fabricas.CrudRepositorioFabrica;
import fabricas.SqlRepositorioFabrica;
import repositorio.ConexionBaseDatos;
import repositorio.DatosRepositorio;

public class Controlador {
    public <T> DatosRepositorio<T> hacerAlgo(int op, ConexionBaseDatos h2BaseDatos, String nombreArchivo,
            Class<T> clase) {
        switch (op) {
            case 1:
                // Fabrica para SQL
                CrudRepositorioFabrica fabricaSql = new SqlRepositorioFabrica(h2BaseDatos);
                return fabricaSql.crearRepositorio(clase);
            case 2:
                // Fabrica para archivo
                CrudRepositorioFabrica fabricaTxt = new ArchivoRepositorioFabrica();
                return fabricaTxt.crearRepositorio(clase);
            default:
                throw new IllegalArgumentException("Operación no válida: " + op);
        }
    }

}
