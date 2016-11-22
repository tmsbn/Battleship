import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class PlayGamePanel extends GamePanel {


    GridBagConstraints constraints = new GridBagConstraints();
    PlayGameInterface playGameInterface;
    Ocean ocean;
    Label infoLabel;
    JButton shootButton;
    int x, y;
    Player player;
    JButton oceanButtons[][];
    Ocean otherPlayerOcean;

    public PlayGamePanel(Player player) {

        this.ocean = player.getOcean();
        this.player = player;
        initializeComponents();
    }

    public void initializeComponents() {

        setLayout(new GridBagLayout());
        setupOcean();
    }


    public void setPlayGameInterface(PlayGameInterface playGameInterface) {
        this.playGameInterface = playGameInterface;
    }

    private void setupOcean() {

        int width = ocean.getWidth();
        int height = ocean.getHeight();

        final JLabel playerNameLabel = new JLabel(player.getName() + "'s GAME");
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = width;
        add(playerNameLabel, constraints);


        oceanButtons = new JButton[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                GridBagConstraints constraints = new GridBagConstraints();
                JButton button = new JButton(".");

                oceanButtons[i][j] = button;
                constraints.gridx = i;
                constraints.gridy = j + 1;
                constraints.weightx = 1;
                button.putClientProperty("x", i);
                button.putClientProperty("y", j);
                add(button, constraints);


                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {

                        JButton oceanButton = (JButton) evt.getSource();
                        resetOceanButtons();
                        oceanButton.setText("X");
                        x = (Integer) oceanButton.getClientProperty("x");
                        y = (Integer) oceanButton.getClientProperty("y");
                        setText("Shoot at location:" + x + "," + y, Color.BLACK);
                        shootButton.setEnabled(true);
                    }
                });
            }

        }

        infoLabel = new Label();
        infoLabel.setAlignment(Label.CENTER);


        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = height + 1;
        constraints.gridwidth = width;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(infoLabel, constraints);


        shootButton = new JButton("Shoot!");
        constraints.gridwidth = width;
        constraints.gridx = 0;
        constraints.gridy = height + 2;
        constraints.fill = GridBagConstraints.CENTER;
        shootButton.setEnabled(false);

        add(shootButton, constraints);
        shootButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (playGameInterface != null)
                    playGameInterface.shootAtLocation(x, y);
            }
        });


    }

    public void setOceanType(char[][] oceanArea, boolean showAll) {

        for (int i = 0; i < oceanButtons.length; i++) {
            for (int j = 0; j < oceanButtons[0].length; j++) {
                if (showAll) {
                    if (oceanArea != null)
                        oceanButtons[i][j].setText("" + oceanArea[i][j]);
                    else
                        oceanButtons[i][j].setText(".");
                } else {
                    if (oceanArea != null && (oceanArea[i][j] == 'O' || oceanArea[i][j] == 'X'))
                        oceanButtons[i][j].setText("" + oceanArea[i][j]);
                    else
                        oceanButtons[i][j].setText(".");
                }
            }
        }
    }

    public void resetOceanButtons() {

        for (int i = 0; i < oceanButtons.length; i++) {
            for (int j = 0; j < oceanButtons[0].length; j++) {
                if (!oceanButtons[i][j].getText().equals("O") && !oceanButtons[i][j].getText().equals("X"))
                    oceanButtons[i][j].setText(".");
            }
        }
    }

    public void enableOceanButton(boolean enable) {

        for (int i = 0; i < oceanButtons.length; i++) {
            for (int j = 0; j < oceanButtons[0].length; j++) {
                oceanButtons[i][j].setEnabled(enable);
            }
        }
    }

    public void setShootMode(boolean currentTurn) {

        if (currentTurn) {

            if (otherPlayerOcean == null)
                setOceanType(null, true);
            else
                setOceanType(otherPlayerOcean.getOceanArea(), false);

            setText("Choose location to shoot", Color.BLACK);
            shootButton.setVisible(true);
            enableOceanButton(true);
        } else {

            setOceanType(ocean.getOceanArea(), true);
            shootButton.setVisible(false);
            setText("Waiting for other player..", Color.BLACK);
            enableOceanButton(false);
        }
        validate();
        repaint();
    }

    public void setText(String label, Color color) {
        infoLabel.setText(label);
        infoLabel.setForeground(color);
    }

    public void updateOtherPlayerOcean(Ocean ocean) {
        this.otherPlayerOcean = ocean;
    }

    /**
     * Mark a coordinate as hit
     *
     * @param x
     * @param y
     * @return returns false if already fired at this spot
     */
    public boolean addHitArea(int x, int y) {
        char cell = ocean.getOceanArea()[x][y];

        if (cell == '.') {

            ocean.getOceanArea()[x][y] = 'O';
            oceanButtons[x][y].setText("O");
            TextView.printString("\nMISS!!\n");
            setText("MISS!", Color.BLUE);

        } else if (Fleet.isAShip(cell)) {

            ocean.getOceanArea()[x][y] = 'X';
            oceanButtons[x][y].setText("X");
            setText("HIT!", Color.BLACK);
            TextView.printString("\nHIT!!\n");
            return true;

        } else {
            TextView.printString("\nYou already fired at this spot!\n");
            return true;
        }

        return false;
    }
}


