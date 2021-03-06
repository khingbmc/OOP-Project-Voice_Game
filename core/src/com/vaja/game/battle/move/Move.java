package com.vaja.game.battle.move;

import com.vaja.game.Vaja;

import com.vaja.game.battle.BATTLE_PARTY;
import com.vaja.game.battle.BattleMechanics;
import com.vaja.game.battle.animation.BattleAnimation;
import com.vaja.game.battle.event.BattleEventQueuer;
import com.vaja.game.model.Monster;

/**
 * it a monster move in battle screen
 */

public abstract class Move {
    protected MoveSpecification spec;
    protected Class<? extends BattleAnimation> animationClass;

    public Move(MoveSpecification spec, Class<? extends BattleAnimation> animationClass) {
        this.spec = spec;
        this.animationClass = animationClass;
    }

    public int useMove(BattleMechanics mechanics, Monster user, Monster target, BATTLE_PARTY party, BattleEventQueuer broadcaster) {
        int damage = mechanics.calculateDamage(this, user, target);
        target.applyDamage(damage);
        return damage;
    }

    public abstract BattleAnimation animation();

    public abstract String message();

    /**
     * @return If this move deals damage
     */
    public abstract boolean isDamaging();

    public String getName() {
        return spec.getName();
    }

    public MOVE_TYPE getType(){
        return spec.getType();
    }

    public MOVE_CATEGORY getCategory() {
        return spec.getCategory();
    }

    public int getPower() {
        return spec.getPower();
    }

    public float getAccuracy() {
        return spec.getAccuracy();
    }

    public MoveSpecification getMoveSpecification() {
        return spec;
    }

    /**
     * @return A copy of this instance.
     */
    public abstract Move clone();
}
