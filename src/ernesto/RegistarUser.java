/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ernesto;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author ifeito-m
 */
public class RegistarUser {
    
    
    String insertQuery = "INSERT INTO usuarios (nombre, contrasena, tipo) VALUES (?, ?, ?)";
    try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
	insertStmt.setString(1, user.getNombreUsuario());
	insertStmt.setString(2, user.getContra());
	insertStmt.setString(3, user.getTipoUsuario());
	insertStmt.executeUpdate();
	JOptionPane.showMessageDialog(this, "Usuario registrado con éxito");
    }
    } catch (SQLException e) {
	e.printStackTrace();
	JOptionPane.showMessageDialog(this, "Error en la base de datos");
    }
}
