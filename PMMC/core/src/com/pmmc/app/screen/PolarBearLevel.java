package com.pmmc.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.pmmc.app.AssetHandler;
import com.pmmc.app.GameLauncher;
import com.pmmc.app.character.PolarBear;

import java.util.ArrayList;
import java.util.Random;


/**
 * PolarBearLevel: The first level presented to the user
 */

public class PolarBearLevel extends Level {
    boolean[] obstacles1, obstacles2, obstacles3;

    ArrayList<Sprite> choices1, choices2, choices3 ;
    private PolarBear bear;
    private Sprite background, staticBear, food,
            iceberg1,
            iceberg2, iceberg3, iceberg4, iceberg5, iceberg6;

    public PolarBearLevel(final GameLauncher game){
        super(game);
        choices1 = new ArrayList<>();
        choices2 = new ArrayList<>();
        choices3 = new ArrayList<>();
        obstacles1 = generateObstacles(1);
        obstacles2 = generateObstacles(2);
        obstacles3 = generateObstacles(3);
        preySpawnHeight = 300;
        preyDespawnable = true;


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

        this.iceberg1 = new Sprite(AssetHandler.assetManager.get(AssetHandler.iceberg1, Texture.class));
        this.iceberg2 = new Sprite(AssetHandler.assetManager.get(AssetHandler.iceberg2, Texture.class));
        this.iceberg3 = new Sprite(AssetHandler.assetManager.get(AssetHandler.iceberg3, Texture.class));
        this.iceberg4 = new Sprite(AssetHandler.assetManager.get(AssetHandler.iceberg4, Texture.class));
        this.iceberg5 = new Sprite(AssetHandler.assetManager.get(AssetHandler.iceberg5, Texture.class));
        this.iceberg6 = new Sprite(AssetHandler.assetManager.get(AssetHandler.iceberg6, Texture.class));
        placeBox2DObstacles(1, obstacles1 );
        placeBox2DObstacles(2, obstacles2 );
        placeBox2DObstacles(3, obstacles3 );
        addPrey(1, generateObstacles(1), 300, 150);
        addPrey(2, generateObstacles(2), 300, 150);
        addPrey(3, generateObstacles(2), 300, 150);


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

        // Add background
        renderBackground(background);

        renderPrey2D(food); // NEEDS HEIGHT WIDTH
        // Add Obstacles
        renderObstacles(1, choices1, obstacles1, 100);
        renderObstacles(2, choices2, obstacles2, 100);
        renderObstacles(3, choices3, obstacles3, 100);

        game.batch.begin();
        renderPlayer2D();
        renderHealthBars();
        renderEndGoal2D(staticBear);
        game.batch.end();
//        renderBackground(blur);
    }

}
