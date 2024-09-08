package dominio;

import java.util.ArrayList;

public class ListadoTodos {
    private ArrayList<Todos> listado = new ArrayList<>();

    public int cantidad() {
        return listado.size();
    }

    public String consultar(int id) {
        if ((id < listado.size() - 1) && (listado.get(id) != null)) {
            return "existe";
        }

        return "no existe";
    }

    public String mostrar(int id) {
        return listado.get(id).informacion();
    }

    public void adicionar(Todos newTodo) {
        listado.add(newTodo);
    }
}
