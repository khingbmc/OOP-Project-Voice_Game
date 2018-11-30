package com.vaja.game.battle;

import com.vaja.game.battle.animation.FaintingAnimation;
import com.vaja.game.battle.animation.StartBattleAnimation;
import com.vaja.game.battle.event.*;
import com.vaja.game.battle.move.Move;
import com.vaja.game.model.Monster;
import com.vaja.voice.SpeechRecognize;

/**
 * i used template pokemon fight
 * haha it look better than any template
 * @author khingbmc
 */
public class Battle implements BattleEventQueuer {




    public enum STATE {
        READY_TO_PROGRESS,
        SELECT_NEW_POKEMON,
        RAN,
        WIN,
        LOSE,
        ;
    }

    @Override
    public void queueEvent(BattleEvent event) {
        this.eventPlayer.queueEvent(event);
    }

    private STATE state;

    private BattleMechanics mechanics;

    private Monster player;
    private Monster opponent;

    private Trainer playerTrainer;
    private Trainer opponentTrainer;

    private BattleEventPlayer eventPlayer;

    public Battle(Trainer player, Monster opponent) {
        this.playerTrainer = player;
        this.player = player.getPokemon(0);
        this.opponent = opponent;
        mechanics = new BattleMechanics();
        this.state = STATE.READY_TO_PROGRESS;
    }

    /**
     * Plays appropiate animation for starting a battle
     */
    public void beginBattle() {
        queueEvent(new MonsterSpriteEvent(opponent.getSprite(), BATTLE_PARTY.OPPONENT));
        queueEvent(new TextEvent("I'm "+player.getName()+"!!!", 1f));
        queueEvent(new MonsterSpriteEvent(player.getSprite(), BATTLE_PARTY.PLAYER));
        queueEvent(new AnimationBattleEvent(BATTLE_PARTY.PLAYER, new StartBattleAnimation()));
    }

    public void setState(STATE state) {
        this.state = state;
    }

    /**
     * Progress the battle one turn.
     * @param input		Index of the move used by the player
     */
    public void progress(int input) {
        if (state != STATE.READY_TO_PROGRESS) {
            return;
        }
        if (mechanics.goesFirst(player, opponent)) {
            playTurn(BATTLE_PARTY.PLAYER, input);
            if (state == STATE.READY_TO_PROGRESS) {
                playTurn(BATTLE_PARTY.OPPONENT, 0);
            }
        } else {
            playTurn(BATTLE_PARTY.OPPONENT, 0);
            if (state == STATE.READY_TO_PROGRESS) {
                playTurn(BATTLE_PARTY.PLAYER, input);
            }
        }
        /*
         * XXX: Status effects go here.
         */
    }

    /**
     * Sends out a new Pokemon, in the case that the old one fainted.
     * This will NOT take up a turn.
     * @param monster	Pokemon the trainer is sending in
     */
    public void chooseMons(Monster monster) {
        this.player = monster;
        queueEvent(new HPAnimationEvent(
                BATTLE_PARTY.PLAYER,
                monster.getCurrentHitpoints(),
                monster.getCurrentHitpoints(),
                monster.getStat(STAT.HITPOINTS),
                0f));
        queueEvent(new MonsterSpriteEvent(monster.getSprite(), BATTLE_PARTY.PLAYER));
        queueEvent(new NameChangeEvent(monster.getName(), BATTLE_PARTY.PLAYER));
        queueEvent(new TextEvent("ชื่อของฉันคือ "+monster.getName()+" สุดเท่ไงล่ะ!"));
        queueEvent(new AnimationBattleEvent(BATTLE_PARTY.PLAYER, new StartBattleAnimation()));
        this.state = STATE.READY_TO_PROGRESS;
    }

    /**
     * Attempts to run away
     */
    public void attemptRun() {
        queueEvent(new TextEvent("Got away successfully...", true));
        this.state = STATE.RAN;
    }

    private void playTurn(BATTLE_PARTY user, int input) {
        BATTLE_PARTY target = BATTLE_PARTY.getOpposite(user);

        Monster battleUser = null;
        Monster monsTarget = null;
        if (user == BATTLE_PARTY.PLAYER) {
            battleUser = player;
            monsTarget = opponent;
        } else if (user == BATTLE_PARTY.OPPONENT) {
            battleUser = opponent;
            monsTarget = player;
        }

        Move move = battleUser.getMove(input);

        /* Broadcast the text graphics */
        queueEvent(new TextEvent(battleUser.getName()+" used\n"+move.getName().toUpperCase()+"!", 0.5f));
        
        SpeechRecognize speech = new SpeechRecognize();
//        mechanics.attemptHit(move, battleUser, monsTarget)
        
        if (battleUser.getName().equals("Brian")) {
        	
        	queueEvent(new TextEvent(speech.getMessage()[1], 0.5f));
        	queueEvent(new TextEvent("You Said:"+speech.getResultText(), 0.5f));
            if(speech.getResultText().equals("dog") || speech.getResultText().equals("ant")) {
            	
            	move.useMove(mechanics, battleUser, monsTarget, user, this);
            }else { // miss
                /* Broadcast the text graphics */
                queueEvent(new TextEvent(battleUser.getName()+"'s\nattack missed!", 0.5f));
            }
        } else {
        	if(mechanics.attemptHit(move, battleUser, monsTarget)) {
            	move.useMove(mechanics, battleUser, monsTarget, user, this);
            }else { // miss
                /* Broadcast the text graphics */
                queueEvent(new TextEvent(battleUser.getName()+"'s\nattack missed!", 0.5f));
            }
        }

        if (player.isFainted()) {
            queueEvent(new AnimationBattleEvent(BATTLE_PARTY.PLAYER, new FaintingAnimation()));
            boolean anyoneAlive = false;
            for (int i = 0; i < getPlayerTrainer().getTeamSize(); i++) {
                if (!getPlayerTrainer().getPokemon(i).isFainted()) {
                    anyoneAlive = true;
                    break;
                }
            }
            if (anyoneAlive) {
                queueEvent(new TextEvent(player.getName()+" fainted!", true));
                this.state = STATE.SELECT_NEW_POKEMON;
            } else {
                queueEvent(new TextEvent("Unfortunately, you've lost...", true));
                this.state = STATE.LOSE;
            }
        } else if (opponent.isFainted()) {
            queueEvent(new AnimationBattleEvent(BATTLE_PARTY.OPPONENT, new FaintingAnimation()));
            queueEvent(new TextEvent("Congratulations! You Win!", true));
            this.state = STATE.WIN;
        }
    }

    public Monster getPlayerPokemon() {
        return player;
    }

    public Monster getOpponentPokemon() {
        return opponent;
    }

    public Trainer getPlayerTrainer() {
        return playerTrainer;
    }

    public Trainer getOpponentTrainer() {
        return opponentTrainer;
    }

    public STATE getState() {
        return state;
    }

    public void setEventPlayer(BattleEventPlayer player) {
        this.eventPlayer = player;
    }


}
