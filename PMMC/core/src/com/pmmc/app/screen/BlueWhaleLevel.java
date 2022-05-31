package com.pmmc.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.pmmc.app.AssetHandler;
import com.pmmc.app.GameLauncher;
import com.pmmc.app.character.BlueWhale;
import com.pmmc.app.screen.quiz.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BlueWhaleLevel extends Level {


    /**
     * PolarBearLevel: The first level presented to the user
     */

    boolean[][] seenPopUps;
    ArrayList<Sprite> popUps;
    private Vector2[] popUpLocations;
    private Sprite background, blur, staticWhale, staticSunfish, staticSwordfish, staticShark, food, toxicFood;
    private Sprite backdrop;

    public BlueWhaleLevel(GameLauncher game) {
        super(game, "sounds/tropical_music.mp3", new ArrayList<>(Arrays.asList(
                Gdx.audio.newSound(Gdx.files.internal("sounds/blue_whale_1.mp3")),
                Gdx.audio.newSound(Gdx.files.internal("sounds/blue_whale_2.mp3"))
        )));
        preySpawnHeight = -1000;
        preyDespawnable = false;
        preySpeed = 10;
        waterPrey = true;
        setWaterWorld(true);
        setWorldSize(24000);
        setOceanDepth(3000);
        setSpacing(400);
        setMinNumPrey(80);
        setBoatStrike(true);
        setOilSpill(true);
        setBoatYAxis(-250);
        setBoatSpeed(50);
    }

    @Override
    public void show() {
        BlueWhale whale = new BlueWhale();
        setPlayer(whale);

        player.setSwimming(true);
        this.background = new Sprite(AssetHandler.assetManager.get(AssetHandler.whaleBackground, Texture.class));
        this.blur = new Sprite(AssetHandler.assetManager.get(AssetHandler.blur, Texture.class));
        this.backdrop = new Sprite(AssetHandler.assetManager.get(AssetHandler.skyBackground3, Texture.class));

        this.staticWhale = new Sprite(AssetHandler.assetManager.get(AssetHandler.blueWhaleSprite, Texture.class));
        this.staticSunfish = new Sprite(AssetHandler.assetManager.get(AssetHandler.sunfishForBackground, Texture.class));
        this.staticSwordfish = new Sprite(AssetHandler.assetManager.get(AssetHandler.swordfishForBackground, Texture.class));
        this.staticShark = new Sprite(AssetHandler.assetManager.get(AssetHandler.shark, Texture.class));

        staticWhale.flip(true, false);
        setEndGoal(staticWhale, preySpawnHeight);

        this.food = new Sprite(AssetHandler.assetManager.get(AssetHandler.krill, Texture.class));
        this.toxicFood = new Sprite(AssetHandler.assetManager.get(AssetHandler.toxicKrill, Texture.class));

        preyWidth = (int) food.getWidth() /3;
        preyHeight = (int) food.getHeight() /3;

        setBoatModel(new Sprite(AssetHandler.assetManager.get(AssetHandler.smallBoatFishingLine, Texture.class)));
        setOilSprite(new Sprite(AssetHandler.assetManager.get(AssetHandler.oilSpill, Texture.class)));

        Sprite pop1 = new Sprite(AssetHandler.assetManager.get(AssetHandler.whaleFoodPop, Texture.class));
        Sprite pop2 = new Sprite(AssetHandler.assetManager.get(AssetHandler.whaleHabitatPop, Texture.class));
        Sprite pop3 = new Sprite(AssetHandler.assetManager.get(AssetHandler.whaleLifePop, Texture.class));
        Sprite pop4 = new Sprite(AssetHandler.assetManager.get(AssetHandler.whaleSocialPop, Texture.class));
        Sprite pop5 = new Sprite(AssetHandler.assetManager.get(AssetHandler.whaleThreatsPop, Texture.class));
        Sprite pop6 = new Sprite(AssetHandler.assetManager.get(AssetHandler.whaleHelpPop, Texture.class));
        seenPopUps = new boolean[][]{new boolean[]{false, false, false, false, false, false}, new boolean[]{false, false, false, false, false, false}};
        popUps = new ArrayList<>(Arrays.asList(pop1, pop2, pop3, pop4, pop5, pop6)); //order maters
        popUpLocations = new Vector2[]{new Vector2((spacing* 7.5f), preySpawnHeight), new Vector2(1000, 0), new Vector2(5000, preySpawnHeight), new Vector2(getWorldSize()-spacing-(pop5.getWidth()/2), getEndGoal().getPosition().y*PPM+100), new Vector2(9000,preySpawnHeight), new Vector2(getWorldSize()-(spacing*3), preySpawnHeight)};//slect location


        addPrey(2, generateObstacles(2), preyWidth, preyHeight, false);
        addPrey(3, generateObstacles(3), preyWidth, preyHeight, false);
    }

    @Override
    public void render(float delta) {
        Render();
        Gdx.gl.glClearColor(0.8f, 0.9f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        renderBackground(backdrop, -1000, 3);
        renderBackground(background, -1 * getOceanDepth() - 50,1);

        renderCustom(staticSunfish, moveImage(getRandomX(1)), -getRandomY(1),staticSunfish.getWidth(),staticSunfish.getHeight(),0.1f);
        renderCustom(staticShark, moveImage(getRandomX(2)), -getRandomY(2),staticShark.getWidth(),staticShark.getHeight(),.5f);
        renderCustom(staticSwordfish, moveImage(getRandomX(3)), -getRandomY(3),staticSwordfish.getWidth(),staticSwordfish.getHeight(),0.4f);

        staticSunfish.flip(true,false);
        staticShark.flip(true,false);
        staticSwordfish.flip(true,false);
        renderCustom(staticSunfish, moveImage(-getRandomX(4)), -getRandomY(4),staticSunfish.getWidth(),staticSunfish.getHeight(),0.1f);
        renderCustom(staticShark, moveImage(-getRandomX(5)), -getRandomY(5),staticShark.getWidth(),staticShark.getHeight(),.5f);
        renderCustom(staticSwordfish, moveImage(-getRandomX(6)), -getRandomY(6),staticSwordfish.getWidth(),staticSwordfish.getHeight(),0.4f);
        staticSunfish.flip(true,false);
        staticShark.flip(true,false);
        staticSwordfish.flip(true,false);

        renderCustom(staticSunfish, moveImage(getRandomX(7)), -getRandomY(7),staticSunfish.getWidth(),staticSunfish.getHeight(),0.1f);
        renderCustom(staticShark, moveImage(getRandomX(8)), -getRandomY(8),staticShark.getWidth(),staticShark.getHeight(),.5f);
        renderCustom(staticSwordfish, moveImage(getRandomX(9)), -getRandomY(9),staticSwordfish.getWidth(),staticSwordfish.getHeight(),0.4f);

        staticSunfish.flip(true,false);
        staticShark.flip(true,false);
        staticSwordfish.flip(true,false);
        renderCustom(staticSunfish, moveImage(-getRandomX(10)), -getRandomY(10),staticSunfish.getWidth(),staticSunfish.getHeight(),0.1f);
        renderCustom(staticShark, moveImage(-getRandomX(11)), -getRandomY(11),staticShark.getWidth(),staticShark.getHeight(),.5f);
        renderCustom(staticSwordfish, moveImage(-getRandomX(12)), -getRandomY(12),staticSwordfish.getWidth(),staticSwordfish.getHeight(),0.4f);
        staticSunfish.flip(true,false);
        staticShark.flip(true,false);
        staticSwordfish.flip(true,false);
        renderBackground(blur, -1 * getOceanDepth() - 50,1);


        seenPopUps = renderPopUps(seenPopUps, popUpLocations, popUps);
        renderPrey2D(food, toxicFood);
        renderBoat(1,0);
        renderOil();
        renderEndGoal2D(staticWhale);
        renderPlayer2D();
        renderBackground(blur, -1 * getOceanDepth() - 50,1);
        renderHealthBars();
        game.batch.end();
    }

    public HashMap<Integer, Question> generateQuestionBank() {
        HashMap<Integer, Question> questionBank = new HashMap<>();
        questionBank.put(1, new Question(
                "Blue whales live in the oceans of the ...",
                "All answers are correct",
                new ArrayList<>(Arrays.asList("All answers are correct", "North Atlantic", "North Pacific", "Southern Hemisphere"))
        ));
        questionBank.put(2, new Question(
                "Blue whales eat almost exclusively this kind of food.",
                "Krill",
                new ArrayList<>(Arrays.asList("Krill", "Shrimp", "Crabs", "All of these are correct"))
        ));
        questionBank.put(3, new Question(
                "How long do blue whales typically live?",
                "80-90+ years",
                new ArrayList<>(Arrays.asList("80-90+ years", "50-60 years", "60-70 years", "70-80 years"))
        ));
        questionBank.put(4, new Question(
                "Nowadays, blue whales are threatened by...",
                "All answers are correct",
                new ArrayList<>(Arrays.asList("All answers are correct", "Ship strikes", "Entanglement from fishing gear", "Ocean pollution"))
        ));
        questionBank.put(5, new Question(
                "In order to keep our blue whale population, what can you do to help?",
                "Eat sustainably caught fish",
                new ArrayList<>(Arrays.asList("Eat sustainably caught fish", "Avoid fertilizer", "Lessen algal bloom", "Use single-use plastic"))
        ));

        return questionBank;
    }

}
