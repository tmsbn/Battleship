import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by tmsbn on 11/9/16.
 */
public interface RMIServerInterface extends Remote {

    void sendMessage(String message) throws RemoteException;

    void sendClientObject(RMIClientInterface rmiClientInterface) throws RemoteException;
}
