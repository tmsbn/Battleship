import java.io.Serializable;

/**
 * Created by tmsbn on 11/6/16.
 */
public interface ClientInterface {

    void isConnected();

    void error(String error);

    void isConnecting();

    void responseFromServer(String line, Serializable serializable);
}
