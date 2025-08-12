/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package buscaminas.vista;



import buscaminas.modelo.Casilla;
import buscaminas.modelo.Tablero;
import buscaminas.util.JugadorDAO;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JOptionPane;


/**
 *
 * @author Ben
 */
public class JuegoGUI extends javax.swing.JFrame {
int numFilas=10;
    int numColumnas=10;
    int numMinas=10;
    JButton[][] botonesTablero;
    Tablero tableroBuscaminas;
    private String nombreJugador;
    private final JugadorDAO jugadorDAO = new JugadorDAO();
    private boolean juegoTerminado = false;   // para ignorar clics después de ganar/perder
    private int casillasDestapadas = 0;       // cuántas casillas SIN mina hemos abierto
    private boolean[][] fueDestapada;         // evita contar dos veces la misma casilla
    
    /**
     * Creates new form JuegoGUI
     */
    public JuegoGUI(String nombreJugador) {
        initComponents();
        this.nombreJugador = nombreJugador;
        setLocationRelativeTo(null);
        //cargarControles();
        crearTableroBuscaminas();
        
    }
    
    //esto va a bloquear el tablero en caso de perdida
    private void bloquearTablero() {
        if (botonesTablero == null) {
            return;
        }
        for (int i = 0; i < botonesTablero.length; i++) {
            for (int j = 0; j < botonesTablero[i].length; j++) {
                if (botonesTablero[i][j] != null) {
                    botonesTablero[i][j].setEnabled(false);
                }
            }
        }
        juegoTerminado = true;
    }
    
    private void perderJuego() {
        if (juegoTerminado) {
            return;
        }
        try {
            jugadorDAO.actualizarPerdidas(nombreJugador);
        } catch (Exception ignore) {
        }

        JOptionPane.showMessageDialog(
                this,
                "Oh no! ¡Perdiste! Pisaste una mina.\nTus datos se han guardado en la base de datos.",
                "Fin del juego",
                JOptionPane.ERROR_MESSAGE
        );
        bloquearTablero();

        int resp = JOptionPane.showConfirmDialog(
                this,
                "¿Deseas jugar de nuevo?",
                "Reintentar",
                JOptionPane.YES_NO_OPTION
        );
        if (resp == JOptionPane.YES_OPTION) {
            // Reinicia una nueva partida con el mismo jugador
            new JuegoGUI(nombreJugador).setVisible(true);
        }
        dispose();
    }
    
      private void ganarJuego() {
        if (juegoTerminado) {
            return;
        }
        try {
            jugadorDAO.actualizarGanadas(nombreJugador);
        } catch (Exception ignore) {
        }

        JOptionPane.showMessageDialog(
                this,
                "🎉 ¡Felicidades " + nombreJugador + "! Has ganado.\nTus datos se han guardado en la base de datos.",
                "Victoria",
                JOptionPane.INFORMATION_MESSAGE
        );
        bloquearTablero();

        int resp = JOptionPane.showConfirmDialog(
                this,
                "¿Deseas jugar de nuevo?",
                "Nueva partida",
                JOptionPane.YES_NO_OPTION
        );
        if (resp == JOptionPane.YES_OPTION) {
            new JuegoGUI(nombreJugador).setVisible(true);
        }
        dispose();
    }
      
    private void verificarVictoria() {
        int totalSinMinas = (numFilas * numColumnas) - numMinas;
        if (casillasDestapadas >= totalSinMinas) {
            ganarJuego();
        }
    }
   
    
    void descargarControles() {
        if (botonesTablero != null) {
            for (int i = 0; i < botonesTablero.length; i++) {
                for (int j = 0; j < botonesTablero[i].length; j++) {
                    if (botonesTablero[i][j] != null) {
                        getContentPane().remove(botonesTablero[i][j]);
                    }
                }
            }
        }
    }
    
    private void juegoNuevo() {
        descargarControles();
        //cargarControles();
        crearTableroBuscaminas();
        repaint();
    }
      
