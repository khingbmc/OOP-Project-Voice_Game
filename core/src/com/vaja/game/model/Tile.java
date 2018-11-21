package com.vaja.game.model;

public class Tile {

    private Actor actor;

    private TERRAIN terrain;

    public Tile(TERRAIN terrain){
        this.terrain = terrain;
    }


    public TERRAIN getTerrain(){
        return terrain;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }
}
