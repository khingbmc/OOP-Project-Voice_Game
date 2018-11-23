package com.vaja.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import com.vaja.game.Setting;
import com.vaja.game.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorldRender {
    private AssetManager asset;
    private World world;

    private Texture grass1, grass2, grassDirt;


    private List<Integer> renderObj = new ArrayList<Integer>();
    private List<YSortable> forRendering = new ArrayList<YSortable>();

    public WorldRender(AssetManager asset, World world){
        this.asset = asset;
        this.world = world;

        this.grass1 = asset.get("res/graphics_unpacked/tiles/grass1.png", Texture.class);
        this.grass2 = asset.get("res/graphics_unpacked/tiles/grass2.png", Texture.class);
        //TextureAtlas atlas = asset.get("res/graphics_unpacked/tiles/texturesDirt.atlas", TextureAtlas.class);

    }

    public void render(SpriteBatch batch, Camera cam){
        float worldStartX = Gdx.graphics.getWidth()/2 - cam.getCameraX()* Setting.SCALE_TILE_S;
        float worldStartY = Gdx.graphics.getHeight()/2 - cam.getCameraY()*Setting.SCALE_TILE_S;

        //render TERRAIN
        for(int x = 0;x < this.world.getMap().getWidth();x++){
            for(int y = 0;y < this.world.getMap().getHeight();y++){
                Texture render;
                if(world.getMap().getTile(x, y).getTerrain() == TERRAIN.GRASS1){
                    render = grass1;
                }else{
                    render = grass2;
                }
                batch.draw(render,
                        worldStartX+x*Setting.SCALE_TILE_S,
                        worldStartY+y*Setting.SCALE_TILE_S,
                        Setting.SCALE_TILE_S,
                        Setting.SCALE_TILE_S);
            }
        }
        /* collect obj and actor */
        for(int x = 0;x < world.getMap().getWidth();x++){
            for(int y = 0;y < world.getMap().getHeight();y++){
                if(world.getMap().getTile(x, y).getObject() != null){
                    WorldObj object = world.getMap().getTile(x, y).getObject();
                    System.out.println(object.getTiles().size());
                    System.out.println(object.isCanWalk());
                    if(renderObj.contains(object.hashCode())){
                        continue;
                    }
                    forRendering.add(object);
                    renderObj.add(object.hashCode());
                }
                if(world.getMap().getTile(x, y).getActor() != null){
                    Actor actor = world.getMap().getTile(x, y).getActor();
                    forRendering.add(actor);
                    System.out.println(actor.getSizeX());
                }
            }
        }
        Collections.sort(forRendering, new WorldObjYComparator());
        Collections.reverse(forRendering);

        for (YSortable loc : forRendering) {

            System.out.println(loc.getSprite());
            batch.draw(loc.getSprite(),
                    worldStartX+loc.getWorldX()*Setting.SCALE_TILE_S,
                    worldStartY+loc.getWorldY()*Setting.SCALE_TILE_S,
                    Setting.SCALE_TILE_S*loc.getSizeX(),
                    Setting.SCALE_TILE_S*loc.getSizeY());
        }
    }

}
