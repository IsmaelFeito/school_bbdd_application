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
public class ProfesorIniciado extends JPanel{
    private JTextField nombreUsuarioField;
    private JButton logoutButton;
    private JButton registroAlumnoButton;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public ProfesorIniciado(CardLayout cardLayout, JPanel mainPanel) throws ClassNotFoundException {
	this.cardLayout = cardLayout;
	this.mainPanel = mainPanel;
	
	setLayout(new GridBagLayout());
	GridBagConstraints gbc = new GridBagConstraints();
	gbc.insets = new Insets(10, 10, 10, 10);
	
	try{
	    Class.forName("org.mariadb.jdbc.Driver");
	    Connection connection = DriverManager.getConnection(SettingsMaria.URL, SettingsMaria.USUARIO, SettingsMaria.PASSWORD);	    
	    String getNombreUsuario = new String("SELECT a.Nombre, a.Apellidos  FROM alumnos a JOIN usuarios u ON a.UsuarioID = u.UsuarioID WHERE u.NombreUsuario = ?;");
	    PreparedStatement checkStmt= connection.prepareStatement(getNombreUsuario);
	    checkStmt.setString(1, nombreUsuarioField.getText().trim());
	    ResultSet rs = checkStmt.executeQuery();
	    rs.next();
	    String nombreCompleto = rs.toString();
	}catch (SQLException e) {
	    e.printStackTrace();
	    JOptionPane.showMessageDialog(this, "Error en la base de datos -> usuario no encontrado");
	}
	
	JLabel nombreCompletoLabel = new JLabel();
    }
    
}
