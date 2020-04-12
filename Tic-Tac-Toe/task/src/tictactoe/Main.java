package tictactoe;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter cells: ");
        String input = scanner.next();

        printHorLine();
        for (int row = 1; row <= 3; row++) {
            for (int col = 1; col <= 5; col++) {
                switch (col) {
                    case 1:
                    case 5:
                        System.out.print("| ");
                        break;
                    default:
                        char c = input.charAt(0);
                        System.out.print(c + " ");
                        input = input.substring(1);
                        //System.out.println("string is now " + input);
                        break;
                }
            }
            System.out.println();
        }
        printHorLine();
    }

    public static void printHorLine(){
        System.out.println("---------");
    }
}
