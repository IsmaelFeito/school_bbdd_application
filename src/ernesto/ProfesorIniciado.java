/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package ernesto;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 *label profe
 * clases y cursos
 * 
 * logout
 * @author ifeito-m
 */

public class ProfesorIniciado extends JPanel {
    private Connection conn;
    private JLabel profesorNameLabel;
    private JLabel alumnoInfoLabel;
    private JTextField nota1Field, nota2Field, nota3Field, notaFinalField;
    private JComboBox<String> cursosComboBox, modulosComboBox;
    private JButton prevButton, nextButton, firstButton, lastButton;
    private JButton editarAlumnoButton, addAlumnoButton, guardarNotasButton, logoutButton;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private String nombreUsuario;
    
    private List<Alumno> alumnos = new ArrayList<>();
    private int currentAlumnoIndex = -1;
    
    public ProfesorIniciado(CardLayout cardLayout, JPanel mainPanel, String nombreUsuario) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.nombreUsuario = nombreUsuario;
        
        try {
            conn = DriverManager.getConnection(SettingsMaria.URL, SettingsMaria.USUARIO, SettingsMaria.PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos.");
            return;
        }
        
        initUI();
        cargarDatosProfesor();
        cargarAlumnos();
        cargarCursos();
        if (!alumnos.isEmpty()) {
            mostrarAlumno(0);
        }
    }
    
    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        
        // Panel superior con información del profesor
        JPanel topPanel = new JPanel(new BorderLayout());
        profesorNameLabel = new JLabel("", SwingConstants.CENTER);
        profesorNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(profesorNameLabel, BorderLayout.CENTER);
        
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        topPanel.add(logoutButton, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Panel central con datos del alumno
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        
        // Información del alumno
        alumnoInfoLabel = new JLabel("No hay alumnos registrados", SwingConstants.CENTER);
        alumnoInfoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        centerPanel.add(alumnoInfoLabel, BorderLayout.NORTH);
        
        // Panel de navegación
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        firstButton = new JButton("Primero");
        prevButton = new JButton("Anterior");
        nextButton = new JButton("Siguiente");
        lastButton = new JButton("Último");
        
        firstButton.addActionListener(e -> mostrarAlumno(0));
        prevButton.addActionListener(e -> mostrarAlumno(currentAlumnoIndex - 1));
        nextButton.addActionListener(e -> mostrarAlumno(currentAlumnoIndex + 1));
        lastButton.addActionListener(e -> mostrarAlumno(alumnos.size() - 1));
        
        navPanel.add(firstButton);
        navPanel.add(prevButton);
        navPanel.add(nextButton);
        navPanel.add(lastButton);
        
        centerPanel.add(navPanel, BorderLayout.CENTER);
        
        // Panel de cursos y módulos
        JPanel cursosModulosPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        cursosComboBox = new JComboBox<>();
        modulosComboBox = new JComboBox<>();
        
        cursosComboBox.addActionListener(e -> cargarModulos());
        
        cursosModulosPanel.add(new JLabel("Curso:"));
        cursosModulosPanel.add(cursosComboBox);
        cursosModulosPanel.add(new JLabel("Módulo:"));
        cursosModulosPanel.add(modulosComboBox);
        
        centerPanel.add(cursosModulosPanel, BorderLayout.SOUTH);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // Panel de notas
        JPanel notasPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        notasPanel.setBorder(BorderFactory.createTitledBorder("Notas del módulo"));
        
        notasPanel.add(new JLabel("Nota 1ª Evaluación:"));
        nota1Field = new JTextField();
        notasPanel.add(nota1Field);
        
        notasPanel.add(new JLabel("Nota 2ª Evaluación:"));
        nota2Field = new JTextField();
        notasPanel.add(nota2Field);
        
        notasPanel.add(new JLabel("Nota 3ª Evaluación:"));
        nota3Field = new JTextField();
        notasPanel.add(nota3Field);
        
        notasPanel.add(new JLabel("Nota Final:"));
        notaFinalField = new JTextField();
        notaFinalField.setEditable(false);
        notasPanel.add(notaFinalField);
        
        guardarNotasButton = new JButton("Guardar Notas");
        guardarNotasButton.addActionListener(e -> guardarNotas());
        notasPanel.add(guardarNotasButton);
        
        add(notasPanel, BorderLayout.SOUTH);
        
        // Panel de botones de gestión
        JPanel gestionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        editarAlumnoButton = new JButton("Editar Alumno");
        addAlumnoButton = new JButton("Añadir Alumno");
        
        editarAlumnoButton.addActionListener(e -> editarAlumno());
        addAlumnoButton.addActionListener(e -> añadirAlumno());
        
        gestionPanel.add(editarAlumnoButton);
        gestionPanel.add(addAlumnoButton);
        
        add(gestionPanel, BorderLayout.SOUTH);
    }
    
