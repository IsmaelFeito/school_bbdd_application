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
public class RegistrarUser extends JPanel {

    private JPanel panelRegistro;
    private JTextField nombreField;
    private JTextField apellidoField;
    private JTextField nombreUsuarioField;
    private JButton submitButton;
    private JButton backButton;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JComboBox<String> tipoUsuarioCombo;

    public RegistrarUser(CardLayout cardLayout, JPanel mainPanel) {
	this.cardLayout = cardLayout;
	this.mainPanel = mainPanel;

	setLayout(new GridBagLayout());
	GridBagConstraints gbc = new GridBagConstraints();
	gbc.insets = new Insets(10, 10, 10, 10);

	JLabel nombreLabel = new JLabel("Nombre: ");
	gbc.gridx = 0; gbc.gridy = 0;
	add(nombreLabel, gbc);
	nombreField = new JTextField(15);
	gbc.gridx = 1; gbc.gridy = 0;
	add(nombreField, gbc);

	JLabel apellidoLabel = new JLabel("Apellidos: ");
	gbc.gridx = 0;gbc.gridy = 1;
	add(apellidoLabel, gbc);
	apellidoField = new JTextField(15);
	gbc.gridx = 1;gbc.gridy = 1;
	add(apellidoField, gbc);

	JLabel tipoUsuarioLabel = new JLabel("Tipo de Usuario: ");
	String[] tipoUsuario = {"Alumno", "Profesor"};
	tipoUsuarioCombo = new JComboBox<>(tipoUsuario);
	add(tipoUsuarioCombo);

	backButton = new JButton("<- Volver");
	backButton.addActionListener(e -> cardLayout.show(mainPanel, "buttonsPanel"));
	gbc.gridx = 0;gbc.gridy = 2;
	add(backButton, gbc);

	submitButton = new JButton("Enviar");//check on database 
	submitButton.addActionListener(l -> {
	    String selectedUserType = (String) tipoUsuarioCombo.getSelectedItem();
	    registroLogin(selectedUserType);
	});
	gbc.gridx = 1;
	gbc.gridy = 2;
	add(submitButton, gbc);
    }

    private void registroLogin(String userType) {
	String nombre = nombreField.getText().trim();
	String apellido = apellidoField.getText().trim();
	if (nombre.isEmpty() || apellido.isEmpty()) {
	    JOptionPane.showMessageDialog(this, "El nombre y el apellido no pueden estar vacíos.");
	    return;
	}
	String nombreUsuarioBase = nombre.toLowerCase() + apellido.toLowerCase();
	String nombreUsuario = nombreUsuarioBase;

	try {
	    Class.forName("org.mariadb.jdbc.Driver");
	    Connection connection = DriverManager.getConnection(SettingsMaria.URL, SettingsMaria.USUARIO, SettingsMaria.PASSWORD);

	    // Verificar si ya existe el nombre base sin número
	    String checkUsuario = "SELECT COUNT(*) FROM Usuarios WHERE NombreUsuario = ?";
	    try (PreparedStatement checkStmt = connection.prepareStatement(checkUsuario)) {
		checkStmt.setString(1, nombreUsuarioBase);
		ResultSet rs = checkStmt.executeQuery();

		if (rs.next() && rs.getInt(1) > 0) {
		    // Si el nombre base ya existe, buscar el siguiente número disponible
		    int i = 1;
		    String nuevoNombreUsuario;
		    do {
			nuevoNombreUsuario = nombreUsuarioBase + i;
			checkStmt.setString(1, nuevoNombreUsuario);
			rs = checkStmt.executeQuery();
			if (rs.next() && rs.getInt(1) == 0) {
			    nombreUsuario = nuevoNombreUsuario;
			    break;
			}
			i++;
		    } while (true);
		}
	    }

	    
	    String contra = Usuario.generatePasswd();
	    String contraHash = Usuario.md5(contra);

	    String addUser = "INSERT INTO Usuarios (NombreUsuario, ContrasenaHash, TipoUsuario) VALUES (?, ?, ?)";;
	    try (PreparedStatement checkStmt1 = connection.prepareStatement(addUser)) {
		checkStmt1.setString(1, nombreUsuario);
		checkStmt1.setString(2, contraHash);
		checkStmt1.setString(3, userType);
		checkStmt1.executeUpdate();
		JOptionPane.showMessageDialog(this, "Usuario registrado con éxito, tu usuaario es: " +nombreUsuario +" y tu cotraceña es: " + contra);
	    }
	}catch(Exception e){
	    System.out.println("Error al guardar los datos del usuario/");
	    e.printStackTrace();
       }
    }
}
