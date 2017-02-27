package io.github.soojison.minesweeper.model;

import android.icu.text.UnicodeSetIterator;

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

    public static final short BOMB = 1;
    public static final short EMPTY = 0;
    public static final short DISCOVERED = 2;
    public static final short UNDISCOVERED = 3;
    public static final short FLAG = 4;

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

    public void resetModel() {
        short bombCount = 0;
        Random rand = new Random();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if(bombCount < 5) {
                    hiddenModel[i][j] = (rand.nextInt(1) == 0)? EMPTY : BOMB;
                    bombCount++;
                }
                hiddenModel[i][j] = EMPTY;
                model[i][j] = UNDISCOVERED;
            }
        }
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

    public void setHiddenModelField(int x, int y, short item) {
        hiddenModel[x][y] = item;
    }
}
