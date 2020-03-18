/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.autonoma.elementos;

import java.io.IOException;
import java.io.StringWriter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *Permite procesar los mensajes que llegar al servidor
 * @author Nicolas Forero Segovia
 * @author Leandra Builes
 * @version 1.1
 */
public class Procesador {
    
    private Juego juego;
    
    private JSONParser parser;
    private JSONObject obj;
    
    public Procesador(Juego juego){
        this.juego = juego;
        this.parser = new JSONParser();
        this.obj = new JSONObject();
    }
    
    /**
     * Me permite saber en que estado del juego me encuentro (inicio, nueva partida, terminar) para
     * posteiormente realizar una acción.
     */
    public String procesar(){
        
        String respuesta = null;
        
        System.out.println("PROCESADOR=> obteniendo estados del juego");
        String[] estados = this.juego.getEstados(); //aca se espera a que se obtenga un estado primero
   
        String jugador1 = estados[0];
        String jugador2 = estados[1];
        System.out.println("PROCESADOR=> Estados de juego son: " + jugador1 + " --- " + jugador2);
        
        JSONObject objJ1;
        JSONObject objJ2;
        
        try {
            Object o = parser.parse(jugador1.trim());
            objJ1 = (JSONObject) o;
            objJ2 = (JSONObject) parser.parse(jugador2.trim());
        } catch (ParseException ex) {
            System.out.println("Error procesando los mensajes. " + ex.getMessage() );
            return null;
        }
        
        int jugada1 =(int) (long)objJ1.get("jugada");
        int jugada2 =(int) (long)objJ2.get("jugada");
        
        if(jugada1==Juego.TERMINAR || jugada2==Juego.TERMINAR){
            respuesta = "cerrar";
        }else{
            if(jugada1==Juego.NUEVA_PARTIDA && jugada2==Juego.NUEVA_PARTIDA ){
                respuesta = this.realizarEstadoPreparado(objJ1,objJ2);
            }else{
                if( (jugada1==Juego.NUEVA_PARTIDA && jugada2==Juego.INICIO) || 
                        (jugada1==Juego.INICIO && jugada2==Juego.NUEVA_PARTIDA) ){
                    respuesta = this.realizarEstadoNoPreparado(objJ1,objJ2);
                }else{
                    if( (jugada1!=Juego.NUEVA_PARTIDA) && (jugada2==Juego.NUEVA_PARTIDA)){
                        respuesta = this.realizarEstadoJ1OK_J2NO(objJ1,objJ2);
                    }else{
                        if( (jugada1==Juego.NUEVA_PARTIDA) && (jugada2!=Juego.NUEVA_PARTIDA)){
                            respuesta = this.realizarEstadoJ1NO_J2OK(objJ1,objJ2);
                        }else{
                            if( (jugada1!=Juego.NUEVA_PARTIDA) && (jugada2!=Juego.NUEVA_PARTIDA)){
                                respuesta = this.jugar(objJ1,objJ2); //en esta se mandan las jugadas para saber resultados
                            }
                        }
                    }
                }
            }
        
        }
            
        return respuesta;
    }
    
    /**
     * Me permite realizar el mensaje de estado preparado con el 
     * protocolo de mensajes (Jugador1, Jugador 2, estado)
     * @param objJ1 el jugador 1 
     * @param objJ2 el jugador 2
     */

    private String realizarEstadoPreparado(JSONObject objJ1, JSONObject objJ2) {
        
        String jugador1 = (String)objJ1.get("jugador");
        String jugador2 = (String)objJ2.get("jugador");
        
        obj.put("jugador1", jugador1);
        obj.put("jugador2", jugador2);
        obj.put("estado", Juego.ESTADO_PREPARADO);
        
        /////////////////////////////////////////////////
        
        try {
            StringWriter out = new StringWriter();
            obj.writeJSONString(out);
            
            String jsonText = out.toString();
            return jsonText;
        } catch (IOException ex) {
            System.out.println("PROCESADOR => Error realizando el mensaje de estado preparado: " + ex.getMessage());
        }
        
        return null;
    }
    
    /**
     * Me permite realizar el mensaje de estado NO preparado con el 
     * protocolo de mensajes (Jugador1, Jugador 2, estado)
     * @param objJ1 el jugador 1 
     * @param objJ2 el jugador 2
     */
    private String realizarEstadoNoPreparado(JSONObject objJ1, JSONObject objJ2) {
    
        String jugador1 = (String)objJ1.get("jugador");
        String jugador2 = (String)objJ2.get("jugador");
        
        obj.put("jugador1", jugador1);
        obj.put("jugador2", jugador2);
        obj.put("estado", Juego.ESTADO_NO_PREPARADO);
        
        /////////////////////////////////////////////////
        
        try {
            StringWriter out = new StringWriter();
            obj.writeJSONString(out);
            
            String jsonText = out.toString();
            return jsonText;
        } catch (IOException ex) {
            System.out.println("PROCESADOR => Error realizando el mensaje de estado NO preparado: " + ex.getMessage());
        }
        
        return null;
    }

