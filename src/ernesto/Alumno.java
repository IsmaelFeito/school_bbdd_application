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
import java.io.*;

/**
 *
 * @author ifeito-m
 */
public class Alumno extends JPanel {
    private JTextField nombreUsuarioField;
    private JPasswordField contrasenaField;
    private JButton backButton;
    private JButton submitButton;
    private JButton registroButton;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    public Alumno(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
	
	setLayout(new GridBagLayout());
	GridBagConstraints gbc = new GridBagConstraints();
	gbc.insets = new Insets(10, 10, 10, 10);
	
	JLabel nombreUserLabel = new JLabel("Nombre de usuario: ");
        gbc.gridx = 0; gbc.gridy = 0; 
	add(nombreUserLabel, gbc);
	
	nombreUsuarioField = new JTextField(15);
	gbc.gridx = 1; gbc.gridy = 0; 
        add(nombreUsuarioField, gbc);
	
	JLabel contrasenaLabel = new JLabel("Contraseña: ");
	gbc.gridx = 0; gbc.gridy = 1; 
        add(contrasenaLabel, gbc);

	contrasenaField = new JPasswordField(15);	
        gbc.gridx = 1; gbc.gridy = 1;
        add(contrasenaField, gbc);
	
	backButton = new JButton("<- Volver");
	backButton.addActionListener(e -> cardLayout.show(mainPanel, "buttonsPanel"));
	gbc.gridx = 0; gbc.gridy = 2; 
	add(backButton, gbc);
	
	registroButton = new JButton("Registrarse");
	registroButton.addActionListener(e -> cardLayout.show(mainPanel, "registrarUser"));
	gbc.gridx = 1; gbc.gridy = 2; 
	add(registroButton, gbc);
	
	submitButton = new JButton("Enviar");//check on database 
	submitButton.addActionListener(l -> {
	    try {
		loginAlumno();
	    } catch (Exception ex) {
                Logger.getLogger(Alumno.class.getName()).log(Level.SEVERE, "Error al registrar usuario", ex);
                JOptionPane.showMessageDialog(this, "Error al registrar usuario: "+ nombreUsuarioField.getText());
            }
	});
	gbc.gridx = 2; gbc.gridy = 2;
        add(submitButton, gbc);
	
	mainPanel.add(this, "alumnoPanel");
	
	RegistrarUser panelRegistro = new RegistrarUser(cardLayout, mainPanel, "Alumno");
	mainPanel.add(panelRegistro, "registrarUser");
    }

    
    private void loginAlumno() throws NoSuchAlgorithmException{
	String nombreUsuario = nombreUsuarioField.getText().trim();
	String passwd = new String(contrasenaField.getPassword());

	if((nombreUsuario.isEmpty() || passwd.isEmpty()) || (nombreUsuario.isEmpty() && passwd.isEmpty())){
	    JOptionPane.showMessageDialog(this, "El usuario y la contraseña no pueden estar vacíos.");
	    return;
	}
	
	try {
	    System.out.println("contraseña str: "+passwd+ "user name str: "+nombreUsuario);
	    Usuario user = new Usuario(nombreUsuario, passwd, "Alumno");

	    //insert query
	    Class.forName("org.mariadb.jdbc.Driver");
	    Connection connection = DriverManager.getConnection(SettingsMaria.URL, SettingsMaria.USUARIO, SettingsMaria.PASSWORD);
	    String checkUsuario = "SELECT COUNT(*) FROM usuarios WHERE nombre = ?";
	  
	    try (PreparedStatement checkStmt = connection.prepareStatement(checkUsuario)){
		checkStmt.setString(1, user.getNombreUsuario());
		ResultSet rs = checkStmt.executeQuery();
		
		if(rs.next() && rs.getInt(1) > 0){
		    String contrasena = rs.getString("contrasena");
		    try{
			if(contrasena.equals(Usuario.md5(passwd))){
			    JOptionPane.showMessageDialog(this, "inicio exitoso");
			    Usuario.registrarEnFichero(nombreUsuario, "inicio exitoso de: ");
			}else{
			    JOptionPane.showMessageDialog(this, "contraseña incorrecta");
			    Usuario.registrarEnFichero(nombreUsuario, "contraseña fallida de: ");			
			}
		    }catch (NoSuchAlgorithmException e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error al encriptar la contraseña");		    
		    }
		}else{
		    JOptionPane.showMessageDialog(this, "Usuario no encontrado");
		    Usuario.registrarEnFichero(nombreUsuario, "Fallo en login - Usuario no encontrado");		    
		}
	    }
	}catch (SQLException e) {
	    e.printStackTrace();
	    JOptionPane.showMessageDialog(this, "Error en la base de datos");
	} catch (ClassNotFoundException ex) {
	    Logger.getLogger(Alumno.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
    
}
