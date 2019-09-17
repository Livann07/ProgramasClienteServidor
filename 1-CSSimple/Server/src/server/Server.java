
package server;
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Server {

    
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        String escritor="";
        boolean resultado=false;
        do{
            try{
             System.out.println("Captura puerto");
             escritor = s.nextLine();
             try{
                 Integer.parseInt(escritor);
                 resultado = true;
             }catch (NumberFormatException excepcion) {
                 resultado = false;
             }
            }catch(Exception e){
                System.out.println("Problema al capturar puerto " + e.toString());   
                System.exit(1);
            }
        }while(escritor.equalsIgnoreCase("") ||!resultado);
        Socket socket = new Socket();
        
        //Creación y validación de sockets
        try{
            ServerSocket socketServidor = new ServerSocket(Integer.parseInt(escritor)); //recibir informacion    
            socket = socketServidor.accept(); 
        }catch(IOException e){
            System.out.println("Error al recibir informacion " + e.toString());
            System.exit(2);
        }
        
        BufferedReader lector = null;
        try{
           lector = new BufferedReader(
           new InputStreamReader(socket.getInputStream())
           );
        }catch(Exception g){
           System.out.println("Error al crear lector " + g.toString());
           System.exit(3);
        }
        
        String entrada;
        
        //Proyectar mensaje de cliente
        try{
          while((entrada = lector.readLine())!=null){
            System.out.println("me dijeron: " + entrada);
          }
        }catch(Exception g){
            System.out.println("Error al proyectar mensaje " + g.toString());
            System.exit(4);
        }
    }
    
}
