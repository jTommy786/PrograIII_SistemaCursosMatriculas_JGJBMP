package udla.mpjgjb.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase ConexionDB
 * Gestiona la conexión a la base de datos MySQL
 */
public class ConexionDB {

    // Configuración de la conexión a MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/cursos_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";        // Cambiar según la configuración de sus compus
    private static final String PASSWORD = "";        // Cambiar según la contraseña local

    
    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

