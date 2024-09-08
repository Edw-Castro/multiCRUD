package persistencia.funcionesAuxiliares;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;

public class FuncionesAuxiliares {

    public static boolean ArchivoExisteId(Object objeto, File archivo, Class<?> entidad) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            String id = FuncionesAuxiliares.obtenerValorIdObjeto(objeto).toString();

            while ((linea = br.readLine()) != null) {
                String[] valores = linea.split(",");
                if (valores[0].equals(entidad.getSimpleName()) && valores[1].equals(id)) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println("Error al verificar ID en el archivo: " + e.getMessage());
        }
        return false;
    }

    public static Object convertirTipoCampo(Field campo, String valor) {

        if (campo.getType().equals(int.class)) {
            return Integer.parseInt(valor);
        } else if (campo.getType().equals(double.class)) {
            return Double.parseDouble(valor);
        } else {
            return valor;
        }
    }

    public static Object obtenerValorIdObjeto(Object valor) {

        // Se evita invocar métodos en tipos que no tienen getId
        if (!valor.getClass().isPrimitive() && !(valor instanceof String) && tieneMetodoGetId(valor)) {
            try {
                return valor.getClass().getMethod("getId").invoke(valor);
            } catch (NoSuchMethodException e) {
                System.err.println("Método getId no encontrado: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error al invocar el método getId: " + e.getMessage());
            }
        }
        return valor;
    }

    public static boolean tieneMetodoGetId(Object valor) {
        try {
            valor.getClass().getMethod("getId");
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    public static void ajustarLongitudDeStrings(StringBuilder sb) {
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2);
        }
    }

    public static void ejecutarSentenciaSQL(String sql, Connection db) {
        try (PreparedStatement pstmt = db.prepareStatement(sql)) {
            pstmt.executeUpdate();
            System.out.println("Objeto agregado exitosamente.");
        } catch (SQLException e) {
            System.err.println("Error al agregar el objeto: " + e.getMessage());
        }
    }

    public static boolean esObjetoComplejo(Field campo) {
        return !campo.getType().isPrimitive() && !campo.getType().equals(String.class);
    }

}
