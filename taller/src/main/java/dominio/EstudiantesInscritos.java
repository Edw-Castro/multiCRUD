package dominio;

import java.util.ArrayList;

public class EstudiantesInscritos {
    private ArrayList<Estudiante> listadoEstudiantes = new ArrayList<>();

    public void imprimirListado() {
        for (Estudiante estudiante : listadoEstudiantes) {
            estudiante.informacion();
        }
    }

    public void adicionar(Estudiante newEstudiante) {
        listadoEstudiantes.add(newEstudiante);
    }
}
