import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by tmsbn on 11/20/16.
 */
public class PlayerPanel extends GamePanel {

    GridBagConstraints constraints = new GridBagConstraints();

    PlayerPanelInterface playerPanelInterface;

    public PlayerPanel(PlayerPanelInterface playerPanelInterface) {

        initComponents();
        this.playerPanelInterface = playerPanelInterface;
    }

    private void initComponents() {

        setLayout(new GridBagLayout());

        JLabel jLabel = new JLabel("Player name:");

        constraints.gridx = 0;
        constraints.gridy = 0;
        add(jLabel, constraints);

        final JTextField jTextField = new JTextField(10);
        constraints.gridx = 1;
        constraints.gridy = 0;
        add(jTextField, constraints);


        final JButton jButton = new JButton("Submit");
        constraints.gridx = 2;
        constraints.gridy = 0;

        add(jButton, constraints);

        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                jButton.setEnabled(false);
                playerPanelInterface.gotName(jTextField.getText());

            }
        });


    }


}
