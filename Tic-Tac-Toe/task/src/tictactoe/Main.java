package tictactoe;

import jdk.swing.interop.SwingInterOpUtils;

import java.util.Scanner;

enum State{
    PLAYING, DRAW , X_WINS, O_WINS, IMPOSSIBLE
}

public class Main {

    //TODO Example 6
    public static final int SIZE = 3;
    public static final int EMPTY = '_';

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        State state = State.PLAYING;

        System.out.print("Enter cells: ");
        String input = scanner.next();
        char[][] playingField = initPlayingField(input);

        char winType = getWinningLineType(playingField);

        if (hasIllegalCounts(playingField)) {
            state = State.IMPOSSIBLE;
        } else {
            switch (winType) {
                case 'O':
                    state = State.O_WINS;
                    break;
                case 'X':
                    state = State.X_WINS;
                    break;
                case '?':
                    if (!hasEmptySpots(playingField)) {
                        state = State.DRAW;
                    }
                    break;
                case '!':
                    //TODO validate that we have lines of both teams and not 2 lines of one team
                    //note: once this is a proper game, that problem should vanish, I think.
                    state = State.IMPOSSIBLE;
                    break;
            }
        }

        switch (state) {
            case PLAYING:
                System.out.println("Game not finished");
                break;
            case DRAW:
                System.out.println("Draw");
                break;
            case X_WINS:
                System.out.println("X wins");
                break;
            case O_WINS:
                System.out.println("O wins");
                break;
            case IMPOSSIBLE:
                System.out.println("Impossible");
                break;
        }

    }

    private static boolean hasIllegalCounts(char[][] playingField) {
        int oCount = 0;
        int xCount = 0;
        for(char[] i : playingField){
            for(char i1 : i){
                if(i1 == 'O') oCount++;
                if(i1 == 'X') xCount++;
            }
        }

        if(Math.max(oCount, xCount) - Math.min(oCount, xCount) > 1){
            return true;
        }
        return false;
    }

    //Returns 'X' or 'O' if there is exactly one line with the corresponding
    //symbol winning. Else, it returns '?'.
    private static char getWinningLineType(char[][] playingField) {
        int count = 0;
        char type = '?';

        //looking at rows and gollumns
        for(int i=0; i<SIZE; i++){
            //TODO: PROPERLY ignore empty spaces.
            if(playingField[i][i] == EMPTY) continue;
            if (playingField[i][0] == playingField[i][1] && playingField[i][0]== playingField[i][2]) {
                count++;
                type = playingField[i][0];
            }
            if(playingField[0][i] == playingField[1][i] && playingField[0][i]== playingField[2][i]){
                count++;
                type = playingField[0][i];
            }
        }
        //looking at both diagonals
        if(playingField[1][1] != '_'){
            if (playingField[0][0] == playingField[1][1] && playingField[0][0] == playingField[2][2]) {
                count++;
                type = playingField[1][1];
            }
            if (playingField[0][2] == playingField[1][1] && playingField[0][2] == playingField[2][0]) {
                count++;
                type = playingField[1][1];
            }
        }


        if(count == 1){
            //return for "Only one winning line".
            //Since we only counted one, the 1 captured char in "type" should be the right one.
            return type;
        }else if (count > 1){
            //return illegal (more than 1 line)
            //TODO: 2 lines for the same player need to be exempt.
            return '!';
        }else if (count == 0){
            //return no line (yet)
            return '?';
        }
        return type;
    }

    private static boolean hasEmptySpots(char[][] playingField) {
        for(char[] i : playingField){
            for(char i1 : i){
                if(i1 == '_') return true;
            }
        }
        return false;
    }

    private static void printArray(char[][] playingField) {
        for(char[] i : playingField){
            for(char i1 : i){
                System.out.print(i1 + " ");
            }
            System.out.println();
        }
    }

    private static char[][] initPlayingField(String input) {
        char[][] array = new char[3][3];

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
                        array[row-1][col-2] = c;
                        input = input.substring(1);
                        //System.out.println("string is now " + input);
                        break;
                }
            }
            System.out.println();
        }
        printHorLine();

        return array;
    }

    public static void printHorLine(){
        System.out.println("---------");
    }
}
