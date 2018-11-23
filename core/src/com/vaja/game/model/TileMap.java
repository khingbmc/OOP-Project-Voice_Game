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
//                if(y >= 22 && y <=32){
//                    if(x == 49 && y == 22) this.tile[x][y] = new Tile(TERRAIN.DIRTDOWN1);
//                    else if(x == 50 && y == 22) this.tile[x][y] = new Tile(TERRAIN.DIRTDOWN2);
//                    else if(x== 51 && y == 22) this.tile[x][y] = new Tile(TERRAIN.DIRTDOWN3);
//                    else if(x == 49 &&(y>=23 && y <= 31)) this.tile[x][y] = new Tile(TERRAIN.DIRTMID1);
//                    else if(x == 50 &&(y>=23 && y <= 31)) this.tile[x][y] = new Tile(TERRAIN.DIRTMID2);
//                    else if(x == 51 &&(y>=23 && y <= 31)) this.tile[x][y] = new Tile(TERRAIN.DIRTMID3);
//                    else if(x == 49 && y == 32) this.tile[x][y] = new Tile(TERRAIN.DIRTUP1);
//                    else if(x == 50 && y == 32) this.tile[x][y] = new Tile(TERRAIN.DIRTUP2);
//                    else if(x == 51 && y == 32) this.tile[x][y] = new Tile(TERRAIN.DIRTUP3);
//                }
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
