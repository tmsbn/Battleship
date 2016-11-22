/**
 * This class is responsible for setting up the fleet
 */

public class Fleet {

    private Ship[] ships = new Ship[Ship.ShipType.values().length];

    Ocean ocean;

    public Fleet(Ocean ocean, Ship[] ships) {

        this.ocean = ocean;
        this.ships = ships;
    }

    /**
     * Check if a given character is represents a ship
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
     * @param x
     * @param y
     * @return
     */
    public boolean isStartInOceanArea(int x, int y) {

        return x >= 0 && y >= 0 && x < ocean.getWidth() && y < ocean.getHeight();
    }


    public Ocean getOcean() {
        return ocean;
    }

    public Ship[] getShips() {
        return ships;
    }

}
