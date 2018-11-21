package com.vaja.game.model;



public class TileMap {

    private Tile[][] tile;

    private int width, height;



    public TileMap(int width, int height){
        this.width = width;
        this.height = height;

        tile = new Tile[this.width][this.height];
        for(int x = 0;x < this.width;x++){
            for(int y = 0;y < this.height;y++){
                if(Math.random() > 0.5d){
                    this.tile[x][y] = new Tile(TERRAIN.GRASS1);
                }
                else{
                    this.tile[x][y] = new Tile(TERRAIN.GRASS2);
                }
            }
        }
    }

    public Tile getTile(int x, int y){
        return tile[x][y];
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }
}
