package udla.mpjgjb.dao;

import udla.mpjgjb.modelo.Curso;
import udla.mpjgjb.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CursoDAO {
    
    // Registra un nuevo curso en la base de datos
    public boolean registrarCurso(Curso curso) {
        String sql = "INSERT INTO curso (nombre, descripcion, cupos_totales, cupos_disponibles, docente_id) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, curso.getNombre());
            pstmt.setString(2, curso.getDescripcion());
            pstmt.setInt(3, curso.getCuposTotales());
            pstmt.setInt(4, curso.getCuposTotales()); // Al inicio, todos los cupos están disponibles
            
            if (curso.getDocenteId() != null) {
                pstmt.setInt(5, curso.getDocenteId());
            } else {
                pstmt.setNull(5, Types.INTEGER);
            }
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al registrar curso: " + e.getMessage());
            return false;
        }
    }
    
    // Lista todos los cursos de la base de datos
    public List<Curso> listarCursos() {
        List<Curso> cursos = new ArrayList<>();
        String sql = "SELECT * FROM curso";
        
        try (Connection conn = ConexionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Curso curso = new Curso();
                curso.setId(rs.getInt("id"));
                curso.setNombre(rs.getString("nombre"));
                curso.setDescripcion(rs.getString("descripcion"));
                curso.setCuposTotales(rs.getInt("cupos_totales"));
                curso.setCuposDisponibles(rs.getInt("cupos_disponibles"));
                
                int docenteId = rs.getInt("docente_id");
                if (!rs.wasNull()) {
                    curso.setDocenteId(docenteId);
                }
                
                cursos.add(curso);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar cursos: " + e.getMessage());
        }
        
        return cursos;
    }
    
    // Busca un curso por su ID
    public Curso buscarCursoPorId(int id) {
        String sql = "SELECT * FROM curso WHERE id = ?";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Curso curso = new Curso();
                curso.setId(rs.getInt("id"));
                curso.setNombre(rs.getString("nombre"));
                curso.setDescripcion(rs.getString("descripcion"));
                curso.setCuposTotales(rs.getInt("cupos_totales"));
                curso.setCuposDisponibles(rs.getInt("cupos_disponibles"));
                
                int docenteId = rs.getInt("docente_id");
                if (!rs.wasNull()) {
                    curso.setDocenteId(docenteId);
                }
                
                return curso;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar curso: " + e.getMessage());
        }
        
        return null;
    }
    
    // Asigna un docente a un curso
    public boolean asignarDocente(int cursoId, int docenteId) {
        String sql = "UPDATE curso SET docente_id = ? WHERE id = ?";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, docenteId);
            pstmt.setInt(2, cursoId);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al asignar docente: " + e.getMessage());
            return false;
        }
    }
    
    // Actualiza los cupos disponibles de un curso
    public boolean actualizarCupos(int cursoId, int cuposDisponibles) {
        String sql = "UPDATE curso SET cupos_disponibles = ? WHERE id = ?";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, cuposDisponibles);
            pstmt.setInt(2, cursoId);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar cupos: " + e.getMessage());
            return false;
        }
    }
    
    // Reduce un cupo disponible de un curso
    public boolean reducirCupo(int cursoId) {
        String sql = "UPDATE curso SET cupos_disponibles = cupos_disponibles - 1 WHERE id = ? AND cupos_disponibles > 0";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, cursoId);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al reducir cupo: " + e.getMessage());
            return false;
        }
    }
    
    // Aumenta un cupo disponible de un curso
    public boolean aumentarCupo(int cursoId) {
        String sql = "UPDATE curso SET cupos_disponibles = cupos_disponibles + 1 WHERE id = ? AND cupos_disponibles < cupos_totales";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, cursoId);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al aumentar cupo: " + e.getMessage());
            return false;
        }
    }
    
    // Verifica si un curso tiene cupos disponibles
    public boolean hayCuposDisponibles(int cursoId) {
        String sql = "SELECT cupos_disponibles FROM curso WHERE id = ?";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, cursoId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("cupos_disponibles") > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al verificar cupos: " + e.getMessage());
        }
        
        return false;
    }
    
    // Actualiza la informacion de un curso
    public boolean actualizarCurso(Curso curso) {
        String sql = "UPDATE curso SET nombre = ?, descripcion = ?, cupos_totales = ? WHERE id = ?";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, curso.getNombre());
            pstmt.setString(2, curso.getDescripcion());
            pstmt.setInt(3, curso.getCuposTotales());
            pstmt.setInt(4, curso.getId());
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar curso: " + e.getMessage());
            return false;
        }
    }
}
