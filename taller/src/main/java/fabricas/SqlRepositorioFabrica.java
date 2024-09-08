package fabricas;

import persistencia.sql.SqlRepositorio;
import repositorio.ConexionBaseDatos;
import repositorio.DatosRepositorio;

public class SqlRepositorioFabrica implements CrudRepositorioFabrica {
    private ConexionBaseDatos conexionBaseDatos;

    public SqlRepositorioFabrica(ConexionBaseDatos conexionBaseDatos) {
        this.conexionBaseDatos = conexionBaseDatos;
    }

    @Override
    public <T> DatosRepositorio<T> crearRepositorio(Class<T> tipoEntidad) {
        return new SqlRepositorio<>(tipoEntidad, conexionBaseDatos);
    }
}
