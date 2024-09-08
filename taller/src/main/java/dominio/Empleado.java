package dominio;

import java.io.Serializable;

public class Empleado extends Persona implements Serializable {
    private Cargo cargo;
    private double salario;

    Empleado() {
    }

    // Constructor con todos los atributos necesarios
    public Empleado(int id, String nombres, String apellidos, Direccion direccion, Cargo cargo, double salario) {
        super(id, nombres, apellidos, direccion);
        this.cargo = cargo;
        this.salario = salario;
    }

    // Getters y Setters
    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    @Override
    public String toString() {
        return super.toString() + ", cargo=" + cargo + ", salario=" + salario + "]";
    }
}
