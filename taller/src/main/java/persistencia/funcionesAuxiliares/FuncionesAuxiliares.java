package persistencia.funcionesAuxiliares;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;

public class FuncionesAuxiliares {

    public static Object recuperarObjetoPorId(Class<?> clase, int id) {
        String nombreArchivo = clase.getSimpleName() + ".txt"; // Archivo donde se almacenan los objetos complejos
        File archivo = new File(nombreArchivo);

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] valores = linea.split(",");
                // Asumimos que el primer valor es el tipo de objeto y el segundo es el ID
                if (valores[0].equals(clase.getSimpleName()) && Integer.parseInt(valores[1]) == id) {
                    // Crear una instancia del objeto
                    Object objeto = clase.getDeclaredConstructor().newInstance();

                    Field[] campos = clase.getDeclaredFields();
                    int indice = 2; // Saltamos el tipo y el ID

                    for (Field campo : campos) {
                        campo.setAccessible(true);
                        // Obtener el valor del archivo y asignarlo al campo
                        if (FuncionesAuxiliares.esObjetoComplejo(campo)) {
                            // Si es un objeto complejo, buscamos su ID
                            Object valorRelacionado = FuncionesAuxiliares.recuperarObjetoPorId(campo.getType(),
                                    Integer.parseInt(valores[indice]));
                            campo.set(objeto, valorRelacionado);
                        } else {
                            // Convertir el valor a su tipo adecuado
                            if (indice < valores.length) {
                                Object valor = FuncionesAuxiliares.convertirValor(campo.getType(), valores[indice]);
                                campo.set(objeto, valor);
                                indice++;
                            }
                        }
                    }
                    return objeto;
                }
            }
        } catch (Exception e) {
            System.err.println("Error al recuperar objeto por ID: " + e.getMessage());
        }
        return null;
    }

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

    public static Object convertirValor(Class<?> tipo, String valor) {
        if (tipo == int.class || tipo == Integer.class) {
            return Integer.parseInt(valor);
        } else if (tipo == double.class || tipo == Double.class) {
            return Double.parseDouble(valor);
        } else if (tipo == float.class || tipo == Float.class) {
            return Float.parseFloat(valor);
        } else if (tipo == long.class || tipo == Long.class) {
            return Long.parseLong(valor);
        } else if (tipo == boolean.class || tipo == Boolean.class) {
            return Boolean.parseBoolean(valor);
        } else if (tipo == String.class) {
            return valor;
        }
        throw new IllegalArgumentException("Tipo no soportado: " + tipo.getName());
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
