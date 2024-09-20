package dominio;

import java.io.Serializable;

public class Pais implements Serializable {
    private int idPais;
    private String nombrePais;

    public Pais() {
    }

    public Pais(int idPais, String nombrePais) {
        this.idPais = idPais;
        this.nombrePais = nombrePais;
    }

    public int getId() {
        return idPais;
    }

    public void setId(int id) {
        this.idPais = id;
    }

    public String getNombre() {
        return nombrePais;
    }

    public void setNombre(String nombre) {
        this.nombrePais = nombre;
    }

    @Override
    public String toString() {
        return "Pais[idPais=" + idPais + ", nombrePais=" + nombrePais + "]";
    }
}
