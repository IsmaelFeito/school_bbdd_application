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
        setLayout(new FlowLayout());
	
	this.profesorButton = new JButton("Profesor");
	this.alumnoButton = new JButton("Alumno");
	this.salirButton = new JButton("Salir");
	
	add(this.profesorButton);
	add(this.alumnoButton);
	add(this.salirButton);
	
	mainPanel.add(this, "buttonsPanel");
	
	JPanel profesorPanel = createPanel("Ventana Profesor");
        Alumno alumnoPanel = new Alumno(cardLayout, mainPanel);

        profesorButton.addActionListener(e -> cardLayout.show(mainPanel, "profesorPanel"));
        alumnoButton.addActionListener(e -> cardLayout.show(mainPanel, "alumnoPanel"));
        salirButton.addActionListener(e -> System.exit(0));
	
        mainPanel.add(profesorPanel, "profesorPanel");
        mainPanel.add(this, "buttonsPanel");
	
    }
    private JPanel createPanel(String name){
	JPanel panel = new JPanel(new BorderLayout());
	
	JLabel label = new JLabel(name, JLabel.CENTER);
	JButton backButton = new JButton("<- Volver");//(go back)
	
	backButton.addActionListener(l -> cardLayout.show(mainPanel, "buttonsPanel"));
	
	panel.add(label, BorderLayout.CENTER);
	panel.add(backButton, BorderLayout.SOUTH);
	
	return panel;
    }
}
