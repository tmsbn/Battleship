import javax.swing.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * RMI Client API
 */
public class ClientRMI implements RMIClientInterface {

    RMIServerInterface rmiServerInterface;
    RMIClientInterface rmiClientInterface;
    ClientInterface clientInterface;

    public void connect(int port, ClientInterface clientInterface) {

        try {
            this.clientInterface = clientInterface;
            Registry registry = LocateRegistry.getRegistry(port);
            rmiClientInterface = (RMIClientInterface) UnicastRemoteObject.exportObject(this, 0);
            clientInterface.isConnecting();

            rmiServerInterface = (RMIServerInterface) registry.lookup("server");

            SwingWorker worker = new SwingWorker<Void, Void>() {
                @Override
                public Void doInBackground() {
                    try {
                        rmiServerInterface.sendClientObject(rmiClientInterface);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
            worker.execute();



        } catch (NotBoundException e) {
            clientInterface.error(e.getMessage());
            e.printStackTrace();
        } catch (RemoteException e) {
            clientInterface.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendToServer(final String line) {

        SwingWorker worker = new SwingWorker<Void, Void>() {
            @Override
            public Void doInBackground() {
                try {
                    TextView.printStringWithExtraSpace("Waiting for server...");
                    rmiServerInterface.sendMessage(line);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        worker.execute();



    }

    @Override
    public void sendMessage(String message) throws RemoteException {
        clientInterface.responseFromServer(message);
    }

    @Override
    public void sendServerConnected() throws RemoteException {
        clientInterface.isConnected();
    }
}




