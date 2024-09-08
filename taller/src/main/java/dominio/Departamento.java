package dominio;

public class Departamento {
    private int idDepartamento;
    private String nombreDepartamento;
    private Pais pais;

    public Departamento() {
    };

    public Departamento(int idDepartamento, String nombreDepartamento, Pais pais) {
        this.idDepartamento = idDepartamento;
        this.nombreDepartamento = nombreDepartamento;
        this.pais = pais;
    }

    public int getId() {
        return idDepartamento;
    }

    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public String getNombreDepartamento() {
        return nombreDepartamento;
    }

    public void setNombreDepartamento(String nombreDepartamento) {
        this.nombreDepartamento = nombreDepartamento;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    @Override
    public String toString() {
        return "Departamento[idDepartamento=" + idDepartamento + ", nombreDepartamento=" + nombreDepartamento +
                ", pais=" + pais + "]";
    }
}
