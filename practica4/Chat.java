import java.io.IOException;
import java.lang.reflect.Field;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Chat {
    
    static void envia_mensajes_multicast(byte[] buffer, String ip, int puerto) throws IOException{
        DatagramSocket socket = new DatagramSocket();
        socket.send(new DatagramPacket(buffer, buffer.length, InetAddress.getByName(ip), puerto));
        socket.close();
    }
    
    static byte[] recibe_mensaje_multicast(MulticastSocket socket, int longitud_mensaje) throws IOException{
        byte[] buffer = new byte[longitud_mensaje];
        DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
        socket.receive(paquete);
        
        return paquete.getData();
    }
    
    static class Worker extends Thread{
        public void run(){
            try {
                //en un ciclo infinito se recibiran los mensajes enviados al
                //grupo 230.0.0.0 a través del puerto 50000 y se desplegarán en la pantalla
                System.setProperty("java.net.preferIPv4Stack", "true");
                
                InetAddress grupo = InetAddress.getByName("230.0.0.0");
                
                MulticastSocket socket = new MulticastSocket(50000);
                
                socket.joinGroup(grupo);
                
                for(;;){
                    byte[] mensaje_en_bytes = recibe_mensaje_multicast(socket, 1000);
                    String mensaje = new String(mensaje_en_bytes, "CP850");
                    System.out.println(mensaje);
                }
            } catch (IOException ex) {
                Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
            }
                        
        }
    }
    
    public static void main(String[] args) throws Exception{

System.setProperty("file.encoding","CP850");
Field charset = Charset.class.getDeclaredField("defaultCharset");
charset.setAccessible(true);
charset.set(null,null);

        new Worker().start();
        String nombre_usuario = "Ana";
        //String nombre_usuario = args[0];
        //En un ciclo infinito se leera cada mensaje del teclado y se enviará el mensaje al 
        //grupo 230.0.0.0 a través del puerto 50000
        System.out.println("Usuario: " + nombre_usuario);
        for(;;){
            System.out.println("Ingrese el mensaje a enviar: ");
		  Scanner scanner = new Scanner(System.in, "CP850");
            String mensaje = scanner.nextLine();
            
            System.setProperty("java.net.preferIPv4Stack", "true");
            mensaje = nombre_usuario + " dice " + mensaje;
            envia_mensajes_multicast(mensaje.getBytes(), "230.0.0.0", 50000);
        }
    }
}
