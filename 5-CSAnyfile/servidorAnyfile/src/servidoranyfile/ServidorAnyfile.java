
package servidoranyfile;
import java.io.*;
import java.net.*;
import java.util.Scanner;


public class ServidorAnyfile {

   
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
        ServidorAnyfile saf = new ServidorAnyfile();
   
        Scanner s = new Scanner(System.in);
        String puerto="";
        boolean cal=false;
        //Validación puerto
        do{
          try{
              System.out.println("Captura puerto");
              puerto= s.nextLine();
              cal=saf.saca(puerto);
            }catch(Exception f){
                System.out.println("Problema al capturar puerto " + f.toString());
                System.exit(0);
            }
        }while(puerto.equalsIgnoreCase("") ||!cal);
        
        
        Socket socket=new Socket();
        ServerSocket socketServidor = null;
        
        while (true) {
            try {
                //Validación de sockets
                socketServidor = new ServerSocket(Integer.parseInt(puerto));
                socket = socketServidor.accept();
            } catch (IOException e) {
                System.out.println("Error al crear sockets " + e.toString());
                System.exit(1);
            }

            //Creación de buffered lector
            BufferedReader lector = null;
            try {
                lector = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );
            } catch (IOException g) {
                System.out.println("Error al crear lector " + g.toString());
                System.exit(2);
            }
            
            //Iniciando PrintWriter
            String entrada = "";
            PrintWriter escritor = null;
            try {
                escritor = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException f) {
                System.out.println("Error al crear escritor " + f.toString());
                System.exit(3);
            }

            //Leer linea de cliente
            try {
                entrada = lector.readLine();
            } catch (IOException h) {
                System.out.println("Error al leer datos " + h.toString());
                System.exit(4);
            }

            //Obtener tamaño de archivo por medio de file
            File e = new File(entrada);
            long tamañoArchivo = (long) e.length();
            
            
            escritor.println(tamañoArchivo);
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(e);
            } catch (IOException q) {
                System.out.println("Error al iniciar FileInput "+q.toString());
                System.exit(5);
            }

            //Creación de buffereds 
            BufferedInputStream bis = new BufferedInputStream(fis);
            BufferedOutputStream bos = null;
            try{
                bos = new BufferedOutputStream(socket.getOutputStream());
            }catch(IOException g) {
                System.out.println("Problema al crear buffereds " + g.toString());
                System.exit(6);
            }

            //Si el tamaño es pequeño, entra
            if(tamañoArchivo < 10000){
                //Variables necesarias para evitar particiones incompletas
                double c1 = (double) tamañoArchivo / 100;
                double c2 = c1 * 100;
                int cant3 = (int) c2;
                double c3 = (double) cant3 / 100;
                float dec = (float) (c3 - ((int) tamañoArchivo / 100));
                double part = dec * 100;
                int supp = (int) tamañoArchivo / 100;

                //Creación de buffer auxiliar, junto con variables auxiliares
                byte[] buffer = null;
                int l = 0;
                int c = 0;

                try{
                    //mientras el total sea menor al tamaño real del archivo
                    while(l < tamañoArchivo){
                        //si el contador es 0, es porque se le debe de agregar aparte de su parte, un faltante
                        if (c == 0) {
                            buffer = new byte[supp + (int) part];
                        } else {
                            buffer = new byte[supp];
                        }
                        
                        bis.read(buffer);

                        //'Enviando archivo'
                        for(int i = 0; i < buffer.length; i++){
                            bos.write(buffer[i]);
                        }
                        l = l + buffer.length;
                        c++;
                    }
                }catch(IOException a){
                    System.out.println("Error al enviar archivo " + a.toString());
                    System.exit(7);
                }
            }else{
                
                //Si el archivo es de un tamaño mediano en adelante
                //Variables de la misma función, para tener particiones exactas
                int r = (int) (tamañoArchivo / 10000);
                String conv = String.valueOf(tamañoArchivo);
                int falt = Integer.parseInt(conv.substring(conv.length() - 4));
                byte[] buffer;
                long l = 0;
                int cont = 0;
                String check = "llega";
                try{
                     //Mientras la parte acumulada no sea mayor al tamaño final
                    while(l < tamañoArchivo){
                        //Si el contador es 0, es porque, además del tamaño entero, necesita un faltante
                        if(cont == 0){
                            buffer = new byte[r + (int) falt];
                            cont++;
                        }else{
                            buffer = new byte[r];
                        }

                        bis.read(buffer);
                        
                        //'Enviando archivo'
                        for(int i = 0; i < buffer.length; i++){
                            bos.write(buffer[i]);
                        }
                        bos.flush();
                        l = l + buffer.length;

                        //Leyendo linea de control
                        try{
                            entrada = lector.readLine();
                            check = entrada;
                        }catch(Exception f){
                            System.out.println("Error al recibir dato de control" + f.toString());
                            System.exit(8);
                        }
                    }
                }catch(IOException j){
                    System.out.println("Error al enviar archivo " + j.toString());
                    System.exit(9);
                }
            }

            //Verificar cierre de buffers
            try{
                bis.close();
                bos.close();
            }catch(IOException r){
                System.out.println("Buffers no cerrados");
                System.exit(10);
            }

            //Verificar cierre sockets
            try{
                socket.close();
                socketServidor.close();
            }catch(IOException j){
                System.out.println("Error al cerrar sockets " + j.toString());
                System.exit(11);
            }
        }
        
    }
    
}
