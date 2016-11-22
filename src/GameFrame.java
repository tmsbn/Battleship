import javax.swing.*;
import java.awt.*;

/**
 * Created by tmsbn on 11/20/16.
 */
public class GameFrame extends JFrame {

    public GameFrame() {

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(getFrameSize());

    }

    private Dimension getFrameSize() {

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dim = tk.getScreenSize();
        dim.setSize((int) (dim.getWidth() * 0.5), (int) (dim.getHeight() * 0.5));
        return dim;
    }

    public void addNewPanel(JPanel jPanelToAdd) {

        getContentPane().removeAll();
        getContentPane().add(jPanelToAdd);
        getContentPane().revalidate();
        getContentPane().repaint();
    }

}
