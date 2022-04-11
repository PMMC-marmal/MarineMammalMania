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

public class TestScreen extends Menu{
    private Sprite bear;
    float bear_speed = 1.0f;
    float bear_x = 100;
    float bear_y = 0;
    int currentFrame = 1;
    int MAX_FRAMES = 64;
    TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("PolarBearWalkingSpriteSheet.atlas"));

    public TestScreen(final GameLauncher game){
        super(game);
    }

    @Override
    public void show(){
        TextureRegion textureRegion = textureAtlas.findRegion("1");
        bear = new Sprite(textureRegion);
        bear.setPosition(bear_x, bear_y);
    }

    @Override
    public void render(float delta){
        // Set default background to blue
        Gdx.gl.glClearColor(0,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        // get user key inputs
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            updateFrame(false,false);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            updateFrame(false,true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            updateFrame(true,false);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            updateFrame(true, true);
        }

        // get user touch/mouse inputs
        if(Gdx.input.isTouched()){
            float xTouchPixels = Gdx.input.getX();
            float yTouchPixels = Gdx.input.getY();
            // move up
            if(yTouchPixels < 2*Gdx.graphics.getHeight()/3)
                updateFrame(false,false);
            // move down
            if(yTouchPixels > Gdx.graphics.getHeight()/3)
                updateFrame(false,true);
            // move right
            if(xTouchPixels > Gdx.graphics.getWidth()/2)
                updateFrame(true,false);
            //move left
            if(xTouchPixels < Gdx.graphics.getWidth()/2)
                updateFrame(true,true);
        }

        bear.setPosition(bear_x, bear_y);
        bear.draw(game.batch);

        game.batch.end();
    }

    private void updateFrame(boolean horizontal, boolean flip){
        currentFrame++;
        if(currentFrame >= MAX_FRAMES){
            currentFrame = 1;
        }
        bear.setRegion(textureAtlas.findRegion(Integer.toString(currentFrame/16+1)));

        if(horizontal)
            if(flip){
                bear.flip(true,false);
                bear_x-=Gdx.graphics.getDeltaTime()+bear_speed;
            }
            else{
                bear_x+=Gdx.graphics.getDeltaTime()+bear_speed;
            }
        else{
            if(flip){
                bear_y-=Gdx.graphics.getDeltaTime()+bear_speed;
            }
            else{
                bear_y+=Gdx.graphics.getDeltaTime()+bear_speed;
            }
        }
    }
}
