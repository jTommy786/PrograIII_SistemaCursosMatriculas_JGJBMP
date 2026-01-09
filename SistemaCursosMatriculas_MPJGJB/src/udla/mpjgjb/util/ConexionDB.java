package udla.mpjgjb.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase ConexionDB
 * Gestiona la conexion a la base de datos MySQL.
 * Esta clase proporciona el metodo para conectarse a la base de datos.
 */
public class ConexionDB {

    /**
     * Configuracion de la conexion a MySQL.
     * URL: direccion de la base de datos (localhost:3306 = servidor local en puerto 3306).
     * USER: usuario de MySQL (cambiar segun tu configuracion).
     * PASSWORD: contrasena de MySQL (cambiar segun tu contrasena).
     */
    private static final String URL = "jdbc:mysql://localhost:3306/cursos_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "sasa";

    /**
     * Metodo que devuelve una conexion a la base de datos.
     * Lanza SQLException si no se puede conectar.
     */
    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
