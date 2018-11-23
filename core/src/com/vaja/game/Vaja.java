package com.vaja.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.vaja.screen.GameScreen;

public class Vaja extends Game {

    private GameScreen gameScreen;
    private BattleScreen battleScreen;
    private TransitionScreen transitionScreen;

    private MoveDatabase moveDatabase;

    private AssetManager assetManager;

    private Skin skin;

    private TweenManager tweenManager;

    private ShaderProgram overlayShader;
    private ShaderProgram transitionShader;

    private String version;

    @Override
    public void create() {
        /*
         * LOAD VERSION
         */
        version = Gdx.files.internal("version.txt").readString();
        System.out.println("Pokemon by Hydrozoa, version "+version);
        Gdx.app.getGraphics().setTitle("Pokemon by Hydrozoa, version "+version);

        /*
         * LOADING SHADERS
         */
        ShaderProgram.pedantic = false;
        overlayShader = new ShaderProgram(
                Gdx.files.internal("res/shaders/overlay/vertexshader.txt"),
                Gdx.files.internal("res/shaders/overlay/fragmentshader.txt"));
        if (!overlayShader.isCompiled()) {
            System.out.println(overlayShader.getLog());
        }
        transitionShader = new ShaderProgram(
                Gdx.files.internal("res/shaders/transition/vertexshader.txt"),
                Gdx.files.internal("res/shaders/transition/fragmentshader.txt"));
        if (!transitionShader.isCompiled()) {
            System.out.println(transitionShader.getLog());
        }

        /*
         * SETTING UP TWEENING
         */
        tweenManager = new TweenManager();
        Tween.registerAccessor(BattleAnimation.class, new BattleAnimationAccessor());
        Tween.registerAccessor(BattleSprite.class, new BattleSpriteAccessor());
        Tween.registerAccessor(AnimatedBattleSprite.class, new BattleSpriteAccessor());
        Tween.registerAccessor(BattleBlinkTransition.class, new BattleBlinkTransitionAccessor());

        /*
         * LOADING ASSETS
         */
        assetManager = new AssetManager();
        assetManager.setLoader(LWorldObjectDb.class, new LWorldObjectLoader(new InternalFileHandleResolver()));
        assetManager.setLoader(LTerrainDb.class, new LTerrainLoader(new InternalFileHandleResolver()));
        assetManager.setLoader(World.class, new WorldLoader(new InternalFileHandleResolver()));

        assetManager.load("res/LTerrain.xml", LTerrainDb.class);
        assetManager.load("res/LWorldObjects.xml", LWorldObjectDb.class);

        assetManager.load("res/graphics_packed/tiles/tilepack.atlas", TextureAtlas.class);
        assetManager.load("res/graphics_packed/ui/uipack.atlas", TextureAtlas.class);
        assetManager.load("res/graphics_packed/battle/battlepack.atlas", TextureAtlas.class);
        assetManager.load("res/graphics/pokemon/bulbasaur.png", Texture.class);
        assetManager.load("res/graphics/pokemon/slowpoke.png", Texture.class);

        for (int i = 0; i < 32; i++) {
            assetManager.load("res/graphics/statuseffect/attack_"+i+".png", Texture.class);
        }
        assetManager.load("res/graphics/statuseffect/white.png", Texture.class);

        for (int i = 0; i < 13; i++) {
            assetManager.load("res/graphics/transitions/transition_"+i+".png", Texture.class);
        }
        assetManager.load("res/font/small_letters_font.fnt", BitmapFont.class);

        File dir = new File("res/worlds/");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                System.out.println("Loading world "+child.getPath());
                assetManager.load(child.getPath(), World.class);
            }
        }

        assetManager.finishLoading();

        skin = SkinGenerator.generateSkin(assetManager);

        moveDatabase = new MoveDatabase();

        gameScreen = new GameScreen(this);
        battleScreen = new BattleScreen(this);
        transitionScreen = new TransitionScreen(this);

        this.setScreen(gameScreen);
    }

    @Override
    public void render() {
        //System.out.println(Gdx.graphics.getFramesPerSecond());

        /* UPDATE */
        tweenManager.update(Gdx.graphics.getDeltaTime());
        if (getScreen() instanceof AbstractScreen) {
            ((AbstractScreen)getScreen()).update(Gdx.graphics.getDeltaTime());
        }

        /* RENDER */
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        getScreen().render(Gdx.graphics.getDeltaTime());
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public Skin getSkin() {
        return skin;
    }

    public TweenManager getTweenManager() {
        return tweenManager;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public BattleScreen getBattleScreen() {
        return battleScreen;
    }

    public void startTransition(AbstractScreen from, AbstractScreen to, Transition out, Transition in, Action action) {
        transitionScreen.startTransition(from, to, out, in, action);
    }

    public ShaderProgram getOverlayShader() {
        return overlayShader;
    }

    public ShaderProgram getTransitionShader() {
        return transitionShader;
    }

    public MoveDatabase getMoveDatabase() {
        return moveDatabase;
    }

    public String getVersion() {
        return version;
    }
}
