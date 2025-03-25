package ernesto;

import java.awt.*;
import javax.swing.*;

public class Profesor extends JPanel {

    private JTextField nombreField;
    private JPasswordField contrasenaField;
    private JButton backButton;
    private JButton submitButton;
    private JButton registroButton;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public Profesor(CardLayout cardLayout, JPanel mainPanel) {
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
        
	nombreField = new JTextField(15);
	gbc.gridx = 1; gbc.gridy = 0;
	add(nombreField, gbc);
	//Contraseña
        JLabel contrasenaLabel = new JLabel("Contraseña: ");
        contrasenaLabel.setForeground(Color.BLACK);
        contrasenaLabel.setOpaque(true);
        gbc.gridx = 0; gbc.gridy = 1;
        add(contrasenaLabel, gbc);

	contrasenaField = new JPasswordField(15);
        gbc.gridx = 1; gbc.gridy = 1;
        add(contrasenaField, gbc);

        // Configurar botón para volver
        backButton = new JButton("<- Volver");
        backButton.setBackground(new Color(255, 102, 102));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "buttonsPanel"));
        gbc.gridx = 0; gbc.gridy = 2;
	add(backButton, gbc);

	// registrar user
	registroButton = new JButton("Registrarse");
	registroButton.addActionListener(e -> cardLayout.show(mainPanel, "registrarUser"));
	gbc.gridx = 1; gbc.gridy = 2; 
	add(registroButton, gbc);
	
        // Configurar botón de envío
        submitButton = new JButton("Acceder ->");
        submitButton.setBackground(new Color(121, 213, 57));
        submitButton.setForeground(Color.WHITE);
        submitButton.addActionListener(e -> cardLayout.show(mainPanel, "buttonsPanel"));
        gbc.gridx = 2; gbc.gridy = 2;
	add(submitButton, gbc);

        // Añadir este panel al mainPanel con la clave "profesorPanel"
        mainPanel.add(this, "ProfesorPanel");

    }

    public String getContrasenaField() {
        return new String(contrasenaField.getPassword());
    }
}
