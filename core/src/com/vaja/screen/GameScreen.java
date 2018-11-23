package com.vaja.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.vaja.game.Setting;
import com.vaja.game.Vaja;
import com.vaja.game.controller.PlayerController;
import com.vaja.game.model.*;
import com.vaja.util.AnimationSet;

public class GameScreen extends AbstractScreen {

    private PlayerController control;

    private World world;
    private Actor player;
    private Camera cam;

    private SpriteBatch batch;

    private WorldRender worldrenderer;

    public GameScreen(Vaja app) {
        super(app);
        batch = new SpriteBatch();

        TextureAtlas atlas = app.getAsset().get("res/graphics_unpacked/tiles_packed/textures.atlas");

        AnimationSet animationSet = new AnimationSet(
                new Animation(0.3f/2, atlas.findRegions("brendan_walk_north"), Animation.PlayMode.LOOP_PINGPONG),
                new Animation(0.3f/2, atlas.findRegions("brendan_walk_south"), Animation.PlayMode.LOOP_PINGPONG),
                new Animation(0.3f/2, atlas.findRegions("brendan_walk_east"), Animation.PlayMode.LOOP_PINGPONG),
                new Animation(0.3f/2, atlas.findRegions("brendan_walk_west"), Animation.PlayMode.LOOP_PINGPONG),
                atlas.findRegion("brendan_stand_north"),
                atlas.findRegion("brendan_stand_south"),
                atlas.findRegion("brendan_stand_east"),
                atlas.findRegion("brendan_stand_west")
        ); //0.3 sec to 2 tile

        cam = new Camera();
        world = new World(100, 100);
        this.player = new Actor(world.getMap(), 50, 2, animationSet);
        world.addActor(player);

        //addHouse(50, 15);
        control = new PlayerController(player);
        worldrenderer = new WorldRender(getApp().getAsset(), world);


//        world = new World(100, 100);
//        this.p_stand_south = new Texture("res/graphics_unpacked/tiles/brendan_stand_south.png");
//        this.grass1 = new Texture("res/graphics_unpacked/tiles/grass1.png");
//        this.grass2= new Texture("res/graphics_unpacked/tiles/grass2.png");
//        map = new TileMap(30, 30);
//        player = new Actor(world.getMap(), 50, 2, animationSet);
//        world.addActor(player);
//        batch = new SpriteBatch();
//
//        control = new PlayerController(player);
//        cam = new Camera();





    }



    @Override
    public void show() {
        Gdx.input.setInputProcessor(control);
    }

    @Override
    public void render(float v) {
        control.update(v);

        //plus 0.5 f because center of tile
        //System.out.println(player.getX()+" "+player.getY());
        float campositionX = player.getWorldX(), campositionY = player.getWorldY();

        if(campositionX <= 9 || campositionX >= 90){
            if(campositionX <= 9) {
                campositionX = 9;
            }
            else{
                campositionX = 90;
            }
            //camX range 9 for border


        }

        if(campositionY <= 6 || campositionY >= 93){
            if(campositionY <= 6) {campositionY = 6;}
            else{campositionY = 93;}

        }
        //camY range 6 for border
        cam.update(campositionX+0.5f, campositionY+0.5f);
        world.update(v);


        batch.begin();

        worldrenderer.render(batch, cam);
        batch.end();


    }

    public void addHouse(int x, int y){
        TextureAtlas atlas = getApp().getAsset().get("res/graphics_unpacked/tiles_packed/textures.atlas", TextureAtlas.class);
        TextureRegion houseRegion = atlas.findRegion("small_house");
        GridPoint2[] gridArray = new GridPoint2[5*4-1];
        int index = 0;
        for(int loopx = 0;loopx < 5;loopx++){
            for(int loopY = 0;loopY < 4;loopY++){
                if(loopx == 3 && loopY == 0){

                    continue;
                }
                gridArray[index] = new GridPoint2(loopx, loopY);
                index++;
            }
        }
        WorldObj house = new WorldObj(x, y, false, houseRegion, 5f, 5f, gridArray);
        world.addObject(house);

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
