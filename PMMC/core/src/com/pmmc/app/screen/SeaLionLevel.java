package com.pmmc.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.pmmc.app.AssetHandler;
import com.pmmc.app.GameLauncher;
import com.pmmc.app.character.SeaLion;

import java.util.ArrayList;

public class SeaLionLevel extends Level{


/**
 * PolarBearLevel: The first level presented to the user
 */


    boolean[] obstacles1, obstacles2, obstacles3;
    ArrayList<Sprite> choices1, choices2, choices3 ;
    private SeaLion seaLion;
    private Sprite background, sandArea, bouy, food, toxicFood;
    private TextureAtlas staticSeaLion;

    public SeaLionLevel(GameLauncher game){
        super(game);
        preySpawnHeight = -500;
        preyDespawnable = false;

        waterPrey = true;
        choices1 = new ArrayList<>();
        obstacles2 = generateObstacles(2);
        obstacles3 = generateObstacles(3);
        setWorldSize(24000);
        setOceanDepth(1000);
        setSpacing(200);
        setBoatStrike(true);
        setWaterWorld(false);
    }

    @Override
    public void show() {
        seaLion = new SeaLion();
        setPlayer(seaLion);
        player.setSwimming(false);
        this.background = new Sprite(AssetHandler.assetManager.get(AssetHandler.waterWithSand, Texture.class));
        this.staticSeaLion = AssetHandler.assetManager.get(AssetHandler.seaLionWalking, TextureAtlas.class);
        this.food = new Sprite(AssetHandler.assetManager.get(AssetHandler.herring, Texture.class));
        this.toxicFood = new Sprite(AssetHandler.assetManager.get(AssetHandler.toxicHerring, Texture.class));

        this.sandArea = new Sprite(AssetHandler.assetManager.get(AssetHandler.sandArea, Texture.class));
        this.bouy = new Sprite(AssetHandler.assetManager.get(AssetHandler.bouy, Texture.class));
        setBoatModel(new Sprite(AssetHandler.assetManager.get(AssetHandler.smallBoat, Texture.class)));

        for (boolean b: obstacles2){
            choices1.add(bouy);
        }
        placeBox2DObstacles(2,new Vector2[]{new Vector2(0, 0), new Vector2((spacing * 2) / PPM, 0), new Vector2((spacing) / PPM, -200 / PPM)}, obstacles3 , -20,true ,"Bouy");
        placeBox2DObstacles(3,new Vector2[]{new Vector2(0, 0), new Vector2((spacing * 2) / PPM, 0), new Vector2((spacing) / PPM, -200 / PPM)}, obstacles3 , -20,true ,"Bouy");

//        placeBox2DObstacles(2, obstacles2 );
//        placeBox2DObstacles(3, obstacles3 );
//        addPrey(1, generateObstacles(1), 300, 150);
        addPrey(2, generateObstacles(2), 100, 50, false);
        addPrey(3, generateObstacles(2), 100, 50, false);
        makePolygonShapeBody(new Vector2[]{new Vector2(0, 0), new Vector2(600/32, -10/32), new Vector2(1850/32, -1200/32), new Vector2(0, -1000/32)},0,0,"Sand");
        makePolygonShapeBody(new Vector2[]{new Vector2(0, 0), new Vector2(0, -1000/32), new Vector2(-(1850/32), -1200/32), new Vector2(-600/32, -10/32)},getWorldSize(),0,"Sand");

    }

    @Override
    public void render(float delta) {
        Render();
        Gdx.gl.glClearColor(0.8f, 0.9f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Add background

        // Add Obstacles


        game.batch.begin();
        renderBackground(background);
        renderCustom(sandArea, 0,-1*sandArea.getHeight() -350 ,sandArea.getWidth(),sandArea.getHeight() +500);
        sandArea.flip(true,false);
        renderCustom(sandArea, (int) (getWorldSize()-sandArea.getWidth()),-1*sandArea.getHeight() -350 ,sandArea.getWidth(),sandArea.getHeight() +500);
        sandArea.flip(true,false);
        renderObstacles(2,choices1,obstacles3,100, 700);
        renderPrey2D(food, toxicFood, 100,50);
        renderBoat();
//        renderEndGoal2D(staticSeaLion);
        renderPlayer2D();
        renderHealthBars();
        game.batch.end();
//        renderBackground(blur);
    }

}
