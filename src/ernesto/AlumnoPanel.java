/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ernesto;

import java.awt.*;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.logging.*;
import javax.swing.*;

/**
 *
 * @author ifeito-m
 */
public class AlumnoPanel extends JPanel {
    private JTextField nombreUsuarioField;
    private JPasswordField contrasenaField;
    private JButton backButton;
    private JButton submitButton;
    private JButton registroButton;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    public AlumnoPanel (CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
	
	setLayout(new GridBagLayout());
	GridBagConstraints gbc = new GridBagConstraints();
	gbc.insets = new Insets(10, 10, 10, 10);

	
	JLabel nombreUsuarioLabel = new JLabel("Nombre de usuario: ");
	nombreUsuarioLabel.setForeground(Color.BLACK);
        nombreUsuarioLabel.setOpaque(true);
        gbc.gridx = 0; gbc.gridy = 0; 
	add(nombreUsuarioLabel, gbc);
	
	nombreUsuarioField = new JTextField(15);
	gbc.gridx = 1; gbc.gridy = 0; 
        add(nombreUsuarioField, gbc);

	JButton clearUserField = new JButton("x");
	clearUserField.addActionListener(l -> 
	    nombreUsuarioField.setText(""));
	gbc.gridx = 2; gbc.gridy = 0;
	clearUserField.setPreferredSize(new Dimension(20, 20));
	clearUserField.setMargin(new Insets(0,0,0,0));
	add(clearUserField, gbc);

	JLabel contrasenaLabel = new JLabel("Contraseña: ");
        contrasenaLabel.setForeground(Color.BLACK);
        contrasenaLabel.setOpaque(true);
	gbc.gridx = 0; gbc.gridy = 1; 
        add(contrasenaLabel, gbc);

	contrasenaField = new JPasswordField(15);	
        gbc.gridx = 1; gbc.gridy = 1;
        add(contrasenaField, gbc);


	JButton clearContraField = new JButton("x");
	clearContraField.addActionListener(l -> 
	    contrasenaField.setText(""));
	gbc.gridx = 2; gbc.gridy = 1;
	clearContraField.setPreferredSize(new Dimension(20, 20));
	clearContraField.setMargin(new Insets(0,0,0,0));
	add(clearContraField, gbc);
		
	backButton = new JButton("<- Volver");
        backButton.setBackground(new Color(255, 102, 102));
        backButton.setForeground(Color.WHITE);
	backButton.addActionListener(e -> cardLayout.show(mainPanel, "buttonsPanel"));
	gbc.gridx = 0; gbc.gridy = 2; 
	add(backButton, gbc);
	
	registroButton = new JButton("Registrarse");
	registroButton.addActionListener(e -> cardLayout.show(mainPanel, "RegistrarUser"));
	gbc.gridx = 1; gbc.gridy = 2; 
	add(registroButton, gbc);
	
	submitButton = new JButton("Entrar ->");
        submitButton.setBackground(new Color(121, 213, 57));
        submitButton.setForeground(Color.WHITE);
	submitButton.addActionListener(e -> {
	    try {
		loginAlumno();
	    } catch (NoSuchAlgorithmException ex) {
		JOptionPane.showMessageDialog(this, "Error en el sistema de seguridad");
		Logger.getLogger(AlumnoPanel.class.getName()).log(Level.SEVERE, null, ex);
	    }
	});
	gbc.gridx = 2; gbc.gridy = 2; 
	add(submitButton, gbc);
}

    
    private void loginAlumno() throws NoSuchAlgorithmException {
	String nombreUsuario = nombreUsuarioField.getText().trim();
	String passwd = new String(contrasenaField.getPassword());

	if(nombreUsuario.isEmpty() || passwd.isEmpty()) {
	    JOptionPane.showMessageDialog(this, "El usuario y la contraseña no pueden estar vacíos.");
	    return;
	}

	// Consulta corregida para alumnos
	String checkUsuario = "SELECT ContrasenaHash FROM Usuarios WHERE NombreUsuario = ? AND TipoUsuario = 'Alumno'";

	try (Connection connection = DriverManager.getConnection(SettingsMaria.URL, SettingsMaria.USUARIO, SettingsMaria.PASSWORD);
	    PreparedStatement checkStmt = connection.prepareStatement(checkUsuario);){
	    checkStmt.setString(1, nombreUsuario);

	    try (ResultSet rs = checkStmt.executeQuery()) {
		if(rs.next()) {
		    String contrasenaHash = rs.getString("ContrasenaHash");
		    if(contrasenaHash.equals(Usuario.md5(passwd))) {
			JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso");
			Usuario.registrarEnFichero(nombreUsuario, "Inicio exitoso de alumno: " + nombreUsuario);

			AlumnoIniciado correrAlumno = new AlumnoIniciado(cardLayout, mainPanel, nombreUsuario);
			mainPanel.add(correrAlumno, "CorrerAlumno");
			cardLayout.show(mainPanel, "CorrerAlumno");
		    } else {
			JOptionPane.showMessageDialog(this, "Contraseña incorrecta");
			contrasenaField.setText("");
			Usuario.registrarEnFichero(nombreUsuario, "Intento fallido de inicio (contraseña incorrecta)");            
		    }
		} else {
		    JOptionPane.showMessageDialog(this, "Usuario no encontrado o no es un alumno");
		    Usuario.registrarEnFichero(nombreUsuario, "Intento fallido - Usuario no encontrado");            
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    JOptionPane.showMessageDialog(this, "Error en la base de datos: " + e.getMessage());
	}
    }    
}
