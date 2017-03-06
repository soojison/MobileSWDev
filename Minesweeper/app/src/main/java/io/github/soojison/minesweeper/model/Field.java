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

    public void setBomb() {
        isBomb = true;
    }

    public boolean isBombOrigin() {
        return isBombOrigin;
    }

    public void setBombOrigin() {
        isBombOrigin = true;
    }

    public boolean isDiscovered() {
        return discovered;
    }

    public void setDiscovered() {
        this.discovered = true;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged() {
        this.discovered = true;
        this.flagged = true;
    }

    public int getBombsNearBy() {
        return bombsNearBy;
    }

    public void setBombsNearBy(int bombsNearBy) {
        this.bombsNearBy = bombsNearBy;
    }
}
