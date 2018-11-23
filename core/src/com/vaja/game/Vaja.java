package com.vaja.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.vaja.screen.GameScreen;

public class Vaja extends Game {

    private GameScreen screen;

    private AssetManager asset;
    @Override
    public void create() {
        asset = new AssetManager();
        asset.load("res/graphics_unpacked/tiles_packed/textures.atlas", TextureAtlas.class);
        asset.load("res/graphics_unpacked/tiles/grass1.png", Texture.class);
        asset.load("res/graphics_unpacked/tiles/grass2.png", Texture.class);
        asset.load("res/graphics_unpacked/tiles_packed/textures.png", Texture.class);
        asset.finishLoading();

        screen = new GameScreen(this);

        this.setScreen(screen);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 1f, 1f);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
    }

    public AssetManager getAsset() {
        return asset;
    }
}
