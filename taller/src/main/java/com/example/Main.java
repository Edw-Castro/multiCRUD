package com.example;

import dominio.Cargo;
import dominio.Departamento;
import dominio.Direccion;
import dominio.Empleado;
import dominio.Estudiante;
import dominio.Municipio;
import dominio.Pais;
import dominio.Persona;
import fabricas.ArchivoRepositorioFabrica;
import fabricas.CrudRepositorioFabrica;
import fabricas.SqlRepositorioFabrica;
import infraestructura.baseDeDatos.Conexion;
import repositorio.ConexionBaseDatos;
import repositorio.DatosRepositorio;

public class Main {

        public static void main(String[] args) {

                // ConexionBaseDatos h2BaseDatos = new Conexion();

                // Crear la fábrica que utiliza el archivo Hola.txt
                CrudRepositorioFabrica fabricaTxt = new ArchivoRepositorioFabrica("Hola.txt");
                // Crear la fábrica que utiliza sql
                // CrudRepositorioFabrica fabricaSql = new SqlRepositorioFabrica(h2BaseDatos);

                // Crear el repositorio para la clase Pais
                DatosRepositorio<Pais> paisRepoTxt = fabricaTxt.crearRepositorio(Pais.class);
                // DatosRepositorio<Pais> paisRepoSql = fabricaSql.crearRepositorio(Pais.class);

                Pais pais = new Pais(123, "Colombia");
                // // paisRepoSql.crearObjeto(pais);
                paisRepoTxt.crearObjeto(pais);
                System.out.println(paisRepoTxt.leerObjeto(123));

                DatosRepositorio<Departamento> departamentoRepoTxt = fabricaTxt.crearRepositorio(Departamento.class);
                // DatosRepositorio<Departamento> departamentoRepoSql =
                // fabricaSql.crearRepositorio(Departamento.class);

                Departamento departamento = new Departamento(123, "Meta2", pais);

                // departamentoRepoSql.crearObjeto(departamento);
                departamentoRepoTxt.crearObjeto(departamento);

                System.out.println(departamentoRepoTxt.leerObjeto(123));

                DatosRepositorio<Municipio> municipioRepoTxt = fabricaTxt.crearRepositorio(Municipio.class);
                // // // DatosRepositorio<Municipio> municipioRepoSql =
                // // fabricaSql.crearRepositorio(Municipio.class);

                Municipio municipio = new Municipio(1, "Restrepo", departamento);

                // // municipioRepoSql.crearObjeto(municipio);
                municipioRepoTxt.crearObjeto(municipio);
                System.out.println(municipioRepoTxt.leerObjeto(1));

                DatosRepositorio<Direccion> direccionRepoTxt = fabricaTxt.crearRepositorio(Direccion.class);
                // // DatosRepositorio<Direccion> direccionRepoSql =
                // fabricaSql.crearRepositorio(Direccion.class);

                Direccion direccion = new Direccion(1, "Calle 10", "Carrera 45",
                                "4.5981,-75.5751", "Apartamento 101",
                                municipio, departamento, pais);

                // // direccionRepoSql.crearObjeto(direccion);
                direccionRepoTxt.crearObjeto(direccion);
                System.out.println(direccionRepoTxt.leerObjeto(1));

        }
}
