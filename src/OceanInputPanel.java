import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class OceanInputPanel extends GamePanel {


    GridBagConstraints constraints = new GridBagConstraints();

    OceanPanelInterface oceanPanelInterface;
    String playerName;
    Ship.ShipType currentShipType = Ship.ShipType.Carrier;
    Ship.ORIENTATION currentOrientation = Ship.ORIENTATION.HORIZONTAL;
    Ship[] ships = new Ship[Ship.ShipType.values().length];
    int dimens[];

    Ocean ocean;
    Label infoLabel;
    JButton submitButton;
    boolean pack = false;

    public OceanInputPanel(String playerName) {

        this.playerName = playerName;
        initializeComponents();
    }

    public OceanInputPanel(String playerName, int[] dimens) {

        this.playerName = playerName;
        this.dimens = dimens;
        initializeComponents();
        pack = true;
    }

    public void setOceanPanelInterface(OceanPanelInterface oceanPanelInterface) {
        this.oceanPanelInterface = oceanPanelInterface;
    }


    public void initializeComponents() {

        setLayout(new GridBagLayout());

        if (dimens == null) {

            final JLabel jLabel1 = new JLabel();
            jLabel1.setText("Ocean's width:");
            constraints.gridx = 0;
            constraints.gridy = 0;
            add(jLabel1, constraints);

            final JTextField jTextField1 = new JTextField(3);
            constraints.gridx = 1;
            constraints.gridy = 0;
            add(jTextField1, constraints);

            final JLabel jLabel2 = new JLabel();
            jLabel2.setText("Ocean's height:");
            constraints.gridx = 2;
            constraints.gridy = 0;
            add(jLabel2, constraints);

            final JTextField jTextField2 = new JTextField(3);
            constraints.gridx = 3;
            constraints.gridy = 0;
            add(jTextField2, constraints);

            final JButton jButton = new JButton("OK");
            constraints.gridx = 4;
            constraints.gridy = 0;
            add(jButton, constraints);

            jButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    OceanInputPanel.this.remove(jLabel1);
                    OceanInputPanel.this.remove(jLabel2);
                    OceanInputPanel.this.remove(jTextField2);
                    OceanInputPanel.this.remove(jTextField1);
                    OceanInputPanel.this.remove(jButton);

                    dimens = new int[2];
                    if (jTextField1.getText().matches("\\d+") && jTextField1.getText().matches("\\d+")) {

                        dimens[0] = Integer.parseInt(jTextField1.getText());
                        dimens[1] = Integer.parseInt(jTextField2.getText());
                    } else {

                        dimens[0] = 10;
                        dimens[1] = 10;
                    }


                    OceanInputPanel.this.dimens = dimens;

                    setupOcean(dimens[0], dimens[1]);

                }
            });
        } else
            setupOcean(dimens[0], dimens[1]);
    }


    private void setupOcean(int width, int height) {

        final JLabel playerNameLabel = new JLabel(playerName + "'s Ocean");
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = width;
        add(playerNameLabel, constraints);

        ocean = new Ocean(width, height);
        final JButton oceanButtons[][] = new JButton[width][height];
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
                        int x = (Integer) oceanButton.getClientProperty("x");
                        int y = (Integer) oceanButton.getClientProperty("y");

                        if (isStartInOceanArea(x, y) && isEndInOceanOArea(x, y, currentOrientation, currentShipType)) {

                            if (isOverlappingWithShip(x, y, currentOrientation, currentShipType)) {
                                TextView.printString("Overlapping with another ship!");
                                infoLabel.setForeground(Color.red);
                                infoLabel.setText("Overlapping with another ship!");

                                return;
                            }

                            infoLabel.setForeground(Color.black);
                            infoLabel.setText("Ship added to ocean!");
                            Ship ship = new Ship(currentShipType, x, y, currentOrientation);
                            addShipToOcean(ship, oceanButtons);

                        } else {
                            infoLabel.setForeground(Color.red);
                            infoLabel.setText("Ship is out of bounds of the ocean, Please enter correct values");
                            TextView.printString("Ship is out of bounds of the ocean, Please enter correct values");
                        }

                    }
                });
            }
        }

        Ship.ShipType[] shipTypes = Ship.ShipType.values();
        GridBagConstraints constraints = new GridBagConstraints();
        infoLabel = new Label("Choose starting location of " + currentShipType);
        constraints.gridx = 0;
        infoLabel.setAlignment(Label.CENTER);
        constraints.gridy = height + 1;
        constraints.gridwidth = width;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(infoLabel, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = height + 2;
        constraints.gridwidth = width;

        Box shipTypeBox = Box.createHorizontalBox();

        for (int i = 0; i < Ship.ShipType.values().length; i++) {

            final JButton button = new JButton(String.valueOf(shipTypes[i]));
            shipTypeBox.add(button);
            button.putClientProperty("shipType", shipTypes[i]);

            if (i == 0) {
                button.setEnabled(false);
            }

            button.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {

                    JButton shipButton = (JButton) evt.getSource();
                    currentShipType = (Ship.ShipType) shipButton.getClientProperty("shipType");
                    infoLabel.setForeground(Color.black);
                    infoLabel.setText("Choose starting location of " + currentShipType);
                    button.setEnabled(false);

                    if (currentShipType == Ship.ShipType.Destroyer)
                        submitButton.setEnabled(true);

                }
            });
        }

        add(shipTypeBox,constraints);


        ButtonGroup group = new ButtonGroup();

        JRadioButton horizontal = new JRadioButton("Horizontal");
        constraints.anchor = GridBagConstraints.EAST;
        constraints.gridwidth = width / 2;
        constraints.gridx = 0;
        constraints.gridy = width + 2;
        constraints.gridy = height + 3;
        add(horizontal, constraints);
        group.add(horizontal);

        horizontal.setSelected(true);

        horizontal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                currentOrientation = Ship.ORIENTATION.HORIZONTAL;
            }
        });

        JRadioButton vertical = new JRadioButton("Vertical");
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = width / 2;
        constraints.gridx = width / 2;
        constraints.gridy = height + 3;
        add(vertical, constraints);
        group.add(vertical);


        vertical.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                currentOrientation = Ship.ORIENTATION.VERTICAL;
            }
        });

        submitButton = new JButton("Submit");
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridwidth = width;
        constraints.gridx = 0;
        constraints.gridy = height + 4;
        submitButton.setEnabled(false);

        add(submitButton, constraints);
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (oceanPanelInterface != null)
                    oceanPanelInterface.gotShipAndOceanInfo(ships, ocean, dimens);
            }
        });

        validate();
        repaint();

        if (pack)
            getGameFrame().pack();

    }

    /**
     * Add a ship to ocean based on coordinate and currentOrientation
     *
     * @param ship
     */
    private void addShipToOcean(Ship ship, JButton[][] jButtons) {


        char[][] oceanArea = ocean.getOceanArea();
        resetOceanWithShip(jButtons);
        addShipToArray(ship);

        for (int i = 0; i < ship.getLength(); i++) {
            if (ship.getOrientation() == Ship.ORIENTATION.VERTICAL) {
                oceanArea[ship.getX()][ship.getY() + i] = ship.getShipType().getSymbol();
                jButtons[ship.getX()][ship.getY() + i].setText(ship.getShipType().getSymbol() + "");
            } else if (ship.getOrientation() == Ship.ORIENTATION.HORIZONTAL) {
                oceanArea[ship.getX() + i][ship.getY()] = ship.getShipType().getSymbol();
                jButtons[ship.getX() + i][ship.getY()].setText(ship.getShipType().getSymbol() + "");
            }
        }

    }

    private void addShipToArray(Ship ship) {

        switch (ship.getShipType()) {
            case Destroyer:
                ships[3] = ship;
                break;
            case Carrier:
                ships[0] = ship;
                break;
            case Cruiser:
                ships[2] = ship;
                break;
            case Battleship:
                ships[1] = ship;
                break;
        }
    }


    /**
     * Reset ocean ship
     *
     * @param jButtons
     */
    private void resetOceanWithShip(JButton[][] jButtons) {

        for (int i = 0; i < jButtons.length; i++) {
            for (int j = 0; j < jButtons[0].length; j++) {
                if (jButtons[i][j].getText().equals(currentShipType.getSymbol() + "")) {
                    ocean.getOceanArea()[i][j] = '.';
                    jButtons[i][j].setText(".");
                }
            }
        }
    }

    /**
     * Check if given ship position overlaps with an existing ship
     *
     * @param x
     * @param y
     * @param orientation
     * @param shipType
     * @return true if overlaps
     */
    private boolean isOverlappingWithShip(int x, int y, Ship.ORIENTATION orientation, Ship.ShipType shipType) {

        for (int i = 0; i < shipType.getLength(); i++) {

            if (orientation == Ship.ORIENTATION.VERTICAL) {
                if (isAShip(ocean.getOceanArea()[x][y + i]))
                    return true;
            } else if (orientation == Ship.ORIENTATION.HORIZONTAL) {
                if (isAShip(ocean.getOceanArea()[x + i][y]))
                    return true;
            }

        }
        return false;
    }

    /**
     * Check if a given character is represents a ship
     *
     * @param cell
     * @return true if its a ship
     */
    public static boolean isAShip(char cell) {

        Ship.ShipType[] shipTypes = Ship.ShipType.values();

        for (Ship.ShipType shipType : shipTypes) {
            if (shipType.getSymbol() == cell)
                return true;
        }

        return false;

    }

    /**
     * Checks if given coordinate is in an ocean area
     *
     * @param x
     * @param y
     * @return
     */
    public boolean isStartInOceanArea(int x, int y) {

        return x >= 0 && y >= 0 && x < ocean.getWidth() && y < ocean.getHeight();
    }

    /**
     * Check if given ship position falls outside ocean area
     *
     * @param x
     * @param y
     * @param orientation
     * @param shipType
     * @return
     */
    private boolean isEndInOceanOArea(int x, int y, Ship.ORIENTATION orientation, Ship.ShipType shipType) {

        if (orientation == Ship.ORIENTATION.VERTICAL) {
            return y + shipType.getLength() - 1 < ocean.getHeight();
        } else
            return x + shipType.getLength() - 1 < ocean.getWidth();
    }


}


