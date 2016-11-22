import java.util.Scanner;

/**
 * This class controls Ocean functions
 **/
public class Ocean{

    private int height;
    private int width;
    private char[][] oceanArea;

    public Ocean(int width, int height) {

        this.width = width;
        this.height = height;
        setupGameArea();
    }

    /**
     * Mark a coordinate as hit
     *
     * @param x
     * @param y
     * @return returns false if already fired at this spot
     */
    public boolean addHitArea(int x, int y) {

        char cell = oceanArea[x][y];

        if (cell == '.') {
            oceanArea[x][y] = 'O';
            TextView.printString("\nMISS!!\n");
        } else if (Fleet.isAShip(cell)) {
            oceanArea[x][y] = 'X';
            TextView.printString("\nHIT!!\n");
            return true;
        } else {
            TextView.printString("\nYou already fired at this spot!\n");
            return true;
        }

        return false;
    }


    /**
     * Print both player 1 and player 2 ocean
     *
     * @param otherOceanArea
     */
    public void printBothGameOceans(char[][] otherOceanArea) {

        for (int i = 0; i < oceanArea.length; i++) {
            for (int j = 0; j < oceanArea[0].length; j++) {


                char cell = oceanArea[i][j];
                TextView.printStringWithoutNewLine(cell + " ");
            }

            TextView.printStringWithoutNewLine("       ");

            for (int k = 0; k < otherOceanArea[0].length; k++) {

                char cell = '.';
                if (!Fleet.isAShip(otherOceanArea[i][k])) {
                    cell = otherOceanArea[i][k];
                }
                TextView.printStringWithoutNewLine(cell + " ");
            }

            TextView.printNewLine();
        }

        TextView.printNewLine();
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public char[][] getOceanArea() {
        return oceanArea;
    }

    public void setupGameArea() {

        oceanArea = new char[width][height];
        for (int i = 0; i < oceanArea.length; i++) {
            for (int j = 0; j < oceanArea[0].length; j++) {
                oceanArea[i][j] = '.';
            }
        }

    }

    /**
     * Formatter function
     *
     * @param nameLength First player name length
     * @param oceanWidth Ocean width
     */
    public static void printTabs(int nameLength, int oceanWidth) {

        int size = ((oceanWidth * 2) - (nameLength + 14)) + 7;

        for (int i = 0; i < size; i++) {
            TextView.printStringWithoutNewLine(" ");
        }
    }

    /**
     * Mark a coordinate as hit
     *
     * @param x
     * @param y
     * @param symbol
     * @return returns false if already fired at this spot
     */

    public boolean addHitArea(int x, int y, char symbol) {
        return false;
    }

    /**
     * Prints ocean as 2d matrix
     */

    public void printMap() {

        TextView.printNewLine();

        for (int i = 0; i < oceanArea.length; i++) {
            for (int j = 0; j < oceanArea[0].length; j++) {
                TextView.printStringWithoutNewLine(oceanArea[i][j] + " ");

            }
            TextView.printNewLine();
        }

        TextView.printNewLine();

    }

    public static int[] getOceanAreaInput() {

        int[] dimen = new int[2];

        Scanner scanner = new Scanner(System.in);
        boolean gotCorrectInput = false;

        while (!gotCorrectInput) {

            TextView.printString("Enter the width and height of the Game Area (w,h):");

            String dimens = scanner.nextLine();

            if (!dimens.matches("\\d+,\\d+"))

                TextView.printString("Incorrect format, please try again!");

            else {

                String[] dimensSplit = dimens.split(",");

                dimen[0] = Integer.parseInt(dimensSplit[0]);
                dimen[1] = Integer.parseInt(dimensSplit[1]);

                if (!checkMaxSize(dimen[0], 5, 20) || !checkMaxSize(dimen[1], 5, 20)) {

                    TextView.printString("Dimension should in (5-20) range, please try again!");

                } else
                    gotCorrectInput = true;
            }
        }

        TextView.printSeparator();

        return dimen;
    }

    /**
     * Check if the given size fits within the max size
     * @param size
     * @param minSize
     * @param maxSize
     * @return
     */
    protected static boolean checkMaxSize(int size, int minSize, int maxSize) {
        return size >= minSize && size <= maxSize;
    }

}
