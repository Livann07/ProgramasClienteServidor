
package servidordirectorio;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ServidorDirectorio {

     //Verificación de puerto entero  
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
       
        ServidorDirectorio sd = new ServidorDirectorio();
        
        Scanner s = new Scanner(System.in);
        String puerto="";
        //Validación puerto
        boolean cal=false;
        do{
           try{
              System.out.println("Captura puerto");
              puerto= s.nextLine();
              cal=sd.saca(puerto);
            }catch(Exception f){
                System.out.println("Problema al capturar puerto " + f.toString());
                System.exit(0);
            }
        }while(puerto.equalsIgnoreCase("")||!cal);
        
        //Creación de sockets
        Socket socket=new Socket();
        ServerSocket socketServidor = null;
        
        while(true){
            //Validación de sockets al asignarle valor
            try{
                socketServidor = new ServerSocket(Integer.parseInt(puerto));
                socket = socketServidor.accept();
            }catch(IOException e){
                System.out.println("Error al crear sockets " + e.toString());
                System.exit(1);
            }

            //Creación y validación de PrintWriter y Buffered al asginarles valor
            BufferedReader lector = null;
            try{
                lector = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );
            }catch(IOException g){
                System.out.println("Error al crear lector " + g.toString());
                System.exit(2);
            }
            String entrada = "";

            PrintWriter escritor = null;
            try{
                escritor = new PrintWriter(socket.getOutputStream(), true);
            }catch(IOException f){
                System.out.println("Error al crear escritor " + f.toString());
                System.exit(3);
            }

            //Validación al leer linea enviada por el cliente
            try{
                entrada = lector.readLine();
            }catch(IOException h){
                System.out.println("Error al leer datos " + h.toString());
                System.exit(4);
            }

            //Verifican que exista y que sea directorio
            boolean pe = false;
            final File e = new File(entrada);
            if(!e.exists()){

                escritor.println("No existe carpeta");
                pe = true;
            }else if(!e.isDirectory()){
                escritor.println("No es directorio");
                pe = true;
            }
            //Obtener nombres de archivos en el fichero, a traves de un tipo file
            if(!pe){
                for(final File ficheroEntrada : e.listFiles()){
                    escritor.println(ficheroEntrada.getName());
                }
            }

            //Dato controlador
            escritor.println("fin");

            //Cerrar sockets
            try{
                socket.close();
                socketServidor.close();
            }catch(IOException j){
                System.out.println("Error al cerrar sockets " + j.toString());
            }
            
        }
        
    }
    
}
