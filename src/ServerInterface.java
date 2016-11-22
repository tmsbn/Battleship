/**
 * Created by tmsbn on 11/6/16.
 */
public interface ServerInterface {

    void serverStarted();

    void clientConnected();

    void gotResponse(String line);

    void error(String error);

    void isRunning();
}
