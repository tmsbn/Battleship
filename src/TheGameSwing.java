/**
 * TheGame.java
 * <p>
 * Version: 1.0
 * <p>
 * Revisions: 1
 * <p>
 **/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * This class implements the Battleship game using RMI connection
 *
 * @author Thomas Binu and Savitha Jayasankar
 **/

public class TheGameSwing {

    protected static String nameServer, nameClient;
    int[] dimen;
    static STATE state = STATE.START_GAME;
    Player player;
    GameUI gameUI;
    PlayGamePanel playGamePanel;

    public static void main(String args[]) {
        TheGameSwing gameSwing = new TheGameSwing();
        gameSwing.startMultiPlayerGame();
    }

    /**
     * Checks for end of game by scanning if all ships are sunk
     *
     * @return is game ended
     */
    public boolean checkEndGame() {

        Fleet fleet = player.getFleet();
        Ocean ocean = player.getOcean();

        boolean hasGameEnded = true;

        for (Ship ship : fleet.getShips()) {

            if (ship.isHasShipSunk())
                continue;

            int x = ship.getX();
            int y = ship.getY();

            boolean shipAsSunk = true;
            for (int i = 0; i < ship.getLength(); i++) {

                if (ship.getOrientation() == Ship.ORIENTATION.VERTICAL) {
                    if (ocean.getOceanArea()[x + i][y] != 'X') {
                        shipAsSunk = false;
                        hasGameEnded = false;
                    }
                } else if (ship.getOrientation() == Ship.ORIENTATION.HORIZONTAL) {
                    if (ocean.getOceanArea()[x][y + i] != 'X') {
                        shipAsSunk = false;
                        hasGameEnded = false;
                    }
                }
            }
            if (shipAsSunk && !ship.isHasShipSunk()) {
                TextView.printString(ship.getShipType().toString() + " has sunk!");
                ship.setHasShipSunk(true);
            }
        }
        return hasGameEnded;
    }

    private void startMultiPlayerGame() {

        gameUI = new GameUI(new ServerPanelInterface() {

            @Override
            public void startServerPressed(GamePanel gamePanel) {
                startServer();

            }

            @Override
            public void startClientPressed(GamePanel gamePanel) {
                startClient();
            }
        });

        gameUI.startGame();

//        TextView.printStringWithSeparator("Welcome to battleShip!");
//        String input = TextBox.getValidatedInput("Start a new game (S) , or search for games (C)?", "c|C|s|S");
//
//        if (input.equals("c") || input.equals("C")) {
//            startClient();
//
//        } else {
//            startServer();
//        }
    }

    public void startServer() {

        final InfoPanel infoPanel = new InfoPanel("Starting game server...");
        gameUI.getGameFrame().addNewPanel(infoPanel);

        final ServerRMI serverRMI = new ServerRMI();
        serverRMI.connect(5001, new ServerInterface() {

            @Override
            public void serverStarted() {

                infoPanel.setMessage("Started game server! Searching for other players...");
                InetAddress address = null;

                try {
                    address = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }

                TextView.printString("Started game server! Searching for other players...");
                TextView.printString("Server's Host Name:" + address);


            }

            @Override
            public void clientConnected() {

                infoPanel.setMessage("The other player has connected!, Starting game...");
                TextView.printString("The other player has connected!, Starting game...");

                final PlayerPanel playerPanel = new PlayerPanel(new PlayerPanelInterface() {
                    @Override
                    public void gotName(String name) {

                        nameServer = name;
                        state = STATE.START_GAME;
                        serverRMI.sendToClient(nameServer, null);
                    }
                });

                gameUI.getGameFrame().addNewPanel(playerPanel);

//                nameServer = TextBox.getInput("Enter player name:");
//                serverRMI.sendToClient(nameServer);

            }

            @Override
            public void gotResponse(String line, Serializable serializable) {

                switch (state) {

                    case START_GAME:

                        //get name from client and send dimens data to client
                        nameClient = line;

                        OceanInputPanel oceanInputPanel = new OceanInputPanel(nameServer);

                        oceanInputPanel.setOceanPanelInterface(new OceanPanelInterface() {

                            @Override
                            public void gotShipAndOceanInfo(Ship[] ships, Ocean ocean, int[] dimens) {

                                TheGameSwing.this.dimen = dimens;
                                state = STATE.PLAY_GAME;
                                serverRMI.sendToClient(dimen[0] + "," + dimen[1], null);
                                Fleet fleet = new Fleet(ocean, ships);
                                player = new Player(nameServer, fleet);

                            }
                        });

                        gameUI.getGameFrame().addNewPanel(oceanInputPanel);

                        break;

                    case PLAY_GAME:

                        if (playGamePanel == null) {

                            playGamePanel = new PlayGamePanel(player);
                            playGamePanel.setPlayGameInterface(new PlayGameInterface() {
                                @Override
                                public void shootAtLocation(int x, int y) {
                                    playGamePanel.setShootMode(false);
                                    serverRMI.sendToClient(x + "," + y, null);
                                }
                            });
                            gameUI.getGameFrame().addNewPanel(playGamePanel);
                            playGamePanel.setShootMode(true);
                        }

                        if (serializable != null && serializable instanceof Ocean) {
                            playGamePanel.updateOtherPlayerOcean((Ocean) serializable);
                        }

                        if (line.equals("exit")) {

                            playGamePanel.setText("You won!", Color.BLUE);

                        } else {

                            if (line.matches("\\d+,\\d+")) {

                                //get hit points from client
                                int[] hitPointsFromClient = getCoordinatesAsIntArray(line);

                                if (playGamePanel.addHitArea(hitPointsFromClient[0], hitPointsFromClient[1])) {

                                    if (checkEndGame()) {

                                        TextView.printString("You LOST! Game over");
                                        playGamePanel.setText("You LOST! Game over", Color.RED);
                                        serverRMI.sendToClient("exit", null);

                                    } else
                                        serverRMI.sendToClient("hit", player.getOcean());

                                    return;
                                } else {
                                    player.getOcean().printMap();

                                    setDelayedShootMode();
                                }

                            } else if (line.equals("started")) {
                                playGamePanel.setText("Get read to play!\nChoose point to shoot", Color.BLACK);
                                playGamePanel.setShootMode(true);
                            } else if (line.equals("hit")) {
                                TextView.printStringWithSeparator("HIT on the other ship!");
                                playGamePanel.setText("HIT on the other ship!", Color.BLUE);
                                setDelayedShootMode();
                            }

                        }
                        break;
                }
            }

            @Override
            public void error(String error) {

            }

            @Override
            public void isRunning() {

            }
        });
    }

