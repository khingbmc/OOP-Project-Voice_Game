package com.vaja.game.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.vaja.game.model.Actor;
import com.vaja.game.model.DIRECTION;

public class PlayerController extends InputAdapter {

    private boolean up, down, left, right, run;

    private Actor player;

    public PlayerController(Actor player) {
        this.player = player;
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.UP){
            up = true;

        }
        if(keycode == Input.Keys.DOWN){
            down = true;

        }
        if(keycode == Input.Keys.LEFT){
            left = true;

        }
        if(keycode == Input.Keys.RIGHT){
            right = true;

        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.UP){
            up = false;

        }
        if(keycode == Input.Keys.DOWN){
            down = false;

        }
        if(keycode == Input.Keys.LEFT){
            left = false;

        }
        if(keycode == Input.Keys.RIGHT){
            right = false;

        }
        return false;
    }
    public void update(float delta){
        if(up){
            if(this.run == false) player.move(DIRECTION.NORTH);

            return;
        }
        if(down){
            player.move(DIRECTION.SOUTH);
            return;
        }
        if(left){
            player.move(DIRECTION.WEST);
            return;
        }
        if(right){
            player.move(DIRECTION.EAST);
            return;
        }
    }
}
