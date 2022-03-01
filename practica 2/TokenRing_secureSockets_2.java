import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.FileOutputStream;
/*
para ejecutar esta vaina es java TokenRing_secureSockets_2 #NODO IP

La IP es consecutiva en cada nodo para unirse al grupo de hilos
ejemplo
    NODO 0 ---> 192.168.10.1
    NODO 1 ---> 192.168.10.2
    ....

    Los ceritifcados ya estan incluidos en el repositorio
    Use 2 certificados, uno para el servidor y otro para el cliente
*/


class TokenRing_secureSocket_2{
	
	static DataInputStream entrada;
	static DataOutputStream salida;
	static boolean inicio = true;
	static int nodo;
    static String ip;
    static short token = 0;

	static class Worker extends Thread{
		public void run(){
			try{
				System.setProperty("javax.net.ssl.keyStore","keystore_servidor.jks");
        		System.setProperty("javax.net.ssl.keyStorePassword", "1234567");

				SSLServerSocketFactory socket_factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
				ServerSocket servidor = socket_factory.createServerSocket(50000+nodo);

				Socket conexion = servidor.accept();
				entrada = new DataInputStream(conexion.getInputStream());
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception{
		nodo = Integer.valueOf(args[0]);
        ip = args[1];

		Worker w = new Worker();
		w.start();

		Socket conexion = null;

		System.setProperty("javax.net.ssl.trustStore", "keystore_cliente.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "123456");

		for(;;){
			try{
				SSLSocketFactory cliente = (SSLSocketFactory) SSLSocketFactory.getDefault();
        		conexion = cliente.createSocket(ip, 50000+(nodo+1)%6);
				break;
			}catch(Exception e){
				Thread.sleep(1000);

			}
		}
			

		salida = new DataOutputStream(conexion.getOutputStream());
		w.join();

		for (;;) {
			if(nodo == 0){
                if(inicio == true){
                    inicio = false;
                    token = 1;
                }else {
                    token = (short) (entrada.readShort() + 1);
                    System.out.println("Nodo: " + (nodo % 6) + ". TOKEN: " + token);
                }
			}else{
				token = (short) (entrada.readShort() + 1);

				if(token > 500)break;
				
				System.out.println("Nodo: " + (nodo % 6) + ". TOKEN: " + token);
			}
			if(nodo == 0 && token >= 500) break;

			salida.writeShort(token);
		}
		salida.writeShort(token);
        entrada.close();
        salida.close();
        conexion.close();
	}
}