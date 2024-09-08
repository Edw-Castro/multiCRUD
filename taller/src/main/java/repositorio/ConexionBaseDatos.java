package repositorio;

import java.sql.Connection;

public interface ConexionBaseDatos {
    Connection obtenerBaseDatos();

    void CerrarBaseDatos();
}
