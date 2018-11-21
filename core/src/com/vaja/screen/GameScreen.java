package com.vaja.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.vaja.game.Setting;
import com.vaja.game.Vaja;
import com.vaja.game.controller.PlayerController;
import com.vaja.game.model.Actor;
import com.vaja.game.model.Camera;
import com.vaja.game.model.TERRAIN;
import com.vaja.game.model.TileMap;
import com.vaja.util.AnimationSet;

public class GameScreen extends AbstractScreen {

    private Camera cam;
    private PlayerController control;
    private Actor player;
    private TileMap map;


    private SpriteBatch batch;

    private Texture p_stand_south;
    private Texture grass1, grass2;

    public GameScreen(Vaja app) {
        super(app);
        TextureAtlas atlas = app.getAsset().get("res/graphics_unpacked/tiles_packed/textures.atlas");
        AnimationSet animationSet = new AnimationSet(
                new Animation(0.3f/2,
                        atlas.findRegions("brendan_walk_north"), Animation.PlayMode.LOOP_PINGPONG),
                new Animation(0.3f/2,
                        atlas.findRegions("brendan_walk_south"), Animation.PlayMode.LOOP_PINGPONG),
                new Animation(0.3f/2,
                        atlas.findRegions("brendan_walk_east"), Animation.PlayMode.LOOP_PINGPONG),
                new Animation(0.3f/2,
                        atlas.findRegions("brendan_walk_west"), Animation.PlayMode.LOOP_PINGPONG),
                atlas.findRegion("brendan_stand_north"),
                atlas.findRegion("brendan_stand_south"),
                atlas.findRegion("brendan_stand_east"),
                atlas.findRegion("brendan_stand_west")
        ); //0.3 sec to 2 tile


        this.p_stand_south = new Texture("res/graphics_unpacked/tiles/brendan_stand_south.png");
        this.grass1 = new Texture("res/graphics_unpacked/tiles/grass1.png");
        this.grass2= new Texture("res/graphics_unpacked/tiles/grass2.png");
        map = new TileMap(30, 30);
        player = new Actor(map, 2, 2, animationSet);
        batch = new SpriteBatch();

        control = new PlayerController(player);
        cam = new Camera();





    }



    @Override
    public void show() {
        Gdx.input.setInputProcessor(control);
    }

    @Override
    public void render(float v) {
        control.update(v);
        player.update(v);
        //plus 0.5 f because center of tile
        System.out.println(player.getX()+" "+player.getY());
        float campositionX = player.getWorldX(), campositionY = player.getWorldY();

        if(campositionX <= 9 || campositionX >= 20){
            if(campositionX <= 9) {
                campositionX = 9;
            }
            else{
                campositionX = 20;
            }
            //camX range 9 for border


        }

        if(campositionY <= 6 || campositionY >= 23){
            if(campositionY <= 6) {campositionY = 6;}
            else{campositionY = 23;}

        }
        //camY range 6 for border
        cam.update(campositionX+0.5f, campositionY+0.5f);
        batch.begin();

        float worldStartX = Gdx.graphics.getWidth()/2 - cam.getCameraX()*Setting.SCALE_TILE_S;
        float worldStartY = Gdx.graphics.getHeight()/2 - cam.getCameraY()*Setting.SCALE_TILE_S;

        //render tilemap

        for(int x =0;x < map.getWidth();x++){
            for(int y = 0;y<map.getHeight();y++){
                Texture render;
                if(map.getTile(x, y).getTerrain() == TERRAIN.GRASS1){
                    render = grass1;
                }else{
                    render = grass2;
                }
                batch.draw(render, worldStartX+x*Setting.SCALE_TILE_S,
                        worldStartY+y*Setting.SCALE_TILE_S,
                        Setting.SCALE_TILE_S,
                        Setting.SCALE_TILE_S);
            }
        }
        //each tile in game is 16 pixel
        //render player
        batch.draw(player.getSprite(),
                worldStartX+player.getWorldX()* Setting.SCALE_TILE_S,
                worldStartY+player.getWorldY()* Setting.SCALE_TILE_S,
                Setting.SCALE_TILE_S,
                Setting.SCALE_TILE_S*1.5f);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
