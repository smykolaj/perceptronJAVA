import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        Perceptron p = new Perceptron("perceptron.data", "perceptron.test.data", 100 );
        //Perceptron p = new Perceptron("wdbc.data", "wdbc.test.data", 700 );
        p.train();
        p.classifyTestSet();
        System.out.println();
        p.plotAccuracy();
        //p.classifyVector(new Vector("6.2,5.5,1.7,1.3,Iris-virginica"));
        menu(p);
    }

    public static void menu(Perceptron p ){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to enter a vector? (yes/no):");
        String choice = scanner.nextLine().trim().toLowerCase();
        if (choice.equals("yes")) {
            enterVector(scanner, p);
        } else if (choice.equals("no")) {
            System.out.println("Exiting...");
        } else {
            System.out.println("Invalid choice. Please enter 'yes' or 'no'.");
        }
    }

    private static void enterVector(Scanner scanner, Perceptron p) {
        System.out.println("Enter vector values (comma-separated):");
        String input = scanner.nextLine();
        String[] valuesStr = input.split(",");
        for (String valueStr : valuesStr) {
            try {
                double value = Double.parseDouble(valueStr.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter numeric values separated by commas.");
                return;
            }
        }
        p.classifySingleVector(input);
    }
}


