/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ernesto;

import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author ifeito-m
 */
public class ButtonsInterface extends JPanel{
    public JButton profesorButton;
    public JButton alumnoButton;
    public JButton salirButton;
    public JTextField desplegable;

    public ButtonsInterface() {
	this.profesorButton = new JButton("Profesor");
	this.alumnoButton = new JButton("Alumno");
	this.salirButton = new JButton("Salir");
	
	add(this.profesorButton);
	add(this.alumnoButton);
	add(this.salirButton);
    }
	
    public void openProfesor(){
	   // Al hacer clic, abre una nueva ventana
	   JFrame profesorFrame = new JFrame("Ventana Profesor");
	   profesorFrame.setSize(400, 200); // Tamaño de la nueva ventana
	   profesorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	   
	   profesorFrame.setLocationRelativeTo(null);
	   
	   profesorFrame.setVisible(true);

	   this.setVisible(false);//oculta la original al crear la de profe
    }
    
    public void openAlumno(){
	   JFrame alumnFrame = new JFrame("Ventana Alumno");
	   alumnFrame.setSize(400, 200);
	   alumnFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	   
	   alumnFrame.setLocationRelativeTo(null);
	   
	   alumnFrame.setVisible(true);
	   
	   this.setVisible(false);//oculta pestaña anterior
    }
    
    public void backButton(){
	
    }
    
    public void salirButton(){
	System.exit(0);
    }

}
