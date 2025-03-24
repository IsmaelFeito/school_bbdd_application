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
public class Alumno extends JPanel {
    private JPanel panelAlumno;
    private JTextField nombreField;
    private JTextField apellidoField;
    private JPasswordField contrasenaField;
    private JButton backButton;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    public Alumno(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
	
	setLayout(new GridLayout(3,2,10,10));
	
	JLabel nombreLabel = new JLabel("Nombre: ");
	nombreField = new JTextField(15);
	
	JLabel apellidoLabel = new JLabel("Apellido: ");
	apellidoField = new JTextField(15);
	
	JLabel contrasenaLabel = new JLabel("Contraseña: ");
	contrasenaField = new JPasswordField(15);	
	
	backButton = new JButton("<- Volver");
	backButton.addActionListener(e -> cardLayout.show(mainPanel, "buttonsPanel"));
	
        add(nombreLabel);
        add(nombreField);
	add(apellidoLabel);
	add(apellidoField);
        add(contrasenaLabel);
        add(contrasenaField);
        add(backButton);
	
	mainPanel.add(this, "alumnoPanel");
    }

    public JPanel getPanel() {
	return panelAlumno;
    }

    public String getApellidoField() {
	return apellidoField.getText();
    }

    public String getContrasenaField() {
	return new String(contrasenaField.getPassword());
    }
    
}
