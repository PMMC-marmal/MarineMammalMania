package com.pmmc.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.pmmc.app.AssetHandler;
import com.pmmc.app.GameLauncher;
import com.pmmc.app.character.Orca;
import com.pmmc.app.screen.quiz.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class OrcaLevel extends Level{


/**
 * PolarBearLevel: The first level presented to the user
 */

 boolean[] obstacles1, obstacles2, obstacles3;
    ArrayList<Sprite> choices1, choices2, choices3 ;
    private Orca orca;
    private Sprite background, blur, food, toxicFood;

    public OrcaLevel(GameLauncher game){
        super(game);
        preySpawnHeight = -1000;
        preyDespawnable = false;

        waterPrey = true;

        obstacles2 = generateObstacles(2);
        obstacles3 = generateObstacles(3);
        setWorldSize(24000);
        setOceanDepth(3000);
        setSpacing(1200);
        setBoatStrike(true);
        setWaterWorld(true);
    }

    @Override
    public void show() {
        orca = new Orca();
        
        setPlayer(orca);
        
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
                "What is the polar bear’s main source of food?",
                "Ringed Seals",
                new ArrayList<>(Arrays.asList("Ringed Seals", "Sea Lions", "Salmon", "Walruses"))
        ));
        questionBank.put(2, new Question(
                "Where do polar bears live?",
                "The Arctic",
                new ArrayList<>(Arrays.asList("The Arctic", "Antarctica", "Asia", "All answers are correct"))
        ));
        questionBank.put(3, new Question(
                "Which of these is NOT a threat to polar bears?",
                "Heavy boat traffic",
                new ArrayList<>(Arrays.asList("Heavy boat traffic", "Oil spills", "Melting ice", "Pollution"))
        ));
        questionBank.put(4, new Question(
                "The Arctic is heating up by...?",
                "14% each decade!",
                new ArrayList<>(Arrays.asList("14% each decade!", "1% each decade!", "4% each decade!", "10% each decade!"))
        ));
        questionBank.put(5, new Question(
                "What can you do to stop the Arctic from melting?",
                "TBA (Correct)",
                new ArrayList<>(Arrays.asList("TBA (Correct)", "TBA", "TBA", "TBA"))
        ));

        return questionBank;
    }
}
