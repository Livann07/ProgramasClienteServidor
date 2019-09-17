
package clientesincrono;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import sun.net.util.IPAddressUtil;
public class ClienteSincrono {
 
     //Obtener ruta de ejecución
     public String ruta(){
        URL link = this.getClass().getProtectionDomain().getCodeSource().getLocation();
        return link.toString();
     }
   
     //Verificar si es entero el puerto
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
        
        ClienteSincrono cs = new ClienteSincrono();
        //Obteniendo ruta
        String pr= cs.ruta();
        int inicio = pr.indexOf(":") +2;
        int fin = pr.indexOf("dist");
        
        String pr2= pr.substring(inicio,fin);
     
        Scanner s = new Scanner(System.in);
        String ip="";
        String [] se;
        boolean au;
        
        //Verificar ip
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
        
        //Verificar puerto
        String puerto="";
        boolean cal=false;
        do{
            try{
              System.out.println("Captura puerto");
              puerto= s.nextLine();
              cal=cs.saca(puerto);
            }catch(Exception f){
                System.out.println("Problema al capturar puerto " + f.toString());
                System.exit(0);
            }
        }while(puerto.equalsIgnoreCase("")||!cal);
     
        
        //Creación de socket y verificación
        Socket socket = null;
        try {
            socket = new Socket(ip,Integer.parseInt(puerto));
        } catch (IOException i) {
            System.out.println("Error, verifica ip y puerto");
            System.out.println("Error al crear el socket " + i.toString());
            System.exit(1);
        }   
               
        System.out.println("Captura archivo a copiar");
       
        //Creación de PrintWriter y buffered para poder recibir y enviar los datos
        PrintWriter escritor=null;
        try{
         escritor= new PrintWriter(
            socket.getOutputStream(),true);    
        }catch(IOException j){
            System.out.println("Error al obtener datos " + j.toString());
            System.exit(2);
        }
         
        BufferedReader lector=null;
        try{
            lector = new BufferedReader(
         new InputStreamReader(socket.getInputStream())
        );  
        }catch(IOException k){
            System.out.println("Error al leer datos " + k.toString());
            System.exit(3);
        }
        
        //Creación de variables que se necesitan para hacer el archivo
        String datos;
        String datosEntrada="";
        Scanner scanner= new Scanner(System.in);
        FileWriter fw=null;
        
        try{
            while(true){
                datos = scanner.nextLine();
                escritor.println(datos);
                if(!datosEntrada.equalsIgnoreCase("fin")){
                    //Posibles errores: archivo inexistente, problemas de lectura, permisos
                    try{
                        datosEntrada = lector.readLine();
                    }catch(IOException l){
                        System.out.println("Error al leer archivo / archivo inexistente " + l.toString());
                        System.exit(5);
                    }

                    //Nombre del archivo
                    int p = datos.lastIndexOf("\\");
                    String c = datos.substring(p + 1);
                    
                    //Creando archivo
                    try{
                        fw = new FileWriter(pr2 + c);
                    }catch(IOException g){
                        System.out.println("Error al crear archivo " + g.toString());
                        System.exit(4);
                    }

                    do{
                        //Escribiendo en el archivo
                        try{
                            fw.write(datosEntrada + "\r\n");
                        }catch(IOException m){
                            System.out.println("Error al escribir en archivo " + m.toString());
                            System.exit(7);
                        }
                        
                        //Leyendo nueva linea
                        try{
                            datosEntrada = lector.readLine();
                        }catch(IOException n){
                            System.out.println("Error al leer linea " + n.toString());
                            System.exit(8);
                        }
                        
                    }while(!datosEntrada.equalsIgnoreCase("fin"));

                    //Cerrando archivo
                    try {
                        fw.close();
                    } catch (IOException o) {
                        System.out.println("Error al cerrar archivo " + o.toString());
                        System.exit(9);
                    }
                    //Cerrando socket
                    try {
                        socket.close();
                    } catch (IOException e) {
                        System.out.println("Error al cerrar socket");
                    }
                    System.exit(10);

                }else{
                    //Se sale ya que termina de escribir archivo
                    System.exit(11);
                }
            }
        }catch(Exception p){
            //Por si el cliente se sale sin terminar el proceso de captura de archivo
            System.out.println("\nProblema al capturar archivo " + p.toString());
            System.exit(12);
        }
    }
    
}
