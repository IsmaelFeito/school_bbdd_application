package ernesto;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

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
    
    private List<AlumnoPersona> alumnos = new ArrayList<>();
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
        modulosComboBox.addActionListener(e -> cargarNotas());
        
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
                "SELECT a.AlumnoID, a.Nombre, a.Apellidos, a.Edad, a.FechaMatricula, " +
                "c.NombreCurso, cl.NombreCiclo " +
                "FROM Alumnos a " +
                "LEFT JOIN Alumnos_Cursos ac ON a.AlumnoID = ac.AlumnoID " +
                "LEFT JOIN Cursos c ON ac.CursoID = c.CursoID " +
                "LEFT JOIN Ciclos cl ON c.CicloID = cl.CicloID " +
                "ORDER BY a.Apellidos, a.Nombre")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                AlumnoPersona alumno = new AlumnoPersona(
                    rs.getInt("AlumnoID"),
                    rs.getString("Nombre"),
                    rs.getString("Apellidos"),
                    rs.getInt("Edad"),
                    rs.getDate("FechaMatricula"),
                    rs.getString("NombreCurso"),
                    rs.getString("NombreCiclo")
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
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT CursoID, NombreCurso FROM Cursos ORDER BY NombreCurso")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cursosComboBox.addItem(rs.getString("NombreCurso"));
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
                "SELECT a.AsignaturaID, a.NombreAsignatura " +
                "FROM Asignaturas a " +
                "JOIN Cursos c ON a.CursoID = c.CursoID " +
                "WHERE c.NombreCurso = ? ORDER BY a.NombreAsignatura")) {
            stmt.setString(1, cursoSeleccionado);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                modulosComboBox.addItem(rs.getString("NombreAsignatura"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar asignaturas/módulos");
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
        AlumnoPersona alumno = alumnos.get(index);
        
        alumnoInfoLabel.setText(String.format("Alumno: %s %s - Edad: %d - Matrícula: %s - Ciclo: %s - Curso: %s", 
                alumno.getNombre(), alumno.getApellidos(), alumno.getEdad(),
                alumno.getFechaMatricula(), alumno.getCiclo(), alumno.getCurso()));
        
        // Actualizar combo box de cursos
        if (alumno.getCurso() != null) {
            cursosComboBox.setSelectedItem(alumno.getCurso());
        }
        
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
        String asignatura = (String) modulosComboBox.getSelectedItem();

        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT Evaluacion1, Evaluacion2, Evaluacion3, NotaFinal " +
                "FROM Notas_Completa WHERE AlumnoID = ? AND AsignaturaID = " +
                "(SELECT AsignaturaID FROM Asignaturas WHERE NombreAsignatura = ?)")) {
            stmt.setInt(1, alumnoID);
            stmt.setString(2, asignatura);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nota1Field.setText(rs.getObject("Evaluacion1") != null ? rs.getString("Evaluacion1") : "");
                nota2Field.setText(rs.getObject("Evaluacion2") != null ? rs.getString("Evaluacion2") : "");
                nota3Field.setText(rs.getObject("Evaluacion3") != null ? rs.getString("Evaluacion3") : "");
                notaFinalField.setText(rs.getObject("NotaFinal") != null ? rs.getString("NotaFinal") : "");
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
            
            // Verificar que al menos una nota tenga valor
            if (nota1 == null && nota2 == null && nota3 == null) {
                JOptionPane.showMessageDialog(this, "Introduzca al menos una nota");
                return;
            }
            
            // Calcular nota final (promedio de las notas existentes)
            float suma = 0;
            int contador = 0;
            
            if (nota1 != null) { suma += nota1; contador++; }
            if (nota2 != null) { suma += nota2; contador++; }
            if (nota3 != null) { suma += nota3; contador++; }
            
            float notaFinal = suma / contador;
            notaFinalField.setText(String.format("%.2f", notaFinal));
            
            // Obtener el ID de la asignatura
            int asignaturaID = obtenerAsignaturaID(modulo);
            if (asignaturaID == -1) {
                JOptionPane.showMessageDialog(this, "No se pudo encontrar el módulo seleccionado");
                return;
            }
            
            // Verificar si ya existen notas para este alumno y módulo
            boolean existeRegistro = existeNotaAlumno(alumnoID, asignaturaID);
            
            if (existeRegistro) {
                // Actualizar registro existente
                actualizarNotas(alumnoID, asignaturaID, nota1, nota2, nota3, notaFinal);
            } else {
                // Insertar nuevo registro
                insertarNotas(alumnoID, asignaturaID, nota1, nota2, nota3, notaFinal);
            }
            
            JOptionPane.showMessageDialog(this, "Notas guardadas correctamente");
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar notas: " + e.getMessage());
        }
    }
    
    private int obtenerAsignaturaID(String nombreAsignatura) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT AsignaturaID FROM Asignaturas WHERE NombreAsignatura = ?")) {
            stmt.setString(1, nombreAsignatura);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("AsignaturaID") : -1;
        }
    }
    
    private boolean existeNotaAlumno(int alumnoID, int asignaturaID) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT 1 FROM Notas_Completa WHERE AlumnoID = ? AND AsignaturaID = ?")) {
            stmt.setInt(1, alumnoID);
            stmt.setInt(2, asignaturaID);
            return stmt.executeQuery().next();
        }
    }
    
    private void actualizarNotas(int alumnoID, int asignaturaID, Float nota1, Float nota2, Float nota3, float notaFinal) 
            throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(
                "UPDATE Notas_Completa SET " +
                "Evaluacion1 = ?, Evaluacion2 = ?, Evaluacion3 = ?, NotaFinal = ? " +
                "WHERE AlumnoID = ? AND AsignaturaID = ?")) {
            
            stmt.setObject(1, nota1, Types.FLOAT);
            stmt.setObject(2, nota2, Types.FLOAT);
            stmt.setObject(3, nota3, Types.FLOAT);
            stmt.setFloat(4, notaFinal);
            stmt.setInt(5, alumnoID);
            stmt.setInt(6, asignaturaID);
            
            stmt.executeUpdate();
        }
    }
    
    private void insertarNotas(int alumnoID, int asignaturaID, Float nota1, Float nota2, Float nota3, float notaFinal) 
            throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO Notas_Completa " +
                "(AlumnoID, AsignaturaID, Evaluacion1, Evaluacion2, Evaluacion3, NotaFinal) " +
                "VALUES (?, ?, ?, ?, ?, ?)")) {
            
            stmt.setInt(1, alumnoID);
            stmt.setInt(2, asignaturaID);
            stmt.setObject(3, nota1, Types.FLOAT);
            stmt.setObject(4, nota2, Types.FLOAT);
            stmt.setObject(5, nota3, Types.FLOAT);
            stmt.setFloat(6, notaFinal);
            
            stmt.executeUpdate();
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
            JOptionPane.showMessageDialog(this, "Formato de nota incorrecto. Use números entre 0 y 10");
            return null;    
        }
    }
    
    private void editarAlumno() {
        if (currentAlumnoIndex < 0 || currentAlumnoIndex >= alumnos.size()) {
            JOptionPane.showMessageDialog(this, "Seleccione un alumno primero");
            return;
        }
        
        AlumnoPersona alumno = alumnos.get(currentAlumnoIndex);
        JPanel editarAlumnoPanel = new EditarAlumno(mainPanel, cardLayout, conn, alumno);
        mainPanel.add(editarAlumnoPanel, "EditarAlumno");
        cardLayout.show(mainPanel, "EditarAlumno");
        
        // Actualizar la lista de alumnos después de editar
        editarAlumnoPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {
                cargarAlumnos();
                if (!alumnos.isEmpty()) {
                    mostrarAlumno(currentAlumnoIndex);
                }
            }
        });
    }
    
    private void añadirAlumno() {
        JPanel añadirAlumnoPanel = new AnadirAlumno(mainPanel, cardLayout, conn);
        mainPanel.add(añadirAlumnoPanel, "AnadirAlumno");
        cardLayout.show(mainPanel, "AnadirAlumno");
        
        // Actualizar la lista de alumnos después de añadir
        añadirAlumnoPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {
                cargarAlumnos();
                if (!alumnos.isEmpty()) {
                    mostrarAlumno(alumnos.size() - 1); // Mostrar el último alumno añadido
                }
            }
        });
    }
    
    private void logout() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        Usuario.registrarEnFichero(nombreUsuario, "Logout de: " + nombreUsuario);
        JOptionPane.showMessageDialog(this, "Has cerrado sesión");
        cardLayout.show(mainPanel, "buttonsPanel");
    }
    
    // Clase interna para representar los datos del alumno
    private static class AlumnoPersona {
        private final int id;
        private final String nombre;
        private final String apellidos;
        private final int edad;
        private final Date fechaMatricula;
        private final String curso;
        private final String ciclo;
        
        public AlumnoPersona(int id, String nombre, String apellidos, int edad, 
                           Date fechaMatricula, String curso, String ciclo) {
            this.id = id;
            this.nombre = nombre;
            this.apellidos = apellidos;
            this.edad = edad;
            this.fechaMatricula = fechaMatricula;
            this.curso = curso;
            this.ciclo = ciclo;
        }
        
        // Getters
        public int getId() { return id; }
        public String getNombre() { return nombre; }
        public String getApellidos() { return apellidos; }
        public int getEdad() { return edad; }
        public Date getFechaMatricula() { return fechaMatricula; }
        public String getCurso() { return curso; }
        public String getCiclo() { return ciclo; }
    }
}