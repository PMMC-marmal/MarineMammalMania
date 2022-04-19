package com.pmmc.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pmmc.app.AssetHandler;
import com.pmmc.app.GameLauncher;
import com.pmmc.app.character.CharacterAbstraction;
import com.pmmc.app.character.PolarBear;


/**
 * PolarBearLevel: The first level presented to the user
 */

public class PolarBearLevel extends Level {
    private PolarBear bear;
    TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("PolarBearWalkingSpriteSheet.atlas"));
    private Sprite background,
            iceberg2,iceberg3,iceberg4;


    boolean[] obstacles;

    public PolarBearLevel(final GameLauncher game){
        super(game);
        obstacles = generateObstacles();
    }

    @Override
    public void show(){
        TextureRegion textureRegion = textureAtlas.findRegion("1");
        bear = new PolarBear(new Sprite(textureRegion), textureAtlas);
        bear.setPosition(bear.getX_position(), bear.getY_position());
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

        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            bear.updateFrame(false,false);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            bear.updateFrame(false,true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            bear.updateFrame(true,false);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            bear.updateFrame(true, true);
        }

        // get user touch/mouse inputs
        if(Gdx.input.isTouched()){
            float xTouchPixels = Gdx.input.getX();
            float yTouchPixels = Gdx.input.getY();
            // move up
            if(yTouchPixels < 2*Gdx.graphics.getHeight()/3)
                bear.updateFrame(false,false);
            // move down
            if(yTouchPixels > Gdx.graphics.getHeight()/3)
                bear.updateFrame(false,true);
            // move right
            if(xTouchPixels > Gdx.graphics.getWidth()/2)
                bear.updateFrame(true,false);
            //move left
            if(xTouchPixels < Gdx.graphics.getWidth()/2)
                bear.updateFrame(true,true);
        }

        bear.setPosition(bear.getX_position(), bear.getY_position());
        bear.draw(game.batch);

        game.batch.end();
    }

}
