package dominio;

public class Direccion {
    private int id;
    private String calle;
    private String carrera;
    private String coordenada;
    private String descripcion;
    private Municipio municipio;
    private Departamento departamento;
    private Pais pais;

    public Direccion() {
    }

    public Direccion(int id, String calle, String carrera, String coordenada, String descripcion, Municipio municipio,
            Departamento departamento, Pais pais) {
        this.id = id;
        this.calle = calle;
        this.carrera = carrera;
        this.coordenada = coordenada;
        this.descripcion = descripcion;
        this.municipio = municipio;
        this.departamento = departamento;
        this.pais = pais;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getCoordenada() {
        return coordenada;
    }

    public void setCoordenada(String coordenada) {
        this.coordenada = coordenada;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    @Override
    public String toString() {
        return "Direccion[calle=" + calle +
                ", carrera=" + carrera +
                ", coordenada=" + coordenada +
                ", descripcion=" + descripcion +
                ", municipio=" + municipio +
                ", departamento=" + departamento +
                ", pais=" + pais + "]";
    }
}
