package ernesto;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import java.util.ArrayList;

public class AnadirAlumno extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Connection conn;
    private JComboBox<String> alumnoComboBox, cursoComboBox, moduloComboBox;
    private JButton asignarBtn, backBtn;

    public AnadirAlumno(JPanel mainPanel, CardLayout cardLayout, Connection conn) {
        this.mainPanel = mainPanel;
        this.cardLayout = cardLayout;
        this.conn = conn;

        initUI();
        cargarDatosIniciales();
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel tituloLabel = new JLabel("Añadir Alumno a Curso/Módulo", SwingConstants.CENTER);
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(tituloLabel, gbc);

        // Selección de Alumno
        JLabel alumnoLabel = new JLabel("Seleccionar Alumno:");
        alumnoComboBox = new JComboBox<>();
        alumnoComboBox.setPreferredSize(new Dimension(200, 25));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        add(alumnoLabel, gbc);
        gbc.gridx = 1;
        add(alumnoComboBox, gbc);

        // Selección de Curso
        JLabel cursoLabel = new JLabel("Seleccionar Curso:");
        cursoComboBox = new JComboBox<>();
        cursoComboBox.addActionListener(e -> cargarModulos());
        gbc.gridx = 0; gbc.gridy = 2;
        add(cursoLabel, gbc);
        gbc.gridx = 1;
        add(cursoComboBox, gbc);

        // Selección de Módulo
        JLabel moduloLabel = new JLabel("Seleccionar Módulo:");
        moduloComboBox = new JComboBox<>();
        gbc.gridx = 0; gbc.gridy = 3;
        add(moduloLabel, gbc);
        gbc.gridx = 1;
        add(moduloComboBox, gbc);

        // Botón Asignar
        asignarBtn = new JButton("Asignar Alumno");
        asignarBtn.addActionListener(e -> asignarAlumno());
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        add(asignarBtn, gbc);

        // Botón Volver
        backBtn = new JButton("Volver");
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "ProfesorIniciado"));
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        add(backBtn, gbc);
    }

    private void cargarDatosIniciales() {
        cargarAlumnos();
        cargarCursos();
    }

    private void cargarAlumnos() {
	alumnoComboBox.removeAllItems();
	try (PreparedStatement stmt = conn.prepareStatement(
		"SELECT AlumnoID, Nombre, Apellidos FROM Alumnos ORDER BY Apellidos, Nombre")) {
	    ResultSet rs = stmt.executeQuery();
	    while (rs.next()) {
		String nombreCompleto = rs.getString("Nombre") + " " + rs.getString("Apellidos");
		alumnoComboBox.addItem(nombreCompleto);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    JOptionPane.showMessageDialog(this, "Error al cargar alumnos");
	}
    }

    private void cargarCursos() {
	cursoComboBox.removeAllItems();
	try (PreparedStatement stmt = conn.prepareStatement(
		"SELECT CursoID, NombreCurso FROM Cursos ORDER BY NombreCurso")) {
	    ResultSet rs = stmt.executeQuery();
	    while (rs.next()) {
		cursoComboBox.addItem(rs.getString("NombreCurso"));
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    JOptionPane.showMessageDialog(this, "Error al cargar cursos");
	}
    }

    private void cargarModulos() {
        moduloComboBox.removeAllItems();
        String cursoSeleccionado = (String) cursoComboBox.getSelectedItem();
        if (cursoSeleccionado == null) return;

        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT m.ModuloID, m.Nombre FROM Modulos m " +
                "JOIN Cursos c ON m.CursoID = c.CursoID " +
                "WHERE c.NombreCurso = ? ORDER BY m.Nombre")) {
            stmt.setString(1, cursoSeleccionado);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                moduloComboBox.addItem(rs.getString("Nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar módulos");
        }
    }

    private void asignarAlumno() {
        String alumnoSeleccionado = (String) alumnoComboBox.getSelectedItem();
        String cursoSeleccionado = (String) cursoComboBox.getSelectedItem();
        String moduloSeleccionado = (String) moduloComboBox.getSelectedItem();

        if (alumnoSeleccionado == null || cursoSeleccionado == null || moduloSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un alumno, un curso y un módulo");
            return;
        }

        try {
            // Obtener IDs
            int alumnoID = obtenerID("SELECT AlumnoID FROM Alumnos WHERE CONCAT(Nombre, ' ', Apellidos) = ?", alumnoSeleccionado);
            int cursoID = obtenerID("SELECT CursoID FROM Cursos WHERE NombreCurso = ?", cursoSeleccionado);
            int moduloID = obtenerID("SELECT ModuloID FROM Modulos WHERE Nombre = ?", moduloSeleccionado);

            if (alumnoID == -1 || cursoID == -1 || moduloID == -1) {
                JOptionPane.showMessageDialog(this, "Error al obtener datos de la base de datos");
                return;
            }

            // Verificar si ya existe la asignación
            if (existeAsignacion(alumnoID, moduloID)) {
                JOptionPane.showMessageDialog(this, "Este alumno ya está asignado a este módulo");
                return;
            }

            // Asignar alumno al módulo
            asignarAlumnoModulo(alumnoID, moduloID);
            
            // Actualizar curso del alumno
//            actualizarCursoAlumno(alumnoID, cursoID);

            JOptionPane.showMessageDialog(this, "Alumno asignado correctamente");
            cardLayout.show(mainPanel, "ProfesorIniciado");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al asignar alumno");
        }
    }

    private int obtenerID(String query, String valor) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, valor);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    private boolean existeAsignacion(int alumnoID, int moduloID) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT 1 FROM Alumnos_Modulos WHERE AlumnoID = ? AND ModuloID = ?")) {
            stmt.setInt(1, alumnoID);
            stmt.setInt(2, moduloID);
            return stmt.executeQuery().next();
        }
    }

    private void asignarAlumnoModulo(int alumnoID, int moduloID) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO Alumnos_Modulos (AlumnoID, ModuloID) VALUES (?, ?)")) {
            stmt.setInt(1, alumnoID);
            stmt.setInt(2, moduloID);
            stmt.executeUpdate();
        }
    }

    private void asociarAlumnoCurso(int alumnoID, String cursoSeleccionado) {
	try {
	    // Obtener ID del curso
	    int cursoID;
	    try (PreparedStatement stmt = conn.prepareStatement(
		    "SELECT CursoID FROM Cursos WHERE NombreCurso = ?")) {
		stmt.setString(1, cursoSeleccionado);
		ResultSet rs = stmt.executeQuery();
		if (!rs.next()) {
		    throw new SQLException("Curso no encontrado");
		}
		cursoID = rs.getInt("CursoID");
	    }

	    // Verificar si ya existe la relación
	    try (PreparedStatement checkStmt = conn.prepareStatement(
		    "SELECT 1 FROM Alumnos_Cursos WHERE AlumnoID = ? AND CursoID = ?")) {
		checkStmt.setInt(1, alumnoID);
		checkStmt.setInt(2, cursoID);
		ResultSet rs = checkStmt.executeQuery();

		if (rs.next()) {
		    JOptionPane.showMessageDialog(this, "El alumno ya está asignado a este curso");
		    return;
		}
	    }

	    // Crear nueva relación
	    try (PreparedStatement insertStmt = conn.prepareStatement(
		    "INSERT INTO Alumnos_Cursos (AlumnoID, CursoID, AñoAcademico) VALUES (?, ?, ?)")) {
		insertStmt.setInt(1, alumnoID);
		insertStmt.setInt(2, cursoID);
		insertStmt.setInt(3, java.time.Year.now().getValue());
		insertStmt.executeUpdate();
		JOptionPane.showMessageDialog(this, "Alumno asignado al curso correctamente");
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    JOptionPane.showMessageDialog(this, "Error al asignar alumno al curso");
	}
    }
}