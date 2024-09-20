package dominio;

public class Estudiante extends Persona {
    private String codigo;
    private String programa;
    private Double promedio;

    public Estudiante() {
        super();
    }

    public Estudiante(int id, String nombres, String apellidos, Direccion direccion, String codigo, String programa,
            Double promedio) {
        super(id, nombres, apellidos, direccion);
        this.codigo = codigo;
        this.programa = programa;
        this.promedio = promedio;
    }

    public String getCodigo() {
        return codigo;
    }

    public int getId() {
        return super.getId();
    }

    public String getPrograma() {
        return programa;
    }

    public Double getPromedio() {
        return promedio;
    }

    @Override
    public String toString() {
        return "Estudiante { Esto viene de persona = " + super.toString() + " codigo: " + codigo + " programa: "
                + programa
                + " promedio: "
                + promedio + "}  ";
    }
}
