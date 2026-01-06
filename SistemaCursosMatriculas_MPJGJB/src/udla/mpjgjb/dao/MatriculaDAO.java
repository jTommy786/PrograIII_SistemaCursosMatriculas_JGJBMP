package udla.mpjgjb.dao;

import udla.mpjgjb.modelo.Matricula;
import udla.mpjgjb.util.ConexionDB;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la gestión de matrículas en la base de datos
 * Responsable de: persistencia, validación de cupos y evitar duplicados
 * Conceptos POO: Encapsulamiento, Integración Java-MySQL
 * @author Julián
 */
public class MatriculaDAO {

    /**
     * Registra una nueva matrícula en la base de datos
     * Valida que el estudiante exista, el curso exista y tenga cupos disponibles
     * Evita matrículas duplicadas
     * @param matricula Objeto Matricula a registrar
     * @return true si se registró exitosamente, false en caso contrario
     */
    public boolean registrarMatricula(Matricula matricula) {
        // Validar que el estudiante no esté ya matriculado en este curso
        if (existeMatricula(matricula.getIdEstudiante(), matricula.getIdCurso())) {
            System.out.println("ERROR: El estudiante ya está matriculado en este curso");
            return false;
        }

        // Validar que hay cupos disponibles
        if (!hayCuposDisponibles(matricula.getIdCurso())) {
            System.out.println("ERROR: No hay cupos disponibles en este curso");
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
                // Reducir cupo disponible del curso
                reducirCupoDelCurso(matricula.getIdCurso());
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Error al registrar matrícula: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lista todas las matrículas registradas en la base de datos
     * @return Lista de matrículas
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
            System.err.println("Error al listar matrículas: " + e.getMessage());
        }

        return matriculas;
    }

    /**
     * Busca una matrícula por su ID
     * @param id ID de la matrícula
     * @return Objeto Matricula si existe, null en caso contrario
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
            System.err.println("Error al buscar matrícula: " + e.getMessage());
        }

        return null;
    }

    /**
     * Obtiene todas las matrículas de un estudiante específico
     * @param idEstudiante ID del estudiante
     * @return Lista de matrículas del estudiante
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
            System.err.println("Error al obtener matrículas del estudiante: " + e.getMessage());
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
            System.err.println("Error al obtener matrículas del curso: " + e.getMessage());
        }

        return matriculas;
    }

    /**
     * Verifica si un estudiante ya está matriculado en un curso (evita duplicados)
     * @param idEstudiante ID del estudiante
     * @param idCurso ID del curso
     * @return true si ya existe la matrícula, false en caso contrario
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
            System.err.println("Error al verificar matrícula duplicada: " + e.getMessage());
        }

        return false;
    }

    /**
     * Verifica si un curso tiene cupos disponibles
     * @param idCurso ID del curso
     * @return true si hay cupos disponibles, false en caso contrario
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
            System.err.println("Error al verificar cupos disponibles: " + e.getMessage());
        }

        return false;
    }

    /**
     * Reduce en uno los cupos disponibles de un curso
     * @param idCurso ID del curso
     * @return true si se actualizó exitosamente, false en caso contrario
     */
    private boolean reducirCupoDelCurso(int idCurso) {
        String sql = "UPDATE curso SET cupos_disponibles = cupos_disponibles - 1 WHERE id_curso = ? AND cupos_disponibles > 0";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idCurso);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al reducir cupo del curso: " + e.getMessage());
            return false;
        }
    }

    /**
     * Aumenta en uno los cupos disponibles de un curso
     * Se utiliza cuando se cancela una matrícula
     * @param idCurso ID del curso
     * @return true si se actualizó exitosamente, false en caso contrario
     */
    private boolean aumentarCupoDelCurso(int idCurso) {
        String sql = "UPDATE curso SET cupos_disponibles = cupos_disponibles + 1 WHERE id_curso = ? AND cupos_disponibles < cupos_maximo";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idCurso);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al aumentar cupo del curso: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cancela una matrícula (la elimina de la base de datos)
     * @param idMatricula ID de la matrícula a cancelar
     * @return true si se canceló exitosamente, false en caso contrario
     */
    public boolean cancelarMatricula(int idMatricula) {
        // Primero obtenemos el ID del curso para liberar el cupo
        Matricula matricula = buscarMatriculaPorId(idMatricula);
        if (matricula == null) {
            System.out.println("ERROR: Matrícula no encontrada");
            return false;
        }

        String sql = "DELETE FROM matricula WHERE id_matricula = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idMatricula);
            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                // Liberar el cupo del curso
                aumentarCupoDelCurso(matricula.getIdCurso());
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Error al cancelar matrícula: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene el número total de estudiantes matriculados en un curso
     * @param idCurso ID del curso
     * @return Número de estudiantes matriculados
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
            System.err.println("Error al obtener cantidad de estudiantes: " + e.getMessage());
        }

        return 0;
    }
}
