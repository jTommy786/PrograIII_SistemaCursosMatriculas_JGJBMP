package udla.mpjgjb.dao;

import udla.mpjgjb.modelo.Matricula;
import udla.mpjgjb.modelo.EstadoMatricula;
import udla.mpjgjb.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// DAO para gestionar matriculas en la base de datos
public class MatriculaDAO {

    // Convierte un String a EstadoMatricula
    private EstadoMatricula convertirEstado(String texto) {
        if (texto == null) return EstadoMatricula.ACTIVA;
        
        try {
            return EstadoMatricula.valueOf(texto.toUpperCase());
        } catch (IllegalArgumentException e) {
            return EstadoMatricula.ACTIVA;
        }
    }

    // Registra una nueva matricula con estado ACTIVA
    // Valida que no exista matricula activa previa y que haya cupos
    public boolean registrarMatricula(Matricula matricula) {
        if (existeMatriculaActiva(matricula.getIdEstudiante(), matricula.getIdCurso())) {
            return false;
        }

        if (!hayCuposDisponibles(matricula.getIdCurso())) {
            return false;
        }

        String sql = "INSERT INTO matricula (id_estudiante, id_curso, fecha, estado) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, matricula.getIdEstudiante());
            pstmt.setInt(2, matricula.getIdCurso());
            pstmt.setDate(3, java.sql.Date.valueOf(matricula.getFecha()));
            pstmt.setString(4, EstadoMatricula.ACTIVA.name());

            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                reducirCupoDelCurso(matricula.getIdCurso());
                return true;
            }
            return false;

        } catch (SQLException e) {
            return false;
        }
    }

    // Lista todas las matriculas registradas
    public List<Matricula> listarMatriculas() {
        List<Matricula> matriculas = new ArrayList<>();
        String sql = "SELECT * FROM matricula";

        try (Connection conn = ConexionDB.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                EstadoMatricula estado = convertirEstado(rs.getString("estado"));
                Matricula matricula = new Matricula(
                        rs.getInt("id_matricula"),
                        rs.getInt("id_estudiante"),
                        rs.getInt("id_curso"),
                        rs.getDate("fecha").toLocalDate(),
                        estado
                );
                matriculas.add(matricula);
            }

        } catch (SQLException e) {
            // Si hay error, devuelve lista vacia
        }

        return matriculas;
    }

    // Busca una matricula por su ID
    public Matricula buscarMatriculaPorId(int id) {
        String sql = "SELECT * FROM matricula WHERE id_matricula = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                EstadoMatricula estado = convertirEstado(rs.getString("estado"));
                return new Matricula(
                        rs.getInt("id_matricula"),
                        rs.getInt("id_estudiante"),
                        rs.getInt("id_curso"),
                        rs.getDate("fecha").toLocalDate(),
                        estado
                );
            }

        } catch (SQLException e) {
            // Si hay error, devuelve null
        }

        return null;
    }

    // Obtiene todas las matriculas de un estudiante
    public List<Matricula> obtenerMatriculasDelEstudiante(int idEstudiante) {
        List<Matricula> matriculas = new ArrayList<>();
        String sql = "SELECT * FROM matricula WHERE id_estudiante = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idEstudiante);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                EstadoMatricula estado = convertirEstado(rs.getString("estado"));
                Matricula matricula = new Matricula(
                        rs.getInt("id_matricula"),
                        rs.getInt("id_estudiante"),
                        rs.getInt("id_curso"),
                        rs.getDate("fecha").toLocalDate(),
                        estado
                );
                matriculas.add(matricula);
            }

        } catch (SQLException e) {
            // Si hay error, devuelve lista vacia
        }

        return matriculas;
    }

    // Obtiene todos los estudiantes matriculados en un curso
    public List<Matricula> obtenerMatriculasDelCurso(int idCurso) {
        List<Matricula> matriculas = new ArrayList<>();
        String sql = "SELECT * FROM matricula WHERE id_curso = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idCurso);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                EstadoMatricula estado = convertirEstado(rs.getString("estado"));
                Matricula matricula = new Matricula(
                        rs.getInt("id_matricula"),
                        rs.getInt("id_estudiante"),
                        rs.getInt("id_curso"),
                        rs.getDate("fecha").toLocalDate(),
                        estado
                );
                matriculas.add(matricula);
            }

        } catch (SQLException e) {
            // Si hay error, devuelve lista vacia
        }

        return matriculas;
    }

    // Verifica si existe una matricula activa para un estudiante en un curso
    public boolean existeMatriculaActiva(int idEstudiante, int idCurso) {
        String sql = "SELECT COUNT(*) FROM matricula WHERE id_estudiante = ? AND id_curso = ? AND estado = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idEstudiante);
            pstmt.setInt(2, idCurso);
            pstmt.setString(3, EstadoMatricula.ACTIVA.name());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            // Si hay error, devuelve false
        }

        return false;
    }

    // Verifica si un curso tiene cupos disponibles
    public boolean hayCuposDisponibles(int idCurso) {
        String sql = "SELECT cupos_disponibles FROM curso WHERE id_curso = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idCurso);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("cupos_disponibles") > 0;
            }

        } catch (SQLException e) {
            // Si hay error, devuelve false
        }

        return false;
    }

    // Reduce en 1 los cupos disponibles de un curso
    private void reducirCupoDelCurso(int idCurso) {
        String sql = "UPDATE curso SET cupos_disponibles = cupos_disponibles - 1 WHERE id_curso = ? AND cupos_disponibles > 0";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idCurso);

        } catch (SQLException e) {
        }
    }

    // Aumenta en 1 los cupos disponibles de un curso
    private void aumentarCupoDelCurso(int idCurso) {
        String sql = "UPDATE curso SET cupos_disponibles = cupos_disponibles + 1 WHERE id_curso = ? AND cupos_disponibles < cupo_maximo";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idCurso);
            pstmt.executeUpdate();

        } catch (SQLException e) {
        }
    }

    // Cancela una matricula (cambia estado a CANCELADA)
    public boolean cancelarMatricula(int idMatricula) {
        Matricula matricula = buscarMatriculaPorId(idMatricula);
        if (matricula == null) {
            return false;
        }

        String sql = "UPDATE matricula SET estado = ? WHERE id_matricula = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, EstadoMatricula.CANCELADA.name());
            pstmt.setInt(2, idMatricula);
            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0 && matricula.getEstado() == EstadoMatricula.ACTIVA) {
                aumentarCupoDelCurso(matricula.getIdCurso());
                return true;
            }
            return filasAfectadas > 0;

        } catch (SQLException e) {
            return false;
        }
    }
    
    // Cambia el estado de una matricula y ajusta los cupos
    public boolean cambiarEstadoMatricula(int idMatricula, EstadoMatricula nuevoEstado) {
        Matricula matricula = buscarMatriculaPorId(idMatricula);
        if (matricula == null) {
            return false;
        }

        EstadoMatricula estadoAnterior = matricula.getEstado();
        String sql = "UPDATE matricula SET estado = ? WHERE id_matricula = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nuevoEstado.name());
            pstmt.setInt(2, idMatricula);
            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                // Si se cambia de ACTIVA a otro estado, aumentar cupo
                if (estadoAnterior == EstadoMatricula.ACTIVA && nuevoEstado != EstadoMatricula.ACTIVA) {
                    aumentarCupoDelCurso(matricula.getIdCurso());
                }
                // Si se cambia de otro estado a ACTIVA, reducir cupo
                else if (estadoAnterior != EstadoMatricula.ACTIVA && nuevoEstado == EstadoMatricula.ACTIVA) {
                    reducirCupoDelCurso(matricula.getIdCurso());
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            return false;
        }
    }
}
