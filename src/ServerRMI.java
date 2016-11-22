import javax.swing.*;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * RMI Server Client
 */
public class ServerRMI implements RMIServerInterface {

    ServerInterface serverInterface;
    RMIClientInterface rmiClientInterface;

    public void connect(final int port, final ServerInterface serverInterface) {
        this.serverInterface = serverInterface;

        try {


            final RMIServerInterface stub = (RMIServerInterface) UnicastRemoteObject.exportObject(this, 0);

            // Bind the remote object's stub in the registry
            SwingWorker worker = new SwingWorker<Void, Void>() {
                @Override
                public Void doInBackground() throws RemoteException, AlreadyBoundException {

                    LocateRegistry.createRegistry(port);
                    TextView.printStringWithExtraSpace("Waiting for server...");
                    Registry registry = LocateRegistry.getRegistry(port);
                    registry.bind("server", stub);
                    serverInterface.serverStarted();

                    return null;
                }
            };
            worker.execute();

        } catch (RemoteException e) {
            e.printStackTrace();
            serverInterface.error(e.getMessage());

        }
    }

    public void sendToClient(final String line) {


        SwingWorker worker = new SwingWorker<Void, Void>() {
            @Override
            public Void doInBackground() {
                try {
                    TextView.printStringWithExtraSpace("Waiting for client...");
                    rmiClientInterface.sendMessage(line);
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
        serverInterface.gotResponse(message);
    }

    @Override
    public void sendClientObject(RMIClientInterface rmiClientInterface) {
        this.rmiClientInterface = rmiClientInterface;
        try {
            this.rmiClientInterface.sendServerConnected();

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        serverInterface.clientConnected();

    }

    public void close() {
        try {
            LocateRegistry.getRegistry().unbind("server");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

}



