package persistencia.archivos;

import repositorio.DatosRepositorio;
import persistencia.funcionesAuxiliares.FuncionesAuxiliares;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import fabricas.ArchivoRepositorioFabrica;
import fabricas.CrudRepositorioFabrica;

public class ArchivoRepositorio<T> implements DatosRepositorio<T> {

    private Class<T> entidad;
    private File archivo;

    public ArchivoRepositorio(Class<T> entidad, String nombreArchivo) {
        this.entidad = entidad;
        this.archivo = new File(nombreArchivo);
    }

    @Override
    public void crearObjeto(T objeto) {
        if (FuncionesAuxiliares.ArchivoExisteId(objeto, archivo, entidad)) {
            System.err.println("El objeto con este ID ya existe.");
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))) {
            StringBuilder sb = new StringBuilder();

            // Guardar el nombre del tipo de objeto
            sb.append(entidad.getSimpleName()).append(",");

            for (Field campo : entidad.getDeclaredFields()) {
                campo.setAccessible(true);
                Object valor = campo.get(objeto);

                // Si el campo es un objeto complejo, se guarda el ID
                if (valor != null) {
                    if (FuncionesAuxiliares.esObjetoComplejo(campo)) {
                        Object idValor = FuncionesAuxiliares.obtenerValorIdObjeto(valor);
                        sb.append(idValor.toString()).append(",");
                    } else {
                        sb.append(valor.toString()).append(",");
                    }
                }
            }

            // Quitar la última coma y agregar una nueva línea
            if (sb.length() > 0) {
                sb.setLength(sb.length() - 1);
            }

            bw.write(sb.toString());
            bw.newLine();

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

                // Verificar que el tipo de objeto en la línea coincida con el tipo esperado
                if (!valores[0].equals(entidad.getSimpleName())) {
                    // Saltar si no es del mismo tipo
                    continue;
                }

                // Se asume al id como el segundo campo
                int idEnArchivo = Integer.parseInt(valores[1]);

                if (idEnArchivo == id) {
                    T objeto = entidad.getDeclaredConstructor().newInstance();

                    Field[] campos = entidad.getDeclaredFields();
                    for (int i = 0; i < campos.length; i++) {
                        campos[i].setAccessible(true);
                        // Si el campo es un objeto complejo, leer ese objeto por su ID
                        if (FuncionesAuxiliares.esObjetoComplejo(campos[i])) {
                            Class<?> claseObjetoComplejo = campos[i].getType();

                            CrudRepositorioFabrica fabrica = new ArchivoRepositorioFabrica(archivo.getName());
                            DatosRepositorio<?> repositorioObjetoComplejo = fabrica
                                    .crearRepositorio(claseObjetoComplejo);

                            Object objetoComplejo = repositorioObjetoComplejo
                                    .leerObjeto(Integer.parseInt(valores[i + 1]));

                            campos[i].set(objeto, objetoComplejo);
                        } else {
                            Object valorConvertido = FuncionesAuxiliares.convertirTipoCampo(campos[i], valores[i + 1]);
                            campos[i].set(objeto, valorConvertido);
                        }
                    }

                    return objeto;
                }
            }
        } catch (Exception e) {
            System.err.println("Error al leer del archivo: " + e.getMessage());
        }

        return null;
    }

    @Override
    public void actualizarObjeto(T objeto, String id) {
        List<String> lineasActualizadas = new ArrayList<>();
        boolean objetoActualizado = false;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] valores = linea.split(",");

                // El ID se asume como el primer campo en cada línea
                String idEnArchivo = valores[0];

                if (idEnArchivo.equals(id)) {
                    // Creamos la nueva línea para el objeto actualizado
                    StringBuilder sb = new StringBuilder();

                    Field[] campos = entidad.getDeclaredFields();
                    for (int i = 0; i < campos.length; i++) {
                        campos[i].setAccessible(true);
                        Object valor = campos[i].get(objeto);

                        if (valor != null) {
                            // Si el campo es un objeto complejo, se actualiza el id correspondiente
                            if (FuncionesAuxiliares.esObjetoComplejo(campos[i])) {
                                Object idValor = FuncionesAuxiliares.obtenerValorIdObjeto(valor);

                                sb.append(idValor.toString()).append(",");
                            } else {
                                sb.append(valor.toString()).append(",");
                            }
                        }
                    }

                    // Quitar la última coma y agregar la nueva línea actualizada
                    if (sb.length() > 0) {
                        sb.setLength(sb.length() - 1);
                    }

                    lineasActualizadas.add(sb.toString());
                    objetoActualizado = true;
                } else {
                    lineasActualizadas.add(linea); // Mantener la línea original si no es el objeto a actualizar
                }
            }
        } catch (Exception e) {
            System.err.println("Error al leer del archivo: " + e.getMessage());
            return;
        }

        // Si se encontró y actualizó el objeto, escribimos de nuevo el archivo
        if (objetoActualizado) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
                for (String lineaActualizada : lineasActualizadas) {
                    bw.write(lineaActualizada);
                    bw.newLine();
                }
                System.out.println("Objeto actualizado correctamente en el archivo.");
            } catch (Exception e) {
                System.err.println("Error al escribir en el archivo: " + e.getMessage());
            }
        } else {
            System.err.println("El objeto con ID " + id + " no fue encontrado.");
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
}
