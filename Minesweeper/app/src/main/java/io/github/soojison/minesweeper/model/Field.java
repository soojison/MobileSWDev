package io.github.soojison.minesweeper.model;


public class Field {

    private boolean isBomb;
    private boolean isBombOrigin;
    private boolean discovered;
    private boolean flagged;
    private int bombsNearBy;

    public Field() {
        isBomb = false;
        isBombOrigin = false;
        discovered = false;
        flagged = false;
        bombsNearBy = 0;
    }

    public void resetField() {
        isBomb = false;
        isBombOrigin = false;
        discovered = false;
        flagged = false;
        bombsNearBy = 0;
    }

    public boolean isBomb() {
        return isBomb;
    }

    public void setBomb(boolean val) {
        isBomb = val;
    }

    public boolean isBombOrigin() {
        return isBombOrigin;
    }

    public void setBombOrigin(boolean val) {
        isBombOrigin = val;
    }

    public boolean isDiscovered() {
        return discovered;
    }

    public void setDiscovered(boolean val) {
        this.discovered = val;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean val) {
        this.discovered = val;
        this.flagged = val;
    }

    public int getBombsNearBy() {
        return bombsNearBy;
    }

    public void setBombsNearBy(int bombsNearBy) {
        this.bombsNearBy = bombsNearBy;
    }
}
