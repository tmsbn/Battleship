/**
 * Ship class containing details about the ship
 *
 */

public class Ship {

    private int length;
    private int x;
    private int y;
    private ShipType shipType;
    private boolean hasShipSunk = false;
    private ORIENTATION orientation;
    public Ship(ShipType shipType, int x, int y, ORIENTATION orientation) {

        this.shipType = shipType;
        this.length = shipType.getLength();
        this.x = x;
        this.y = y;
        this.orientation = orientation;
    }

    public int getLength() {
        return length;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public ORIENTATION getOrientation() {
        return orientation;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public boolean isHasShipSunk() {
        return hasShipSunk;
    }

    public void setHasShipSunk(boolean hasShipSunk) {
        this.hasShipSunk = hasShipSunk;
    }

    public enum ShipType {
        Carrier(5, 'C'), Battleship(4, 'B'), Cruiser(3, 'R'), Destroyer(2, 'D');

        private int length;
        private char symbol;

        ShipType(int length, char symbol) {
            this.length = length;
            this.symbol = symbol;
        }

        public int getLength() {
            return length;
        }

        public char getSymbol() {
            return symbol;
        }
    }

    public enum ORIENTATION {
        HORIZONTAL, VERTICAL
    }
}
