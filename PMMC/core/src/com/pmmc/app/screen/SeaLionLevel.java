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
import com.pmmc.app.character.SeaLion;

import java.util.ArrayList;

public class SeaLionLevel extends Level{


/**
 * PolarBearLevel: The first level presented to the user
 */


    boolean[] obstacles1, obstacles2, obstacles3;
    ArrayList<Sprite> choices1, choices2, choices3 ;
    private SeaLion seaLion;
    private Sprite background, blur, food;

    public SeaLionLevel(final GameLauncher game){
        super(game);
        choices1 = new ArrayList<>();
        choices2 = new ArrayList<>();
        choices3 = new ArrayList<>();
        obstacles1 = generateObstacles(1);
        obstacles2 = generateObstacles(2);
        obstacles3 = generateObstacles(3);
        preySpawnHeight = -200;
        preyDespawnable = false;
    }

    @Override
    public void show() {
        seaLion = new SeaLion();
        setPlayer(seaLion);
        player.setSwimming(false);
        this.background = new Sprite(AssetHandler.assetManager.get(AssetHandler.waterWithSand, Texture.class));
        this.blur = new Sprite(AssetHandler.assetManager.get(AssetHandler.blur, Texture.class));
        this.food = new Sprite(AssetHandler.assetManager.get(AssetHandler.seaLionSprite, Texture.class));


//        addPrey(createBox(4200,150, 300,300, true, true,"toxic food"));

    }

    @Override
    public void render(float delta) {
        Render();
        Gdx.gl.glClearColor(0.8f, 0.9f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Add background
        renderBackground(background);

        renderPrey2D(food); // NEEEDS HIEGHT WIDTH
        // Add Obstacles


        game.batch.begin();
        renderPlayer2D();
        game.batch.end();
//        renderBackground(blur);
    }

}
