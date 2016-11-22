import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by tmsbn on 11/20/16.
 */
public class StartGamePanel extends GamePanel {

    JButton jButton1, jButton2;
    GridBagConstraints constraints = new GridBagConstraints();
    ServerPanelInterface serverPanelInterface;

    public StartGamePanel(ServerPanelInterface serverPanelInterface) {
        this.serverPanelInterface = serverPanelInterface;
        initComponents();
    }

    private void initComponents() {

        setLayout(new GridBagLayout());

        jButton1 = new JButton("Start a new Game!");
        jButton2 = new JButton("Join a game!");

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 12;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        add(jButton1, constraints);

        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serverPanelInterface.startServerPressed(StartGamePanel.this);

            }
        });

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        add(jButton2, constraints);

        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serverPanelInterface.startClientPressed(StartGamePanel.this);
            }
        });
    }


}
