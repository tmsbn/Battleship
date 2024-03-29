import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by tmsbn on 11/9/16.
 */
public interface RMIClientInterface extends Remote {

    void sendMessage(String message, Serializable serializable) throws RemoteException;

    void sendServerConnected() throws RemoteException;
}
