/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ernesto;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author ifeito-m
 */
public class ButtonsInterface extends JPanel{
    private JButton profesorButton;
    private JButton alumnoButton;
    private JButton salirButton;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    public ButtonsInterface(CardLayout cardLayout, JPanel mainPanel) {	
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
	
	setLayout(new FlowLayout());
	
	this.salirButton = new JButton("Salir");
        this.profesorButton = new JButton("Profesor");
        this.alumnoButton = new JButton("Alumno");	
	
	add(this.profesorButton);
	add(this.alumnoButton);
	add(this.salirButton);

        profesorButton.addActionListener(e -> cardLayout.show(mainPanel, "ProfesorPanel"));
        alumnoButton.addActionListener(e -> cardLayout.show(mainPanel, "AlumnoPanel"));
        salirButton.addActionListener(e -> System.exit(0));
	
    }    
}