    private void cargarDatosProfesor() {
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT p.Nombre, p.Apellidos FROM Profesores p JOIN Usuarios u ON p.UsuarioID = u.UsuarioID WHERE u.NombreUsuario = ?")) {
            stmt.setString(1, nombreUsuario.trim());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String nombreCompleto = rs.getString("Nombre") + " " + rs.getString("Apellidos");
                profesorNameLabel.setText("Bienvenido Profesor: " + nombreCompleto);
            } else {
                JOptionPane.showMessageDialog(this, "Usuario no encontrado");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error en la base de datos");
        }
    }
    
    private void cargarAlumnos() {
        alumnos.clear();
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT AlumnoID, Nombre, Apellidos, Curso FROM Alumnos ORDER BY Apellidos, Nombre")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Alumno alumno = new Alumno(
                    rs.getInt("AlumnoID"),
                    rs.getString("Nombre"),
                    rs.getString("Apellidos"),
                    rs.getString("Curso")
                );
                alumnos.add(alumno);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar alumnos");
        }
    }
    
    private void cargarCursos() {
        cursosComboBox.removeAllItems();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT Curso FROM Alumnos ORDER BY Curso")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cursosComboBox.addItem(rs.getString("Curso"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar cursos");
        }
    }
    
    private void cargarModulos() {
        modulosComboBox.removeAllItems();
        String cursoSeleccionado = (String) cursosComboBox.getSelectedItem();
        if (cursoSeleccionado == null) return;
        
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT ModuloID, Nombre FROM Modulos WHERE Curso = ? ORDER BY Nombre")) {
            stmt.setString(1, cursoSeleccionado);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                modulosComboBox.addItem(rs.getString("Nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar módulos");
        }
    }
    
    private void mostrarAlumno(int index) {
        if (alumnos.isEmpty()) {
            alumnoInfoLabel.setText("No hay alumnos registrados");
            return;
        }
        
        if (index < 0) index = 0;
        if (index >= alumnos.size()) index = alumnos.size() - 1;
        
        currentAlumnoIndex = index;
        Alumno alumno = alumnos.get(index);
        
        alumnoInfoLabel.setText(String.format("Alumno: %s %s - Curso: %s", 
                alumno.getNombre(), alumno.getApellidos(), alumno.getCurso()));
        
        // Actualizar combo box de cursos
        cursosComboBox.setSelectedItem(alumno.getCurso());
        
        // Cargar notas del módulo seleccionado
        cargarNotas();
        
        // Actualizar estado de los botones de navegación
        firstButton.setEnabled(index > 0);
        prevButton.setEnabled(index > 0);
        nextButton.setEnabled(index < alumnos.size() - 1);
        lastButton.setEnabled(index < alumnos.size() - 1);
    }
    
    private void cargarNotas() {
        if (currentAlumnoIndex < 0 || currentAlumnoIndex >= alumnos.size()) return;
        if (modulosComboBox.getSelectedItem() == null) return;
        
        int alumnoID = alumnos.get(currentAlumnoIndex).getId();
        String modulo = (String) modulosComboBox.getSelectedItem();
        
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT Nota1, Nota2, Nota3, NotaFinal FROM Notas WHERE AlumnoID = ? AND ModuloID = " +
                "(SELECT ModuloID FROM Modulos WHERE Nombre = ?)")) {
            stmt.setInt(1, alumnoID);
            stmt.setString(2, modulo);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                nota1Field.setText(rs.getString("Nota1"));
                nota2Field.setText(rs.getString("Nota2"));
                nota3Field.setText(rs.getString("Nota3"));
                notaFinalField.setText(rs.getString("NotaFinal"));
            } else {
                nota1Field.setText("");
                nota2Field.setText("");
                nota3Field.setText("");
                notaFinalField.setText("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar notas");
        }
    }
    
    private void guardarNotas() {
        if (currentAlumnoIndex < 0 || currentAlumnoIndex >= alumnos.size()) {
            JOptionPane.showMessageDialog(this, "No hay alumno seleccionado");
            return;
        }
        
        if (modulosComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un módulo");
            return;
        }
        
        int alumnoID = alumnos.get(currentAlumnoIndex).getId();
        String modulo = (String) modulosComboBox.getSelectedItem();
        
        try {
            // Validar notas
            Float nota1 = parseNota(nota1Field.getText());
            Float nota2 = parseNota(nota2Field.getText());
            Float nota3 = parseNota(nota3Field.getText());
            
            if (nota1 == null && nota2 == null && nota3 == null) {
                JOptionPane.showMessageDialog(this, "Introduzca al menos una nota");
                return;
            }
            
            // Calcular nota final (promedio)
            float suma = 0;
            int contador = 0;
            
            if (nota1 != null) { suma += nota1; contador++; }
            if (nota2 != null) { suma += nota2; contador++; }
            if (nota3 != null) { suma += nota3; contador++; }
            
            float notaFinal = suma / contador;
            notaFinalField.setText(String.format("%.2f", notaFinal));
            
            // Guardar en la base de datos
            try (PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT 1 FROM Notas WHERE AlumnoID = ? AND ModuloID = " +
                    "(SELECT ModuloID FROM Modulos WHERE Nombre = ?)")) {
                checkStmt.setInt(1, alumnoID);
                checkStmt.setString(2, modulo);
                ResultSet rs = checkStmt.executeQuery();
                
                if (rs.next()) {
                    // Actualizar registro existente
                    try (PreparedStatement updateStmt = conn.prepareStatement(
                            "UPDATE Notas SET Nota1 = ?, Nota2 = ?, Nota3 = ?, NotaFinal = ? " +
                            "WHERE AlumnoID = ? AND ModuloID = (SELECT ModuloID FROM Modulos WHERE Nombre = ?)")) {
                        updateStmt.setObject(1, nota1, Types.FLOAT);
                        updateStmt.setObject(2, nota2, Types.FLOAT);
                        updateStmt.setObject(3, nota3, Types.FLOAT);
                        updateStmt.setFloat(4, notaFinal);
                        updateStmt.setInt(5, alumnoID);
                        updateStmt.setString(6, modulo);
                        updateStmt.executeUpdate();
                    }
                } else {
                    // Insertar nuevo registro
                    try (PreparedStatement insertStmt = conn.prepareStatement(
                            "INSERT INTO Notas (AlumnoID, ModuloID, Nota1, Nota2, Nota3, NotaFinal) " +
                            "VALUES (?, (SELECT ModuloID FROM Modulos WHERE Nombre = ?), ?, ?, ?, ?)")) {
                        insertStmt.setInt(1, alumnoID);
                        insertStmt.setString(2, modulo);
                        insertStmt.setObject(3, nota1, Types.FLOAT);
                        insertStmt.setObject(4, nota2, Types.FLOAT);
                        insertStmt.setObject(5, nota3, Types.FLOAT);
                        insertStmt.setFloat(6, notaFinal);
                        insertStmt.executeUpdate();
                    }
                }
                
                JOptionPane.showMessageDialog(this, "Notas guardadas correctamente");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar notas");
        }
    }
    
    private Float parseNota(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return null;
        }
        
        try {
            float nota = Float.parseFloat(texto.trim());
            if (nota < 0 || nota > 10) {
                JOptionPane.showMessageDialog(this, "Las notas deben estar entre 0 y 10");
                return null;
            }
            return nota;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Formato de nota incorrecto");
            return null;
        }
    }
    
    private void editarAlumno() {
        if (currentAlumnoIndex < 0 || currentAlumnoIndex >= alumnos.size()) {
            JOptionPane.showMessageDialog(this, "Seleccione un alumno primero");
            return;
        }
        
        Alumno alumno = alumnos.get(currentAlumnoIndex);
        JPanel editarAlumnoPanel = new EditarAlumno(mainPanel, cardLayout, conn, alumno);
        mainPanel.add(editarAlumnoPanel, "EditarAlumno");
        cardLayout.show(mainPanel, "EditarAlumno");
    }
    
    private void añadirAlumno() {
        JPanel añadirAlumnoPanel = new AnadirAlumno(mainPanel, cardLayout, conn);
        mainPanel.add(añadirAlumnoPanel, "AnadirAlumno");
        cardLayout.show(mainPanel, "AnadirAlumno");
    }
    
    private void logout() {
        Usuario.registrarEnFichero(nombreUsuario, "Logout de: " + nombreUsuario);
        JOptionPane.showMessageDialog(this, "Has cerrado sesión");
        cardLayout.show(mainPanel, "buttonsPanel");
    }
    
    // Clase interna para representar alumnos
    private static class Alumno {
        private int id;
        private String nombre;
        private String apellidos;
        private String curso;
        
        public Alumno(int id, String nombre, String apellidos, String curso) {
            this.id = id;
            this.nombre = nombre;
            this.apellidos = apellidos;
            this.curso = curso;
        }
        
        public int getId() { return id; }
        public String getNombre() { return nombre; }
        public String getApellidos() { return apellidos; }
        public String getCurso() { return curso; }
    }
}