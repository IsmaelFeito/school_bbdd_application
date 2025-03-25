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
	
	String userTypeTemp;
	if (userType.equalsIgnoreCase("Alumno"))
	    userTypeTemp = "Alumno";
	else 
	    userTypeTemp = new String("Profesor");
	String backPanel = new String(userTypeTemp+"Panel");
	
	backButton = new JButton("<- Volver");
	backButton.addActionListener(e -> cardLayout.show(mainPanel, backPanel));
	gbc.gridx = 0; gbc.gridy = 2;
	add(backButton, gbc);
	
	submitButton = new JButton("Enviar");//check on database 
	submitButton.addActionListener(l -> {
	    registroLogin(userType);
	});
	gbc.gridx = 1; gbc.gridy = 2;
	add(submitButton, gbc);
    }
    
	private void registroLogin(String userType){
	    String nombre = nombreField.getText().trim();
	    String apellido = apellidoField.getText().trim();
	    try {
		String nombreUsuario = new String (nombre.toLowerCase() + apellido.toLowerCase());
		
		Class.forName("org.mariadb.jdbc.Driver");
		Connection connection = DriverManager.getConnection(SettingsMaria.URL, SettingsMaria.USUARIO, SettingsMaria.PASSWORD);
		String checkUsuario = "SELECT COUNT(*) FROM Usuarios WHERE nombreUsuario = ?";
		try (PreparedStatement checkStmt = connection.prepareStatement(checkUsuario)){
		    checkStmt.setString(1, nombreUsuario);
		    ResultSet rs = checkStmt.executeQuery();
		    if (!rs.next() || rs.getInt(1) <=1){
			String addUser = "INSERT INTO Usuarios (nombre, contrasena, tipo) VALUES (?, ?, ?)";;
			
			String contra = Usuario.generatePasswd();
			try (PreparedStatement checkStmt1 = connection.prepareStatement(addUser)){
			    checkStmt1.setString(1, nombreUsuario);
			    checkStmt1.setString(2, contra);
			    checkStmt1.setString(3, userType);
			    checkStmt1.executeUpdate();
			    JOptionPane.showMessageDialog(this, "Usuario registrado con éxito");
			}
		    }
		    while (rs.next()){
		    }
			
		}
	    }catch(Exception e){
		System.out.println("Error al guardar los datos del usuario/");
		e.printStackTrace();
	    }
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
//
//	    }
	}

}
