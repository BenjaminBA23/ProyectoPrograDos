/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package buscaminas.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author pame
 */
public class ConexionBD {

    // credenciales de la base
    private static String URL        = "jdbc:sqlserver://localhost:1433;databaseName=BuscaminasDB;encrypt=true;trustServerCertificate=true";
    private static String USUARIO    = "sa";
    private static String CONTRASENA = "112306";

    static {
        //  Forzar carga del driver (robusto incluso si el autoload falla)
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("[BD] Driver SQL Server cargado.");
        } catch (ClassNotFoundException e) {
            System.err.println("[BD] Error: No se encontró el driver JDBC de SQL Server en el classpath.");
            e.printStackTrace();
        }

        // Permitir override por variables de entorno si las defines en tu máquina
        // Útil cuando alguien más ejecuta el proyecto con su propia configuración.
        String envUrl  = System.getenv("DB_URL");
        String envUser = System.getenv("DB_USER");
        String envPass = System.getenv("DB_PASSWORD");
        if (envUrl  != null && !envUrl.isBlank())  URL        = envUrl;
        if (envUser != null && !envUser.isBlank()) USUARIO    = envUser;
        if (envPass != null && !envPass.isBlank()) CONTRASENA = envPass;
    }

    /** Devuelve una conexión o null si falló (no lanza NPE en los DAOs). */
    public static Connection getConexion() {
        try {
            System.out.println("[BD] Conectando a: " + URL + " (usuario=" + USUARIO + ")");
            return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
        } catch (SQLException e) {
            System.err.println("[BD] Error al conectar con la base de datos.");
            e.printStackTrace(); // Stack trace completo (útil para ver Login failed / ClientConnectionId / etc.)
            return null;
        }
    }
}