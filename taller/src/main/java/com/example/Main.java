package com.example;

import controladores.Controlador;
import dominio.Departamento;
import dominio.Direccion;
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

        // Método ajustado para devolver un DatosRepositorio
        public <T> DatosRepositorio<T> hacerAlgo(int op, ConexionBaseDatos h2BaseDatos, Class<T> clase) {
                switch (op) {
                        case 1:
                                // Fabrica para SQL
                                CrudRepositorioFabrica fabricaSql = new SqlRepositorioFabrica(h2BaseDatos);
                                return fabricaSql.crearRepositorio(clase);
                        case 2:
                                // Fabrica para archivo
                                CrudRepositorioFabrica fabricaTxt = new ArchivoRepositorioFabrica();
                                return fabricaTxt.crearRepositorio(clase);
                        default:
                                throw new IllegalArgumentException("Operación no válida: " + op);
                }
        }

        public static void main(String[] args) {

                ConexionBaseDatos h2BaseDatos = new Conexion();

                Pais eeeuu = new Pais(789, "EEUU");

                Controlador controladorInstancia = new Controlador();

                DatosRepositorio<Pais> paisRepo = controladorInstancia.hacerAlgo(2, h2BaseDatos, Pais.class);
                DatosRepositorio<Departamento> departamentoRepo = controladorInstancia.hacerAlgo(2, h2BaseDatos,
                                Departamento.class);
                DatosRepositorio<Municipio> municipioRepo = controladorInstancia.hacerAlgo(2, h2BaseDatos,
                                Municipio.class);
                DatosRepositorio<Direccion> direccionRepo = controladorInstancia.hacerAlgo(2, h2BaseDatos,
                                Direccion.class);

                // GUARDANDO EN LA BD
                // paisRepo.crearObjeto(eeeuu);

                System.out.println("LEYENDO PAÍS es: " + paisRepo.leerObjeto(789));

                Departamento california = new Departamento(125, "California", eeeuu);

                // departamentoRepo.crearObjeto(california);

                Municipio losAngeles = new Municipio(852, "Los angeles", california);
                // municipioRepo.crearObjeto(losAngeles);

                Direccion direccion = new Direccion(143, "asdasd", "asdasd", "a55dasd",
                                "sadasdasd", losAngeles,
                                california, eeeuu);

                // direccionRepo.crearObjeto(direccion);

                DatosRepositorio<Estudiante> estudianteRepo = controladorInstancia.hacerAlgo(2, h2BaseDatos,
                                Estudiante.class);

                DatosRepositorio<Persona> personaRepo = controladorInstancia.hacerAlgo(2, h2BaseDatos, Persona.class);
                Persona edwPersona = new Persona(123, "Edward", "Castro", direccion);
                // personaRepo.crearObjeto(edwPersona);

                Estudiante edward = new Estudiante(123, "Edward", "Castro", direccion, "160004625", "Ing. Sistemas",
                                40.55);
                // estudianteRepo.crearObjeto(edward);
                System.out.println("LEYENDO ESTUDIANTE es: " + estudianteRepo.leerObjeto(123));

        }
}
