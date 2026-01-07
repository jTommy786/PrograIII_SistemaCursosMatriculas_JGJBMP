package udla.mpjgjb.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase ConexionDB
 * Gestiona la conexión a la base de datos MySQL
 * Conceptos POO: Encapsulamiento
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
        // JDBC 4+ registra automáticamente el driver si está en el classpath
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

