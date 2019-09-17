
package servidorbidireccional;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ServidorBidireccional {

    
    public static void main(String[] args) {
       
        //Validación puerto 
        Scanner s = new Scanner(System.in);
        String puerto="";
        boolean resultado=false;
        do{
          try{
             System.out.println("Captura puerto");
             puerto = s.nextLine();
             try{
                 Integer.parseInt(puerto);
                 resultado = true;
              }catch (NumberFormatException excepcion) {
                 resultado = false;
              }
          }catch(Exception e){
                System.out.println("Problema al capturar puerto " + e.toString());   
                System.exit(1);
          }
        }while(puerto.equalsIgnoreCase("")||!resultado);
        
        
        //Creación y validación de sockets
        Socket socket=new Socket();
        ServerSocket socketServidor = null;
        
        try{
            socketServidor = new ServerSocket(Integer.parseInt(puerto));    
            socket = socketServidor.accept(); 
        }catch(IOException e){
            System.out.println("Error al crear sockets " + e.toString());
            System.exit(1);
        }
        
        //Creación y validación de Buffered y PrintWriter, que permite el envío bidi
        BufferedReader lector = null;
        try{
         lector = new BufferedReader(
         new InputStreamReader(socket.getInputStream()) 
        );
        }catch(IOException g){
            System.out.println("Error al crear lector " + g.toString());
            System.exit(2);
        }
         
         PrintWriter escritor=null;
         try{
            escritor = new PrintWriter(socket.getOutputStream(),true);
         }catch(IOException f){
            System.out.println("Error al crear escritor " + f.toString());
            System.exit(3);
         }
         String entrada="";
         Scanner scanner = new Scanner(System.in);
         String salida;
          
        //Se hará hasta que el cliente de "fin" como entrada de dato
        try{
         do{
             try{
                entrada=lector.readLine();
             }catch(IOException h){
                System.out.println("Error al leer datos " + h.toString());
                System.exit(4);
             }
             
             System.out.println(entrada);
             //Si es "fin" se cierran los sockets
             if(entrada.equalsIgnoreCase("fin")){
                 try{
                    System.out.println("Me voy");
                    socket.close();
                    socketServidor.close();
                    System.exit(0); 
                 }catch(IOException j){
                    System.out.println("Error al cerrar sockets " + j.toString());
                    System.exit(5);
                 } 
             }
             salida = scanner.nextLine();
             escritor.println(salida);
         }while(!entrada.equalsIgnoreCase("fin"));
        }catch(Exception x){
            System.out.println("Conexión cerrada " + x.toString());
        }
        //En dado caso se force la salida
        try{
            socket.close();
            socketServidor.close();
            System.exit(6);
        }catch(IOException j) {
            System.out.println("Error al cerrar sockets " + j.toString());
            System.exit(6);
        }
         
    }
    
}
