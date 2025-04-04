package ernesto;

import java.awt.*;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.logging.*;
import javax.swing.*;

public class ProfesorPanel extends JPanel {

    private JTextField nombreUsuarioField;
    private JPasswordField contrasenaField;
    private JButton backButton;
    private JButton submitButton;
    private JButton registroButton;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public ProfesorPanel (CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        // Establecer el layout de este panel
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel nombreLabel = new JLabel("Nombre de usuario: ");
        nombreLabel.setForeground(Color.BLACK);
        nombreLabel.setOpaque(true);
	gbc.gridx = 0; gbc.gridy = 0;
	add(nombreLabel, gbc);
        
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
	
	//Contraseña
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

        // Configurar botón para volver
        backButton = new JButton("<- Volver");
        backButton.setBackground(new Color(255, 102, 102));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "buttonsPanel"));
        gbc.gridx = 0; gbc.gridy = 2;
	add(backButton, gbc);

	// registrar user
	registroButton = new JButton("Registrarse");
	mainPanel.add(new RegistrarUser(cardLayout, mainPanel));
	registroButton.addActionListener(e -> cardLayout.show(mainPanel, "RegistrarUser"));
	gbc.gridx = 1; gbc.gridy = 2; 
	add(registroButton, gbc);
	
        // Configurar botón de envío
        submitButton = new JButton("Acceder ->");
        submitButton.setBackground(new Color(121, 213, 57));
        submitButton.setForeground(Color.WHITE);
        submitButton.addActionListener(e -> {
	    try {
		loginProfesor();
	    } catch (NoSuchAlgorithmException | ClassNotFoundException ex) {
		Logger.getLogger(ProfesorPanel.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Error al registrar usuario: "+ nombreUsuarioField.getText());
	    }
	    nombreUsuarioField.setText("");
	    contrasenaField.setText("");
	});
        gbc.gridx = 2; gbc.gridy = 2;
	add(submitButton, gbc);

    }
		
    private void loginProfesor() throws NoSuchAlgorithmException, ClassNotFoundException{
	String nombreUsuario = nombreUsuarioField.getText().trim();
	String passwd = new String(contrasenaField.getPassword());

	if((nombreUsuario.isEmpty() || passwd.isEmpty()) || (nombreUsuario.isEmpty() && passwd.isEmpty())){
	    JOptionPane.showMessageDialog(this, "El usuario y la contraseña no pueden estar vacíos.");
	    return;
	}
	
	String checkUsuario = "SELECT ContrasenaHash FROM Usuarios WHERE NombreUsuario = ? AND TipoUsuario = 'Profesor'";
	    
	try (Connection connection = DriverManager.getConnection(SettingsMaria.URL, SettingsMaria.USUARIO, SettingsMaria.PASSWORD)){
	    PreparedStatement checkStmt = connection.prepareStatement(checkUsuario);
	    
	    checkStmt.setString(1, nombreUsuario);
	  
	    try (ResultSet rs = checkStmt.executeQuery()){
		if(rs.next()){
		    String contrasena = rs.getString("ContrasenaHash");
		    if(contrasena.equals(Usuario.md5(passwd))){
			JOptionPane.showMessageDialog(this, "inicio exitoso");
			Usuario.registrarEnFichero(nombreUsuario, "inicio exitoso de: " + nombreUsuario);

			ProfesorIniciado CorrerProfe = new ProfesorIniciado(cardLayout, mainPanel, nombreUsuario);
			mainPanel.add(CorrerProfe, "CorrerProfe");
			cardLayout.show(mainPanel, "CorrerProfe");
		    }else{
			JOptionPane.showMessageDialog(this, "contraseña incorrecta");
			nombreUsuarioField.setText("");
			contrasenaField.setText("");
			Usuario.registrarEnFichero(nombreUsuario, "contraseña fallida de: " + nombreUsuario);
		    }
		}else{
		    JOptionPane.showMessageDialog(this, "Usuario no encontrado");
		    Usuario.registrarEnFichero(nombreUsuario, "Fallo en login - Usuario no encontrado");		    
		}
	    }
	}catch (SQLException e) {
	    e.printStackTrace();
	    JOptionPane.showMessageDialog(this, "Error en la base de datos");
	}
    }

}
