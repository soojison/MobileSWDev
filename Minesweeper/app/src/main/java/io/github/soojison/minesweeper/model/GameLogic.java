package io.github.soojison.minesweeper.model;

import java.util.Random;


// Singleton Model
public class GameLogic {
    private static GameLogic instance = null;

    private GameLogic() {

    }

    public static GameLogic getInstance() {
        if(instance == null) {
            instance = new GameLogic();
        }
        return instance;
    }

    public static final short EMPTY = 0;
    public static final short ONE = 1;
    public static final short TWO = 2;
    public static final short THREE = 3;
    public static final short FOUR = 4;
    public static final short FIVE = 5;

    public static final short BOMB = 6;
    public static final short DISCOVERED = 7;
    public static final short UNDISCOVERED = 8;
    public static final short FLAG = 9;
    public static final short BOMB_ORIGIN = 10;

    private short[][] hiddenModel = {
            {EMPTY, BOMB, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, BOMB, EMPTY},
            {EMPTY, EMPTY, BOMB, EMPTY, EMPTY},
            {EMPTY, BOMB, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, BOMB}
    }; // has info about bombs n shit

    private short[][] model = { //  displays to user
            {UNDISCOVERED, UNDISCOVERED, UNDISCOVERED, UNDISCOVERED, UNDISCOVERED},
            {UNDISCOVERED, UNDISCOVERED, UNDISCOVERED, UNDISCOVERED, UNDISCOVERED},
            {UNDISCOVERED, UNDISCOVERED, UNDISCOVERED, UNDISCOVERED, UNDISCOVERED},
            {UNDISCOVERED, UNDISCOVERED, UNDISCOVERED, UNDISCOVERED, UNDISCOVERED},
            {UNDISCOVERED, UNDISCOVERED, UNDISCOVERED, UNDISCOVERED, UNDISCOVERED}
    };

    public void printModel() {
        for (short[] a:model) {
            for (int i = 0; i < 5; i++) {
                System.out.print(a[i]);
            }
            System.out.println();
        }
        System.out.println(gameWon());
    }

    public void resetModel() {
        Random rand = new Random();
        int mineCount = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                hiddenModel[i][j] = EMPTY;
                model[i][j] = UNDISCOVERED;
            }
        }

        // adding random 5 bombs
        // consider the 2D array in terms of 1D array
        // the row would be index / width, column would be index % width
        while(mineCount < 5) {
            int bomb = rand.nextInt(25);
            int row = bomb / 5;
            int col = bomb % 5;
            if(hiddenModel[row][col] != BOMB) {
                hiddenModel[row][col] = BOMB;
                mineCount++;
            }
        }

        calculateHints();

    }

    private boolean isInBound(int val, int min, int max) {
        return Math.min(Math.max(val, min), max) == val;
    }


    public void calculateHints() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if(hiddenModel[i][j] != BOMB) {
                    hiddenModel[i][j] = minesNear(i,j);
                }
            }
        }
    }

    private short minesNear(int i, int j) {
        short mines = 0;
        mines += mineAt(i-1, j-1);
        mines += mineAt(i-1, j);
        mines += mineAt(i-1, j+1);
        mines += mineAt(i, j-1);
        mines += mineAt(i, j+1);
        mines += mineAt(i+1, j-1);
        mines += mineAt(i+1, j);
        mines += mineAt(i+1, j+1);
        return mines;
    }

    private short mineAt(int x, int y) {
        if(isInBound(x, 0, 5-1) && isInBound(y, 0, 5-1)) {
            if(hiddenModel[x][y] == BOMB) return 1;
        }
        return 0;
    }


    public short getModelField(int x, int y) {
        return model[x][y];
    }

    public void setModelField(int x, int y, short item) {
        model[x][y] = item;
    }

    public short getHiddenModelField(int x, int y) {
        return hiddenModel[x][y];
    }

    public void gameOver() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if(hiddenModel[i][j] == BOMB) {
                    if(model[i][j] != BOMB_ORIGIN) {
                        model[i][j] = BOMB;
                    }
                }
            }
        }
    }

    public void expandNearbyEmpty(int x, int y) {
        if(isInBound(x, 0, 5-1) && isInBound(y, 0, 5-1)) {
            if(hiddenModel[x][y] == EMPTY && model[x][y] == UNDISCOVERED) {
                model[x][y] = DISCOVERED;
                expandNearbyEmpty(x-1, y-1);
                expandNearbyEmpty(x-1, y);
                expandNearbyEmpty(x-1, y+1);
                expandNearbyEmpty(x, y-1);
                expandNearbyEmpty(x, y+1);
                expandNearbyEmpty(x+1, y-1);
                expandNearbyEmpty(x+1, y);
                expandNearbyEmpty(x+1, y+1);
            } else if(hiddenModel[x][y] != BOMB && model[x][y] == UNDISCOVERED) {
                model[x][y] = DISCOVERED;
            }
        }
    }

    public boolean gameWon() {
        boolean won = true;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                won &= (model[i][j] == DISCOVERED || model[i][j] == FLAG);
            }
        }
        return won;
    }

    //TODO: WIN GAME -- if you've flagged all the bombs you win
    // expand doesn't expand flags
    // when you lose, the flag over bomb --> bomb with cross
    // Make it so that you don't lose on your first turn
    // TODO: FLAG on wrong tile = game over
    //TODO: Undo Flags
}
