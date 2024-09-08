package repositorio;

import dominio.Persona;

public interface PersonaRepositorio {
    public void crear(Persona Persona);

    public Persona leer(int id);

    public void actualizar(int id, Persona persona);

    public void eliminar(int id);
}
