package com.vaja.game.model;

public enum DIRECTION {
    NORTH(0, 2),
    EAST(2, 0),
    WEST(-2, 0),
    SOUTH(0, -2),
    ;

    private int dx, dy;

    private DIRECTION(int dx, int dy){
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }
}
