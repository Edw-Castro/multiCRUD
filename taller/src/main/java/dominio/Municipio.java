package dominio;

public class Municipio {
    private int idMunicipio;
    private String nombreMunicipio;
    private Departamento departamento;

    public Municipio() {
    }

    public Municipio(int idMunicipio, String nombreMunicipio, Departamento departamento) {
        this.idMunicipio = idMunicipio;
        this.nombreMunicipio = nombreMunicipio;
        this.departamento = departamento;
    }

    // Getters y Setters
    public int getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(int idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public String getNombreMunicipio() {
        return nombreMunicipio;
    }

    public void setNombreMunicipio(String nombreMunicipio) {
        this.nombreMunicipio = nombreMunicipio;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    @Override
    public String toString() {
        return "Municipio[idMunicipio=" + idMunicipio +
                ", nombreMunicipio=" + nombreMunicipio +
                ", departamento=" + departamento + "]";
    }
}
