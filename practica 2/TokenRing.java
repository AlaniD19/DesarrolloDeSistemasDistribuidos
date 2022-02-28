import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;

public class TokenRing {
    static void enviar_token(int nodo, short token){
        Socket conexion = null;
        for(;;){//conectarse al nodo 'Nodo' con reintentos.
            try{
                conexion = new Socket("localhost", 51000+nodo);
                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                
                salida.writeShort(token);   //enviar el token 
                conexion.close();    //desconectarse
                break;
            }catch(Exception e){
                
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
        
        //definir las propiedades (4) donde se indica el nombre del keystore del cliente y del servidor     

        int nodo = Integer.parseInt(args[0]);
        
        if(nodo == 0){
            short token = 0;
            enviar_token(1, token);  
        }
        
        System.out.println("Nodo: " + nodo);
        ServerSocket servidor = new ServerSocket(51000 + nodo);
        
        for(;;){    //siempre espera la conexion del cliente
            Socket conexion = servidor.accept();    //aceptar la conexión del cliente
            
            DataInputStream entrada = new DataInputStream(conexion.getInputStream());
            
            short token = entrada.readShort(); //leemos el token del server anterior
            
            token++;    //incrementamos en 1 el token
            
            if(nodo == 0 && token >= 500) 
                System.exit(0); //terminamos la ejecucion
            
            System.out.println("Nodo: " + (nodo % 6) + ". TOKEN: " + token); //desplegar el token
            enviar_token((nodo+1)%6, token);
        }
        
    }
}