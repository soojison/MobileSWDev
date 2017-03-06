package io.github.soojison.minesweeper.model;

import java.util.Random;

// Singleton Model
public class GameLogic {
    public static final int GRID_SIZE = 5;
    public static final int NUM_MINES = 3;
    private static GameLogic instance = null;
    private Field[][] fieldModel = new Field[5][5];

    private GameLogic() {
        for(int i = 0; i < GRID_SIZE; i++){
            for(int j = 0; j < GRID_SIZE; j++){
                fieldModel[i][j] = new Field();
            }
        }
    }

    public static GameLogic getInstance() {
        if(instance == null) {
            instance = new GameLogic();
        }
        return instance;
    }

    private void generateBombs() {
        Random rand = new Random();
        int mineCount = 0;
        // add 3 bombs randomly by considering the 2D array in terms of 1D array
        // the row would be index / width, column would be index % width
        while(mineCount < NUM_MINES) {
            int bomb = rand.nextInt(GRID_SIZE*GRID_SIZE);
            int row = bomb / GRID_SIZE;
            int col = bomb % GRID_SIZE;
            if(!fieldModel[row][col].isBomb()) {
                fieldModel[row][col].setBomb(true);
                mineCount++;
            }
        }
    }

    private boolean isInBound(int val, int min, int max) {
        return Math.min(Math.max(val, min), max) == val;
    }

    private void calculateHints() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if(!fieldModel[i][j].isBomb()) {
                    fieldModel[i][j].setBombsNearBy(minesNear(i, j));
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
        if(isInBound(x, 0, GRID_SIZE-1) && isInBound(y, 0, GRID_SIZE-1)) {
            if(fieldModel[x][y].isBomb()) return 1;
        }
        return 0;
    }

    public void resetModel() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                fieldModel[i][j].resetField();
            }
        }
        generateBombs();
        calculateHints();
    }

    public void expandNearbyEmpty(int x, int y) {
        if(isInBound(x, 0, GRID_SIZE-1) && isInBound(y, 0, GRID_SIZE-1)) {
            if(fieldModel[x][y].getBombsNearBy() == 0 && !fieldModel[x][y].isDiscovered()) {
                fieldModel[x][y].setDiscovered(true);
                expandNearbyEmpty(x-1, y-1);
                expandNearbyEmpty(x-1, y);
                expandNearbyEmpty(x-1, y+1);
                expandNearbyEmpty(x, y-1);
                expandNearbyEmpty(x, y+1);
                expandNearbyEmpty(x+1, y-1);
                expandNearbyEmpty(x+1, y);
                expandNearbyEmpty(x+1, y+1);
            } else if(!fieldModel[x][y].isBomb() && !fieldModel[x][y].isDiscovered()) {
                fieldModel[x][y].setDiscovered(true);
            }
        }
    }

    public boolean gameIsWon() {
        boolean won = true;
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                won &= (fieldModel[i][j].isBomb() == fieldModel[i][j].isFlagged()) && fieldModel[i][j].isDiscovered(); // if all bombs are flagged
            }
        }
        return won;
    }

    public Field getFieldAt(int i, int j) {
        return fieldModel[i][j];
    }

    public void setFieldAsBombOrigin(int i, int j) {
        fieldModel[i][j].setDiscovered(true);
        fieldModel[i][j].setBombOrigin(true);
    }

    public void setFieldAsDiscovered(int i, int j) {
        fieldModel[i][j].setDiscovered(true);
    }

    public void setFieldAsFlag (int i, int j) {
        fieldModel[i][j].setDiscovered(true);
        fieldModel[i][j].setFlagged(true);
    }

    public void gameOver() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if(fieldModel[i][j].isBomb()) {
                    fieldModel[i][j].setDiscovered(true);
                }
            }

        }
    }
}
