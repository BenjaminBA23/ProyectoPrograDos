/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package buscaminas.modelo;

import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author pame
 */
public class Tablero {
     Casilla[][] casillas;//matriz para el tablero

    int numFilas;
    int numColumnas;
    int numMinas;
    boolean generacionMinas;
    int numCasillasAbiertas;
    
    private Consumer<List<Casilla>> eventoPartidaPerdida;
    private Consumer<List<Casilla>> eventoPartidaGanada;

    private Consumer<Casilla> eventoCasillaAbierta;

    public Tablero(int numFilas, int numColumnas, int numMinas) {
        this.numFilas = numFilas;
        this.numColumnas = numColumnas;
        this.numMinas = numMinas;
        this.inicializarCasillas();
        this.generacionMinas = false;
    }

   
    
    private void inicializarCasillas() {
        casillas = new Casilla[this.numFilas][this.numColumnas];
        for (int i = 0; i < casillas.length; i++) {
            for (int j = 0; j < casillas[i].length; j++) {
                casillas[i][j] = new Casilla(i, j);
            }
        }
    }
    
    
      /*
    private void generarMinas(int posFilaIgnorar, int posColumnaIgnorar) {
        int minasGeneradas = 0;//variable temp
        while (minasGeneradas != numMinas) {//mientras minas sea diferente
            int posTmpFila;
            int posTmpColumna;
            do {
                posTmpFila = (int) (Math.random() * casillas.length);
                posTmpColumna = (int) (Math.random() * casillas[0].length);
            } while ((posTmpFila == posFilaIgnorar && posTmpColumna == posColumnaIgnorar)
                    || casillas[posTmpFila][posTmpColumna].isMina());
            casillas[posTmpFila][posTmpColumna].setMina(true);
            minasGeneradas++;
        }
        actualizarNumeroMinasAlrededor();
        this.generacionMinas = true;
        this.imprimirTablero();
    }
    
    /*
    //para imprimir la forma del tablero 
    private void imprimirTablero() {
        for (int i = 0; i < casillas.length; i++) {
            for (int j = 0; j < casillas[i].length; j++) {
                System.out.print(casillas[i][j].isMina() ? "*" : "0");
            }
            System.out.println("");
        }
    }*/

    //para imprimir la forma de las pisatas (los numero)
    private void imprimirPistas() {
        for (int i = 0; i < casillas.length; i++) {
            for (int j = 0; j < casillas[i].length; j++) {
               // System.out.print(casillas[i][j].getNumMinasAlrededor());
            }
            System.out.println("");
        }
    }

    public static void main(String[] args) {
        Tablero tablero = new Tablero(5, 5, 5);
        //tablero.imprimirTablero();
        System.out.println("---");
        tablero.imprimirPistas();
       
    }
    
    /*
    private void actualizarNumeroMinasAlrededor() {
        for (int i = 0; i < casillas.length; i++) {
            for (int j = 0; j < casillas[i].length; j++) {
                if (casillas[i][j].isMina()) {
                    //List<Casilla> casillasAlrededor = obtenerCasillasAlrededor(i, j);
                    //casillasAlrededor.forEach((c)->c.incrementarNumeroMinasAlrededor());
                }
            }
        }
    }*/
    
    //
    
    //
    
    //
   
    //
    
    //
    
    //
    
    //
    
    //
    
    
}
