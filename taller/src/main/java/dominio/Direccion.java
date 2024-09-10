package dominio;

public class Direccion {
    private int iddireccion;
    private String calledireccion;
    private String carreradireccion;
    private String coordenadadireccion;
    private String descripciondireccion;
    private Municipio municipio;
    private Departamento departamento;
    private Pais pais;

    public Direccion() {
    }

    public Direccion(int id, String calle, String carrera, String coordenadadireccion, String descripciondireccion,
            Municipio municipio,
            Departamento departamento, Pais pais) {
        this.iddireccion = id;
        this.calledireccion = calle;
        this.carreradireccion = carrera;
        this.coordenadadireccion = coordenadadireccion;
        this.descripciondireccion = descripciondireccion;
        this.municipio = municipio;
        this.departamento = departamento;
        this.pais = pais;
    }

    // Getters y Setters
    public int getId() {
        return iddireccion;
    }

    public void setId(int id) {
        this.iddireccion = id;
    }

    public String getCalle() {
        return calledireccion;
    }

    public void setCalle(String calle) {
        this.calledireccion = calle;
    }

    public String getCarrera() {
        return carreradireccion;
    }

    public void setCarrera(String carrera) {
        this.carreradireccion = carrera;
    }

    public String getCoordenada() {
        return coordenadadireccion;
    }

    public void setCoordenada(String coordenadadireccion) {
        this.coordenadadireccion = coordenadadireccion;
    }

    public String getDescripcion() {
        return descripciondireccion;
    }

    public void setDescripcion(String descripciondireccion) {
        this.descripciondireccion = descripciondireccion;
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
        return "Direccion[calle=" + calledireccion +
                ", carrera=" + carreradireccion +
                ", coordenada =" + coordenadadireccion +
                ", descripcion =" + descripciondireccion +
                ", municipio =" + municipio +
                ", departamento=" + departamento +
                ", pais=" + pais + "]";
    }
}
