package ernesto;

import javax.swing.*;
import java.awt.*;

public class InterfazReferencia extends JFrame {

    private JPanel mainPanel;
    private JButton profesorButton;
    private JButton alumnoButton;
    private JButton salirButton;

    public InterfazReferencia() {
        setTitle("Acceso a la Plataforma");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);//centrado de la ventana
        initComponents();
    }

    private void initComponents() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 1, 10, 10));

        JLabel titleLabel = new JLabel("Bienvenido", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));//interesante el cambio de letra
        mainPanel.add(titleLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());//ajustar botones de izda a dcha

        alumnoButton = new JButton("Acceso Alumno");
        profesorButton = new JButton("Acceso Profesor");

        buttonPanel.add(alumnoButton);
        buttonPanel.add(profesorButton);
        mainPanel.add(buttonPanel);

        salirButton = new JButton("Salir");
        mainPanel.add(salirButton);

        add(mainPanel);

        salirButton.addActionListener(e -> System.exit(0));

        alumnoButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Accediendo como Alumno..."));
        profesorButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Accediendo como Profesor..."));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Interfaz app = new Interfaz();
            app.setVisible(true);
        });
    }
}
