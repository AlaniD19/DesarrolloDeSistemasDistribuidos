import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteRMIP5 {
    static int N = 8;
    static double[][] A = new double[N][N];
    static double[][] B = new double[N][N];
    static double[][] C = new double[N][N];
    
    static double[][] A1, A2, A3, A4, B1, B2, B3, B4;
    
    static void inicializa_matrices(double[][] A, double[][] B, double[][] C, int N){
        for(int i = 0; i < N; i++)
            for(int j = 0; j < N; j++){
                A[i][j] = i + 2 * j;
                B[i][j] = 3 * i - j;
                C[i][j] = 0;
            }     
    }
    
    static void transpuesta(double[][] B, int N){
        for(int i = 0; i < N; i++)
            for(int j = 0; j < i; j++){
                double x = B[i][j];
                B[i][j] = B[j][i];
                B[j][i] = x;
            }
    }
    
    static void imprime_matriz(double[][] A, int N){
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                System.out.print(A[i][j] + " ");
            }System.out.println("");
        }
    }
    
    static double[][] separa_matriz(double[][] A, int inicio, int N){
        double[][] M = new double[N/4][N];
        for(int i = 0; i < N / 4; i++)
            for(int j = 0; j < N; j++)
                M[i][j] = A[i + inicio][j];
        return M;
    }        
            
    static void acomoda_matriz(double[][] C, double[][] A, int renglon, int columna, int N){
        for(int i = 0; i < N / 4; i++)
            for(int j = 0; j < N / 4; j++)
                C[i + renglon][j + columna] = A[i][j];
    }    
    
    static double checksum(double[][] C, int N){
        double check = 0;
        for(int i = 0; i < N; i++)
            for(int j = 0; j < N; j++)
                check += C[i][j];
            
        return check;
    }
    
    static class Worker extends Thread {
        int nodo;
        Worker(int nodo){
            this.nodo = nodo;
        }
        
        public void run() {
            try {
                String url = "rmi://localhost/pruebaPractica5";
                
                InterfaceRMIP5 interf = (InterfaceRMIP5)Naming.lookup(url);
                
                if(nodo == 1){
                    double C1[][] = interf.multiplica_matrices(A1, B1, N);
                    double C2[][] = interf.multiplica_matrices(A1, B2, N);
                    double C3[][] = interf.multiplica_matrices(A1, B3, N);
                    double C4[][] = interf.multiplica_matrices(A1, B4, N);
                    acomoda_matriz(C, C1, 0, 0, N); //matriz original, cual pasamos, renglon, col, N
                    acomoda_matriz(C, C2, 0, N/4, N);
                    acomoda_matriz(C, C3, 0, N/2, N);
                    acomoda_matriz(C, C4, 0, 3*N/4, N);
                    
                }else if(nodo == 2){
                    double C5[][] = interf.multiplica_matrices(A2, B1, N);
                    double C6[][] = interf.multiplica_matrices(A2, B2, N);
                    double C7[][] = interf.multiplica_matrices(A2, B3, N);
                    double C8[][] = interf.multiplica_matrices(A2, B4, N);
                    acomoda_matriz(C, C5, N/4, 0, N); //matriz original, cual pasamos, renglon, col, N
                    acomoda_matriz(C, C6, N/4, N/4, N);
                    acomoda_matriz(C, C7, N/4, N/2, N);
                    acomoda_matriz(C, C8, N/4, 3*N/4, N);
                    
                }else if(nodo == 3){
                    double C9[][] = interf.multiplica_matrices(A3, B1, N);
                    double C10[][] = interf.multiplica_matrices(A3, B2, N);
                    double C11[][] = interf.multiplica_matrices(A3, B3, N);
                    double C12[][] = interf.multiplica_matrices(A3, B4, N);
                    acomoda_matriz(C, C9, N/2, 0, N); //matriz original, cual pasamos, renglon, col, N
                    acomoda_matriz(C, C10, N/2, N/4, N);
                    acomoda_matriz(C, C11, N/2, N/2, N);
                    acomoda_matriz(C, C12, N/2, 3*N/4, N);
                    
                }else if(nodo == 4){
                    double C13[][] = interf.multiplica_matrices(A4, B1, N);
                    double C14[][] = interf.multiplica_matrices(A4, B2, N);
                    double C15[][] = interf.multiplica_matrices(A4, B3, N);
                    double C16[][] = interf.multiplica_matrices(A4, B4, N);
                    acomoda_matriz(C, C13, 3*N/4, 0, N); //matriz original, cual pasamos, renglon, col, N
                    acomoda_matriz(C, C14, 3*N/4, N/4, N);
                    acomoda_matriz(C, C15, 3*N/4, N/2, N);
                    acomoda_matriz(C, C16, 3*N/4, 3*N/4, N);
                }
            } catch (NotBoundException ex) {
                Logger.getLogger(ClienteRMIP5.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MalformedURLException ex) {
                Logger.getLogger(ClienteRMIP5.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RemoteException ex) {
                Logger.getLogger(ClienteRMIP5.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void main(String[] args) throws Exception {
        int nodo = 0;
        
        if(nodo == 0){ //Cliente
            inicializa_matrices(A, B, C, N);
            
            if(N == 8){
                System.out.println("Matriz A:");
                imprime_matriz(A,N);
                System.out.println("Matriz B");
                imprime_matriz(B,N);
            }            
            
            transpuesta(B, N);
            
            A1 = separa_matriz(A, 0, N);
            A2 = separa_matriz(A, N/4, N);
            A3 = separa_matriz(A, N/2, N);
            A4 = separa_matriz(A, 3*N/4, N);
            
            B1 = separa_matriz(B, 0, N);
            B2 = separa_matriz(B, N/4, N);
            B3 = separa_matriz(B, N/2, N);
            B4 = separa_matriz(B, 3*N/4, N);
            
            Worker w[] = new Worker[4];   //instancia de 4 hilos (para conectarnos a c/u de los server)
            for(int i = 0; i < 4; i++){
                w[i] = new Worker(i+1);
                w[i].start();   //los iniciamos
            }
            
            for(int i = 0; i < 3; i++)
                w[i].join();    //esperamos a que terminen los 3 hilos            
            
            if(N == 8){
                System.out.println("Matriz C:");
                imprime_matriz(C, N);
            }
            
            double checksum = checksum(C, N);
            System.out.println("Checksum: " + checksum);
            
        }
        
    }
}
