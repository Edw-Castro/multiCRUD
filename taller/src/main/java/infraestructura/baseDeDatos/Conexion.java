package infraestructura.baseDeDatos;

import java.sql.Connection;
import java.sql.DriverManager;

import repositorio.ConexionBaseDatos;
import utilsTaller.Constantes;

public final class Conexion implements ConexionBaseDatos {
    private static Connection database;

    // public static Connection getDatabase() {
    // if (database == null) {
    // try {
    // Class.forName("org.h2.Driver");
    // database = DriverManager
    // .getConnection(Constantes.urlBD, Constantes.usuarioBD, "");

    // } catch (Exception e) {
    // System.out.println("Error: " + e);
    // }
    // }

    // return database;
    // }

    @Override
    public Connection obtenerBaseDatos() {
        if (database == null) {
            try {
                Class.forName("org.h2.Driver");
                database = DriverManager
                        .getConnection(Constantes.urlBD, Constantes.usuarioBD, "");

            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }

        return database;
    }

    @Override
    public void CerrarBaseDatos() {
        try {

            Conexion.database.close();
            System.out.println("----------------------------------------------------------");
            System.out.println("Conexión a la base de datos cerrada satisfactoriamente");
            System.out.println("----------------------------------------------------------");
        } catch (Exception e) {
            System.out.println("No se pudo cerrar la bd: " + e);
        }
    }
}
