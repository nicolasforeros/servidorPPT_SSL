/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.autonoma.actores;

import co.edu.autonoma.elementos.Juego;
import co.edu.autonoma.elementos.Procesador;
import co.edu.autonoma.redes.RedEntrada;
import co.edu.autonoma.redes.RedSalida;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *Permite crear una sesión de juego
 * @author Nicolas Forero Segovia
 * @author Leandra Builes
 * @version 1.1
 */
public class SesionJuego extends Thread{
    
    private Socket socketJ1;
    private Socket socketJ2;
    
    private RedEntrada redInJ1;
    private RedEntrada redInJ2;
    
    private RedSalida redOut;
    
    private Juego juego;
    
    private Procesador procesador;

     /**
     * Constructor; permite inicializar el socket del jugador 1 y el socket del jugagor 2
     * @param socketJ1 socket del jugador 1
     * @param socketJ2 socket del jugagor 2
     */
    SesionJuego(Socket socketJ1, Socket socketJ2) {
        this.socketJ1 = socketJ1;
        this.socketJ2 = socketJ2;
        
        this.juego = new Juego();
        this.procesador = new Procesador(this.juego);
    }
    
    /**
     * Permite crear los flujos de conexión para posteriormente procesar los mensajes
     */
    @Override
    public void run(){
        String mensajeSalida;
        String mensajeInicio;
        
        DataInputStream inJ1;
        DataInputStream inJ2;
        DataOutputStream outJ1;
        DataOutputStream outJ2;
        
        try{
            inJ1 = new DataInputStream(this.socketJ1.getInputStream());
            inJ2 = new DataInputStream(this.socketJ2.getInputStream());

            outJ1 = new DataOutputStream(this.socketJ1.getOutputStream());
            outJ2 = new DataOutputStream(this.socketJ2.getOutputStream());            
        }catch(IOException ex){
            System.out.println("Error al crear los flujos de conexión: " + ex.getMessage());
            return;
        }
        
        System.out.println("Creando red de entrada J1");
        this.redInJ1 = new RedEntrada(this.juego, inJ1, RedEntrada.JUGADOR_1);
        this.redInJ1.start();
        
        System.out.println("Creando red de entrada J2");
        this.redInJ2 = new RedEntrada(this.juego, inJ2, RedEntrada.JUGADOR_2);
        this.redInJ2.start();
        
        System.out.println("Creando red de salida");
        this.redOut = new RedSalida(outJ1,outJ2);
        
        mensajeInicio = this.procesador.iniciarJuego();
        this.redOut.enviarMensaje(mensajeInicio);
        
        while(true){
            
            mensajeSalida = this.procesador.procesar();
            
            if(mensajeSalida!=null){
                
                this.redOut.enviarMensaje(mensajeSalida);

                if(mensajeSalida.equalsIgnoreCase("cerrar")) break;
                
            }
            
        }
        
        try 
        {
            if(socketJ1 != null && socketJ2!=null){
                socketJ1.close();
                socketJ2.close();
            }
        }
        catch (IOException e) 
        {
            System.out.println("Error cerrando la sesion con los jugadores ");
        }
        
    }
}
