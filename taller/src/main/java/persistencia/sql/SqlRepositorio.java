package persistencia.sql;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import repositorio.ConexionBaseDatos;
import repositorio.DatosRepositorio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fabricas.CrudRepositorioFabrica;
import fabricas.SqlRepositorioFabrica;
import infraestructura.baseDeDatos.Conexion;

public class SqlRepositorio<T> implements DatosRepositorio<T> {

    private Connection db;
    private Class<T> entidad;

    public SqlRepositorio(Class<T> entidad, ConexionBaseDatos bd) {
        this.entidad = entidad;
        this.db = bd.obtenerBaseDatos();
    }

    @Override
    public void crearObjeto(T objeto) {
        String tabla = entidad.getSimpleName();
        StringBuilder campos = new StringBuilder();
        StringBuilder valores = new StringBuilder();

        for (Field campo : entidad.getDeclaredFields()) {
            campo.setAccessible(true);
            try {
                Object valor = campo.get(objeto);
                if (valor != null) {
                    valor = obtenerValorCampo(valor);
                    campos.append(campo.getName()).append(", ");
                    valores.append("'").append(valor.toString()).append("', ");
                }
            } catch (IllegalAccessException e) {
                System.err.println("Error al acceder al campo: " + e.getMessage());
            }
        }

        ajustarLongitudDeStrings(campos);
        ajustarLongitudDeStrings(valores);

        String sql = "INSERT INTO " + tabla + " (" + campos.toString() + ") VALUES (" + valores.toString() + ")";

        ejecutarSentenciaSQL(sql);
    }

    @Override
    public T leerObjeto(int id) {
        String tabla = entidad.getSimpleName();
        String sql = "SELECT * FROM " + tabla + " WHERE id" + tabla + " = ?";

        try {
            PreparedStatement pstmt = db.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Crear una nueva instancia del objeto de la entidad
                T objeto = (T) entidad.getDeclaredConstructor().newInstance();

                // Recorremos los campos de la entidad y sus valores
                for (Field campo : entidad.getDeclaredFields()) {
                    campo.setAccessible(true);
                    Object valor = rs.getObject(campo.getName());

                    if (!campo.getType().isPrimitive() && !campo.getType().equals(String.class)) {
                        // Si el campo es un objeto complejo, lo cargamos recursivamente
                        Class<?> tipoCampo = campo.getType();
                        ConexionBaseDatos h2BaseDatos = new Conexion();
                        CrudRepositorioFabrica fabrica = new SqlRepositorioFabrica(h2BaseDatos);
                        SqlRepositorio<?> auxRepo = (SqlRepositorio<?>) fabrica.crearRepositorio(tipoCampo);

                        // Asumimos que 'valor' es el ID del objeto relacionado
                        Object objetoRelacionado = auxRepo.leerObjeto((Integer) valor);
                        campo.set(objeto, objetoRelacionado);
                    } else {
                        // Asignar el valor directamente al campo
                        campo.set(objeto, valor);
                    }
                }
                return objeto;
            } else {
                System.out.println("No se encontró el objeto con ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta: " + e.getMessage());
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException
                | InvocationTargetException e) {
            e.printStackTrace(); // Manejamos cualquier otra excepción
        }

        return null;
    }

    @Override
    public void actualizarObjeto(T objeto, String id) {
        String tabla = entidad.getSimpleName();
        StringBuilder valores = new StringBuilder();

        // Construimos la consulta SQL para actualizar
        for (Field campo : entidad.getDeclaredFields()) {
            campo.setAccessible(true);
            try {
                Object valor = campo.get(objeto);
                if (valor != null) {
                    // Si el campo es un objeto y no un tipo primitivo ni un String
                    if (!campo.getType().isPrimitive() && !(valor instanceof String)) {
                        // Obtener el valor de ID del objeto si existe
                        if (tieneMetodoGetId(valor)) {
                            valor = valor.getClass().getMethod("getId").invoke(valor);
                        }
                    }
                    valores.append(campo.getName()).append(" = '").append(valor.toString()).append("', ");
                }
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        // Quitar la coma sobrante
        if (valores.length() > 0) {
            valores.setLength(valores.length() - 2);
        }

        // Crear la consulta SQL
        String sql = "UPDATE " + tabla + " SET " + valores.toString() + " WHERE id" + tabla + "= ?";

        try {
            PreparedStatement pstmt = db.prepareStatement(sql);
            pstmt.setString(1, id); // Establecemos el valor del ID en la consulta
            pstmt.executeUpdate();
            System.out.println("Objeto actualizado exitosamente.");
        } catch (SQLException e) {
            System.out.println("Error al actualizar el objeto: " + e.getMessage());
        }
    }

    @Override
    public void eliminarObjeto(String id) {
        String tabla = entidad.getSimpleName();
        String sql = "DELETE FROM " + tabla + " WHERE id" + tabla + " = ?";

        try {
            PreparedStatement pstmt = db.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            System.out.println("Objeto eliminado exitosamente.");
        } catch (SQLException e) {
            System.out.println("Error al eliminar el objeto: " + e.getMessage());
        }
    }

    private Object obtenerValorCampo(Object valor) {
        // Evitar invocar métodos en tipos que no tienen getId
        if (!valor.getClass().isPrimitive() && !(valor instanceof String) && tieneMetodoGetId(valor)) {
            try {
                return valor.getClass().getMethod("getId").invoke(valor);
            } catch (NoSuchMethodException e) {
                System.err.println("Método getId no encontrado: " + e.getMessage());
            } catch (InvocationTargetException | IllegalAccessException e) {
                System.err.println("Error al invocar el método getId: " + e.getMessage());
            }
        }
        return valor;
    }

    // Método para verificar si la clase tiene el método getId
    public boolean tieneMetodoGetId(Object valor) {
        try {
            valor.getClass().getMethod("getId");
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    public void ajustarLongitudDeStrings(StringBuilder sb) {
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2);
        }
    }

    public void ejecutarSentenciaSQL(String sql) {
        try (PreparedStatement pstmt = db.prepareStatement(sql)) {
            pstmt.executeUpdate();
            System.out.println("Objeto agregado exitosamente.");
        } catch (SQLException e) {
            System.err.println("Error al agregar el objeto: " + e.getMessage());
        }
    }

}
