
package clienteanyfile;


import java.io.*;
import java.net.*;

import java.util.Scanner;
import sun.net.util.IPAddressUtil;

public class ClienteAnyfile {
    
    //Obtener ruta de la ejecución del proyecto
    public String ruta(){
        URL link = this.getClass().getProtectionDomain().getCodeSource().getLocation();
        return link.toString();
    }
    
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
   
    public static void main(String[] args){
        
        ClienteAnyfile caf = new ClienteAnyfile();
        
        //Obtención de ruta limpia
        String pr= caf.ruta();
        int inicio = pr.indexOf(":") +2;
        int fin = pr.indexOf("dist");
        String pr2= pr.substring(inicio,fin);
     
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
               System.exit(0);
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
              cal=caf.saca(puerto);
            }catch(Exception f){
                System.out.println("Problema al capturar puerto " + f.toString());
                System.exit(1);
            }
        }while(puerto.equalsIgnoreCase("")||!cal);
     
        //Validación de socket
        Socket socket = null;
        try {
            socket = new Socket(ip,Integer.parseInt(puerto));
        } catch (IOException i) {
            System.out.println("Error, verifica ip y puerto");
            System.out.println("Error al crear el socket " + i.toString());
            System.exit(2);
        }   
        
        //Creación de PrintWriter y Buffered
        PrintWriter escritor=null;
        try{
         escritor= new PrintWriter(
            socket.getOutputStream(),true);    
        }catch(IOException j){
            System.out.println("Error al obtener datos " + j.toString());
            System.exit(3);
        }
        
        BufferedReader lector=null;
        try{
            lector = new BufferedReader(
         new InputStreamReader(socket.getInputStream())
        );  
        }catch(IOException k){
            System.out.println("Error al leer datos " + k.toString());
            System.exit(4);
        }
               
        System.out.println("Captura archivo a copiar");
        
        String dir;
        String datosEntrada="";
        Scanner scanner= new Scanner(System.in);
        
        try{
            while(true){
                //Se envía la dirección
                dir = scanner.nextLine();
                escritor.println(dir);

                //Leer linea 
                try{
                    datosEntrada = lector.readLine();
                }catch(IOException l){
                    System.out.println("Error al leer archivo / archivo inexistente " + l.toString());
                    System.exit(5);
                }

                //Obtener nombre
                int p = dir.lastIndexOf("\\");
                String name = dir.substring(p + 1);
 
                //Obtener tamaño
                long tamaño = Long.parseLong(datosEntrada);

                //Elementos para el server
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(pr2 + name);
                } catch (IOException t) {
                    System.out.println(t);
                }
                BufferedOutputStream out = new BufferedOutputStream(fos);

                 
                //Buffered interno
                BufferedInputStream in = null;
                try{
                    in = new BufferedInputStream(socket.getInputStream());
                }catch(IOException y){
                    System.out.println(y);
                }

                //Para archivos pequeños, evitando problemas de tamaños  
                if (tamaño < 10000) {
                    
                    //Variables necesarias para evitar particiones incompletas
                    double c1 = (double) tamaño / 100;
                    double c2 = c1 * 100;
                    int cant3 = (int) c2;
                    double c3 = (double) cant3 / 100;
                    float dec = (float) (c3 - ((int) tamaño / 100));
                    double part = dec * 100;
                    int supp = (int) tamaño / 100;
                    
                    //Creación del buffer auxiliar
                    byte[] buffer = null;
                    int c = 0;
                    try {
                        //Primera linea de descarga de archivo
                        int l = 0;
                        System.out.print("Descargando archivo... 0%");
                        while (l < tamaño) {
                            //si el contador es 0, es porque se le debe de agregar aparte de su parte, un faltante
                            if (c == 0) {
                                buffer = new byte[supp + (int) part];
                            } else {
                                buffer = new byte[supp];
                            }

                            //Llenando buffer
                            for (int i = 0; i < buffer.length; i++) {
                                buffer[i] = (byte) in.read();
                            }

                            //Escritura de archivo
                            try {
                                out.write(buffer);
                            } catch (IOException e) {
                            }
                            l = l + buffer.length;
                            c++;
                            
                            //
                            System.out.print("\r");
                            System.out.print("Descargando archivo... " + c + "%");

                        }
                      
                    }catch(IOException u){
                        System.out.println("Problemas al recibir el archivo" + u);
                        System.exit(6);
                    }
                }else{
                    //Si el archivo es de un tamaño mediano en adelante
                    //Variables de la misma función, para tener particiones exactas
                    int r = (int) (tamaño / 10000);
                    String conv = String.valueOf(tamaño);
                    int falt = Integer.parseInt(conv.substring(conv.length() - 4));
                    int cont = 0;
                    int prc = 0;
                    int l = 0;
                    
                    //Creación del buffer 
                    byte[] buffer = null;
                    try {
                        //Primera linea de descarga
                        long llenado = 0;
                        System.out.print("Descargando archivo... 0%");
                        //Mientras la parte acumulada no sea mayor al tamaño final
                        while (llenado < tamaño) {
                            //Si el contador es 0, es porque, además del tamaño entero, necesita un faltante
                            if(cont == 0){
                                buffer = new byte[r + (int) falt];
                                cont++;
                            }else{
                                buffer = new byte[r];
                            }
                            
                            for(int i = 0; i < buffer.length; i++){
                                buffer[i] = (byte) in.read();
                            }
                            //Escribiendo en archivo
                            try{
                                out.write(buffer);
                            }catch(IOException e){
                                System.out.println("Error al grabar " + e.toString());
                                System.exit(7);
                            }

                            llenado = llenado + buffer.length;
                            l++;

                            //Si se completó al tamaño del archivo
                            if(l == 100){
                                prc++;
                                System.out.print("\r");
                                System.out.print("Descargando archivo... " + prc + "%");
                                l = 0;
                            }
                            System.out.print("\r");
                            String llega = "llega";

                            //Mandar dato de control al server
                            try{
                                escritor.println(llega);
                            }catch(Exception e){
                                System.out.println("Error al enviar datos de control " + e.toString());
                                System.exit(8);
                            }
                            out.flush();
                        }
                    }catch(IOException t){
                        System.out.println("Error al descargar archivo " + t.toString());
                        System.exit(9);
                    }
                }
                
                System.out.print("\r");
                System.out.println("Descargando archivo... " + 100 + "%");
                System.out.print("Archivo descargado");
                
                //Cerrando diferentes buffered y socket
                try {
                    in.close();
                    out.close();
                    socket.close();
                } catch (IOException x) {
                    System.out.println("Error al cerrar flujos" + x.toString());
                    System.exit(10);
                }
                //Archivo ya enviado
                System.exit(11);

            }
        } catch (Exception e) {
            System.out.println("Problema al capturar archivo " + e.toString());
            System.exit(12);
        }
    }
    
}
