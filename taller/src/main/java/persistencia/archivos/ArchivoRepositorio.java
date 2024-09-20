package persistencia.archivos;

import repositorio.DatosRepositorio;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ArchivoRepositorio<T> implements DatosRepositorio<T> {

    private Class<T> entidad;
    private File archivo;

    public ArchivoRepositorio(Class<T> entidad, String nombreArchivo) {
        this.entidad = entidad;
        this.archivo = new File(nombreArchivo);

        // Crear el archivo si no existe
        try {
            if (!archivo.exists()) {
                archivo.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void crearObjeto(T objeto) {
        List<T> listaObjetos = new ArrayList<>();

        // Leer los objetos existentes para no sobreescribirlos
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Archivo.txt"))) {
            listaObjetos = (List<T>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado, se creará uno nuevo.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Agregar el nuevo objeto a la lista
        listaObjetos.add(objeto);

        // Serializar la lista de objetos
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Archivo.txt"))) {
            oos.writeObject(listaObjetos);
            System.out.println("Objeto serializado correctamente.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public T leerObjeto(int filtroId) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Archivo.txt"))) {
            // Deserializar la lista de objetos
            List<T> listaDeserializada = (List<T>) ois.readObject();
            System.out.println("Lista deserializada correctamente.");

            // Buscar el primer objeto que coincida con el filtro
            for (T objeto : listaDeserializada) {
                try {
                    // Usar reflexión para llamar al método getId()
                    int id = (int) objeto.getClass().getMethod("getId").invoke(objeto);
                    if (id == filtroId) {
                        // Si coincide, retornar el objeto
                        System.out.println("Objeto encontrado: " + objeto);
                        return objeto;
                    }
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }

            // Si no se encuentra ningún objeto, devolver null
            System.out.println("No se encontró ningún objeto con ID: " + filtroId);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void actualizarObjeto(T objeto, String id) {
        // List<T> objetos = new ArrayList<>();
        // boolean actualizado = false;

        // try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
        // String linea;
        // while ((linea = reader.readLine()) != null) {
        // T objExistente = deserializar(linea);
        // if (obtenerId(objExistente) == Integer.parseInt(id)) {
        // objetos.add(objeto); // Actualizar con el nuevo objeto
        // actualizado = true;
        // } else {
        // objetos.add(objExistente);
        // }
        // }
        // } catch (IOException e) {
        // e.printStackTrace();
        // }

        // // Guardar de nuevo todos los objetos en el archivo
        // if (actualizado) {
        // try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
        // for (T obj : objetos) {
        // writer.write(serializar(obj));
        // writer.newLine();
        // }
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        // }

    }

    @Override
    public void eliminarObjeto(String id) {
        // List<T> objetos = new ArrayList<>();

        // try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
        // String linea;
        // while ((linea = reader.readLine()) != null) {
        // T objExistente = deserializar(linea);
        // if (obtenerId(objExistente) != Integer.parseInt(id)) {
        // objetos.add(objExistente);
        // }
        // }
        // } catch (IOException e) {
        // e.printStackTrace();
        // }

        // // Sobrescribir el archivo sin el objeto eliminado
        // try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
        // for (T obj : objetos) {
        // writer.write(serializar(obj));
        // writer.newLine();
        // }
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
    }

}
