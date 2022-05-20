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
    private Dolphin dolphin;
    private Sprite background, blur, staticDolphin, food, toxicFood, pop1, pop2, pop3, pop4, pop5;

    public DolphinLevel(GameLauncher game) {
        super(game);
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
        dolphin = new Dolphin();

        setPlayer(dolphin);

        player.setSwimming(true);
        this.background = new Sprite(AssetHandler.assetManager.get(AssetHandler.waterWithSand, Texture.class));
        this.blur = new Sprite(AssetHandler.assetManager.get(AssetHandler.blur, Texture.class));
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

        this.pop1 = new Sprite(AssetHandler.assetManager.get(AssetHandler.dolphinFoodPop, Texture.class));
        this.pop2 = new Sprite(AssetHandler.assetManager.get(AssetHandler.dolphinHabitatPop, Texture.class));
        this.pop3 = new Sprite(AssetHandler.assetManager.get(AssetHandler.dolphinLifePop, Texture.class));
        this.pop4 = new Sprite(AssetHandler.assetManager.get(AssetHandler.dolphinSocialPop, Texture.class));
        this.pop5 = new Sprite(AssetHandler.assetManager.get(AssetHandler.dolphinThreatsPop, Texture.class));
        seenPopUps = new boolean[][]{new boolean[]{false, false, false, false, false}, new boolean[]{false, false, false, false, false}};
        popUps = new ArrayList<>(Arrays.asList(pop2, pop1, pop3, pop4, pop5)); //order maters
        popUpLocations = new Vector2[]{new Vector2(1000, 200), new Vector2(3000, 200), new Vector2(5000, 200), new Vector2(getWorldSize()-spacing-(pop5.getWidth()/2), getEndGoal().getPosition().y+200), new Vector2(9000, 200)};//slect location

        backgroundDolphins = new ArrayList<>();
        for (int i = 0; i < 6; i++){
            backgroundDolphins.add(new Random().nextInt(500));
        }

        addPrey(2, generateObstacles(2), preyWidth, preyHeight, false);
        addPrey(3, generateObstacles(2), preyWidth, preyHeight, false);

    }

    @Override
    public void render(float delta) {
        Render();
        Gdx.gl.glClearColor(0.8f, 0.9f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        renderBackground(background, -1 * getOceanDepth() - 50);
        seenPopUps = renderPopUps(seenPopUps, popUpLocations, popUps);
        renderPrey2D(food, toxicFood);
        renderBoat();
        renderOil();
        renderTrash();
        renderEndGoal2D(staticDolphin);
        staticDolphin.flip(true,false);
        renderCustom(staticDolphin,getWorldSize()-backgroundDolphins.get(0),preySpawnHeight + 200, staticDolphin.getWidth()/3,staticDolphin.getHeight()/3);
        renderCustom(staticDolphin,getWorldSize()-backgroundDolphins.get(1),preySpawnHeight + 100, staticDolphin.getWidth()/2,staticDolphin.getHeight()/2);
        renderCustom(staticDolphin,getWorldSize()-backgroundDolphins.get(2),preySpawnHeight + 000, staticDolphin.getWidth()/2,staticDolphin.getHeight()/2);
        renderCustom(staticDolphin,getWorldSize()-backgroundDolphins.get(3),preySpawnHeight - 300, staticDolphin.getWidth()/1.5f,staticDolphin.getHeight()/1.5f);
        renderCustom(staticDolphin,getWorldSize()-backgroundDolphins.get(4),preySpawnHeight - 400, staticDolphin.getWidth()/2,staticDolphin.getHeight()/2);
        renderCustom(staticDolphin,getWorldSize()-backgroundDolphins.get(5),preySpawnHeight - 500, staticDolphin.getWidth()/1.3f,staticDolphin.getHeight()/1.3f);
        staticDolphin.flip(true,false);
        renderPlayer2D();
        renderBackground(blur, -1 * getOceanDepth() - 50);
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
                "Dolphins mainly suffer from what due to coastal development and pollution?",
                "Habitat loss",
                new ArrayList<>(Arrays.asList("Habitat loss", "Overfeeding", "Disease", "Reproductive failure"))
        ));
        questionBank.put(4, new Question(
                "Dolphins travel in groups composed of juveniles, nursery groups, and ...?",
                "Strongly bonded adult males",
                new ArrayList<>(Arrays.asList("Strongly bonded adult males", "Elderly dolphins", "Small fish", "Protective sharks"))
        ));
        questionBank.put(5, new Question(
                "What can you do to help the bottlenose dolphins keep their habitats?",
                "TBA (Correct)",
                new ArrayList<>(Arrays.asList("TBA (Correct)", "TBA", "TBA", "TBA"))
        ));

        return questionBank;
    }

}
