import java.io.Serializable;

/**
 * Created by tmsbn on 11/6/16.
 */
public interface ServerInterface {

    void serverStarted();

    void clientConnected();

    void gotResponse(String line, Serializable serializable);

    void error(String error);

    void isRunning();
}
