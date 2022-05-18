package com.pmmc.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.pmmc.app.AssetHandler;
import com.pmmc.app.GameLauncher;
import com.pmmc.app.character.PolarBear;
import com.pmmc.app.screen.quiz.Question;
import com.pmmc.app.screen.quiz.Quiz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;


/**
 * PolarBearLevel: The first level presented to the user
 */

public class PolarBearLevel extends Level {
    boolean[] obstacles1, obstacles2, obstacles3;
    boolean[][] seenPopUps;
    private Vector2[] popUpLocations;
    ArrayList<Sprite> choices1, choices2, choices3, popUps ;
    private PolarBear bear;
    private Sprite background, pop1,pop2,pop3,pop4,pop5,
            staticBear,
            food, toxicFood,
            iceberg1, iceberg2, iceberg3, iceberg4, iceberg5, iceberg6;

    public PolarBearLevel(final GameLauncher game){
        super(game);
        choices1 = new ArrayList<>();
        choices2 = new ArrayList<>();
        choices3 = new ArrayList<>();
        obstacles1 = generateObstacles(1);
        obstacles2 = generateObstacles(2);
        obstacles3 = generateObstacles(3);
        preySpawnHeight = 100;
        preyDespawnable = true;
        setWorldSize(24000);
        setOceanDepth(2000);
        setSpacing(600);
        setWaterWorld(false);
    }

    @Override
    public void show() {
        bear = new PolarBear();
        setPlayer(bear);
        player.setSwimming(false);
        this.background = new Sprite(AssetHandler.assetManager.get(AssetHandler.waterWithSand, Texture.class));

        this.staticBear = new Sprite(AssetHandler.assetManager.get(AssetHandler.polarBearSprite, Texture.class));
        staticBear.flip(true,false);

        this.food = new Sprite(AssetHandler.assetManager.get(AssetHandler.seal, Texture.class));
        this.toxicFood = new Sprite(AssetHandler.assetManager.get(AssetHandler.toxicSeal, Texture.class));

        this.iceberg1 = new Sprite(AssetHandler.assetManager.get(AssetHandler.iceberg1, Texture.class));
        this.iceberg2 = new Sprite(AssetHandler.assetManager.get(AssetHandler.iceberg2, Texture.class));
        this.iceberg3 = new Sprite(AssetHandler.assetManager.get(AssetHandler.iceberg3, Texture.class));
        this.iceberg4 = new Sprite(AssetHandler.assetManager.get(AssetHandler.iceberg4, Texture.class));
        this.iceberg5 = new Sprite(AssetHandler.assetManager.get(AssetHandler.iceberg5, Texture.class));
        this.iceberg6 = new Sprite(AssetHandler.assetManager.get(AssetHandler.iceberg6, Texture.class));

        this.pop1 = new Sprite(AssetHandler.assetManager.get(AssetHandler.polarFoodPop, Texture.class));
        this.pop2 = new Sprite(AssetHandler.assetManager.get(AssetHandler.polarHabitatPop, Texture.class));
        this.pop3 = new Sprite(AssetHandler.assetManager.get(AssetHandler.polarLifePop, Texture.class));
        this.pop4 = new Sprite(AssetHandler.assetManager.get(AssetHandler.polarSocialPop, Texture.class));
        this.pop5 = new Sprite(AssetHandler.assetManager.get(AssetHandler.polarThreatsPop, Texture.class));
        seenPopUps = new boolean[][]{new boolean[]{false, false, false, false, false}, new boolean[]{false, false, false, false, false}};

        popUps = new ArrayList<>(Arrays.asList(pop2, pop1, pop3, pop4, pop5)); //order maters
        popUpLocations = new Vector2[]{new Vector2(1000,200),new Vector2(3000,200),new Vector2(5000,200),new Vector2(7000,200),new Vector2(9000,200)};//slect location

        placeBox2DObstacles(1,new Vector2[]{}, obstacles1 , 0,false,"IceBerg");
        placeBox2DObstacles(2,new Vector2[]{new Vector2(0, 0), new Vector2((spacing * 2) / PPM, 0), new Vector2((spacing) / PPM, -200 / PPM)}, obstacles2 , 0,true ,"IceBerg");
        placeBox2DObstacles(3,new Vector2[]{new Vector2(0, 0), new Vector2((spacing) / PPM, 0), new Vector2((spacing / 2) / PPM, -200 / PPM)}, obstacles3 , 0,false,"IceBerg");

        addPrey(1, generateObstacles(1), (int)food.getWidth(), (int)food.getHeight(), true);
        addPrey(2, generateObstacles(2), (int)food.getWidth(), (int)food.getHeight(), true);
        addPrey(3, generateObstacles(2), (int)food.getWidth(), (int)food.getHeight(), true);


        choices1.add(this.iceberg1);
        Sprite[] options2 = {this.iceberg2, this.iceberg3, this.iceberg4};
        for (int i = 0 ; i < 5; i++) {
            choices2.add(options2[new Random().nextInt(options2.length-1)]);
        }
        Sprite[] options3 = {this.iceberg5, this.iceberg6};
        for (int i = 0 ; i < 2; i++) {
            choices3.add(options3[new Random().nextInt(options3.length-1)]);
        }
    }

    @Override
    public void render(float delta) {
        Render();
        Gdx.gl.glClearColor(0.8f, 0.9f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        game.batch.begin();
        // Add background
        renderBackground(background);

        renderPrey2D(food, toxicFood, (int)food.getWidth(), (int)food.getHeight()); // NEEDS HEIGHT WIDTH
        // Add Obstacles
        renderObstacles(1, choices1, obstacles1, 0,0);
        renderObstacles(2, choices2, obstacles2, 0,400);
        renderObstacles(3, choices3, obstacles3, 0, 400);
        seenPopUps = renderPopUps(seenPopUps,popUpLocations,popUps);
        renderEndGoal2D(staticBear);
        renderPlayer2D();
        renderHealthBars();
        game.batch.end();
//        renderBackground(blur);
    }

    /**
     * Note to whoever implements the quiz. All you really need to do is:
     * 1. Create a generateQuestionBank method
     * 2. Wherever you want to trigger the Quiz screen, do so like this:
     *          game.setScreen(new Quiz(game, generateQuestionBank()));
     * **/

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