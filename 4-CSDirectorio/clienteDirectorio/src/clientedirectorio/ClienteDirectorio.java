package clientedirectorio;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import sun.net.util.IPAddressUtil;


public class ClienteDirectorio {
    
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
         
        ClienteDirectorio cd = new ClienteDirectorio();
        
        Scanner s = new Scanner(System.in);
        String ip="";
        String [] se;
        boolean au;
        //Validación de ip
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
        boolean cal=false;
        do{
           try{
              System.out.println("Captura puerto");
              puerto= s.nextLine();
              cal=cd.saca(puerto);
            }catch(Exception f){
                System.out.println("Problema al capturar puerto " + f.toString());
                System.exit(0);
            }
            
        }while(puerto.equalsIgnoreCase("")||!cal);
     
        //Creación de socket y validación
        Socket socket = null;
        try {
            socket = new Socket(ip,Integer.parseInt(puerto));
            if(!socket.isConnected())
                System.exit(1);
        } catch (IOException i) {
            System.out.println("Error, verifica ip y puerto");
            System.out.println("Error al crear el socket " + i.toString());
            System.exit(1);
        }   
        
        
        //Creación y validación de PrintWriter y buffered
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
        
        //Variables que serviran para obtener los nombres de los archivos en fichero
        System.out.println("Captura lista de fichero para obtener nombres");
        String datos;
        String datosEntrada="";
        Scanner scanner= new Scanner(System.in);
        try{
          while(true){
            datos = scanner.nextLine();
            escritor.println(datos);
            //Se realiza si el valor regresado por el servidor es diferente de "fin", 
            //Que es cuando se termina de leer el contenido de la carpeta
            if(!datosEntrada.equalsIgnoreCase("fin")){
                try{
                    datosEntrada = lector.readLine();
                }catch(IOException l){
                    System.out.println("Error al leer carpeta / carpeta inexistente " + l.toString());
                    System.exit(5);
                }

                do{
                    System.out.println(datosEntrada);
                    try{
                        datosEntrada = lector.readLine();
                    }catch(IOException n){
                        System.out.println("Error al leer linea " + n.toString());
                        System.exit(8);
                    }
                }while(!datosEntrada.equalsIgnoreCase("fin"));
                
                try{
                    socket.close();
                }catch(IOException e){
                    System.out.println("Error al cerrar socket" + e.toString());
                }
                System.exit(10);

            }else{
                System.out.println(datosEntrada);
                System.exit(11);
            }
          }
        }catch(Exception k){
            System.out.println("Problema al capturar directorio " + k.toString());
        }
    }

}
