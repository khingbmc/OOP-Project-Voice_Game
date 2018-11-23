package com.vaja.game.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.vaja.game.model.YSortable;

import java.util.ArrayList;
import java.util.List;

public class WorldObj implements YSortable {

    private int x, y;

    private TextureRegion texture;
    private float sizeX, sizeY;

    private List<GridPoint2> tiles;
    private boolean canWalk;

    public WorldObj(int x, int y, TextureRegion texture, float sizeX, float sizeY, GridPoint2[] tiles){
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.tiles = new ArrayList<GridPoint2>();
        for(GridPoint2 p : tiles){
            this.tiles.add(p);
        }
        this.canWalk = true;
    }

    public WorldObj(int x, int y, boolean canWalk, TextureRegion texture, float sizeX, float sizeY, GridPoint2[] tiles){
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.sizeY = sizeY;
        this.sizeX = sizeX;
        this.tiles = new ArrayList<GridPoint2>();
        for(GridPoint2 p : tiles){
            this.tiles.add(p);
        }
        this.canWalk = canWalk;
    }

    public void update(float delta){

    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public TextureRegion getTexture() {
        return texture;
    }

    public List<GridPoint2> getTiles() {
        return tiles;
    }

    public boolean isCanWalk() {
        return canWalk;
    }


    @Override
    public float getWorldX() {
        return 0;
    }

    @Override
    public float getWorldY() {
        return 0;
    }

    @Override
    public TextureRegion getSprite() {
        return null;
    }


    @Override
    public float getSizeX() {
        return sizeX;
    }

    @Override
    public float getSizeY() {
        return sizeY;
    }
}
