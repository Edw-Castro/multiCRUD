package com.example;

import dominio.Pais;
import fabricas.ArchivoRepositorioFabrica;
import fabricas.CrudRepositorioFabrica;
import repositorio.DatosRepositorio;

public class Main {

        public static void main(String[] args) {

                CrudRepositorioFabrica fabricaTxt = new ArchivoRepositorioFabrica("Hola.txt");
                DatosRepositorio<Pais> paisRepoTxt = fabricaTxt.crearRepositorio(Pais.class);
                Pais pais2 = new Pais(143, "Mexico");

                paisRepoTxt.crearObjeto(pais2);
                paisRepoTxt.leerObjeto(143);
                paisRepoTxt.eliminarObjeto("143");

        }
}