/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.autonoma.elementos;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nikof
 */
public class Juego {
    
    public static final int TERMINAR=-2; 
    public static final int INICIO=-1; 
    public static final int PIEDRA=0; 
    public static final int PAPEL=1; 
    public static final int TIJERA=2;
    public static final int NUEVA_PARTIDA=3;
    
    public static final int ESTADO_PREPARADO=4;
    public static final int ESTADO_NO_PREPARADO=5;
    public static final int ESTADO_J1OK_J2NO=6;
    public static final int ESTADO_J1NO_J2OK=7;
    public static final int ESTADO_J1OK_J2OK=8;
    
    private String estadoJ1;
    private String estadoJ2;
    
    public Juego(){
        estadoJ1 = "{\"jugada\":"+Juego.NUEVA_PARTIDA+",\"jugador\":null}";
        estadoJ2 = "{\"jugada\":"+Juego.NUEVA_PARTIDA+",\"jugador\":null}";
    }
    
    public synchronized void setEstadoJ1(String estado){
        this.estadoJ1 = estado;
        notify();
    }
    
    public synchronized void setEstadoJ2(String estado){
        this.estadoJ2 = estado;
        notify();
    }
    
    public synchronized String[] getEstados(){
        String[] estados = new String[2];
        try {
            wait();
        } catch (InterruptedException ex) {
            System.out.println("JUEGO => Error obteniendo estado, interrupcion " + ex.getMessage());
        }
        
        estados[0] = estadoJ1;
        estados[1] = estadoJ2;
        
        return estados;
    }
    
    //////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////
//    private int jugadaJ1;
//    private int jugadaJ2;
//    private int estado;
//
//    public int getJugadaJ1() {
//        return jugadaJ1;
//    }
//
//    public synchronized void setJugadaJ1(int jugadaJ1) {
//        this.jugadaJ1 = jugadaJ1;
//        
//        this.calcularEstado();
//        
//        notify();
//    }
//
//    public int getJugadaJ2() {
//        return jugadaJ2;
//    }
//
//    public synchronized void setJugadaJ2(int jugadaJ2) {
//        this.jugadaJ2 = jugadaJ2;
//        
//        this.calcularEstado();
//        
//        notify();
//    }
//
//    public synchronized int getEstado() {
//        
//        try {
//            wait();
//        } catch (InterruptedException ex) {
//            System.out.println("JUEGO => Error obteniendo estado, interrupcion " + ex.getMessage());
//        }
//        return estado;
//    }
//
//    public void setEstado(int estado) {
//        this.estado = estado;
//    }
//    
//    public void reiniciar(){
//        this.jugadaJ1 = (Juego.INICIO);
//        this.jugadaJ2 = (Juego.INICIO);
//    }
//    
//    private void calcularEstado(){
//        
//        if(this.jugadaJ1==Juego.NUEVA_PARTIDA && this.jugadaJ2==Juego.NUEVA_PARTIDA ){
//            this.setEstado(Juego.ESTADO_PREPARADO);
//        }else{
//            
//            if( (this.jugadaJ1==Juego.NUEVA_PARTIDA && this.jugadaJ2==Juego.INICIO) || 
//                    (this.jugadaJ1==Juego.INICIO && this.jugadaJ2==Juego.NUEVA_PARTIDA) ){
//                this.setEstado(Juego.ESTADO_NO_PREPARADO);
//            }else{
//                if( (this.jugadaJ1!=Juego.NUEVA_PARTIDA) && (this.jugadaJ2==Juego.NUEVA_PARTIDA)){
//                    this.setEstado(Juego.ESTADO_J1OK_J2NO);
//                }else{
//                    if( (this.jugadaJ1==Juego.NUEVA_PARTIDA) && (this.jugadaJ2!=Juego.NUEVA_PARTIDA)){
//                        this.setEstado(Juego.ESTADO_J1NO_J2OK);
//                    }else{
//                        if( (this.jugadaJ1!=Juego.NUEVA_PARTIDA) && (this.jugadaJ2!=Juego.NUEVA_PARTIDA)){
//                            this.setEstado(Juego.ESTADO_J1OK_J2OK);
//                        }
//                    }
//                }
//            }
//        }
//        
//    }
//    
}
