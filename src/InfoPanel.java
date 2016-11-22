import javax.swing.*;
import java.awt.*;

/**
 * Created by tmsbn on 11/20/16.
 */
public class InfoPanel extends GamePanel {

    JLabel jLabel;
    GridBagConstraints constraints = new GridBagConstraints();

    public InfoPanel(String message) {
        initComponents(message);
    }

    private void initComponents(String message) {

        setLayout(new GridBagLayout());

        jLabel = new JLabel(message);

        constraints.gridx = 0;
        constraints.gridy = 0;
        add(jLabel, constraints);

    }

    public void setMessage(String text) {
        jLabel.setText(text);
    }


}
