import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceRMIP5 extends Remote{
    public double[][] multiplica_matrices(double[][] A, double[][] B, int N) throws RemoteException;
}
