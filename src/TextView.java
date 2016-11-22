/**
 * Created by tmsbn on 11/6/16.
 */
public class TextView {

    public static void printStringWithSeparator(String name) {

        System.out.println("-------------------" + name + "---------------------\n");
    }

    public static void printSeparator() {

        String Separator = "-------------------------------";
        System.out.println(Separator);
    }

    public static void printString(String name) {

        System.out.println(name);
    }

    public static void printStringWithExtraSpace(String name) {

        System.out.println("\n" + name + "\n");
    }

    public static void printStringWithoutNewLine(String name) {

        System.out.print(name);
    }

    public static void printNewLine() {

        System.out.println();
    }


}
