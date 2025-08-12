/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package buscaminas.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author pame
 */
public class JugadorDAO {
    
      public void registrarJugador(String nombre) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM jugador WHERE nombre = ?) " +
                     "INSERT INTO jugador (nombre) VALUES (?)";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, nombre);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("registrarJugador: " + e.getMessage());
        }
    }
      
    public void actualizarGanadas(String nombre) {
        String sql = "UPDATE jugador SET partidas_ganadas = partidas_ganadas + 1 WHERE nombre = ?";
        try (Connection conn = ConexionBD.getConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("actualizarGanadas: " + e.getMessage());
        }
    }
    
    public void actualizarPerdidas(String nombre) {
        String sql = "UPDATE jugador SET partidas_perdidas = partidas_perdidas + 1 WHERE nombre = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println(" actualizarPerdidas: " + e.getMessage());
        }
    }
    
    
    //
    
    
    //
    
}
