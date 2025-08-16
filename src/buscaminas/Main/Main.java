/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package buscaminas.Main;

import buscaminas.vista.MenuPrincipal;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 *
 * @author Ben
 * * Aquí se configura el "Look and Feel" (apariencia visual) 
 * y se lanza la ventana del menú principal.
 */
public class Main {
    public static void main(String[] args) {
        
        try {
             // Recorremos todos los "Look and Feel" instalados en el sistema
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                 // Si existe el estilo "Nimbus", lo aplicamos
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;// salimos del bucle, ya encontramos Nimbus
                }
            }
        } catch (Exception ignore) {}
        // Si ocurre un error al cambiar el Look and Feel, lo ignoramos
        // y el programa usará el estilo por defecto.
        
        // Lanzar la interfaz gráfica en el Event Dispatch Thread (EDT).
        // Esto es la mejor práctica en Swing para evitar problemas de concurrencia.
        SwingUtilities.invokeLater(() -> {
            new MenuPrincipal().setVisible(true);
        });
    }
}
