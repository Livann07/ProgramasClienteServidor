
package clientebidireccional;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import sun.net.util.IPAddressUtil;

public class ClienteBidireccional {

    
    public static void main(String[] args) {
        
        //Por si se quiere cerrar de otra manera la conexión
        System.out.println("Para terminar al realizar conexión, tecleé la palabra 'fin'");
        
         //Validación de ip
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
           
        }while(ip.equalsIgnoreCase("") || se.length<4 || se.length>4||!au);
        
        //Validación de puerto
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
     
        //Creación y validación de sockets
        Socket socket = null;
        try {
            socket = new Socket(ip,Integer.parseInt(puerto));
        } catch (IOException i) {
            System.out.println("Error, verifica ip y puerto");
            System.out.println("Error al crear el socket " + i.toString());
            System.exit(3);
        }   
        
        //Creación de Buffered y PrintWriter, que permite el envío bidi
        BufferedReader lector=null;
        try{
            lector = new BufferedReader(
         new InputStreamReader(socket.getInputStream())
        );  
        }catch(IOException k){
            System.out.println("Error al leer datos " + k.toString());
            System.exit(4);
        }
        
        PrintWriter escritor=null;
        try{
         escritor= new PrintWriter(
            socket.getOutputStream(),true);    
        }catch(IOException j){
            System.out.println("Error al obtener datos " + j.toString());
            System.exit(5);
        }
        
        //Creación de scanner y variables para guardar lo del scanner y servidor
        String datos;
        String datosEntrada="";
        Scanner scanner = new Scanner(System.in);
        
        //Imprimir mientras se reciba algo diferente a null (cuando el server acaba)
        try{
        while(true && datosEntrada!=null){
            datos=scanner.nextLine();
            escritor.println(datos);
            
            try{
              datosEntrada = lector.readLine();    
            }catch(IOException l){
                System.out.println("Error al leer datos " + l.toString());
                System.exit(6);
            }
            
            if(datosEntrada!=null){
              System.out.println(datosEntrada);
            }
         }
        }catch(Exception r){
            System.out.println("Conexión cerrada " + r.toString());
        }
        
        try{
            socket.close();
            System.exit(7);
        }catch(Exception n){
            System.out.println("Error al cerrar socket" + n.toString());
            System.exit(7);
        }
        
    }
    
}
