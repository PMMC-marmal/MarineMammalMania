package com.pmmc.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
    TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("PolarBearWalkingSpriteSheet.atlas"));
    boolean[] obstacles1, obstacles2, obstacles3;
    ArrayList<Sprite> choices1, choices2, choices3 ;
    private PolarBear bear;
    private Sprite polarBear,
            background,
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
    }

    @Override
    public void show() {
        System.out.println("Runing show");
        TextureRegion textureRegion = textureAtlas.findRegion("1");
        bear = new PolarBear(new Sprite(textureRegion), textureAtlas);
        setPlayer(bear);
        this.background = new Sprite(AssetHandler.assetManager.get(AssetHandler.waterWithSand, Texture.class));
        this.iceberg1 = new Sprite(AssetHandler.assetManager.get(AssetHandler.iceberg1, Texture.class));
        this.iceberg2 = new Sprite(AssetHandler.assetManager.get(AssetHandler.iceberg2, Texture.class));
        this.iceberg3 = new Sprite(AssetHandler.assetManager.get(AssetHandler.iceberg3, Texture.class));
        this.iceberg4 = new Sprite(AssetHandler.assetManager.get(AssetHandler.iceberg4, Texture.class));
        this.iceberg5 = new Sprite(AssetHandler.assetManager.get(AssetHandler.iceberg5, Texture.class));
        this.iceberg6 = new Sprite(AssetHandler.assetManager.get(AssetHandler.iceberg6, Texture.class));
        placeBox2DObstacles(1, obstacles1 );
        placeBox2DObstacles(2, obstacles2 );
        placeBox2DObstacles(3, obstacles3 );
        choices1.add(this.iceberg1);
        Sprite[] options2 = {this.iceberg2, this.iceberg3, this.iceberg4};
        for (int i = 0 ; i < 5; i++) {
            choices2.add(options2[new Random().nextInt(2)]);
        }
        Sprite[] options3 = {this.iceberg5, this.iceberg6};
        for (int i = 0 ; i < 2; i++) {
            choices3.add(options3[new Random().nextInt(1)]);
        }
    }

    @Override
    public void render(float delta) {
        Render();
        Gdx.gl.glClearColor(0.8f, 0.9f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Add background
        renderBackground(background);
        renderObstacles(1, choices1, obstacles1, 100);
        renderObstacles(2, choices2, obstacles2, 100);
        renderObstacles(3, choices3, obstacles3, 100);
//        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
//            bear.updateFrame(false, false);
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
//            bear.updateFrame(false, true);
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
//            bear.updateFrame(true, false);
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
//            bear.updateFrame(true, true);
//
//        }
//
//        // get user touch/mouse inputs
//        if (Gdx.input.isTouched()) {
//            float xTouchPixels = Gdx.input.getX();
//            float yTouchPixels = Gdx.input.getY();
//            // move up
//
//            if (yTouchPixels < 2 * Gdx.graphics.getHeight() / 3)
//                bear.updateFrame(false, false);
//            // move down
//            if (yTouchPixels > Gdx.graphics.getHeight() / 3)
//                bear.updateFrame(false, true);
//            // move right
//            if (xTouchPixels > Gdx.graphics.getWidth() / 2)
//                bear.updateFrame(true, false);
//            //move left
//            if (xTouchPixels < Gdx.graphics.getWidth() / 2)
//                bear.updateFrame(true, true);
//
//        }
        game.batch.begin();
//        bear.draw(game.batch);
        renderPlayer2D();
        game.batch.end();
    }

}
