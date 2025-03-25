/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ernesto;

import java.awt.*;
import java.sql.*;
import javax.swing.*;

/**
 *
 * @author ifeito-m
 */
public class RegistrarUser extends JPanel{
    private JPanel panelRegistro;
    private JTextField nombreField;
    private JTextField apellidoField;
    private JTextField nombreUsuarioField;
    private JButton submitButton;
    private JButton backButton;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private String userType;
    
    public RegistrarUser(CardLayout cardLayout, JPanel mainPanel, String userType) {
	this.cardLayout = cardLayout;
	this.mainPanel = mainPanel;
	this.
	
	setLayout(new GridBagLayout());
	GridBagConstraints gbc = new GridBagConstraints();
	gbc.insets = new Insets(10, 10, 10, 10);
	
	JLabel nombreLabel = new JLabel("Nombre: ");
	gbc.gridx = 0; gbc.gridy = 0;
	add(nombreLabel, gbc);
	nombreField = new JTextField(15);
	gbc.gridx = 1; gbc.gridy = 0;
	add(nombreField, gbc);
	
	JLabel apellidoLabel = new JLabel("Contraseña: ");
	gbc.gridx = 0; gbc.gridy = 1;
	add(apellidoLabel, gbc);
	apellidoField = new JPasswordField(15);
	gbc.gridx = 1; gbc.gridy = 1;
	add(apellidoField, gbc);
	
//	JLabel nombreUsuarioLabel = new JLabel("Nombre de usuario: ");
//	gbc.gridx = 0; gbc.gridy = 2;
//	add(nombreUsuarioLabel, gbc);
//	nombreUsuarioField = new JPasswordField(15);
//	gbc.gridx = 1; gbc.gridy = 2;
//	add(apellidoField, gbc);
	
	backButton = new JButton("<- Volver");
	backButton.addActionListener(e -> cardLayout.show(mainPanel, "buttonsPanel"));
	gbc.gridx = 0; gbc.gridy = 3;
	add(nombreLabel, gbc);
	
	submitButton = new JButton("Enviar");//check on database 
	submitButton.addActionListener(l -> {
	    registroLogin();
	});
	gbc.gridx = 1; gbc.gridy = 3;
	add(submitButton, gbc);
    }
    
	private void registroLogin(){
	    String nombre = nombreField.getText().trim();
	    String apellido = apellidoField.getText().trim();
//	    String nombreUsuario = nombreUsuarioField.getText().trim();
	    //str tipoUsuario
	    if((nombre.isEmpty() || apellido.isEmpty()) || (nombre.isEmpty() && apellido.isEmpty())){
		JOptionPane.showMessageDialog(this, "El usuario y la apellido no pueden estar vacíos.");
		return;
	    }
//	    String insertQuery = "INSERT INTO usuarios (nombre, contrasena, tipo) VALUES (?, ?, ?)";
//	    try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
//		insertStmt.setString(1, user.getNombreUsuario());
//		insertStmt.setString(2, user.getContra());
//		insertStmt.setString(3, user.getTipoUsuario());
//		insertStmt.executeUpdate();
//		JOptionPane.showMessageDialog(this, "Usuario registrado con éxito");
//	    } catch (SQLException e) {
//		e.printStackTrace();
//		JOptionPane.showMessageDialog(this, "Error en la base de datos");

	    }
	}

}
