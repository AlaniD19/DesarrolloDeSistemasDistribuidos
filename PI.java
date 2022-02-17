import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class PI{
    double valorPI = 0;

    static class Worker extends Thread{
            int nodo;
            Worker(int nodo){
                    this.nodo = nodo;
            }
            public void run(){
                    //conectarse al servidor "localhost al puerto 50000+nodo con re esperar la sumatoria
                    //pi += sumatoria(sincronizarlo)
            }
    }

    public static void main(String[] args) throws IOException{
        
            int nodo = 1;
           // int nodo = convertir args[0] a entero;
            
            if(nodo == 0){
                System.out.println("Nodo 0");
                    //crear 4 threads pasar como parámetro un número de nodo de 1 a 4
                    //iniciar los 4 threads
                    //esperar a que terminen los 4 threads (esto es una barrera)
                    //desplegar el valor de la variable "pi"
            }else{                
                ServerSocket servidor = new ServerSocket(50000 + nodo); //abrir el puerto 5000+nodo
                
                Socket conexion = servidor.accept();    //esperar la conexión del cliente
                
                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                
                double subSuma = 0;
                
                for(int i = 0; i <= 999999; i++)    //calcular la sumatoria
                    subSuma = subSuma + (4.0 / (8*i + 2*(nodo-2) + 3));
                
                if(nodo%2 == 0)
                    subSuma = subSuma * (-1);               
                
                salida.writeDouble(subSuma);    //envia la sumatoria al cliente
            }
    }
}
