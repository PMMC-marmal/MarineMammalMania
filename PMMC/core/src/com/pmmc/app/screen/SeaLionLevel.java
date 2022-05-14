package com.pmmc.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
    private Sprite background, food, toxicFood;

    public SeaLionLevel(GameLauncher game){
        super(game);
        preySpawnHeight = -500;
        preyDespawnable = false;

        waterWorld = true;

        obstacles2 = generateObstacles(2);
        obstacles3 = generateObstacles(3);
    }

    @Override
    public void show() {
        seaLion = new SeaLion();
        setWorldSize(24000);
        setSpacing(600);
        setPlayer(seaLion);
        player.setSwimming(false);
        this.background = new Sprite(AssetHandler.assetManager.get(AssetHandler.waterWithSand, Texture.class));
        this.food = new Sprite(AssetHandler.assetManager.get(AssetHandler.herring, Texture.class));
        this.toxicFood = new Sprite(AssetHandler.assetManager.get(AssetHandler.toxicHerring, Texture.class));

        placeBox2DObstacles(2, obstacles2 );
        placeBox2DObstacles(3, obstacles3 );
//        addPrey(1, generateObstacles(1), 300, 150);
        addPrey(2, generateObstacles(2), 300, 150);
        addPrey(3, generateObstacles(2), 300, 150);
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

        renderPrey2D(food, toxicFood); // NEEEDS HIEGHT WIDTH
        renderPlayer2D();
        renderHealthBars();
        game.batch.end();
//        renderBackground(blur);
    }

}
