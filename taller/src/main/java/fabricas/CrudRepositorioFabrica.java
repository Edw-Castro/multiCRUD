package fabricas;

import repositorio.DatosRepositorio;

public interface CrudRepositorioFabrica {
    <T> DatosRepositorio<T> crearRepositorio(Class<T> tipoEntidad);
}