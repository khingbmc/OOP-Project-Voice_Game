package com.vaja.game.model;


import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.List;

public class World {

    private TileMap map;
    private List<Actor> actors;
    private List<WorldObj> objects;


    public World(int width, int height){
        this.map = new TileMap(width, height);
        actors = new ArrayList<Actor>();
        objects = new ArrayList<WorldObj>();
    }

    public void addActor(Actor a){
        this.map.getTile(a.getX(), a.getY()).setActor(a);
        actors.add(a);
    }

    public void addObject(WorldObj obj){
        for(GridPoint2 p : obj.getTiles()){
            this.map.getTile(obj.getX()+p.x, obj.getY()+p.y).setObject(obj);
        }
        this.objects.add(obj);
    }

    public void update(float delta){
        for(Actor a : actors){
            a.update(delta);
        }
        for(WorldObj o : objects){
            o.update(delta);
        }
    }

    public TileMap getMap() {
        return map;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public List<WorldObj> getObjects() {
        return objects;
    }
}
