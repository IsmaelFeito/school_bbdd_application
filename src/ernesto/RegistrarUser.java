/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ernesto;

import java.awt.*;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author ifeito-m
 */
public class RegistrarUser extends JPanel {

    private JPanel panelRegistro;
    private JTextField nombreField;
    private JButton clearNombreButton;
    private JTextField apellidoField;
    private JButton clearApellidoButton;
    private JTextField nombreUsuarioField;
    private JTextField edadField;
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

	JLabel tipoUsuarioLabel = new JLabel("Tipo de Usuario: ");
	String[] tipoUsuario = {"Alumno", "Profesor"};
	gbc.gridx = 0;
	gbc.gridy = 0;
	add(tipoUsuarioLabel, gbc);
	tipoUsuarioCombo = new JComboBox<>(tipoUsuario);
	gbc.gridx = 1;
	gbc.gridy = 0;
	add(tipoUsuarioCombo, gbc);

	JLabel nombreLabel = new JLabel("Nombre: ");
	gbc.gridx = 0;
	gbc.gridy = 1;
	add(nombreLabel, gbc);
	nombreField = new JTextField(15);
	gbc.gridx = 1;
	gbc.gridy = 1;
	add(nombreField, gbc);
	clearNombreButton = new JButton("x");
	clearNombreButton.addActionListener(l
		-> nombreField.setText(""));
	gbc.gridx = 2;
	gbc.gridy = 1;
	add(clearNombreButton, gbc);
	clearNombreButton.setPreferredSize(new Dimension(20, 20));
	clearNombreButton.setMargin(new Insets(0, 0, 0, 0));

	JLabel apellidoLabel = new JLabel("Apellidos: ");
	gbc.gridx = 0;
	gbc.gridy = 2;
	add(apellidoLabel, gbc);
	apellidoField = new JTextField(15);
	gbc.gridx = 1;
	gbc.gridy = 2;
	add(apellidoField, gbc);
	clearApellidoButton = new JButton("x");
	clearApellidoButton.addActionListener(l
		-> apellidoField.setText(""));
	gbc.gridx = 2;
	gbc.gridy = 2;
	add(clearApellidoButton, gbc);
	clearApellidoButton.setPreferredSize(new Dimension(20, 20));
	clearApellidoButton.setMargin(new Insets(0, 0, 0, 0));

	JLabel edadLabel = new JLabel("Edad: ");
	gbc.gridx = 0;
	gbc.gridy = 3;
	add(edadLabel, gbc);
	edadField = new JTextField(15);
	gbc.gridx = 1;
	gbc.gridy = 3;
	add(edadField, gbc);
	JButton clearEdadButton = new JButton("x");
	clearEdadButton.addActionListener(l
		-> edadField.setText(""));
	gbc.gridx = 2;
	gbc.gridy = 3;
	add(clearEdadButton, gbc);
	clearEdadButton.setPreferredSize(new Dimension(20, 20));
	clearEdadButton.setMargin(new Insets(0, 0, 0, 0));

	backButton = new JButton("<- Volver");
	backButton.addActionListener(e -> cardLayout.show(mainPanel, "buttonsPanel"));
	gbc.gridx = 0;
	gbc.gridy = 4;
	add(backButton, gbc);

	submitButton = new JButton("Enviar");//check on database 
	submitButton.addActionListener(l -> {
	    try {
		String selectedUserType = (String) tipoUsuarioCombo.getSelectedItem();
		registroLogin(selectedUserType);
		nombreField.setText("");
		apellidoField.setText("");
		edadField.setText("");
	    } catch (SQLException | ClassNotFoundException | NoSuchAlgorithmException ex) {
		Logger.getLogger(RegistrarUser.class.getName()).log(Level.SEVERE, null, ex);
	    }
	});
	gbc.gridx = 1;
	gbc.gridy = 4;
	add(submitButton, gbc);
    }

    private void registroLogin(String userType) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {
	String nombre = nombreField.getText().replace(" ", "");
	String apellido = apellidoField.getText().replace(" ", "");
	String edadTexto = edadField.getText().trim();

	int edad = 0;
	try {
	    edad = Integer.parseInt(edadTexto);
	} catch (NumberFormatException e) {
	    JOptionPane.showMessageDialog(this, "La edad debe ser un número válido.");
	    return;
	}
	if (nombre.isEmpty() || apellido.isEmpty() || edad < 0) {
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
	    try (PreparedStatement checkStmt1 = connection.prepareStatement(addUser, Statement.RETURN_GENERATED_KEYS)) {
		checkStmt1.setString(1, nombreUsuario);
		checkStmt1.setString(2, contraHash);
		checkStmt1.setString(3, userType);
		checkStmt1.executeUpdate();

		ResultSet id = checkStmt1.getGeneratedKeys();
		if (id.next()) {
		    int userId = id.getInt(1);

		    String insertTipoUsuario = "";
		    if (userType.equalsIgnoreCase("Alumno")) {
			insertTipoUsuario = "INSERT INTO Alumnos (Nombre, Apellidos, Edad, FechaMatricula, UsuarioID) VALUES (?, ?, ?, ?, ?)";
			try (PreparedStatement stmtAlumno = connection.prepareStatement(insertTipoUsuario)) {
			    stmtAlumno.setString(1, nombre);
			    stmtAlumno.setString(2, apellido);
			    stmtAlumno.setInt(3, edad);
			    stmtAlumno.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
			    stmtAlumno.setInt(5, userId);
			    stmtAlumno.executeUpdate();
			} catch (Exception e) {
			    // Si falla la inserción en Alumnos, se elimina el usuario insertado previamente
			    try (PreparedStatement delUser = connection.prepareStatement("DELETE FROM Usuarios WHERE NombreUsuario = ?")) {
				delUser.setString(1, nombreUsuario);
				delUser.executeUpdate();
			    } catch (SQLException ex) {
				ex.printStackTrace();
			    }
			}
		    } else if (userType.equalsIgnoreCase("Profesor")) {
			insertTipoUsuario = "INSERT INTO Profesores (Nombre, Apellidos, UsuarioID) VALUES (?, ?, ?)";
			try (PreparedStatement stmtProfesor = connection.prepareStatement(insertTipoUsuario)) {
			    stmtProfesor.setString(1, nombre);
			    stmtProfesor.setString(2, apellido);
			    stmtProfesor.setInt(3, userId);
			    stmtProfesor.executeUpdate();
			} catch (Exception e) {
			    // Si falla la inserción en Profesores, se elimina el usuario insertado previamente
			    try (PreparedStatement delUser = connection.prepareStatement("DELETE FROM Usuarios WHERE NombreUsuario = ?")) {
				delUser.setString(1, nombreUsuario);
				delUser.executeUpdate();
			    } catch (SQLException ex) {
				ex.printStackTrace();
			    }
			}
		    }
		}
		JOptionPane.showMessageDialog(this, "Usuario registrado con éxito, tu usuaario es: " + nombreUsuario + " y tu cotraceña es: " + contra);

	    }
	} catch (SQLException e) {
	    JOptionPane.showMessageDialog(this, "Error al guardar los datos del usuario/");
	}
    }
}
