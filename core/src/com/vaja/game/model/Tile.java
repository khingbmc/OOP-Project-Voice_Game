package com.vaja.game.model;

public class Tile {

    private Actor actor;

    private TERRAIN terrain;

    private WorldObj object;

    public Tile(TERRAIN terrain){
        this.terrain = terrain;
    }


    public TERRAIN getTerrain(){
        return terrain;
    }

    public Actor getActor() {
        return actor;
    }

    public WorldObj getObject() {
        return object;
    }

    public void setObject(WorldObj object) {
        this.object = object;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }
}
