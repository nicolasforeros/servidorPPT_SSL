/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.autonoma.redes;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *Red de salida de los mensajes
 * @author Nicolas Forero Segovia
 * @author Leandra Builes
 * @version 1.1
 */
public class RedSalida {
    
    private DataOutputStream outJ1;
    private DataOutputStream outJ2;

    /**
     * Constructor; permite inicializar el mensaje de salida del jugador 1 y el mensaje de 
     * salida del jugador 2
     * @param outJ1 El mensaje de salida del jugador 1
     * @param outJ2 El mensaje de salida del jugador 2
     */
    public RedSalida(DataOutputStream outJ1, DataOutputStream outJ2) {
        this.outJ1 = outJ1;
        this.outJ2 = outJ2;
    }
    
     /**
     * Me permite enviar el mensaje de salida del jugador 1 y del jugador 2
     * @param mensajeEnviar El mensaje que se va a enviar del jugador 1 y del jugador 2
     */
    public void enviarMensaje(String mensajeEnviar){
        
        try {
            System.out.println("RED SALIDA=> Escribiendo mensaje: " + mensajeEnviar);
            outJ1.writeUTF(mensajeEnviar);
            outJ2.writeUTF(mensajeEnviar);
            
            outJ1.flush();
            outJ1.flush();
        } catch (IOException ex) {
            System.out.println("RED SALIDA => Error enviando mensaje: " + ex.getMessage());
        }
    }
    
}
