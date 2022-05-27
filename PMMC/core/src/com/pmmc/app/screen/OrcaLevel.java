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
    private Sprite background;
    private Sprite backdrop;
    private Sprite blur;
    private Sprite staticOrca;
    private Sprite food;
    private Sprite toxicFood;

    public OrcaLevel(GameLauncher game) {
        super(game, "sounds/tropical_music.mp3", new ArrayList<>(Arrays.asList(
                Gdx.audio.newSound(Gdx.files.internal("sounds/orca_1.wav")),
                Gdx.audio.newSound(Gdx.files.internal("sounds/orca_2.wav")),
                Gdx.audio.newSound(Gdx.files.internal("sounds/orca_3.wav"))
        )));
        preySpawnHeight = -1000;
        preyDespawnable = false;
        preySpeed = 18;
        waterPrey = true;
        setWaterWorld(true);
        setWorldSize(24000);
        setOceanDepth(2000);
        setSpacing(1200);
        setMinNumPrey(5);
        setBoatStrike(true);
        setOilSpill(true);
        setBoatYAxis(-250);
    }

    @Override
    public void show() {
        Orca orca = new Orca();
        setPlayer(orca);
        setBoatModel(new Sprite(AssetHandler.assetManager.get(AssetHandler.smallBoat, Texture.class)));
        player.setSwimming(true);

        this.background = new Sprite(AssetHandler.assetManager.get(AssetHandler.waterWithSand, Texture.class));
        this.blur = new Sprite(AssetHandler.assetManager.get(AssetHandler.blur, Texture.class));
        this.backdrop = new Sprite(AssetHandler.assetManager.get(AssetHandler.skyBackground2, Texture.class));

        this.staticOrca = new Sprite(AssetHandler.assetManager.get(AssetHandler.killerWhaleSprite, Texture.class));
        staticOrca.flip(true, false);
        setEndGoal(staticOrca, preySpawnHeight);

        this.food = new Sprite(AssetHandler.assetManager.get(AssetHandler.salmon, Texture.class));
        this.toxicFood = new Sprite(AssetHandler.assetManager.get(AssetHandler.toxicSalmon, Texture.class));

        preyWidth = (int) food.getWidth() /3;
        preyHeight = (int) food.getHeight() /3;

        setBoatModel(new Sprite(AssetHandler.assetManager.get(AssetHandler.smallBoatFishingLine, Texture.class)));
        setOilSprite(new Sprite(AssetHandler.assetManager.get(AssetHandler.oilSpill, Texture.class)));

        Sprite pop1 = new Sprite(AssetHandler.assetManager.get(AssetHandler.orcaFoodPop, Texture.class));
        Sprite pop2 = new Sprite(AssetHandler.assetManager.get(AssetHandler.orcaHabitatPop, Texture.class));
        Sprite pop3 = new Sprite(AssetHandler.assetManager.get(AssetHandler.orcaLifePop, Texture.class));
        Sprite pop4 = new Sprite(AssetHandler.assetManager.get(AssetHandler.orcaSocialPop, Texture.class));
        Sprite pop5 = new Sprite(AssetHandler.assetManager.get(AssetHandler.orcaThreatsPop, Texture.class));
        seenPopUps = new boolean[][]{new boolean[]{false, false, false, false, false}, new boolean[]{false, false, false, false, false}};
        popUps = new ArrayList<>(Arrays.asList(pop1, pop2, pop3, pop4, pop5)); //order maters
        popUpLocations = new Vector2[]{new Vector2((spacing* 7.5f)-(pop1.getWidth()/2), preySpawnHeight+200), new Vector2(3000, 0), new Vector2(5000, 0), new Vector2(getWorldSize()-spacing-(pop5.getWidth()/2), getEndGoal().getPosition().y+200), new Vector2(9000, 0)};//slect location

        addPrey(2, generateObstacles(2), preyWidth, preyHeight, false);
        addPrey(3, generateObstacles(3), preyWidth, preyHeight, false);
    }

    @Override
    public void render(float delta) {
        Render();
        Gdx.gl.glClearColor(0.8f, 0.9f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        renderBackground(backdrop, -300, 3);
        renderBackground(background, -1 * getOceanDepth() - 50, 1);
        seenPopUps = renderPopUps(seenPopUps, popUpLocations, popUps);
        renderPrey2D(food, toxicFood);
        renderBoat();
        renderOil();
        renderEndGoal2D(staticOrca);
        renderPlayer2D();
        renderBackground(blur, -1 * getOceanDepth() - 50,1);
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
                new ArrayList<>(Arrays.asList("All answers are correct", "Decreased quality of prey", "Disturbance from ship presence", "Fisheries, gill nets, ropes"))
        ));
        questionBank.put(5, new Question(
                "What can you do to prevent our southern resident killer whales from dying?",
                "Select sustainably caught fish",
                new ArrayList<>(Arrays.asList("Select sustainably caught fish", "Make drought friendly landscapes", "Avoid fertilizers", "Carpool or ride your bike"))
        ));

        return questionBank;
    }
}
