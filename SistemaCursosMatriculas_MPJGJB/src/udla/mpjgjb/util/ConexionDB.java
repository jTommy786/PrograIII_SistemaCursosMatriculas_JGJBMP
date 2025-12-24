package udla.mpjgjb.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

        private static final String URL =
                "jdbc:mysql://localhost:3306/cursos_db?useSSL=false&serverTimezone=UTC";
        private static final String USER = "root"; //es el por defecto en mi maquina cambienle segun el suyo (probablemente sea igual)
        private static final String PASSWORD = ""; //sus contraseñas ñas

        public static Connection getConexion() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
    }

}
