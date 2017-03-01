package hu.ait.tictactoe.model;

// A singleton model = only one instance of it can exist

import android.widget.Chronometer;

import hu.ait.tictactoe.MainActivity;
import hu.ait.tictactoe.R;

public class TicTacToeModel {

    // we should have one instance of this
    private static TicTacToeModel instance = null;



    // private constructor: you cannot create a new instance of this in the main method
    private TicTacToeModel() {

    }

    // singleton design pattern: if there is no object, then create it
    // otherwise do not do anything because you want only one instance of the model
    public static TicTacToeModel getInstance() {
        if (instance == null) {
            instance = new TicTacToeModel();
        }
        return instance;
    }

    // creating constants in java (use the final keyword so it's uneditable)
    public static final short EMPTY =  0;
    public static final short CIRCLE = 1;
    public static final short CROSS = 2;

    private short[][] model = {
            {EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY}
    };

    // circle will be the first player
    private short nextPlayer = CIRCLE;

    public void resetModel() {
        for (int i = 0; i < 3; i++) {           //fori + enter = shortcut for creating for loops
            for (int j = 0; j < 3; j++) {
                model[i][j] = EMPTY;
            }
        }
        nextPlayer = CIRCLE;

        // you could also do this, but this causes more work for garbage collector:
        //      instance = null;
    }

    public boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(model[i][j] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    public short checkWinner() {
        short winner = EMPTY;

        // diagonal
        if (model[0][0] == model[1][1] && model[1][1] == model[2][2] ||
                model[0][2] == model[1][1] && model[1][1] == model[2][0]) {
            return model[1][1];
        }

        // row or col
        for (int i = 0; i < 3; i++) {
            if(model[i][0] == model[i][1] && model[i][1] == model[i][2]) {
                return model[i][0];
            } else if (model[0][i] == model[1][i] && model[1][i] == model[2][i]) {
                return model[0][i];
            }
        }

        return winner;
    }

    public void changeNextPlayer() {
        nextPlayer = (nextPlayer == CIRCLE) ? CROSS : CIRCLE;

    }


    public void setField(int x, int y, short player) {
        model[x][y] = player;
    }

    public short getField(int x, int y) {
        return model[x][y];
    }

    // cmd + n: auto getter setter generator
    public short getNextPlayer() {
        return nextPlayer;
    }


}
