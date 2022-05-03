package com.pmmc.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureArray;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pmmc.app.AssetHandler;
import com.pmmc.app.GameLauncher;
import com.pmmc.app.character.PolarBear;

public class TestScreen extends Menu{
    private PolarBear bear;
    // the different frames of the animation
    TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("PolarBearWalkingSpriteSheet.atlas"));

    public TestScreen(final GameLauncher game){
        super(game);
    }

    @Override
    public void show(){
        // gets the first frame of the animation
        TextureRegion textureRegion = textureAtlas.findRegion("1");
        bear = new PolarBear(new Sprite(textureRegion), textureAtlas);
        bear.setPosition(bear.getX_position(), bear.getY_position());
    }

    @Override
    public void render(float delta){
        // Set default background to blue
        Gdx.gl.glClearColor(0,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        // get user key inputs
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            bear.updateFrame(false,false,false);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            bear.updateFrame(false,true,false);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            bear.updateFrame(true,false,false);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            bear.updateFrame(true, true,false);
        }

        // get user touch/mouse inputs
        if(Gdx.input.isTouched()){
            float xTouchPixels = Gdx.input.getX();
            float yTouchPixels = Gdx.input.getY();
            // move up
            if(yTouchPixels < 2*Gdx.graphics.getHeight()/3)
                bear.updateFrame(false,false,false);
            // move down
            if(yTouchPixels > Gdx.graphics.getHeight()/3)
                bear.updateFrame(false,true,false);
            // move right
            if(xTouchPixels > Gdx.graphics.getWidth()/2)
                bear.updateFrame(true,false,false);
            //move left
            if(xTouchPixels < Gdx.graphics.getWidth()/2)
                bear.updateFrame(true,true,false);
        }

        bear.setPosition(bear.getX_position(), bear.getY_position());
        bear.draw(game.batch);

        game.batch.end();
    }
}

