package persistencia.archivos;

import repositorio.DatosRepositorio;

import java.io.*;
import java.lang.reflect.Field;
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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, true))) {
            // Serializar el objeto a texto
            String objetoSerializado = serializar(objeto);
            writer.write(objetoSerializado);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public T leerObjeto(int id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                // Deserializar el texto en un objeto
                T objeto = deserializar(linea);
                if (obtenerId(objeto) == id) {
                    return objeto;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void actualizarObjeto(T objeto, String id) {
        List<T> objetos = new ArrayList<>();
        boolean actualizado = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                T objExistente = deserializar(linea);
                if (obtenerId(objExistente) == Integer.parseInt(id)) {
                    objetos.add(objeto); // Actualizar con el nuevo objeto
                    actualizado = true;
                } else {
                    objetos.add(objExistente);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Guardar de nuevo todos los objetos en el archivo
        if (actualizado) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
                for (T obj : objetos) {
                    writer.write(serializar(obj));
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void eliminarObjeto(String id) {
        List<T> objetos = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                T objExistente = deserializar(linea);
                if (obtenerId(objExistente) != Integer.parseInt(id)) {
                    objetos.add(objExistente);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Sobrescribir el archivo sin el objeto eliminado
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
            for (T obj : objetos) {
                writer.write(serializar(obj));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Serializa el objeto en una línea de texto
    // Cambiar T por Object para aceptar cualquier tipo de objeto
    private String serializar(Object objeto) {
        StringBuilder sb = new StringBuilder();
        Field[] campos = objeto.getClass().getDeclaredFields();
        for (Field campo : campos) {
            campo.setAccessible(true);
            try {
                Object valor = campo.get(objeto);
                if (esObjetoAnidado(campo)) {
                    // Obtener el ID del objeto anidado
                    int idAnidado = obtenerId(valor);
                    sb.append(idAnidado).append(";");
                } else {
                    sb.append(valor).append(";");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    // Deserializa un objeto a partir de una línea de texto
    private T deserializar(String texto) {
        try {
            T objeto = entidad.getDeclaredConstructor().newInstance();
            String[] valores = texto.split(";");
            Field[] campos = entidad.getDeclaredFields();

            if (valores.length != campos.length) {
                throw new IllegalStateException(
                        "El número de campos en el archivo no coincide con la estructura del objeto.");
            }

            int indiceValor = 0;

            for (Field campo : campos) {
                campo.setAccessible(true);
                if (esObjetoAnidado(campo)) {
                    Class<?> tipoCampo = campo.getType();
                    // Leer el ID del objeto anidado
                    int idAnidado = Integer.parseInt(valores[indiceValor]);
                    // Crear y usar el repositorio adecuado
                    String nombreArchivoAnidado = tipoCampo.getSimpleName() + ".txt";
                    DatosRepositorio<?> repoAnidado = new ArchivoRepositorio<>(tipoCampo, nombreArchivoAnidado);
                    Object objetoAnidado = repoAnidado.leerObjeto(idAnidado);
                    campo.set(objeto, objetoAnidado);
                } else {
                    Object valorConvertido = convertirTipo(campo.getType(), valores[indiceValor]);
                    campo.set(objeto, valorConvertido);
                }
                indiceValor++;
            }
            return objeto;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Convierte el tipo de campo de String al tipo del campo
    private Object convertirTipo(Class<?> tipo, String valor) {
        if (tipo == int.class || tipo == Integer.class) {
            return Integer.parseInt(valor);
        } else if (tipo == double.class || tipo == Double.class) {
            return Double.parseDouble(valor);
        } else if (tipo == boolean.class || tipo == Boolean.class) {
            return Boolean.parseBoolean(valor);
        }
        return valor; // Para Strings u otros tipos complejos
    }

    // Comprueba si el campo es un objeto anidado
    private boolean esObjetoAnidado(Field campo) {
        return !campo.getType().isPrimitive() && !campo.getType().equals(String.class);
    }

    // Obtiene el ID del objeto, asumiendo que el ID es el primer campo del objeto
    private int obtenerId(Object objeto) {
        try {
            Field campoId = objeto.getClass().getDeclaredFields()[0];
            campoId.setAccessible(true);
            return (int) campoId.get(objeto);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
