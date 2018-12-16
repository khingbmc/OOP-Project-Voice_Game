package com.vaja.game.battle;

import java.io.IOException;

import java.net.URL;

import com.vaja.game.battle.animation.FaintingAnimation;
import com.vaja.game.battle.animation.StartBattleAnimation;
import com.vaja.game.battle.event.*;
import com.vaja.game.battle.move.Move;
import com.vaja.game.model.Monster;
import com.vaja.voice.SpeechRecognize;

import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import edu.cmu.sphinx.util.props.PropertyException;

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
    private SpeechRecognize speech;
    
    private String resultText;
	private Recognizer recognizer;
	private Microphone microphone;
	private String[] message;

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
    
    public void recognizer() {
    	this.message = new String[2];
    	try {
            URL url;
            
            url = SpeechRecognize.class.getResource("helloworld.config.xml");
           

            System.out.println();
            queueEvent(new TextEvent("Loading...", 0.5f));
            
        	

            ConfigurationManager cm = new ConfigurationManager(url);

            recognizer = (Recognizer) cm.lookup("recognizer");
            microphone = (Microphone) cm.lookup("microphone");


            /* allocate the resource necessary for the recognizer */
            recognizer.allocate();

            /* the microphone will keep recording until the program exits */
	    if (microphone.startRecording()) {

		this.message[0] = "Start speaking.(dog | love | ant | sexy)";
		queueEvent(new TextEvent(getMessage()[0], 0.5f));

		
		this.message[1] = "Waiting....";
		queueEvent(new TextEvent(getMessage()[1], 0.5f));
		

                    
		    Result result = recognizer.recognize();
		    
		    if (result != null) {
		    	
			resultText = result.getBestFinalResultNoFiller();
			System.out.println(resultText);
			
		    } else {
			System.out.println("I can't hear what you said.\n");
		    }
		    queueEvent(new TextEvent("You Said: "+getResultText(), 0.5f));
		
	    } else {
		System.out.println("Cannot start microphone.");
		recognizer.deallocate();
		System.exit(1);
	    }
        } catch (IOException e) {
            System.err.println("Problem when loading HelloWorld: " + e);
            e.printStackTrace();
        } catch (PropertyException e) {
            System.err.println("Problem configuring HelloWorld: " + e);
            e.printStackTrace();
        } catch (InstantiationException e) {
            System.err.println("Problem creating HelloWorld: " + e);
            e.printStackTrace();
        }
    }
    
    public String getResultText() {
		return resultText;
	}



	public String[] getMessage() {
		return message;
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
        queueEvent(new TextEvent(" "+monster.getName()+" สุดเท่ไงล่ะ!"));
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
    	int num = 0;
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
        
        
        
        //        mechanics.attemptHit(move, battleUser, monsTarget)
        
        if (battleUser.getName().equals("Brian")) {
        	recognizer();
        	
            if(getResultText().equals("dog") || getResultText().equals("ant")) {
            	num++;
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
            if (!anyoneAlive) {
            	queueEvent(new TextEvent("You're Die ", true));
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
