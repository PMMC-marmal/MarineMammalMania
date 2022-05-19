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
    private BlueWhale whale;
    private Sprite background, blur, staticWhale, food, toxicFood, pop1, pop2, pop3, pop4, pop5;

    public BlueWhaleLevel(GameLauncher game) {
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
        whale = new BlueWhale();
        setPlayer(whale);

        player.setSwimming(true);
        this.background = new Sprite(AssetHandler.assetManager.get(AssetHandler.waterWithSand, Texture.class));
        this.blur = new Sprite(AssetHandler.assetManager.get(AssetHandler.blur, Texture.class));
        this.staticWhale = new Sprite(AssetHandler.assetManager.get(AssetHandler.blueWhaleSprite, Texture.class));
        staticWhale.flip(true, false);
        setEndGoal(staticWhale, preySpawnHeight);

        this.food = new Sprite(AssetHandler.assetManager.get(AssetHandler.krill, Texture.class));
        this.toxicFood = new Sprite(AssetHandler.assetManager.get(AssetHandler.toxicKrill, Texture.class));

        preywidth = (int) food.getWidth() ;
        preyHeight = (int) food.getHeight() ;

        setBoatModel(new Sprite(AssetHandler.assetManager.get(AssetHandler.smallBoatFishingLine, Texture.class)));
        setOilSprite(new Sprite(AssetHandler.assetManager.get(AssetHandler.oilSpill, Texture.class)));

        this.pop1 = new Sprite(AssetHandler.assetManager.get(AssetHandler.whaleFoodPop, Texture.class));
        this.pop2 = new Sprite(AssetHandler.assetManager.get(AssetHandler.whaleHabitatPop, Texture.class));
        this.pop3 = new Sprite(AssetHandler.assetManager.get(AssetHandler.whaleLifePop, Texture.class));
        this.pop4 = new Sprite(AssetHandler.assetManager.get(AssetHandler.whaleSocialPop, Texture.class));
        this.pop5 = new Sprite(AssetHandler.assetManager.get(AssetHandler.whaleThreatsPop, Texture.class));
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
        renderEndGoal2D(staticWhale);
        renderPlayer2D();
        renderBackground(blur, -1 * getOceanDepth() - 50);
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
                "In order to keep our world’s blue whale population, what can you do to help?",
                "TBA (Correct)",
                new ArrayList<>(Arrays.asList("TBA (Correct)", "TBA", "TBA", "TBA"))
        ));

        return questionBank;
    }

}
