/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package buscaminas.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author pame
 */
public class JugadorDAO {
    //
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
    
    // ---- LECTURA para mostrar en JOptionPane (tabla) ----
    public DefaultTableModel obtenerEstadisticasModelo() {
        String[] cols = {"Jugador", "Ganadas", "Perdidas", "Total", "WinRate %"};
        DefaultTableModel modelo = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        String sql = "SELECT nombre, partidas_ganadas, partidas_perdidas " +
                     "FROM jugador ORDER BY partidas_ganadas DESC, nombre ASC";

        try (Connection conn = ConexionBD.getConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            boolean hay = false;
            while (rs.next()) {
                hay = true;
                String n = rs.getString("nombre");
                int g = rs.getInt("partidas_ganadas");
                int p = rs.getInt("partidas_perdidas");
                int tot = g + p;
                double wr = tot == 0 ? 0.0 : (g * 100.0) / tot;
                modelo.addRow(new Object[]{ n, g, p, tot, String.format("%.1f", wr) });
            }
            if (!hay) modelo.addRow(new Object[]{"(sin datos)", 0, 0, 0, "0.0"});

        } catch (SQLException e) {
            System.err.println("obtenerEstadisticasModelo: " + e.getMessage());
        }
        return modelo;
    }
    
    // Totales generales
    public int[] obtenerTotales() {
        String sql = "SELECT SUM(partidas_ganadas) g, SUM(partidas_perdidas) p FROM jugador";
        try (Connection conn = ConexionBD.getConexion(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                int g = rs.getInt("g");
                int p = rs.getInt("p");
                return new int[]{g, p, g + p};
            }
        } catch (SQLException e) {
            System.err.println(" obtenerTotales: " + e.getMessage());
        }
        return new int[]{0, 0, 0};
    }
    // se fija si exite un jugador con ese nombre en la base de datos 
     public boolean existeJugador(String nombre) {
        final String sql = "SELECT 1 FROM jugador WHERE nombre = ?";
        try (Connection conn = ConexionBD.getConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("existeJugador: " + e.getMessage());
        }
        return false;
    }

// Crea un jugador nuevo con 0 ganadas y 0 perdidas
    public boolean crearJugador(String nombre) {
        final String sql = "INSERT INTO jugador (nombre, partidas_ganadas, partidas_perdidas) VALUES (?, 0, 0)";
        try (Connection conn = ConexionBD.getConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("crearJugador: " + e.getMessage());
        }
        return false;
    }
}
