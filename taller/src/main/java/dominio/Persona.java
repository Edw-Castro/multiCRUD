package dominio;

import java.io.Serializable;

public class Persona implements Todos, Serializable {
    private int id;
    private String nombre;
    private String apellido;
    private Direccion direccion;

    public Persona() {
    }

    public Persona(int id, String nombre, String apellido, Direccion direccion) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    @Override
    public String informacion() {
        return "Persona [id=" + id + ", nombre=" + nombre + ", apellido=" + apellido + ", direccion=" + direccion + "]";
    }

    @Override
    public String toString() {
        return informacion();
    }
}
