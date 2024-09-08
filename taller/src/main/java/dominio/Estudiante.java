package dominio;

public class Estudiante extends Persona {
    private String codigo;
    private String programa;
    private Double promedio;

    Estudiante() {
    }

    public Estudiante(int id, String nombres, String apellidos, Direccion direccion, String codigo, String programa,
            Double promedio) {
        super(id, nombres, apellidos, direccion);
        this.codigo = codigo;
        this.programa = programa;
        this.promedio = promedio;
    }

    @Override
    public String toString() {
        return super.toString() + ", codigo=" + codigo + ", programa=" + programa + ", promedio=" + promedio + "]";
    }

    public String getCodigo() {
        return codigo;
    }

    public String getPrograma() {
        return programa;
    }

    public Double getPromedio() {
        return promedio;
    }
}
