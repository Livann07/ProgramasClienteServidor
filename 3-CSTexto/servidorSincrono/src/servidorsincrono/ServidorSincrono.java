
package servidorsincrono;

import java.io.*;
import java.net.*;
import java.util.Scanner;


public class ServidorSincrono {
     
    //Verifica que sea entero el puerto capturado
    public boolean saca(String puerto){
         boolean o=false;
         try{
           Integer.parseInt(puerto);
           o = true;
         }catch (NumberFormatException excepcion) {
           o = false;
         }
         return o;
     }
    
    public static void main(String[] args) {
        
        ServidorSincrono ss = new ServidorSincrono();
        Scanner s = new Scanner(System.in);
        String puerto="";
         boolean cal=false;
        //Validación puerto
        do{
           try{
              System.out.println("Captura puerto");
              puerto= s.nextLine();
              cal=ss.saca(puerto);
            }catch(Exception f){
                System.out.println("Problema al capturar puerto " + f.toString());
                System.exit(0);
            }
        }while(puerto.equalsIgnoreCase("")||!cal);
        
        //Creación de sockets
        Socket socket=new Socket();
        ServerSocket socketServidor = null;
        while(true){
            
            //Validación al momento de dar valor a sockets
            try {
                socketServidor = new ServerSocket(Integer.parseInt(puerto));
                socket = socketServidor.accept();
            } catch (IOException e) {
                System.out.println("Error al crear sockets " + e.toString());
                System.exit(1);
            }
            
            //Creación de PrintWriter y buffered 
            BufferedReader lector = null;
            try {
                lector = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );
            } catch (IOException g) {
                System.out.println("Error al crear lector " + g.toString());
                System.exit(2);
            }
            String entrada = "";

            String salida;

            PrintWriter escritor = null;
            try {
                escritor = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException f) {
                System.out.println("Error al crear escritor " + f.toString());
                System.exit(3);
            }
            
            //Leyendo linea
            try{
                entrada = lector.readLine();
            }catch(IOException h){
                System.out.println("Error al leer datos " + h.toString());
                System.exit(4);
            }

            //Validación de que exista el archivo
            File e = new File(entrada);
            if(!e.exists()){
                System.out.println("No existe archivo");
                System.exit(0);
            }

            //Creación y validación de scanner
            Scanner scanner = null;
            try{
                scanner = new Scanner(e);
            }catch(IOException k){
                System.out.println("Error al encontrar archivo " + k.toString());
                System.exit(8);
            }
            
            //Enviando datos del archivo al cliente
            while(scanner.hasNextLine()){
                salida = scanner.nextLine();
                escritor.println(salida);
            }
            escritor.println("fin");
            
            //Cerrando sockets
            try{
                socket.close();
                socketServidor.close();
            }catch(IOException j){
                System.out.println("Error al cerrar sockets " + j.toString());
                System.exit(9);
            }
        }
        
    }
    
}