    public void setDelayedShootMode() {

        int delay = 2000;

        Timer timer = new Timer(delay, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                playGamePanel.setShootMode(true);
            }
        });
        timer.setRepeats(false);
        timer.start();

    }

    public void startClient() {

        final InfoPanel infoPanel = new InfoPanel("Starting client...");
        gameUI.getGameFrame().addNewPanel(infoPanel);

        final ClientRMI clientRMI = new ClientRMI();
        clientRMI.connect(5001, new ClientInterface() {

            @Override
            public void isConnected() {

                //infoPanel.setMessage("Connected to server!, Starting game...");
                infoPanel.setMessage("Connected to server!, Starting game...");
                TextView.printString("Connected to server!, Starting game...");
            }

            @Override
            public void error(String error) {
                TextView.printString("error");
            }

            @Override
            public void isConnecting() {

                TextView.printString("Searching for active games...");
                infoPanel.setMessage("Searching for active games...");
            }

            @Override
            public void responseFromServer(String line, final Serializable serializable) {

                switch (state) {
                    case START_GAME:

                        //Get name from server and send name of client to server
                        nameServer = line;

                        final PlayerPanel playerPanel = new PlayerPanel(new PlayerPanelInterface() {
                            @Override
                            public void gotName(String name) {

                                nameClient = name;
                                state = STATE.GOT_NAME;
                                clientRMI.sendToServer(nameClient, null);
                            }
                        });

                        gameUI.getGameFrame().addNewPanel(playerPanel);
                        break;

                    case GOT_NAME:

                        //get dimensions from server and setup the game
                        dimen = getCoordinatesAsIntArray(line);

                        OceanInputPanel oceanInputPanel = new OceanInputPanel(nameClient, dimen);

                        oceanInputPanel.setOceanPanelInterface(new OceanPanelInterface() {

                            @Override
                            public void gotShipAndOceanInfo(Ship[] ships, Ocean ocean, int[] dimens) {

                                state = STATE.PLAY_GAME;

                                Fleet fleet = new Fleet(ocean, ships);
                                player = new Player(nameClient, fleet);

                                clientRMI.sendToServer("started", player.getOcean());

                                if (playGamePanel == null) {

                                    playGamePanel = new PlayGamePanel(player);
                                    playGamePanel.setPlayGameInterface(new PlayGameInterface() {
                                        @Override
                                        public void shootAtLocation(int x, int y) {
                                            playGamePanel.setShootMode(false);
                                            clientRMI.sendToServer(x + "," + y, player.getOcean());
                                        }
                                    });
                                    playGamePanel.setShootMode(false);
                                    gameUI.getGameFrame().addNewPanel(playGamePanel);
                                }
                            }
                        });

                        gameUI.getGameFrame().addNewPanel(oceanInputPanel);

                        TextView.printStringWithSeparator("Get read to play!");

                        break;

                    case PLAY_GAME:

                        playGamePanel.setShootMode(false);

                        if (serializable != null && serializable instanceof Ocean) {
                            playGamePanel.updateOtherPlayerOcean((Ocean) serializable);
                        }

                        if (line.equals("exit")) {

                            TextView.printString("You WON! Congrats!");
                            playGamePanel.setText("You won! Congrats!", Color.BLUE);

                        } else {

                            if (line.matches("\\d+,\\d+")) {

                                //get hit points from client
                                int[] hitPointsFromClient = getCoordinatesAsIntArray(line);

                                if (playGamePanel.addHitArea(hitPointsFromClient[0], hitPointsFromClient[1])) {
                                    player.getOcean().printMap();

                                    if (checkEndGame()) {

                                        TextView.printString("You LOST! Game over");
                                        playGamePanel.setText("You LOST! Game over", Color.RED);
                                        clientRMI.sendToServer("exit", player.getOcean());

                                    } else
                                        clientRMI.sendToServer("hit", player.getOcean());

                                    break;

                                } else {
                                    player.getOcean().printMap();
                                    setDelayedShootMode();
                                }


                            } else if (line.equals("started")) {
                                TextView.printStringWithSeparator("Get read to play!\nChoose point to shoot");
                                playGamePanel.setText("Get read to play!\nChoose point to shoot", Color.BLACK);

                            } else if (line.equals("hit")) {
                                TextView.printStringWithSeparator("HIT on the other ship!");
                                playGamePanel.setText("HIT on the other ship!", Color.BLUE);
                                setDelayedShootMode();
                            }
                        }

                        break;


                }

            }
        });
    }

    private static int[] getCoordinatesAsIntArray(String line) {

        int result[] = new int[2];
        String[] dimens = line.split(",");
        result[0] = Integer.parseInt(dimens[0]);
        result[1] = Integer.parseInt(dimens[1]);

        return result;
    }

    enum STATE {
        START_GAME, GOT_NAME, OCEAN_SETUP, SHOOT, PLAY_GAME
    }
}
