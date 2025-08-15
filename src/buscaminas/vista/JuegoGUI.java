/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package buscaminas.vista;



import buscaminas.modelo.Casilla;
import buscaminas.modelo.Tablero;
import buscaminas.util.JugadorDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author Ben
 */
//
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
        cargarControles();
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
    
    //aca se crea la interfaz y como van a ir los controles en el Jframe
    private void cargarControles(){
        
        int posXReferencia=25;
        int posYReferencia=25;
        int anchoControl=30;
        int altoControl=30;
        
        botonesTablero = new JButton[numFilas][numColumnas];
        for (int i = 0; i < botonesTablero.length; i++) {//con el for se hace un recorrido en la matriz y luego se intancia los botones
            for (int j = 0; j < botonesTablero[i].length; j++) {
                botonesTablero[i][j]=new JButton();//instanciar los botones
                botonesTablero[i][j].setName(i+","+j);//el lugar que van a tomar los botones
                botonesTablero[i][j].setBorder(null);// esto es para que el boton salga sin bordes 
                if (i==0 && j==0){
                    botonesTablero[i][j].setBounds(posXReferencia, //el setBounds es para mostrar los botones pero hay que pasarle parametros como referencia
                            posYReferencia, anchoControl, altoControl);
                    
                }else if (i==0 && j!=0){ //con esto se crea el resto de cuadros en jframe 
                    botonesTablero[i][j].setBounds(
                            botonesTablero[i][j-1].getX()+botonesTablero[i][j-1].getWidth(), //la pocision del cuadro va a ser la misma que el anterior mas lo que mide 
                            posYReferencia, anchoControl, altoControl);
                }else{// aca seria para las siguientes filas 
                    botonesTablero[i][j].setBounds(
                            botonesTablero[i-1][j].getX(), 
                            botonesTablero[i-1][j].getY()+botonesTablero[i-1][j].getHeight(),  //la pocision del cuadro va a ser la misma que el anterior mas lo que mide 
                            anchoControl, altoControl);                    
                }
                botonesTablero[i][j].addActionListener(new ActionListener() {//esto es la accioon que va a hacer el boton
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnClick(e);
                    }

                });
                
                botonesTablero[i][j].addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mousePressed(java.awt.event.MouseEvent e) {
                        if (juegoTerminado) {
                            return;
                        }

                        // Solo botón derecho
                        if (javax.swing.SwingUtilities.isRightMouseButton(e)) {
                            JButton btn = (JButton) e.getSource();
                            String[] coord = btn.getName().split(",");
                            int f = Integer.parseInt(coord[0]);
                            int c = Integer.parseInt(coord[1]);

                            // Alternar marca en el modelo
                            boolean cambio = tableroBuscaminas.toggleMarca(f, c);
                            if (!cambio) {
                                // Límite de banderas alcanzado o casilla abierta
                                java.awt.Toolkit.getDefaultToolkit().beep();
                                return;
                            }

                            // Reflejar visualmente usando el estado real
                            buscaminas.modelo.Casilla cas = tableroBuscaminas.getCasilla(f, c);
                            if (cas.isMarcada()) {
                                // Usa "⚑" (o "F" si prefieres ASCII)
                                btn.setText("⚑");
                                // No deshabilites; debe poder desmarcarse
                            } else {
                                // Limpia el texto al desmarcar
                                btn.setText("");
                            }
                        }
                    }
                });
                getContentPane().add(botonesTablero[i][j]); //con esto se agrega el control al contenerdor
            }
            
        }
        
        //esto es para darle formato a la ventana y que se actualice para que no quede descuadrada 
       this.setSize(botonesTablero[numFilas-1][numColumnas-1].getX()+
                botonesTablero[numFilas-1][numColumnas-1].getWidth()+30,
                botonesTablero[numFilas-1][numColumnas-1].getY()+
                botonesTablero[numFilas-1][numColumnas-1].getHeight()+70
                );
    }
    
    private void btnClick(ActionEvent e) {
        if (juegoTerminado) return; 
        JButton btn = (JButton) e.getSource();
        String[] coordenada = btn.getName().split(",");
        int posFila = Integer.parseInt(coordenada[0]);
        int posColumna = Integer.parseInt(coordenada[1]);
        tableroBuscaminas.seleccionarCasilla(posFila, posColumna);
   

    }
    
    
    
    
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
int num = 0;
    boolean valido = false;

    while (!valido) {
        String input = JOptionPane.showInputDialog(this,
            "Por favor digite tamaño de la matriz (n*n), debe ser un número entero mayor a 3 y máximo 25");

        if (input == null) return; // Canceló

        try {
            num = Integer.parseInt(input); // Solo acepta enteros

            if (num > 3 && num <= 25) {
                valido = true;
            } else if (num <= 3) {
                JOptionPane.showMessageDialog(this, 
                    "El tamaño debe ser mayor a 3.", 
                    "Valor inválido", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "El tamaño máximo permitido es 25.", 
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
    }//GEN-LAST:event_elegirTamanioActionPerformed

    private void comenzarJuegoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comenzarJuegoActionPerformed
        juegoNuevo();
    }//GEN-LAST:event_comenzarJuegoActionPerformed

    private void ElegirMinasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ElegirMinasActionPerformed
int num = 0;
    boolean valido = false;

    while (!valido) {
        String input = JOptionPane.showInputDialog(this, 
            "Por favor digite número de minas (entero mayor a 3 y menor a 90)");

        if (input == null) return; // Canceló

        try {
            num = Integer.parseInt(input); // Solo enteros

            if (num > 3 && num < 90) {
                valido = true;
            } else if (num <= 3) {
                JOptionPane.showMessageDialog(this, 
                    "El número de minas debe ser mayor a 3.", 
                    "Valor inválido", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "El número de minas debe ser menor que 90.", 
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
