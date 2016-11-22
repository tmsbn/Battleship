import javax.swing.*;

/**
 * Created by tmsbn on 11/20/16.
 */
public class GameUI {

    ServerPanelInterface serverPanelInterface;
    GameFrame gameFrame;

    public GameUI(ServerPanelInterface serverPanelInterface) {
        this.serverPanelInterface = serverPanelInterface;
    }

    public void startGame() {


        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                gameFrame = new GameFrame();
                gameFrame.setVisible(true);
                StartGamePanel startGamePanel = new StartGamePanel(serverPanelInterface);
                gameFrame.add(startGamePanel);
            }
        });
    }

    public static void main(String args[]) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                GameFrame gameFrame = new GameFrame();
                gameFrame.setVisible(true);

            }
        });
    }

    public GameFrame getGameFrame() {
        return gameFrame;
    }

}
