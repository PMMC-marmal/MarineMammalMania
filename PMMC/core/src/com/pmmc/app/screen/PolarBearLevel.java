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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;


/**
 * PolarBearLevel: The first level presented to the user
 */

public class PolarBearLevel extends Level {
    boolean[] obstacles1;
    boolean[][] seenPopUps;
    ArrayList<Sprite> choices1, choices2, choices3, popUps;
    private Vector2[] popUpLocations;
    private Sprite background;
    private Sprite backdrop;
    private Sprite blur;
    private Sprite staticBear;
    private Sprite food;
    private Sprite toxicFood;

    public PolarBearLevel(final GameLauncher game) {
        super(game, new ArrayList<>(Arrays.asList(
                Gdx.audio.newSound(Gdx.files.internal("sounds/polar_bear_1.mp3")),
                Gdx.audio.newSound(Gdx.files.internal("sounds/polar_bear_2.mp3"))
        )));
        choices1 = new ArrayList<>();
        choices2 = new ArrayList<>();
        choices3 = new ArrayList<>();
        obstacles1 = generateObstacles(1);
        obstacles2 = generateObstacles(2);
        obstacles3 = generateObstacles(4);
        preySpawnHeight = 100;
        preyDespawnable = true;
        preySpeed = 15;
        waterPrey = false;
        setWorldSize(28000);
        setOceanDepth(1000);
        setSpacing(700);
        setWaterWorld(false);
        setOilSpill(true);

    }

    @Override
    public void show() {
        PolarBear bear = new PolarBear();
        setPlayer(bear);
        player.setSwimming(false);
        this.background = new Sprite(AssetHandler.assetManager.get(AssetHandler.waterWithSand, Texture.class));
        this.backdrop = new Sprite(AssetHandler.assetManager.get(AssetHandler.polarBackdrop, Texture.class));
        this.blur = new Sprite(AssetHandler.assetManager.get(AssetHandler.blur, Texture.class));

        this.staticBear = new Sprite(AssetHandler.assetManager.get(AssetHandler.polarBearSprite, Texture.class));
        staticBear.flip(true, false);
        setEndGoal(staticBear, 100);

        this.food = new Sprite(AssetHandler.assetManager.get(AssetHandler.seal, Texture.class));
        this.toxicFood = new Sprite(AssetHandler.assetManager.get(AssetHandler.toxicSeal, Texture.class));

        preyWidth = (int) food.getWidth() / 2;
        preyHeight = (int) food.getHeight() / 2;

        setOilSprite(new Sprite(AssetHandler.assetManager.get(AssetHandler.oilSpill, Texture.class)));


        Sprite iceberg1 = new Sprite(AssetHandler.assetManager.get(AssetHandler.iceberg1, Texture.class));
        Sprite iceberg2 = new Sprite(AssetHandler.assetManager.get(AssetHandler.iceberg2, Texture.class));
        Sprite iceberg3 = new Sprite(AssetHandler.assetManager.get(AssetHandler.iceberg3, Texture.class));
        Sprite iceberg4 = new Sprite(AssetHandler.assetManager.get(AssetHandler.iceberg4, Texture.class));
        Sprite iceberg5 = new Sprite(AssetHandler.assetManager.get(AssetHandler.iceberg5, Texture.class));
        Sprite iceberg6 = new Sprite(AssetHandler.assetManager.get(AssetHandler.iceberg6, Texture.class));

        Sprite pop1 = new Sprite(AssetHandler.assetManager.get(AssetHandler.polarFoodPop, Texture.class));
        Sprite pop2 = new Sprite(AssetHandler.assetManager.get(AssetHandler.polarHabitatPop, Texture.class));
        Sprite pop3 = new Sprite(AssetHandler.assetManager.get(AssetHandler.polarLifePop, Texture.class));
        Sprite pop4 = new Sprite(AssetHandler.assetManager.get(AssetHandler.polarSocialPop, Texture.class));
        Sprite pop5 = new Sprite(AssetHandler.assetManager.get(AssetHandler.polarThreatsPop, Texture.class));
        seenPopUps = new boolean[][]{new boolean[]{false, false, false, false, false}, new boolean[]{false, false, false, false, false}};

        popUps = new ArrayList<>(Arrays.asList(pop1, pop2, pop3, pop4, pop5)); //order maters
        popUpLocations = new Vector2[]{new Vector2((spacing* 7.5f)-(pop1.getWidth()/2), preySpawnHeight+100), new Vector2(1000, 200), new Vector2(3000, 200), new Vector2(getWorldSize()-(spacing/2f)-(pop5.getWidth()/2), (getEndGoal().getPosition().y * PPM)+100), new Vector2(9000, 200)};//slect location

        placeBox2DObstacles(1, new Vector2[]{}, obstacles1, 0, false, "IceBerg");
        placeBox2DObstacles(2, new Vector2[]{new Vector2(0, 0), new Vector2((spacing * 2) / PPM, 0), new Vector2((spacing) / PPM, -200 / PPM)}, obstacles2, 0, true, "IceBerg");
        placeBox2DObstacles(3, new Vector2[]{new Vector2(0, 0), new Vector2((spacing) / PPM, 0), new Vector2((spacing / 2f) / PPM, -200 / PPM)}, obstacles3, 0, false, "IceBerg");

        addPrey(2, generateObstacles(2), preyWidth, preyHeight, true);
        addPrey(3, generateObstacles(3), preyWidth, preyHeight, true);


        choices1.add(iceberg1);
        Sprite[] options2 = {iceberg2, iceberg3, iceberg4};
        for (int i = 0; i < 5; i++) {
            choices2.add(options2[new Random().nextInt(options2.length - 1)]);
        }
        Sprite[] options3 = {iceberg5, iceberg6};
        for (int i = 0; i < 2; i++) {
            choices3.add(options3[new Random().nextInt(options3.length - 1)]);
        }
    }

