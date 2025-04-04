/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ernesto;

import java.util.Date;

/**
 *
 * @author ifeito-m
 */
    // Clase interna para representar alumnos
public class AlumnoPersona {
        private final int id;
        private final String nombre;
        private final String apellidos;
        private final int edad;
        private final Date fechaMatricula;
        private final String curso;
        private final String ciclo;
        
        public AlumnoPersona(int id, String nombre, String apellidos, int edad, 
                           Date fechaMatricula, String curso, String ciclo) {
            this.id = id;
            this.nombre = nombre;
            this.apellidos = apellidos;
            this.edad = edad;
            this.fechaMatricula = fechaMatricula;
            this.curso = curso;
            this.ciclo = ciclo;
        }
        
        // Getters
        public int getId() { return id; }
        public String getNombre() { return nombre; }
        public String getApellidos() { return apellidos; }
        public int getEdad() { return edad; }
        public Date getFechaMatricula() { return fechaMatricula; }
        public String getCurso() { return curso; }
        public String getCiclo() { return ciclo; }
    }