/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ernesto;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import java.awt.event.*;

/**
 *
 * @author ifeito-m
 */

public class EditarAlumno extends JPanel {
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private Connection conn;
    private Alumno alumno;
    
    private JTextField nombreField, apellidosField;
    private JComboBox<String> cursoComboBox;
    private JButton guardarBtn, cancelarBtn, eliminarBtn;
    
    public EditarAlumno(JPanel mainPanel, CardLayout cardLayout, Connection conn, Alumno alumno) {
        this.mainPanel = mainPanel;
        this.cardLayout = cardLayout;
        this.conn = conn;
        this.alumno = alumno;
        
        initUI();
        cargarDatosAlumno();
        cargarCursos();
    }
    
    private void initUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Título
        JLabel tituloLabel = new JLabel("Editar Alumno", SwingConstants.CENTER);
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(tituloLabel, gbc);
        
        // Campos de edición
        JLabel nombreLabel = new JLabel("Nombre:");
        nombreField = new JTextField(20);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        add(nombreLabel, gbc);
        gbc.gridx = 1;
        add(nombreField, gbc);
        
        JLabel apellidosLabel = new JLabel("Apellidos:");
        apellidosField = new JTextField(20);
        gbc.gridx = 0; gbc.gridy = 2;
        add(apellidosLabel, gbc);
        gbc.gridx = 1;
        add(apellidosField, gbc);
        
        JLabel cursoLabel = new JLabel("Curso:");
        cursoComboBox = new JComboBox<>();
        gbc.gridx = 0; gbc.gridy = 3;
        add(cursoLabel, gbc);
        gbc.gridx = 1;
        add(cursoComboBox, gbc);
        
        // Panel de botones
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        guardarBtn = new JButton("Guardar");
        guardarBtn.addActionListener(e -> guardarCambios());
        botonesPanel.add(guardarBtn);
        
        cancelarBtn = new JButton("Cancelar");
        cancelarBtn.addActionListener(e -> cardLayout.show(mainPanel, "ProfesorIniciado"));
        botonesPanel.add(cancelarBtn);
        
        eliminarBtn = new JButton("Eliminar");
        eliminarBtn.addActionListener(e -> eliminarAlumno());
        eliminarBtn.setBackground(new Color(255, 100, 100));
        botonesPanel.add(eliminarBtn);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        add(botonesPanel, gbc);
    }
    
    private void cargarDatosAlumno() {
        nombreField.setText(alumno.getNombre());
        apellidosField.setText(alumno.getApellidos());
    }
    
    private void cargarCursos() {
        cursoComboBox.removeAllItems();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT NombreCurso FROM Cursos ORDER BY NombreCurso")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cursoComboBox.addItem(rs.getString("NombreCurso"));
            }
            
            // Seleccionar el curso actual del alumno
            if (alumno.getCurso() != null) {
                cursoComboBox.setSelectedItem(alumno.getCurso());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar cursos");
        }
    }
    
    private void guardarCambios() {
        String nuevoNombre = nombreField.getText().trim();
        String nuevosApellidos = apellidosField.getText().trim();
        String nuevoCurso = (String) cursoComboBox.getSelectedItem();
        
        if (nuevoNombre.isEmpty() || nuevosApellidos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre y apellidos son obligatorios");
            return;
        }
        
        try {
            // Actualizar datos básicos del alumno
            try (PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE Alumnos SET Nombre = ?, Apellidos = ?, Curso = ? WHERE AlumnoID = ?")) {
                stmt.setString(1, nuevoNombre);
                stmt.setString(2, nuevosApellidos);
                stmt.setString(3, nuevoCurso);
                stmt.setInt(4, alumno.getId());
                stmt.executeUpdate();
            }
            
            // Si cambió el curso, actualizar relaciones
            if (nuevoCurso != null && !nuevoCurso.equals(alumno.getCurso())) {
                actualizarCursoAlumno(alumno.getId(), nuevoCurso);
            }
            
            JOptionPane.showMessageDialog(this, "Cambios guardados correctamente");
            cardLayout.show(mainPanel, "ProfesorIniciado");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar cambios");
        }
    }
    
    private void actualizarCursoAlumno(int alumnoID, String nombreCurso) throws SQLException {
        // Obtener ID del nuevo curso
        int cursoID;
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT CursoID FROM Cursos WHERE NombreCurso = ?")) {
            stmt.setString(1, nombreCurso);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                throw new SQLException("Curso no encontrado");
            }
            cursoID = rs.getInt("CursoID");
        }
        
        // Actualizar relación en Alumnos_Cursos
        try (PreparedStatement checkStmt = conn.prepareStatement(
                "SELECT 1 FROM Alumnos_Cursos WHERE AlumnoID = ?")) {
            checkStmt.setInt(1, alumnoID);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                // Actualizar relación existente
                try (PreparedStatement updateStmt = conn.prepareStatement(
                        "UPDATE Alumnos_Cursos SET CursoID = ? WHERE AlumnoID = ?")) {
                    updateStmt.setInt(1, cursoID);
                    updateStmt.setInt(2, alumnoID);
                    updateStmt.executeUpdate();
                }
            } else {
                // Crear nueva relación
                try (PreparedStatement insertStmt = conn.prepareStatement(
                        "INSERT INTO Alumnos_Cursos (AlumnoID, CursoID) VALUES (?, ?)")) {
                    insertStmt.setInt(1, alumnoID);
                    insertStmt.setInt(2, cursoID);
                    insertStmt.executeUpdate();
                }
            }
        }
    }
    
    private void eliminarAlumno() {
        int confirmacion = JOptionPane.showConfirmDialog(
            this, 
            "¿Está seguro que desea eliminar este alumno?\nEsta acción no se puede deshacer.", 
            "Confirmar eliminación", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                // Eliminar relaciones primero para evitar problemas de clave foránea
                try (PreparedStatement stmt = conn.prepareStatement(
                        "DELETE FROM Alumnos_Modulos WHERE AlumnoID = ?")) {
                    stmt.setInt(1, alumno.getId());
                    stmt.executeUpdate();
                }
                
                try (PreparedStatement stmt = conn.prepareStatement(
                        "DELETE FROM Alumnos_Cursos WHERE AlumnoID = ?")) {
                    stmt.setInt(1, alumno.getId());
                    stmt.executeUpdate();
                }
                
                try (PreparedStatement stmt = conn.prepareStatement(
                        "DELETE FROM Notas WHERE AlumnoID = ?")) {
                    stmt.setInt(1, alumno.getId());
                    stmt.executeUpdate();
                }
                
                // Finalmente eliminar el alumno
                try (PreparedStatement stmt = conn.prepareStatement(
                        "DELETE FROM Alumnos WHERE AlumnoID = ?")) {
                    stmt.setInt(1, alumno.getId());
                    stmt.executeUpdate();
                }
                
                JOptionPane.showMessageDialog(this, "Alumno eliminado correctamente");
                cardLayout.show(mainPanel, "ProfesorIniciado");
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al eliminar alumno");
            }
        }
    }
    
    // Clase interna para representar alumnos (debe coincidir con la usada en ProfesorIniciado)
    public static class Alumno {
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
