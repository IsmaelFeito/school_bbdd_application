/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ernesto;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import static javax.swing.SwingConstants.CENTER;
/**
 *label profe
 * clases y cursos
 * 
 * logout
 * @author ifeito-m
 */
public class ProfesorIniciado extends JPanel{
    private JTextField nombreUsuarioField;
    private JButton logoutButton;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public ProfesorIniciado(CardLayout cardLayout, JPanel mainPanel, String nombreUsuario) throws ClassNotFoundException {
	this.cardLayout = cardLayout;
	this.mainPanel = mainPanel;
	
	setLayout(new GridBagLayout());
	GridBagConstraints gbc = new GridBagConstraints();
	gbc.insets = new Insets(10, 10, 10, 10);
	
        JLabel datosLabel = ProfesorNameLabel(nombreUsuario);
        datosLabel.setHorizontalAlignment(SwingConstants.CENTER);
	gbc.gridx = 0; gbc.gridy = 0;
	gbc.gridwidth = 5;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	add(datosLabel, gbc);
	
	JButton prevButton = new JButton("Anterior alumno");
	prevButton.addActionListener(l -> {
	    
	});
	gbc.gridx=0; gbc.gridy = 1; gbc.gridwidth = 1;
	add(prevButton, gbc);

	JButton editarAlumno = new JButton("Editar Alumno");
	editarAlumno.addActionListener(l -> {});
	gbc.gridx=1; gbc.gridy=1;
	add(editarAlumno, gbc);
	
	logoutButton = new JButton("Logout");
	logoutButton.addActionListener(l -> {
	    Usuario.registrarEnFichero(nombreUsuario, ":Logout de: " + nombreUsuario);
	    JOptionPane.showMessageDialog(this, "Has cerrado sesión");
	    
	    cardLayout.show(mainPanel, "buttonsPanel");
	});
	gbc.gridx = 3; gbc.gridy = 1;
	add(logoutButton, gbc);
//	chargeInfoProdesor();
    }

    private JLabel ProfesorNameLabel(String nombreUsuario){
	try (Connection connection = DriverManager.getConnection(SettingsMaria.URL, SettingsMaria.USUARIO, SettingsMaria.PASSWORD)){    

	    String getNombreUsuario = new String("SELECT p.Nombre, p.Apellidos  FROM Profesores p JOIN Usuarios u ON p.UsuarioID = u.UsuarioID WHERE u.NombreUsuario = ?;");
	    PreparedStatement checkStmt= connection.prepareStatement(getNombreUsuario);
	    checkStmt.setString(1, nombreUsuario.trim());
	    ResultSet rs = checkStmt.executeQuery();
	    if (rs.next()){
		String nombreCompleto = rs.getString("Nombre")+ " "+ rs.getString("Apellidos");
		JLabel nombreCompletoLabel = new JLabel("Bienvenido Profesor: "+ nombreCompleto, SwingConstants.CENTER);
		return nombreCompletoLabel;
	    }else{
		JOptionPane.showMessageDialog(this, "Ususario no encontrado");
		return new JLabel("Usuario no encontrado", SwingConstants.CENTER);
	    }
	}catch (SQLException e) {
	    e.printStackTrace();
	    JOptionPane.showMessageDialog(this, "Error en la base de datos -> usuario no encontrado");
	    return new JLabel("Usuario no encontrado", SwingConstants.CENTER);
	}
    }
}
    
