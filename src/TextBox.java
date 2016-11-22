import java.util.Scanner;

/**
 * Created by tmsbn on 11/6/16.
 */
public class TextBox {

    public static String getInput(String question){

        Scanner scanner = new Scanner(System.in);
        System.out.print(question);
        return scanner.nextLine();
    }

    public static String getValidatedInput(String question, String regexPattern) {

        while (true) {
            String input = TextBox.getInput(question);
            if (input.matches(regexPattern)) {
                return input;
            } else {
                TextView.printString("Incorrect format, please try again!");
            }
        }
    }
}
