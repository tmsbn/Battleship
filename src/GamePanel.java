import javax.swing.*;

/**
 * Created by tmsbn on 11/20/16.
 */
public abstract class GamePanel extends JPanel{

    GameFrame getGameFrame(){

        return (GameFrame) SwingUtilities.windowForComponent(this);
    }
}
