package com.pmmc.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.pmmc.app.AssetHandler;
import com.pmmc.app.GameLauncher;
import com.pmmc.app.character.SeaLion;
import com.pmmc.app.screen.quiz.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SeaLionLevel extends Level {


    /**
     * PolarBearLevel: The first level presented to the user
     */

    boolean[][] seenPopUps;
    ArrayList<Sprite> choices1, popUps;
    private Vector2[] popUpLocations;
    private Sprite background;
    private Sprite blur;
    private Sprite backdrop;
    private Sprite sandArea;
    private Sprite staticSeaLion;
    private Sprite food;
    private Sprite toxicFood;
    private Sprite staticSunfish;
    private Sprite staticSwordfish;

    private int currentFrame = 0;

    public SeaLionLevel(GameLauncher game) {
        super(game, "sounds/tropical_music.mp3", new ArrayList<>(Arrays.asList(
                Gdx.audio.newSound(Gdx.files.internal("sounds/sea_lion_1.mp3")),
                Gdx.audio.newSound(Gdx.files.internal("sounds/sea_lion_2.mp3")),
                Gdx.audio.newSound(Gdx.files.internal("sounds/sea_lion_3.mp3"))
        )));
        preySpawnHeight = -500;
        preyDespawnable = false;
        preySpeed = 20;
        waterPrey = true;
        choices1 = new ArrayList<>();
        obstacles2 = generateObstacles(4);
        obstacles3 = generateObstacles(4);
        setWorldSize(28000);
        setOceanDepth(1200);
        setSpacing(400);
        setMinNumPrey(5);
        setBoatStrike(true);
        setBoatYAxis(0);
        setWaterWorld(false);
        setPredator(true);
        setBoatSpeed(50);
    }

    @Override
    public void show() {
        SeaLion seaLion = new SeaLion();
        setPlayer(seaLion);
        player.setSwimming(false);
        this.background = new Sprite(AssetHandler.assetManager.get(AssetHandler.waterWithSand, Texture.class));
        this.blur = new Sprite(AssetHandler.assetManager.get(AssetHandler.blur, Texture.class));
        this.backdrop = new Sprite(AssetHandler.assetManager.get(AssetHandler.seaLionBackground, Texture.class));

        this.staticSeaLion = new Sprite(AssetHandler.assetManager.get(AssetHandler.seaLionSprite, Texture.class));
        this.staticSunfish = new Sprite(AssetHandler.assetManager.get(AssetHandler.sunfishForBackground, Texture.class));
        this.staticSwordfish = new Sprite(AssetHandler.assetManager.get(AssetHandler.swordfishForBackground, Texture.class));
        staticSeaLion.flip(true, false);
        setEndGoal(staticSeaLion, 200);

        this.food = new Sprite(AssetHandler.assetManager.get(AssetHandler.squid2, TextureAtlas.class).findRegion("1"));
        this.toxicFood =  new Sprite(AssetHandler.assetManager.get(AssetHandler.toxicSquid2, TextureAtlas.class).findRegion("1"));

        preyWidth = (int) food.getWidth() /3;
        preyHeight = (int) food.getHeight() /3;

        this.sandArea = new Sprite(AssetHandler.assetManager.get(AssetHandler.sandArea, Texture.class));
        Sprite bouy = new Sprite(AssetHandler.assetManager.get(AssetHandler.bouy, Texture.class));

        setBoatModel(new Sprite(AssetHandler.assetManager.get(AssetHandler.smallBoat, Texture.class)));

        setPredatorSprite(new Sprite(AssetHandler.assetManager.get(AssetHandler.killerWhaleSprite, Texture.class)));
        setPredatorScale(2);

        Sprite pop1 = new Sprite(AssetHandler.assetManager.get(AssetHandler.lionFoodPop, Texture.class));
        Sprite pop2 = new Sprite(AssetHandler.assetManager.get(AssetHandler.lionHabitatPop, Texture.class));
        Sprite pop3 = new Sprite(AssetHandler.assetManager.get(AssetHandler.lionLifePop, Texture.class));
        Sprite pop4 = new Sprite(AssetHandler.assetManager.get(AssetHandler.lionSocialPop, Texture.class));
        Sprite pop5 = new Sprite(AssetHandler.assetManager.get(AssetHandler.lionThreatsPop, Texture.class));
        Sprite pop6 = new Sprite(AssetHandler.assetManager.get(AssetHandler.lionHelpPop, Texture.class));

        seenPopUps = new boolean[][]{new boolean[]{false, false, false, false, false, false}, new boolean[]{false, false, false, false, false, false}};

        popUps = new ArrayList<>(Arrays.asList(pop1, pop2, pop3, pop4, pop5, pop6)); //order maters
        popUpLocations = new Vector2[]{new Vector2((spacing* 7.5f), preySpawnHeight), new Vector2(800, 200), new Vector2(5000, preySpawnHeight), new Vector2(getWorldSize()-(800), (getEndGoal().getPosition().y* PPM)+100), new Vector2(9000, preySpawnHeight), new Vector2(getWorldSize()-(spacing*4), preySpawnHeight)};//slect location

        for (boolean ignored : obstacles2) {
            choices1.add(bouy);
        }
        placeBox2DObstacles(2, new Vector2[]{new Vector2(0, 0), new Vector2((spacing) / PPM, 0), new Vector2((spacing / 2f) / PPM, -5 / PPM)}, obstacles3, 10, false, "Bouy");
        placeBox2DObstacles(3, new Vector2[]{new Vector2(0, 0), new Vector2((spacing) / PPM, 0), new Vector2((spacing / 2f) / PPM, -5 / PPM)}, obstacles3, 10, false, "Bouy");


        addPrey(2, generateObstacles(2), preyWidth, preyHeight, false);
        addPrey(3, generateObstacles(3), preyWidth, preyHeight, false);

        makePolygonShapeBody(new Vector2[]{new Vector2(0, 0), new Vector2(600 / PPM, -10 / PPM), new Vector2(1850 / PPM, -1400 / PPM), new Vector2(0, -1000 / PPM)}, 0, 0, true, "Sand");
        makePolygonShapeBody(new Vector2[]{new Vector2(0, 0), new Vector2(0, -1000 / PPM), new Vector2(-(1850 / PPM), -1400 / PPM), new Vector2(-600 / PPM, -10 / PPM)}, getWorldSize(), 0, true, "Sand");

    }

    @Override
    public void render(float delta) {
        Render();
        currentFrame++;
        updateSprite(toxicFood, AssetHandler.assetManager.get(AssetHandler.toxicSquid2, TextureAtlas.class), currentFrame);
        currentFrame = updateSprite(food, AssetHandler.assetManager.get(AssetHandler.squid2, TextureAtlas.class), currentFrame);

        Gdx.gl.glClearColor(0.8f, 0.9f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        renderBackground(backdrop, 0, 5);
        renderBackground(background, -1 * getOceanDepth() - 50,1);

        renderCustom(staticSunfish, moveImage(getRandomX(51)), -getRandomY(1),staticSunfish.getWidth(),staticSunfish.getHeight(),0.3f);
        staticSunfish.flip(true,false);
        renderCustom(staticSunfish, moveImage(-getRandomX(52)), -getRandomY(2),staticSunfish.getWidth(),staticSunfish.getHeight(),0.3f);
        staticSunfish.flip(true,false);
        renderCustom(staticSwordfish, moveImage(getRandomX(53)), -getRandomY(3),staticSwordfish.getWidth(),staticSwordfish.getHeight(),0.5f);
        staticSwordfish.flip(true,false);
        renderCustom(staticSwordfish, moveImage(-getRandomX(54)), -getRandomY(4),staticSwordfish.getWidth(),staticSwordfish.getHeight(),0.5f);
        staticSwordfish.flip(true,false);
        renderCustom(staticSunfish, moveImage(getRandomX(55)), -getRandomY(5),staticSunfish.getWidth(),staticSunfish.getHeight(),0.3f);
        staticSunfish.flip(true,false);
        renderCustom(staticSunfish, moveImage(-getRandomX(56)), -getRandomY(6),staticSunfish.getWidth(),staticSunfish.getHeight(),0.3f);
        staticSunfish.flip(true,false);
        renderCustom(staticSwordfish, moveImage(getRandomX(57)), -getRandomY(7),staticSwordfish.getWidth(),staticSwordfish.getHeight(),0.5f);
        staticSwordfish.flip(true,false);
        renderCustom(staticSwordfish, moveImage(-getRandomX(58)), -getRandomY(8),staticSwordfish.getWidth(),staticSwordfish.getHeight(),0.5f);
        staticSwordfish.flip(true,false);
        renderCustom(staticSunfish, moveImage(getRandomX(59)), -getRandomY(9),staticSunfish.getWidth(),staticSunfish.getHeight(),0.3f);
        staticSunfish.flip(true,false);
        renderCustom(staticSunfish, moveImage(-getRandomX(60)), -getRandomY(10),staticSunfish.getWidth(),staticSunfish.getHeight(),0.3f);
        staticSunfish.flip(true,false);
        renderCustom(staticSwordfish, moveImage(getRandomX(61)), -getRandomY(11),staticSwordfish.getWidth(),staticSwordfish.getHeight(),0.5f);
        staticSwordfish.flip(true,false);
        renderCustom(staticSwordfish, moveImage(-getRandomX(62)), -getRandomY(12),staticSwordfish.getWidth(),staticSwordfish.getHeight(),0.5f);
        staticSwordfish.flip(true,false);

        renderBackground(blur, -1 * getOceanDepth() - 50,1);


        renderCustom(sandArea, 0, -1 * sandArea.getHeight() - 550, sandArea.getWidth(), sandArea.getHeight() + 700,1);
        sandArea.flip(true, false);
        renderCustom(sandArea, (int) (getWorldSize() - sandArea.getWidth()), -1 * sandArea.getHeight() - 550, sandArea.getWidth(), sandArea.getHeight() + 700,1);
        sandArea.flip(true, false);
        renderObstacles(2, choices1, obstacles3, 10, 700, false);
        renderObstacles(3, choices1, obstacles3, 10, 700, false);
        renderPrey2D(food, toxicFood);
        renderBoat(1,0);
        renderPredator();
        seenPopUps = renderPopUps(seenPopUps, popUpLocations, popUps);

        for (int i =0 ; i< 20;i++){
            renderCustom(new Sprite(AssetHandler.assetManager.get(AssetHandler.kelpForBackground, Texture.class)), getRandomX(i), -getOceanDepth()-50,474,342,1);
        }

        renderEndGoal2D(staticSeaLion);
        renderPlayer2D();
        renderBackground(blur, -1 * getOceanDepth() - 50,1);
        renderHealthBars();
        game.batch.end();
    }

    public HashMap<Integer, Question> generateQuestionBank() {
        HashMap<Integer, Question> questionBank = new HashMap<>();
        questionBank.put(1, new Question(
                "Where can California Sea Lions NOT be found living?",
                "Tropical lakes",
                new ArrayList<>(Arrays.asList("Tropical lakes", "Sandy beaches", "Temperate beaches", "Rock platforms"))
        ));
        questionBank.put(2, new Question(
                "California Sea Lions typically live from ...?",
                "20-30 years old",
                new ArrayList<>(Arrays.asList("20-30 years old", "15-20 years old", "15-25 years old", "20-25 years old"))
        ));
        questionBank.put(3, new Question(
                "California Sea Lions commonly suffer from what, due to human impacts?",
                "Malnourishment",
                new ArrayList<>(Arrays.asList("Malnourishment", "Loss of hearing", "Blindness", "Reproductive failure"))
        ));
        questionBank.put(4, new Question(
                "What product made out of oil causes cancer, and is often mistaken as food?",
                "Plastic",
                new ArrayList<>(Arrays.asList("Plastic", "Algae", "Metal debris", "Fishing gear"))
        ));
        questionBank.put(5, new Question(
                "What can you do to prevent California Sea Lions from getting sick?",
                "Avoid single-use plastics",
                new ArrayList<>(Arrays.asList("Avoid single-use plastics", "Carpool, ride your bike, or walk", "Eat organic food", "None of these"))
        ));

        return questionBank;
    }
}
