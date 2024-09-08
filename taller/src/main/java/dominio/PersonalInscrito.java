package dominio;

import java.util.ArrayList;

public class PersonalInscrito {
    private ArrayList<Persona> listadoPersonas = new ArrayList<>();

    public void imprimirListado() {
        for (Persona persona : listadoPersonas) {
            persona.informacion();
        }
    }

    public void adicionar(Persona newPersona) {
        listadoPersonas.add(newPersona);
    }
}
