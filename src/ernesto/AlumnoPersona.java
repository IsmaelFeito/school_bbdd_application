/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ernesto;

/**
 *
 * @author ifeito-m
 */
    // Clase interna para representar alumnos
public class AlumnoPersona {
    private int id;
    private String nombre;
    private String apellidos;
    private String curso;

    public AlumnoPersona(int id, String nombre, String apellidos, String curso) {
	this.id = id;
	this.nombre = nombre;
	this.apellidos = apellidos;
	this.curso = curso;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellidos() { return apellidos; }
    public String getCurso() { return curso; }
}
