package com.pmmc.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.pmmc.app.AssetHandler;
import com.pmmc.app.GameLauncher;


/**
 * PolarBearLevel: The first level presented to the user
 */

public class PolarBearLevel extends Level {

    private Sprite polarBear,
            background,
            iceberg2,iceberg3,iceberg4;


    boolean[] obstacles;

    public PolarBearLevel(final GameLauncher game){
        super(game);
        obstacles = generateObstacles();
    }

    @Override
    public void show(){
        this.polarBear = new Sprite(AssetHandler.assetManager.get(AssetHandler.polarBearSprite, Texture.class));
        this.background = new Sprite(AssetHandler.assetManager.get(AssetHandler.waterWithSand, Texture.class));
        this.iceberg2 = new Sprite(AssetHandler.assetManager.get(AssetHandler.iceberg2,Texture.class));
        this.iceberg3 = new Sprite(AssetHandler.assetManager.get(AssetHandler.iceberg2,Texture.class));
        this.iceberg4 = new Sprite(AssetHandler.assetManager.get(AssetHandler.iceberg2,Texture.class));
    }

    @Override
    public void render(float delta){
        // Set default background to black
        Gdx.gl.glClearColor(0.8f,0.9f,1f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Sprite[] choices = {iceberg2, iceberg3, iceberg4 };



        // Add background
        renderBackground(background);
        renderObstacles(1,choices, obstacles);
        game.batch.begin();


        game.batch.end();
    }

}
