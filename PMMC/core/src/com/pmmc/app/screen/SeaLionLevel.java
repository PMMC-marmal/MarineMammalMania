package com.pmmc.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
    boolean[] obstacles2, obstacles3;
    ArrayList<Sprite> choices1, popUps;
    private Vector2[] popUpLocations;
    private SeaLion seaLion;
    private Sprite background, blur, pop1, pop2, pop3, pop4, pop5, sandArea, staticSeaLion, bouy, food, toxicFood;

    public SeaLionLevel(GameLauncher game) {
        super(game);
        preySpawnHeight = -500;
        preyDespawnable = false;
        preySpeed = 20;
        waterPrey = true;
        choices1 = new ArrayList<>();
        obstacles2 = generateObstacles(2);
        obstacles3 = generateObstacles(3);
        setWorldSize(24000);
        setOceanDepth(1000);
        setSpacing(400);
        setBoatStrike(true);
        setBoatYAxis(0);
        setWaterWorld(false);
    }

    @Override
    public void show() {
        seaLion = new SeaLion();
        setPlayer(seaLion);
        player.setSwimming(false);
        this.background = new Sprite(AssetHandler.assetManager.get(AssetHandler.waterWithSand, Texture.class));
        this.blur = new Sprite(AssetHandler.assetManager.get(AssetHandler.blur, Texture.class));
        this.staticSeaLion = new Sprite(AssetHandler.assetManager.get(AssetHandler.seaLionSprite, Texture.class));
        staticSeaLion.flip(true, false);
        setEndGoal(staticSeaLion, 200);

        this.food = new Sprite(AssetHandler.assetManager.get(AssetHandler.squid, Texture.class));
        this.toxicFood = new Sprite(AssetHandler.assetManager.get(AssetHandler.toxicSquid, Texture.class));

        preywidth = (int) food.getWidth() ;
        preyHeight = (int) food.getHeight() ;

        this.sandArea = new Sprite(AssetHandler.assetManager.get(AssetHandler.sandArea, Texture.class));
        this.bouy = new Sprite(AssetHandler.assetManager.get(AssetHandler.bouy, Texture.class));
        setBoatModel(new Sprite(AssetHandler.assetManager.get(AssetHandler.smallBoat, Texture.class)));

        this.pop1 = new Sprite(AssetHandler.assetManager.get(AssetHandler.lionFoodPop, Texture.class));
        this.pop2 = new Sprite(AssetHandler.assetManager.get(AssetHandler.lionHabitatPop, Texture.class));
        this.pop3 = new Sprite(AssetHandler.assetManager.get(AssetHandler.lionLifePop, Texture.class));
        this.pop4 = new Sprite(AssetHandler.assetManager.get(AssetHandler.lionSocialPop, Texture.class));
        this.pop5 = new Sprite(AssetHandler.assetManager.get(AssetHandler.lionThreatsPop, Texture.class));
        seenPopUps = new boolean[][]{new boolean[]{false, false, false, false, false}, new boolean[]{false, false, false, false, false}};

        popUps = new ArrayList<>(Arrays.asList(pop2, pop1, pop3, pop4, pop5)); //order maters
        popUpLocations = new Vector2[]{new Vector2(500, 200), new Vector2(3000, 200), new Vector2(5000, 200), new Vector2(7000, 200), new Vector2(9000, 200)};//slect location

        for (boolean b : obstacles2) {
            choices1.add(bouy);
        }
        placeBox2DObstacles(2, new Vector2[]{new Vector2(0, 0), new Vector2((spacing) / PPM, 0), new Vector2((spacing / 2) / PPM, -5 / PPM)}, obstacles3, 10, false, "Bouy");
        placeBox2DObstacles(3, new Vector2[]{new Vector2(0, 0), new Vector2((spacing) / PPM, 0), new Vector2((spacing / 2) / PPM, -5 / PPM)}, obstacles3, 10, false, "Bouy");


        addPrey(2, generateObstacles(2), preywidth, preyHeight, false);
        addPrey(3, generateObstacles(2), preywidth, preyHeight, false);

        makePolygonShapeBody(new Vector2[]{new Vector2(0, 0), new Vector2(600 / 32, -10 / 32), new Vector2(1850 / 32, -1200 / 32), new Vector2(0, -1000 / 32)}, 0, 0, true, "Sand");
        makePolygonShapeBody(new Vector2[]{new Vector2(0, 0), new Vector2(0, -1000 / 32), new Vector2(-(1850 / 32), -1200 / 32), new Vector2(-600 / 32, -10 / 32)}, getWorldSize(), 0, true, "Sand");

    }

    @Override
    public void render(float delta) {
        Render();
        Gdx.gl.glClearColor(0.8f, 0.9f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Add background

        // Add Obstacles


        game.batch.begin();
        renderBackground(background, -1 * getOceanDepth() - 50);
        renderCustom(sandArea, 0, -1 * sandArea.getHeight() - 350, sandArea.getWidth(), sandArea.getHeight() + 500);
        sandArea.flip(true, false);
        renderCustom(sandArea, (int) (getWorldSize() - sandArea.getWidth()), -1 * sandArea.getHeight() - 350, sandArea.getWidth(), sandArea.getHeight() + 500);
        sandArea.flip(true, false);
        renderObstacles(2, choices1, obstacles3, 10, 700, false);
        renderObstacles(3, choices1, obstacles3, 10, 700, false);
        renderPrey2D(food, toxicFood);
        renderBoat();
        seenPopUps = renderPopUps(seenPopUps, popUpLocations, popUps);

        renderEndGoal2D(staticSeaLion);
        renderPlayer2D();
        renderHealthBars();
        renderBackground(blur, -1 * getOceanDepth() - 50);
        game.batch.end();
//
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
                "What do California Sea Lions use to recognize other individuals?",
                "Sight, sound, and scent",
                new ArrayList<>(Arrays.asList("Sight, sound, and scent", "Only sight and scent", "Scent, touch, and sound", "None of these"))
        ));
        questionBank.put(5, new Question(
                "What can you do to help the California Sea Lions?",
                "TBA (Correct)",
                new ArrayList<>(Arrays.asList("TBA (Correct)", "TBA", "TBA", "TBA"))
        ));

        return questionBank;
    }

}
