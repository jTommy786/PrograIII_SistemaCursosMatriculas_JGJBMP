package udla.mpjgjb.dao;

import udla.mpjgjb.modelo.Docente;
import udla.mpjgjb.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// DAO para la gestion de docentes en la base de datos
public class DocenteDAO {

    // Registra un nuevo docente en la base de datos
    public boolean registrarDocente(Docente docente) {
        String sql = "INSERT INTO docente (nombre, cedula, especialidad) VALUES (?, ?, ?)";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, docente.getNombre());
            pstmt.setString(2, docente.getCedula());
            pstmt.setString(3, docente.getEspecialidad());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    // Lista todos los docentes de la base de datos
    public List<Docente> listarDocentes() {
        List<Docente> docentes = new ArrayList<>();
        String sql = "SELECT * FROM docente";

        try (Connection conn = ConexionDB.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Docente docente = new Docente(
                        rs.getInt("id_docente"),
                        rs.getString("nombre"),
                        rs.getString("cedula"),
                        rs.getString("especialidad")
                );
                docentes.add(docente);
            }
        } catch (SQLException e) {
            // Si hay error, devuelve lista vacia
        }

        return docentes;
    }

    // Busca un docente por su ID
    public Docente buscarDocentePorId(int id) {
        String sql = "SELECT * FROM docente WHERE id_docente = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Docente(
                    rs.getInt("id_docente"),
                    rs.getString("nombre"),
                    rs.getString("cedula"),
                    rs.getString("especialidad")
                );
            }
        } catch (SQLException e) {
            // Si hay error, devuelve null
        }
        return null;
    }

    // Actualiza un docente existente
    public boolean actualizarDocente(Docente docente) {
        String sql = "UPDATE docente SET nombre = ?, cedula = ?, especialidad = ? WHERE id_docente = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, docente.getNombre());
            pstmt.setString(2, docente.getCedula());
            pstmt.setString(3, docente.getEspecialidad());
            pstmt.setInt(4, docente.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    // Elimina un docente por su ID
    // Verifica que no tenga cursos asignados antes de eliminar
    public boolean eliminarDocente(int id) {
        // Primero verificar si el docente tiene cursos asignados
        String sqlVerificar = "SELECT COUNT(*) FROM curso WHERE id_docente = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmtVerif = conn.prepareStatement(sqlVerificar)) {
            
            pstmtVerif.setInt(1, id);
            ResultSet rs = pstmtVerif.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                // El docente tiene cursos asignados, no se puede eliminar
                return false;
            }
            
        } catch (SQLException e) {
            return false;
        }
        
        // Si no tiene cursos asignados, proceder con la eliminación
        String sql = "DELETE FROM docente WHERE id_docente = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}