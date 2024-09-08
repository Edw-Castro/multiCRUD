package persistencia.archivos;

import repositorio.DatosRepositorio;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ArchivoRepositorio<T> implements DatosRepositorio<T> {

    private Class<T> entidad;
    private File archivo;

    public ArchivoRepositorio(Class<T> entidad, String nombreArchivo) {
        this.entidad = entidad;
        this.archivo = new File(nombreArchivo);
    }

    @Override
    public void crearObjeto(T objeto) {
        if (existeId(objeto)) {
            System.err.println("El objeto con este ID ya existe.");
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))) {
            StringBuilder sb = new StringBuilder();

            for (Field campo : entidad.getDeclaredFields()) {
                campo.setAccessible(true);
                Object valor = campo.get(objeto);
                if (valor != null) {
                    sb.append(valor.toString()).append(",");
                }
            }

            // Quitar la última coma y agregar una nueva línea
            if (sb.length() > 0) {
                sb.setLength(sb.length() - 1);
            }
            bw.write(sb.toString());
            bw.newLine();

            System.out.println("Objeto guardado en el archivo.");
        } catch (Exception e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

    @Override
    public T leerObjeto(int id) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] valores = linea.split(",");
                int idEnArchivo = Integer.parseInt(valores[0]); // Asumimos que el ID es el primer campo

                System.out.println("Leyendo línea: " + linea);
                System.out.println("ID en archivo: " + idEnArchivo);

                if (idEnArchivo == id) {
                    T objeto = entidad.getDeclaredConstructor().newInstance();

                    Field[] campos = entidad.getDeclaredFields();
                    for (int i = 0; i < campos.length; i++) {
                        campos[i].setAccessible(true);
                        Object valorConvertido = convertirTipoCampo(campos[i], valores[i]);
                        campos[i].set(objeto, valorConvertido);
                    }

                    return objeto;
                }
            }
        } catch (Exception e) {
            System.err.println("Error al leer del archivo: " + e.getMessage());
        }

        return null; // Retorna null si no se encuentra el objeto con el ID especificado
    }

    @Override
    public void actualizarObjeto(T objeto, String id) {
        List<String> lineas = new ArrayList<>();

        // Leer todas las líneas en memoria
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
        } catch (Exception e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }

        // Modificar la línea correspondiente
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (String linea : lineas) {
                String[] valores = linea.split(",");
                if (valores[0].equals(id)) {
                    StringBuilder sb = new StringBuilder();
                    for (Field campo : entidad.getDeclaredFields()) {
                        campo.setAccessible(true);
                        Object valor = campo.get(objeto);
                        if (valor != null) {
                            sb.append(valor.toString()).append(",");
                        }
                    }
                    if (sb.length() > 0) {
                        sb.setLength(sb.length() - 1);
                    }
                    bw.write(sb.toString());
                } else {
                    bw.write(linea);
                }
                bw.newLine();
            }
            System.out.println("Objeto actualizado en el archivo.");
        } catch (Exception e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

    @Override
    public void eliminarObjeto(String id) {
        List<String> lineas = new ArrayList<>();

        // Leer todas las líneas en memoria
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
        } catch (Exception e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }

        // Sobrescribir el archivo sin la línea que se quiere eliminar
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (String linea : lineas) {
                String[] valores = linea.split(",");
                if (!valores[0].equals(id)) { // El ID está en la primera posición
                    bw.write(linea);
                    bw.newLine();
                }
            }
            System.out.println("Objeto eliminado del archivo.");
        } catch (Exception e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

    private boolean existeId(T objeto) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] valores = linea.split(",");
                Field campoId = entidad.getDeclaredFields()[0]; // Asumimos que el ID es el primer campo
                campoId.setAccessible(true);
                Object idObjeto = campoId.get(objeto);

                if (valores[0].equals(idObjeto.toString())) {
                    return true; // El ID ya existe
                }
            }
        } catch (Exception e) {
            System.err.println("Error al verificar si el ID existe: " + e.getMessage());
        }

        return false;
    }

    private Object convertirTipoCampo(Field campo, String valor) {
        if (campo.getType().equals(int.class)) {
            return Integer.parseInt(valor);
        } else if (campo.getType().equals(double.class)) {
            return Double.parseDouble(valor);
        } else {
            return valor;
        }
    }
}
