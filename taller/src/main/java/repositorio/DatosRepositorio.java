package repositorio;

public interface DatosRepositorio<T> {

    public void crearObjeto(T obj);

    public T leerObjeto(int id);

    public void actualizarObjeto(T objeto, String i);

    public void eliminarObjeto(String id);
}