    private void crearTableroBuscaminas() {
        juegoTerminado = false;
        casillasDestapadas = 0;
        fueDestapada = new boolean[numFilas][numColumnas];
        tableroBuscaminas = new Tablero(numFilas, numColumnas, numMinas);
        tableroBuscaminas.setEventoPartidaPerdida(new Consumer<List<Casilla>>() {
            @Override
            public void accept(List<Casilla> t) {
                for (Casilla casillaConMina : t) {//proceso donde se muestran las minas si el usuario toca un boton con minas
                    botonesTablero[casillaConMina.getPosFila()][casillaConMina.getPosColumna()].setText("*");//aca es donde se agrega un * donde hay minas
                }
                perderJuego();
            }
        });
        tableroBuscaminas.setEventoPartidaGanada(new Consumer<List<Casilla>>() {
            @Override
            public void accept(List<Casilla> t) {
                for (Casilla casillaConMina : t) {
                    botonesTablero[casillaConMina.getPosFila()][casillaConMina.getPosColumna()].setText(":)");
                }
                ganarJuego();
            }
        });

        // ---- EVENTO: CASILLA ABIERTA ----
    tableroBuscaminas.setEventoCasillaAbierta(new java.util.function.Consumer<Casilla>() {
        @Override
        public void accept(Casilla t) {
            // deshabilitar y pintar número
            botonesTablero[t.getPosFila()][t.getPosColumna()].setEnabled(false);
            botonesTablero[t.getPosFila()][t.getPosColumna()]
                .setText(t.getNumMinasAlrededor() == 0 ? "" : String.valueOf(t.getNumMinasAlrededor()));

            // contar SOLO la primera vez que se destapa
            if (!fueDestapada[t.getPosFila()][t.getPosColumna()]) {
                fueDestapada[t.getPosFila()][t.getPosColumna()] = true;
                casillasDestapadas++;
            }

            // comprobar si ya se destapó todo lo que no es mina
            verificarVictoria();
        }
    });
    }
    
    //
    
    
    //
    
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        comenzarJuego = new javax.swing.JMenuItem();
        elegirTamanio = new javax.swing.JMenuItem();
        ElegirMinas = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jMenu2.setText("EXTRAS");
        jMenu2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        comenzarJuego.setText("Comenzar nuevo Juego");
        comenzarJuego.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comenzarJuegoActionPerformed(evt);
            }
        });
        jMenu2.add(comenzarJuego);

        elegirTamanio.setText("Elegir el Tamaño de la tabla");
        elegirTamanio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                elegirTamanioActionPerformed(evt);
            }
        });
        jMenu2.add(elegirTamanio);

        ElegirMinas.setText("Elegir Numeros de Minas");
        ElegirMinas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ElegirMinasActionPerformed(evt);
            }
        });
        jMenu2.add(ElegirMinas);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("<-Acá podas agregar extras al juego");
        jMenu3.setEnabled(false);
        jMenu3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 458, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 321, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void elegirTamanioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elegirTamanioActionPerformed
         juegoNuevo();
    }//GEN-LAST:event_elegirTamanioActionPerformed

    private void comenzarJuegoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comenzarJuegoActionPerformed
        int num = 0;
    boolean valido = false;

    while (!valido) {
        String input = JOptionPane.showInputDialog(this, 
            "Por favor digite tamaño de la matriz (n*n), debe ser un número entero mayor a 3");

        if (input == null) return; // Canceló

        try {
            num = Integer.parseInt(input); // Solo acepta enteros
            if (num > 3) {
                valido = true;
            } else {
                JOptionPane.showMessageDialog(this, 
                    "El tamaño debe ser mayor a 3.", 
                    "Valor inválido", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Por favor ingrese solo números enteros.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    this.numFilas = num;
    this.numColumnas = num;
    juegoNuevo();
    }//GEN-LAST:event_comenzarJuegoActionPerformed

    private void ElegirMinasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ElegirMinasActionPerformed
        int num = 0;
    boolean valido = false;

    while (!valido) {
        String input = JOptionPane.showInputDialog(this, 
            "Por favor digite número de minas (entero mayor a 3)");

        if (input == null) return; // Canceló

        try {
            num = Integer.parseInt(input); // Solo enteros
            if (num > 3 && num < (numFilas * numColumnas)) {
                valido = true;
            } else {
                JOptionPane.showMessageDialog(this, 
                    "El número de minas debe ser mayor a 3 y menor que el total de casillas.", 
                    "Valor inválido", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Por favor ingrese solo números enteros.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    this.numMinas = num;
    juegoNuevo();
    }//GEN-LAST:event_ElegirMinasActionPerformed

    /**
     * @param args the command line arguments
     */
 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem ElegirMinas;
    private javax.swing.JMenuItem comenzarJuego;
    private javax.swing.JMenuItem elegirTamanio;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    // End of variables declaration//GEN-END:variables
}
