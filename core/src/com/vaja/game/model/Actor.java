package com.vaja.game.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.vaja.util.AnimationSet;

public class Actor {
    private int x, y;
    private DIRECTION facing;

    private float worldX, worldY;

    //what would player from and what would player go
    private int srcX, srcY, destX, destY;
    private float animTimer; //what the animation pass the time
    private float ANIM_TIME = 0.5f; //what the animation time set

    private float walkTimer;
    private boolean moveRequestThisFrame;

    private ACTOR_STATE state;
    private AnimationSet animationSet;

    //state of actor

    private TileMap map;

    public enum ACTOR_STATE{
        WALKING,
        STANDING,
        ;
    }

    public Actor(TileMap map , int x, int y, AnimationSet animations) {
        this.map = map;
        this.worldX = this.x = x;
        this.worldY = this.y = y;
        this.animationSet = animations;


        map.getTile(x, y).setActor(this);
        this.state = ACTOR_STATE.STANDING;
        this.facing = DIRECTION.SOUTH;
    }

    /**
     * this method is move player in corner
     * @return
     */
    public boolean move(DIRECTION direction){
        if(state == ACTOR_STATE.WALKING){
            if(facing == direction){
                moveRequestThisFrame = true;
            }
            return false;
        }

        if(x+direction.getDx() >= map.getWidth() || x+direction.getDx() < 0 ||
        y+direction.getDy() >= map.getHeight() || y+direction.getDy() <0) return false;

        if(map.getTile(direction.getDx()+x, direction.getDy()+y).getActor() != null) return false;
        //init move before update map
        initMove(direction);

        map.getTile(x, y).setActor(null);
        x += direction.getDx();
        y += direction.getDy();
        map.getTile(x, y).setActor(this);//set actor stand on it

        return true;
    }

    public void update(float delta){
        if(state == ACTOR_STATE.WALKING){
            animTimer += delta;
            walkTimer +=delta;
            this.worldX = Interpolation.linear.apply(srcX, destX, animTimer/ANIM_TIME);
            this.worldY = Interpolation.linear.apply(srcY, destY, animTimer/ANIM_TIME);
            if(animTimer > ANIM_TIME){
                float leftOverTime = animTimer-ANIM_TIME;
                walkTimer -= leftOverTime;
                finishMove();
                if(moveRequestThisFrame){
                    move(facing);
                }else{
                    walkTimer = 0f;
                }
            }
        }
        moveRequestThisFrame = false;
    }

    public void initMove(DIRECTION direction){
        this.facing = direction;
        this.srcX = x;
        this.srcY = y;
        this.destX = x+direction.getDx();
        this.destY = y+direction.getDy();
        this.worldY = y;
        this.worldX = x;
        animTimer = 0f;
        state = ACTOR_STATE.WALKING;


    }

    public void finishMove(){
        state = ACTOR_STATE.STANDING;
        this.worldX = destX;
        this.worldY = destY;
        srcY = srcX = 0;
        destY = destX = 0;
    }

    public float getWorldX() {
        return worldX;
    }

    public float getWorldY() {
        return worldY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public TextureRegion getSprite(){
        if(state == ACTOR_STATE.WALKING){
            return animationSet.getWalking(facing).getKeyFrame(walkTimer);
        }else if(state == ACTOR_STATE.STANDING){
            return animationSet.getStanding(facing);
        }
        return animationSet.getStanding(DIRECTION.SOUTH);
    }
}
