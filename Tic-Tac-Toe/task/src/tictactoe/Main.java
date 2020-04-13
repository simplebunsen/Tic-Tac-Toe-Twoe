package tictactoe;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.util.Scanner;

enum State{
    PLAYING, DRAW , X_WINS, O_WINS, IMPOSSIBLE
}

public class Main {

    public static final int SIZE = 3;
    public static final char EMPTY = '_';
    public static final char X = 'X';
    public static final char O = 'O';

    public static void main(String[] args) {
        State state = State.PLAYING;
        char currentMove = 'X';

        char[][] playingField = updatePlayingField("_________");

        boolean flag = true;
        while(flag){
            System.out.print("Enter the coordinates: ");
            int[] coords = processInput();
            if(coords[0] == -1) continue;

            char[][] backup = playingField.clone();
            playingField = updatePlayingField(playingField, coords[0], coords[1], currentMove);
            if(playingField != null) {
                printField(playingField);
            }else{
                //playing field is null => ERROR while updating
                playingField = backup;
                continue;
            }

            //check playing field before next input

            state = getCurrentState(playingField);

            switch (state) {
                case PLAYING:
                    if(currentMove == 'X') {
                        currentMove ='O';
                    }else currentMove = 'X';
                    continue;
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
                    //TODO: impossible is sloppy, see below
                    System.out.println("I think that it is impossible, continuing anyways");
                    continue;
            }
            flag = false;

        }
    }

    private static State getCurrentState(char[][] playingField) {
        State state = State.PLAYING;
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
                    /*
                    ---------
                    | ! X X |
                    | X O O |
                    | X O O |
                    ---------
                    If ! goes X, it returns impossible even though the move is a valid one.
                     */
                    state = State.IMPOSSIBLE;
                    break;
            }
        }
        return state;
    }

    private static int[] processInput(){
        Scanner scanner = new Scanner(System.in);

        int hor = 0;
        int vert = 0;
        boolean hasBullshit = false;

        if(scanner.hasNextInt()){
            hor = scanner.nextInt();
            if(scanner.hasNextInt()){
                vert = scanner.nextInt();
            }else{
                scanner.next();
                hasBullshit = true;
            }
        }else{
            scanner.next();
            hasBullshit = true;
        }

        if(hasBullshit){
            System.out.println("You should enter numbers!");
            return new int[]{-1, -1};
        }
        return new int[]{hor, vert};


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
        //System.out.println("we have x times " + xCount + " and o times "+ oCount);
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

    //worse version of printField()
    private static void printArray(char[][] playingField) {
        for(char[] i : playingField){
            for(char i1 : i){
                System.out.print(i1 + " ");
            }
            System.out.println();
        }
    }

    //initializing playing field by string
    private static char[][] updatePlayingField(String input) {
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

    //updating playing field by char and coords
    private static char[][] updatePlayingField(char[][] field, int row, int col, char input){
        //validate coords range
        if (row > 3 || row < 1 || col > 3 || col < 1) {
            System.out.println("Coordinates should be from 1 to 3!");
            return null;
        }
        //quit being silly coordinates
        //fixing them here since they got mixed up somewhere else and I'm to lazy to track down right now..
        int hor = row - 1;
        int vert = 4 - col - 1;
        //validate coords not occupied
        if (field[vert][hor] != '_'){
            System.out.println("This cell is occupied! Choose another one!");
            return null;
        }
        //replace empty space with input
        field[vert][hor] = input;
        return field;
    }

    private static void printField(char[][] playingField){
        printHorLine();
        for (int row = 1; row <= 3; row++) {
            for (int col = 1; col <= 5; col++) {
                switch (col) {
                    case 1:
                    case 5:
                        System.out.print("| ");
                        break;
                    default:
                        char c = playingField[row-1][col-2];
                        System.out.print(c + " ");
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
