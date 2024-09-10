package fabricas;

import persistencia.archivos.ArchivoRepositorio;
import repositorio.DatosRepositorio;

public class ArchivoRepositorioFabrica implements CrudRepositorioFabrica {

    @Override
    public <T> DatosRepositorio<T> crearRepositorio(Class<T> clase) {
        String nombreArchivo = clase.getSimpleName() + ".txt";
        return new ArchivoRepositorio<>(clase, nombreArchivo);
    }
}