    @Override
    public void render(float delta) {
        Render();
        Gdx.gl.glClearColor(0.8f, 0.9f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        // Add background
        renderBackground(backdrop, -100, 1);
        renderBackground(background, -1 * getOceanDepth() - 50,1);

        renderPrey2D(food, toxicFood);
        renderObstacles(1, choices1, obstacles1, 540, 0, false);
        renderObstacles(2, choices2, obstacles2, 0, 300, true);
        renderObstacles(3, choices3, obstacles3, 0, 300, false);
        seenPopUps = renderPopUps(seenPopUps, popUpLocations, popUps);
        renderOil();
        renderEndGoal2D(staticBear);
        renderPlayer2D();
        renderHealthBars();
        renderBackground(blur, -1 * getOceanDepth() - 50, 1);
        game.batch.end();
//
    }

    /**
     * Note to whoever implements the quiz. All you really need to do is:
     * 1. Create a generateQuestionBank method
     * 2. Wherever you want to trigger the Quiz screen, do so like this:
     * game.setScreen(new Quiz(game, generateQuestionBank()));
     **/

    public HashMap<Integer, Question> generateQuestionBank() {
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
                "What is causing polar bears to starve?",
                "Climate change",
                new ArrayList<>(Arrays.asList("Climate change", "Plastics", "Chemical pollutants", "Fishing gear"))
        ));
        questionBank.put(4, new Question(
                "The Arctic is heating up by...?",
                "14% each decade!",
                new ArrayList<>(Arrays.asList("14% each decade!", "1% each decade!", "4% each decade!", "10% each decade!"))
        ));
        questionBank.put(5, new Question(
                "What can you do to reduce the effects of climate change?",
                "Carpool, ride your bike, or walk",
                new ArrayList<>(Arrays.asList("Carpool, ride your bike, or walk", "Eat organic food", "Avoid using fertilizers", "Make drought friendly landscapes"))
        ));

        return questionBank;
    }

}