/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package buscaminas.modelo;

/**
 *Representa una celda (casilla) del tablero de Buscaminas.
 * @author pame
 *  * Estados principales:
 * - mina:     indica si esta casilla contiene una mina.
 * - abierta:  indica si el jugador ya destapó la casilla.
 * - marcada:  indica si el jugador colocó una bandera (clic derecho).
 * - numMinasAlrededor: cuántas minas hay en sus 8 vecinas.
 * Posición:
 * - posFila  y posColumna: ubicación dentro de la matriz del tablero.
 */
public class Casilla {
    private int posFila;//Fila en la matriz del tablero
    private int posColumna;//Columna en la matriz del tablero
    private boolean mina;//true si esta casilla contiene una mina
    private int numMinasAlrededor;//Cantidad de minas en las casillas adyacentes (0..8).
    private boolean abierta;//true si la casilla ya fue destapada por el jugador
    private boolean marcada;//true si el jugador colocó una bandera (clic derecho)

    public Casilla(int posFila, int posColumna) {
        this.posFila = posFila;
        this.posColumna = posColumna;
    }
    // Getters / Setters de posición
    public int getPosFila() {
        return posFila;
    }

    public void setPosFila(int posFila) {
        this.posFila = posFila;
    }

    public int getPosColumna() {
        return posColumna;
    }

    public void setPosColumna(int posColumna) {
        this.posColumna = posColumna;
    }

    public boolean isMina() {
        return mina;
    }

    public void setMina(boolean mina) {
        this.mina = mina;
    }

    public int getNumMinasAlrededor() {
        return numMinasAlrededor;
    }

    public void setNumMinasAlrededor(int numMinasAlrededor) {
        this.numMinasAlrededor = numMinasAlrededor;
    }
    
    public void incrementarNumeroMinasAlrededor(){
        this.numMinasAlrededor++;
    }

    public boolean isAbierta() {
        return abierta;
    }

    public void setAbierta(boolean abierta) {
        this.abierta = abierta;
    }

    public boolean isMarcada() {
        return marcada;
    }

    public void setMarcada(boolean marcada) {
        this.marcada = marcada;
    }
    
    
}
