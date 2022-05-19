package com.pmmc.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.pmmc.app.AssetHandler;
import com.pmmc.app.GameLauncher;
import com.pmmc.app.character.Orca;
import com.pmmc.app.screen.quiz.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class OrcaLevel extends Level {


    /**
     * PolarBearLevel: The first level presented to the user
     */

    boolean[][] seenPopUps;
    ArrayList<Sprite> popUps;
    private Vector2[] popUpLocations;
    private Orca orca;
    private Sprite background, blur, staticOrca, food, toxicFood, pop1, pop2, pop3, pop4, pop5;

    public OrcaLevel(GameLauncher game) {
        super(game);
        preySpawnHeight = -1000;
        preyDespawnable = false;
        preySpeed = 20;
        waterPrey = true;
        setWaterWorld(true);
        setWorldSize(24000);
        setOceanDepth(3000);
        setSpacing(1200);
        setBoatStrike(true);
        setOilSpill(true);
        setBoatYAxis(-250);
    }

    @Override
    public void show() {
        orca = new Orca();
        setPlayer(orca);
        setBoatModel(new Sprite(AssetHandler.assetManager.get(AssetHandler.smallBoat, Texture.class)));
        player.setSwimming(true);

        this.background = new Sprite(AssetHandler.assetManager.get(AssetHandler.waterWithSand, Texture.class));
        this.blur = new Sprite(AssetHandler.assetManager.get(AssetHandler.blur, Texture.class));
        this.staticOrca = new Sprite(AssetHandler.assetManager.get(AssetHandler.killerWhaleSprite, Texture.class));
        staticOrca.flip(true, false);
        setEndGoal(staticOrca, preySpawnHeight);

        this.food = new Sprite(AssetHandler.assetManager.get(AssetHandler.salmon, Texture.class));
        this.toxicFood = new Sprite(AssetHandler.assetManager.get(AssetHandler.toxicSalmon, Texture.class));

        preywidth = (int) food.getWidth() ;
        preyHeight = (int) food.getHeight() ;

        setBoatModel(new Sprite(AssetHandler.assetManager.get(AssetHandler.smallBoatFishingLine, Texture.class)));
        setOilSprite(new Sprite(AssetHandler.assetManager.get(AssetHandler.oilSpill, Texture.class)));

        this.pop1 = new Sprite(AssetHandler.assetManager.get(AssetHandler.orcaFoodPop, Texture.class));
        this.pop2 = new Sprite(AssetHandler.assetManager.get(AssetHandler.orcaHabitatPop, Texture.class));
        this.pop3 = new Sprite(AssetHandler.assetManager.get(AssetHandler.orcaLifePop, Texture.class));
        this.pop4 = new Sprite(AssetHandler.assetManager.get(AssetHandler.orcaSocialPop, Texture.class));
        this.pop5 = new Sprite(AssetHandler.assetManager.get(AssetHandler.orcaThreatsPop, Texture.class));
        seenPopUps = new boolean[][]{new boolean[]{false, false, false, false, false}, new boolean[]{false, false, false, false, false}};
        popUps = new ArrayList<>(Arrays.asList(pop2, pop1, pop3, pop4, pop5)); //order maters
        popUpLocations = new Vector2[]{new Vector2(1000, 200), new Vector2(3000, 200), new Vector2(5000, 200), new Vector2(7000, 200), new Vector2(9000, 200)};//slect location

        addPrey(2, generateObstacles(2), preywidth, preyHeight, false);
        addPrey(3, generateObstacles(2), preywidth, preyHeight, false);
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
        renderEndGoal2D(staticOrca);
        renderPlayer2D();
        renderBackground(blur, -1 * getOceanDepth() - 50);
        renderHealthBars();
        game.batch.end();
    }

    public HashMap<Integer, Question> generateQuestionBank() {
        HashMap<Integer, Question> questionBank = new HashMap<>();
        questionBank.put(1, new Question(
                "What do the southern resident killer whales mainly eat?",
                "Chinook salmon",
                new ArrayList<>(Arrays.asList("Chinook salmon", "Sardines", "Squid", "Herring"))
        ));
        questionBank.put(2, new Question(
                "Female southern resident killer whales can live up to ...?",
                "80-100 years old",
                new ArrayList<>(Arrays.asList("80-100 years old", "40-60 years old", "60-80 years old", "100-120 years old"))
        ));
        questionBank.put(3, new Question(
                "How many pods do the southern resident killer whales have?",
                "3 pods",
                new ArrayList<>(Arrays.asList("3 pods", "2 pods", "5 pods", "8 pods"))
        ));
        questionBank.put(4, new Question(
                "Which of the following is a threat to the southern resident killer whales?",
                "All answers are correct",
                new ArrayList<>(Arrays.asList("All answers are correct", "Decreased quality of prey", "Disturbance from ship presence", "None of these"))
        ));
        questionBank.put(5, new Question(
                "What can you do to help preserve our southern resident killer whales?",
                "TBA (Correct)",
                new ArrayList<>(Arrays.asList("TBA (Correct)", "TBA", "TBA", "TBA"))
        ));

        return questionBank;
    }
}
