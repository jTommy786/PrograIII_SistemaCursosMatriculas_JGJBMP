package udla.mpjgjb.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase ConexionDB
 * Gestiona la conexión a la base de datos MySQL
 * Conceptos POO: Encapsulamiento, Patrón Singleton
 * @author Julián
 */
public class ConexionDB {

    // Configuración de la conexión a MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/cursos_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";        // Cambiar según la configuración local
    private static final String PASSWORD = "Julianbonilla1";        // Cambiar según la contraseña local

    /**
     * Obtiene una conexión a la base de datos
     * @return Conexión a MySQL
     * @throws SQLException Si hay error en la conexión
     */
    public static Connection getConexion() throws SQLException {
        try {
            // Cargar el driver JDBC de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver de MySQL no encontrado: " + e.getMessage(), e);
        }
    }

    /**
     * Verifica si la conexión a la base de datos es válida
     * @return true si la conexión es válida, false en caso contrario
     */
    public static boolean verificarConexion() {
        try (Connection conn = getConexion()) {
            return conn.isValid(2); // Timeout de 2 segundos
        } catch (SQLException e) {
            System.err.println("Error al verificar conexión: " + e.getMessage());
            return false;
        }
    }
}

