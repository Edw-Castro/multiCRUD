package dominio;

import java.io.Serializable;

public class Cargo implements Todos, Serializable {
    private int id;
    private String nombre;

    public Cargo() {
    }

    public Cargo(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String informacion() {
        return "Cargo [id=" + id + ", nombre=" + nombre + "]";
    }

    @Override
    public String toString() {
        return "Cargo [id=" + id + ", nombre=" + nombre + "]";
    }
}