    /**
     * Me permite realizar el mensaje de Jugador 1 preparado, esperando el jugador 2, con el
     * protocolo de mensajes (Jugador1, Jugador 2, estado)
     * @param objJ1 el jugador 1 
     * @param objJ2 el jugador 2
     */
    private String realizarEstadoJ1OK_J2NO(JSONObject objJ1, JSONObject objJ2) {

        String jugador1 = (String)objJ1.get("jugador");
        String jugador2 = (String)objJ2.get("jugador");
        int jugada1 =(int) (long)objJ1.get("jugada");
        int jugada2 =(int) (long)objJ2.get("jugada");
        
        obj.put("jugador1", jugador1);
        obj.put("jugador2", jugador2);
        obj.put("jugada1", jugada1);
        obj.put("jugada2", jugada2);
        obj.put("estado", Juego.ESTADO_J1OK_J2NO);
        
        /////////////////////////////////////////////////
        
        try {
            StringWriter out = new StringWriter();
            obj.writeJSONString(out);
            
            String jsonText = out.toString();
            return jsonText;
        } catch (IOException ex) {
            System.out.println("PROCESADOR => Error realizando el mensaje de estado J1 OK J2 NO: " + ex.getMessage());
        }
        
        return null;
    }

    /**
     * Me permite realizar el mensaje de Jugador 2 preparado, esperando el jugador 1, con el
     * protocolo de mensajes (Jugador1, Jugador 2, estado)
     * @param objJ1 el jugador 1 
     * @param objJ2 el jugador 2
     */    
    private String realizarEstadoJ1NO_J2OK(JSONObject objJ1, JSONObject objJ2) {
        
        String jugador1 = (String)objJ1.get("jugador");
        String jugador2 = (String)objJ2.get("jugador");
        int jugada1 =(int) (long)objJ1.get("jugada");
        int jugada2 =(int) (long)objJ2.get("jugada");
        
        obj.put("jugador1", jugador1);
        obj.put("jugador2", jugador2);
        obj.put("jugada1", jugada1);
        obj.put("jugada2", jugada2);
        obj.put("estado", Juego.ESTADO_J1NO_J2OK);
        
        /////////////////////////////////////////////////
        
        try {
            StringWriter out = new StringWriter();
            obj.writeJSONString(out);
            
            String jsonText = out.toString();
            return jsonText;
        } catch (IOException ex) {
            System.out.println("PROCESADOR => Error realizando el mensaje de estado J1 NO J2 OK: " + ex.getMessage());
        }
        
        return null;
    }

    /**
     * Me permite jugar y conocer el ganador de la sesión de juego, con el protocolo
     * de mensajes (jugador1, jugador2, jugada1,jugada2,estado,ganador)
     * @param objJ1 La jugada del jugador 1 
     * @param objJ2 La jugada del jugador 2 
     */
    private String jugar(JSONObject objJ1, JSONObject objJ2) {
        
        String jugador1 = (String)objJ1.get("jugador");
        String jugador2 = (String)objJ2.get("jugador");
        
        int jugada1 =(int) (long)objJ1.get("jugada");
        int jugada2 =(int) (long)objJ2.get("jugada");
        
        String ganador = "empate";
        
        if (jugada1!=jugada2) {
            if (jugada1==Juego.PIEDRA && jugada2 == Juego.PAPEL) ganador = jugador2;
            
            if (jugada1==Juego.PIEDRA && jugada2 == Juego.TIJERA) ganador = jugador1;
            
            if (jugada1==Juego.PAPEL && jugada2 == Juego.PIEDRA) ganador = jugador1;
            
            if (jugada1==Juego.PAPEL && jugada2 == Juego.TIJERA) ganador = jugador2;
            
            if (jugada1==Juego.TIJERA && jugada2 == Juego.PIEDRA) ganador = jugador2;
            
            if (jugada1==Juego.TIJERA && jugada2 == Juego.PAPEL) ganador = jugador1;   
        }
        
        obj.put("jugador1", jugador1);
        obj.put("jugador2", jugador2);
        obj.put("jugada1", jugada1);
        obj.put("jugada2", jugada2);
        obj.put("estado", Juego.ESTADO_J1OK_J2OK);
        obj.put("ganador", ganador);

        /////////////////////////////////////////////////
        
        try {
            StringWriter out = new StringWriter();
            obj.writeJSONString(out);
            
            String jsonText = out.toString();
            
            this.reiniciarJuego(objJ1,objJ2);
            
            return jsonText;
        } catch (IOException ex) {
            System.out.println("PROCESADOR => Error realizando el mensaje de estado J1 NO J2 OK: " + ex.getMessage());
        }
        
        return null;
        
    }

    public String iniciarJuego() {
        
        obj.put("jugada1", Juego.NUEVA_PARTIDA);
        obj.put("jugada2", Juego.NUEVA_PARTIDA);
        obj.put("estado", Juego.ESTADO_PREPARADO);
        
        try {
            StringWriter out = new StringWriter();
            obj.writeJSONString(out);
            
            String jsonText = out.toString();
            return jsonText;
        } catch (IOException ex) {
            System.out.println("PROCESADOR => Error realizando el mensaje de inicio de juego: " + ex.getMessage());
        }
        return null;
    }
    
    private void reiniciarJuego(JSONObject objJ1, JSONObject objJ2){
        
        objJ1.put("jugada", Juego.INICIO);
        objJ2.put("jugada", Juego.INICIO);
        
        try {
            StringWriter outJ1 = new StringWriter();
            objJ1.writeJSONString(outJ1);
            StringWriter outJ2 = new StringWriter();
            objJ2.writeJSONString(outJ2);
            
            String jsonTextJ1 = outJ1.toString();
            String jsonTextJ2 = outJ2.toString();
            
            this.juego.setEstadoJ1(jsonTextJ1);
            this.juego.setEstadoJ2(jsonTextJ2);
            
        } catch (IOException ex) {
            System.out.println("PROCESADOR => Error realizando el mensaje de inicio de juego: " + ex.getMessage());
        }        
    }
}
