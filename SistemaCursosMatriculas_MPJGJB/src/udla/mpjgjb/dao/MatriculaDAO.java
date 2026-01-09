package udla.mpjgjb.dao;

import udla.mpjgjb.modelo.Matricula;
import udla.mpjgjb.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar matriculas en la base de datos.
 * Maneja la inscripcion de estudiantes en cursos.
 * Valida cupos disponibles y evita inscripciones duplicadas.
 */
public class MatriculaDAO {

    /**
     * Registra una nueva matricula en la base de datos.
     * Primero valida que el estudiante no este ya inscrito en el curso.
     * Luego verifica que haya cupos disponibles.
     * Si todo esta bien, registra la matricula y reduce los cupos disponibles.
     */
    public boolean registrarMatricula(Matricula matricula) {
        if (existeMatricula(matricula.getIdEstudiante(), matricula.getIdCurso())) {
            return false;
        }

        if (!hayCuposDisponibles(matricula.getIdCurso())) {
            return false;
        }

        String sql = "INSERT INTO matricula (id_estudiante, id_curso, fecha) VALUES (?, ?, ?)";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, matricula.getIdEstudiante());
            pstmt.setInt(2, matricula.getIdCurso());
            pstmt.setDate(3, java.sql.Date.valueOf(matricula.getFecha()));

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

    /**
     * Lista todas las matriculas registradas en el sistema.
     * Devuelve una lista con todas las inscripciones.
     */
    public List<Matricula> listarMatriculas() {
        List<Matricula> matriculas = new ArrayList<>();
        String sql = "SELECT * FROM matricula";

        try (Connection conn = ConexionDB.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Matricula matricula = new Matricula(
                        rs.getInt("id_matricula"),
                        rs.getInt("id_estudiante"),
                        rs.getInt("id_curso"),
                        rs.getDate("fecha").toLocalDate()
                );
                matriculas.add(matricula);
            }

        } catch (SQLException e) {
            // Si hay error, devuelve lista vacia
        }

        return matriculas;
    }

    /**
     * Busca una matricula especifica usando su ID.
     * Devuelve la matricula si existe, null si no se encuentra.
     */
    public Matricula buscarMatriculaPorId(int id) {
        String sql = "SELECT * FROM matricula WHERE id_matricula = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Matricula(
                        rs.getInt("id_matricula"),
                        rs.getInt("id_estudiante"),
                        rs.getInt("id_curso"),
                        rs.getDate("fecha").toLocalDate()
                );
            }

        } catch (SQLException e) {
            // Si hay error, devuelve null
        }

        return null;
    }

    /**
     * Obtiene todos los cursos en los que esta inscrito un estudiante.
     * Devuelve una lista con todas las matriculas del estudiante.
     */
    public List<Matricula> obtenerMatriculasDelEstudiante(int idEstudiante) {
        List<Matricula> matriculas = new ArrayList<>();
        String sql = "SELECT * FROM matricula WHERE id_estudiante = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idEstudiante);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Matricula matricula = new Matricula(
                        rs.getInt("id_matricula"),
                        rs.getInt("id_estudiante"),
                        rs.getInt("id_curso"),
                        rs.getDate("fecha").toLocalDate()
                );
                matriculas.add(matricula);
            }

        } catch (SQLException e) {
            // Si hay error, devuelve lista vacia
        }

        return matriculas;
    }

    /**
     * Obtiene todos los estudiantes matriculados en un curso específico
     * @param idCurso ID del curso
     * @return Lista de matrículas del curso
     */
    public List<Matricula> obtenerMatriculasDelCurso(int idCurso) {
        List<Matricula> matriculas = new ArrayList<>();
        String sql = "SELECT * FROM matricula WHERE id_curso = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idCurso);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Matricula matricula = new Matricula(
                        rs.getInt("id_matricula"),
                        rs.getInt("id_estudiante"),
                        rs.getInt("id_curso"),
                        rs.getDate("fecha").toLocalDate()
                );
                matriculas.add(matricula);
            }

        } catch (SQLException e) {
            // Si hay error, devuelve lista vacia
        }

        return matriculas;
    }

    /**
     * Verifica si un estudiante ya esta inscrito en un curso.
     * Esto evita que un estudiante se inscriba dos veces en el mismo curso.
     * Devuelve true si ya existe, false si no.
     */
    public boolean existeMatricula(int idEstudiante, int idCurso) {
        String sql = "SELECT COUNT(*) FROM matricula WHERE id_estudiante = ? AND id_curso = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idEstudiante);
            pstmt.setInt(2, idCurso);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            // Si hay error, devuelve false
        }

        return false;
    }

    /**
     * Verifica si un curso tiene cupos libres para mas estudiantes.
     * Devuelve true si hay cupos, false si esta lleno.
     */
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

    /**
     * Reduce en 1 los cupos disponibles cuando un estudiante se inscribe.
     * Solo reduce si hay cupos disponibles mayores a 0.
     */
    private boolean reducirCupoDelCurso(int idCurso) {
        String sql = "UPDATE curso SET cupos_disponibles = cupos_disponibles - 1 WHERE id_curso = ? AND cupos_disponibles > 0";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idCurso);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Aumenta en 1 los cupos disponibles cuando se cancela una matricula.
     * Esto devuelve el cupo para que otro estudiante pueda inscribirse.
     */
    private void aumentarCupoDelCurso(int idCurso) {
        String sql = "UPDATE curso SET cupos_disponibles = cupos_disponibles + 1 WHERE id_curso = ? AND cupos_disponibles < cupo_maximo";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idCurso);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cancela una matrícula (la elimina de la base de datos)
     * @param idMatricula ID de la matrícula a cancelar
     * @return true si se canceló exitosamente, false en caso contrario
     */
    public boolean cancelarMatricula(int idMatricula) {
        Matricula matricula = buscarMatriculaPorId(idMatricula);
        if (matricula == null) {
            return false;
        }

        String sql = "DELETE FROM matricula WHERE id_matricula = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idMatricula);
            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                aumentarCupoDelCurso(matricula.getIdCurso());
                return true;
            }
            return false;

        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Obtiene el número total de estudiantes matriculados en un curso
     * ID del curso
     * Retorna el número de estudiantes matriculados
     */
    public int obtenerCantidadEstudiantesEnCurso(int idCurso) {
        String sql = "SELECT COUNT(*) FROM matricula WHERE id_curso = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idCurso);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            // Si hay error, devuelve 0
        }

        return 0;
    }
}
