/*  Instituto Politécnico Nacional
*   Escuela Superior de Computo
*
*   4CV11 | Desarrollo de Sistemas Distribuidos
*       -> Diana Paola De la Cruz Sierra
*       -> Alan Ignacio Delgado Alarcon
* */
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class PI{
    static double valorPI = 0;

    static class Worker extends Thread{
            int nodo;
            Worker(int nodo){
                    this.nodo = nodo;
            }
            public synchronized void run(){
                Socket conexion = null;
                //conectarse al servidor "localhost al puerto 50000+nodo con re esperar la sumatoria
                for(;;) {
                    try {
                        conexion = new Socket("localhost", 50000+nodo);
                        DataInputStream entrada = new DataInputStream(conexion.getInputStream());

                        double x = entrada.readDouble();
                        valorPI += x;   //pi += sumatoria(sincronizarlo)

                        conexion.close();
                        break;
                    } catch (Exception e) {
                        try {
                            Worker.sleep(100);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
    }

    public static void main(String[] args) throws IOException, InterruptedException{
            int nodo = Integer.parseInt(args[0]);
           // int nodo = convertir args[0] a entero;
            
            if(nodo == 0){
			Worker w[] = new Worker[4];
			for(int i=0; i < 4; i++){
				w[i] = new Worker(i+1);
				w[i].start();
			}
			for(int i=0; i < 4; i++){
				w[i].join();
			}
			System.out.println("El valor de PI es: " + valorPI);
            }else{                
                ServerSocket servidor;
		        System.out.println("Nodo: " + nodo);
              
                servidor = new ServerSocket(50000 + nodo); //abrir el puerto 5000+nodo
		        System.out.println("Server port: " + (50000+nodo));

                Socket conexion = servidor.accept();    //esperar la conexión del cliente
                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                double subSuma = 0;

                for(int i = 0; i <= 999999; i++)    //calcular la sumatoria
                   subSuma = subSuma + (4.0 / (8.0*i + 2.0*((double)nodo-2.0) + 3.0));

                if(nodo%2 == 0)
                   subSuma = subSuma * (-1);               

                salida.writeDouble(subSuma);    //envia la sumatoria al cliente
                servidor.close();
            }
    }
}
