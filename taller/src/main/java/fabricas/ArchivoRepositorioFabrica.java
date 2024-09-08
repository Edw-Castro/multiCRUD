package fabricas;

import persistencia.archivos.ArchivoRepositorio;
import repositorio.DatosRepositorio;

public class ArchivoRepositorioFabrica implements CrudRepositorioFabrica {
    private String archivo;

    public ArchivoRepositorioFabrica(String archivo) {
        this.archivo = archivo;
    }

    @Override
    public <T> DatosRepositorio<T> crearRepositorio(Class<T> tipoEntidad) {
        return new ArchivoRepositorio<>(tipoEntidad, archivo);
    }
}