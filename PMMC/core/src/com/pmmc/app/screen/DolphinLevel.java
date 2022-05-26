package com.pmmc.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.pmmc.app.AssetHandler;
import com.pmmc.app.GameLauncher;
import com.pmmc.app.character.Dolphin;
import com.pmmc.app.screen.quiz.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class DolphinLevel extends Level {


    /**
     * PolarBearLevel: The first level presented to the user
     */
    boolean[][] seenPopUps;
    ArrayList<Sprite> popUps;
    ArrayList<Integer> backgroundDolphins;
    private Vector2[] popUpLocations;
    private Sprite background, backdrop;
    private Sprite blur;
    private Sprite staticDolphin;
    private Sprite food;
    private Sprite toxicFood;

    public DolphinLevel(GameLauncher game) {
        super(game, new ArrayList<>(Arrays.asList(
                Gdx.audio.newSound(Gdx.files.internal("sounds/dolphin_1.wav")),
                Gdx.audio.newSound(Gdx.files.internal("sounds/dolphin_2.wav")),
                Gdx.audio.newSound(Gdx.files.internal("sounds/dolphin_3.wav"))
        )));
        preySpawnHeight = -1000;
        preyDespawnable = false;
        preySpeed = 20;
        waterPrey = true;
        setWaterWorld(true);
        setWorldSize(30000);
        setOceanDepth(2000);
        setSpacing(600);
        setMinNumPrey(5);
        setBoatStrike(true);
        setOilSpill(true);
        setTrash(true);
        setBoatYAxis(-250);
    }

    @Override
    public void show() {
        Dolphin dolphin = new Dolphin();

        setPlayer(dolphin);

        player.setSwimming(true);
        this.background = new Sprite(AssetHandler.assetManager.get(AssetHandler.waterWithSand, Texture.class));
        this.blur = new Sprite(AssetHandler.assetManager.get(AssetHandler.blur, Texture.class));
        this.backdrop = new Sprite(AssetHandler.assetManager.get(AssetHandler.skyBackground4, Texture.class));

        this.staticDolphin = new Sprite(AssetHandler.assetManager.get(AssetHandler.dolphinSprite, Texture.class));
        staticDolphin.flip(true, false);
        setEndGoal(staticDolphin, preySpawnHeight-100);

        this.food = new Sprite(AssetHandler.assetManager.get(AssetHandler.herring, Texture.class));
        this.toxicFood = new Sprite(AssetHandler.assetManager.get(AssetHandler.herring, Texture.class));

        preyWidth = (int) food.getWidth() /4;
        preyHeight = (int) food.getHeight() /4;

        setBoatModel(new Sprite(AssetHandler.assetManager.get(AssetHandler.smallBoatFishingLine, Texture.class)));
        setOilSprite(new Sprite(AssetHandler.assetManager.get(AssetHandler.oilSpill, Texture.class)));
        setTrashSprite(new Sprite(AssetHandler.assetManager.get(AssetHandler.trashBag, Texture.class)));

        Sprite pop1 = new Sprite(AssetHandler.assetManager.get(AssetHandler.dolphinFoodPop, Texture.class));
        Sprite pop2 = new Sprite(AssetHandler.assetManager.get(AssetHandler.dolphinHabitatPop, Texture.class));
        Sprite pop3 = new Sprite(AssetHandler.assetManager.get(AssetHandler.dolphinLifePop, Texture.class));
        Sprite pop4 = new Sprite(AssetHandler.assetManager.get(AssetHandler.dolphinSocialPop, Texture.class));
        Sprite pop5 = new Sprite(AssetHandler.assetManager.get(AssetHandler.dolphinThreatsPop, Texture.class));
        seenPopUps = new boolean[][]{new boolean[]{false, false, false, false, false}, new boolean[]{false, false, false, false, false}};
        popUps = new ArrayList<>(Arrays.asList(pop1, pop2, pop3, pop4, pop5)); //order maters
        popUpLocations = new Vector2[]{new Vector2((spacing* 7.5f)-(pop1.getWidth()/2), preySpawnHeight+200), new Vector2(spacing, 0), new Vector2(1000, 0), new Vector2(getWorldSize()-spacing, (getEndGoal().getPosition().y * PPM )+100), new Vector2(2000, 0)};//slect location

        backgroundDolphins = new ArrayList<>();
        for (int i = 0; i < 6; i++){
            backgroundDolphins.add(new Random().nextInt(500));
        }

        addPrey(2, generateObstacles(2), preyWidth, preyHeight, false);
        addPrey(3, generateObstacles(3), preyWidth, preyHeight, false);

    }

    @Override
    public void render(float delta) {
        Render();
        Gdx.gl.glClearColor(0.8f, 0.9f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        renderBackground(backdrop,-600,1);
        renderBackground(background, -1 * getOceanDepth() - 50,1);
        seenPopUps = renderPopUps(seenPopUps, popUpLocations, popUps);
        renderPrey2D(food, toxicFood);
        renderBoat();
        renderOil();
        renderTrash();
        renderEndGoal2D(staticDolphin);
        staticDolphin.flip(true,false);
        renderCustom(staticDolphin,getWorldSize()-backgroundDolphins.get(0),preySpawnHeight + 200, staticDolphin.getWidth(),staticDolphin.getHeight(),.33f);
        renderCustom(staticDolphin,getWorldSize()-backgroundDolphins.get(1),preySpawnHeight + 100, staticDolphin.getWidth(),staticDolphin.getHeight(),.5f);
        renderCustom(staticDolphin,getWorldSize()-backgroundDolphins.get(2),preySpawnHeight + 000, staticDolphin.getWidth(),staticDolphin.getHeight(),.5f);
        renderCustom(staticDolphin,getWorldSize()-backgroundDolphins.get(3),preySpawnHeight - 300, staticDolphin.getWidth(),staticDolphin.getHeight(),.66f);
        renderCustom(staticDolphin,getWorldSize()-backgroundDolphins.get(4),preySpawnHeight - 400, staticDolphin.getWidth(),staticDolphin.getHeight(),.5f);
        renderCustom(staticDolphin,getWorldSize()-backgroundDolphins.get(5),preySpawnHeight - 500, staticDolphin.getWidth(),staticDolphin.getHeight(),.77f);
        staticDolphin.flip(true,false);
        renderPlayer2D();
        renderBackground(blur, -1 * getOceanDepth() - 50,1);
        renderHealthBars();
        game.batch.end();
//        renderBackground(blur);
    }

    public HashMap<Integer, Question> generateQuestionBank() {
        HashMap<Integer, Question> questionBank = new HashMap<>();
        questionBank.put(1, new Question(
                "Bottlenose dolphins can be found in ...?",
                "All answers are correct",
                new ArrayList<>(Arrays.asList("All answers are correct", "Southern California", "Temperate waters", "Tropical waters"))
        ));
        questionBank.put(2, new Question(
                "All of these are part of the bottlenose dolphin diet except for which?",
                "Seaweed",
                new ArrayList<>(Arrays.asList("Seaweed", "Herring", "Shrimp", "Squid"))
        ));
        questionBank.put(3, new Question(
                "Dolphins mainly suffer from what due to coastal development?",
                "Habitat loss",
                new ArrayList<>(Arrays.asList("Habitat loss", "Overfeeding", "Disease", "Reproductive failure"))
        ));
        questionBank.put(4, new Question(
                "What is making the sea life, including dolphins, very sick?",
                "Chemical pollution and plastics",
                new ArrayList<>(Arrays.asList("Chemical pollution and plastics", "Climate change", "Harmful algal blooms", "Fertilizers"))
        ));
        questionBank.put(5, new Question(
                "What can you do to help the bottlenose dolphins from getting sick?",
                "Use environmentally safe products",
                new ArrayList<>(Arrays.asList("Use environmentally safe products", "Carpool, bike, or walk", "Avoid using fertilizers", "Use the Seafood Watch app"))
        ));

        return questionBank;
    }

}
