
package cliente_1;
import java.net.*;
import java.io.*;
import java.util.Scanner;
import sun.net.util.IPAddressUtil;

public class Cliente_1 {

   
    public static void main(String[] args) {
         Scanner s = new Scanner(System.in);
        String ip="";
        String [] se;
        boolean au;
        do{
           try{
             System.out.println("Captura ip valida");
             ip=s.nextLine();
           }catch(Exception e){
               System.out.println("Problema al ingresar dirección ip " + e.toString()); 
               System.exit(1);
           }
           se=ip.split("\\.");
           au= IPAddressUtil.isIPv4LiteralAddress(ip);
        }while(ip.equalsIgnoreCase("")||se.length<4||se.length>4||!au);
        
        //Verificar ip valida por medio de los puntos
        
        String puerto="";
        boolean resultado=false;
        do{
            try{
              System.out.println("Captura puerto"); 
              puerto=s.nextLine();
                try{
                    Integer.parseInt(puerto);
                    resultado = true;
                }catch (NumberFormatException excepcion) {
                    resultado = false;
                }
            }catch(Exception f){
                System.out.println("Problema al capturar puerto " + f.toString());
                System.exit(2);
            }
            
        }while(puerto.equalsIgnoreCase("")||!resultado);
        
        //Si no se captura, no deja avanzar
        
        String msj="";
        try{
           System.out.println("Captura mensaje");
           msj=s.nextLine();
        }catch(Exception g){
            System.out.println("Problema al capturar mensaje " + g.toString());
            System.exit(3);
        }
        
        //Creación y validación de socket
        Socket socket=null;
        try{
            socket = new Socket(ip,Integer.parseInt(puerto));
        }catch(IOException i) {
            System.out.println("Error, verifica ip y puerto");
            System.out.println("Error al crear el socket " + i.toString());
            System.exit(4);
        }
        
        PrintWriter escritor = null;
        try{
          escritor = new PrintWriter(socket.getOutputStream(),true);
        }catch(IOException e) {
           System.out.println("Error al mandar info " + e.toString());
           System.exit(5);
        }
        
        //Envío de mensaje a server
        escritor.println(msj);
        try{
          socket.close();
        }catch(IOException f){
            System.out.println("Error al cerrar el socket " + f.toString());
            System.exit(6);
        }
    }
    
}
