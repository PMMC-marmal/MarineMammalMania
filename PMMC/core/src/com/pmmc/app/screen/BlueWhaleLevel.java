package com.pmmc.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pmmc.app.AssetHandler;
import com.pmmc.app.GameLauncher;
import com.pmmc.app.character.BlueWhale;
import com.pmmc.app.screen.quiz.Question;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BlueWhaleLevel extends Level{


/**
 * PolarBearLevel: The first level presented to the user
 */

 boolean[] obstacles1, obstacles2, obstacles3;
    ArrayList<Sprite> choices1, choices2, choices3 ;
    private BlueWhale whale;
    private Sprite background, blur, food, toxicFood;

    public BlueWhaleLevel(GameLauncher game){
        super(game);
        preySpawnHeight = -1000;
        preyDespawnable = false;

        waterPrey = true;

        obstacles2 = generateObstacles(2);
        obstacles3 = generateObstacles(3);
    }

    @Override
    public void show() {
        whale = new BlueWhale();
        setWorldSize(24000);
        setOceanDepth(3000);
        setSpacing(1200);
        setPlayer(whale);
        setBoatStrike(true);
        setWaterWorld(true);
//        setBoatModel(new Sprite(AssetHandler.assetManager.get(AssetHandler.smallBoat, Texture.class)));
        player.setSwimming(true);
        this.background = new Sprite(AssetHandler.assetManager.get(AssetHandler.waterWithSand, Texture.class));
        this.food = new Sprite(AssetHandler.assetManager.get(AssetHandler.krill, Texture.class));
        this.toxicFood = new Sprite(AssetHandler.assetManager.get(AssetHandler.toxicKrill, Texture.class));


//        placeBox2DObstacles(2, obstacles2 );
//        placeBox2DObstacles(3, obstacles3 );
//        addPrey(1, generateObstacles(1), 300, 150);
        addPrey(2, generateObstacles(2), 100, 50, false);
        addPrey(3, generateObstacles(2), 100, 50, false);



//        addPrey(createBox(4200,150, 300,300, true, true,"toxic food"));

    }

    @Override
    public void render(float delta) {
        Render();
        Gdx.gl.glClearColor(0.8f, 0.9f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        renderBackground(background,-1 * getOceanDepth()-50);

        renderPrey2D(food, toxicFood, 100,50);
        renderPlayer2D();
        renderHealthBars();
        game.batch.end();
//        renderBackground(blur);
    }

    public HashMap<Integer, Question> generateQuestionBank(){
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
