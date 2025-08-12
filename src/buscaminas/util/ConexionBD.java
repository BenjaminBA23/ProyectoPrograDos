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
    
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=BuscaminasDB;encrypt=true;trustServerCertificate=true";
    private static final String USUARIO = "sa"; // este es el usuario de SQL Server
    private static final String CONTRASENA = "112306"; // la contraseña

    public static Connection getConexion() {
        Connection conexion = null;
        try {
            conexion = (Connection) DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            //System.out.println("Conexión exitosa a la base de datos.");
        } catch (SQLException e) {
            System.err.println("Error al conectar: " + e.getMessage());
        }
        return conexion;
    }
}