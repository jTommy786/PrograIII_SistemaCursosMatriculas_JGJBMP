package udla.mpjgjb.dao;

import udla.mpjgjb.modelo.Estudiante;
import udla.mpjgjb.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la gestion de estudiantes en la base de datos
 */
public class EstudianteDAO {

    // Registra un nuevo estudiante en la base de datos
    public boolean registrarEstudiante(Estudiante estudiante) {
        String sql = "INSERT INTO estudiante (nombre, cedula, email) VALUES (?, ?, ?)";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, estudiante.getNombre());
            pstmt.setString(2, estudiante.getCedula());
            pstmt.setString(3, estudiante.getEmail());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lista todos los estudiantes de la base de datos
    public List<Estudiante> listarEstudiantes() {
        List<Estudiante> estudiantes = new ArrayList<>();
        String sql = "SELECT * FROM estudiante";

        try (Connection conn = ConexionDB.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Estudiante estudiante = new Estudiante(
                        rs.getInt("id_estudiante"),
                        rs.getString("nombre"),
                        rs.getString("cedula"),
                        rs.getString("email")
                );
                estudiantes.add(estudiante);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return estudiantes;
    }

    // Busca un estudiante por su ID
    public Estudiante buscarEstudiantePorId(int id) {
        String sql = "SELECT * FROM estudiante WHERE id_estudiante = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Estudiante(
                        rs.getInt("id_estudiante"),
                        rs.getString("nombre"),
                        rs.getString("cedula"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}