package com.pmmc.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
    float bear_x = 0;
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
        // Set default background to black
        Gdx.gl.glClearColor(0,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        // draw polar bear
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            bear_y+=Gdx.graphics.getDeltaTime()+bear_speed;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            bear_y-=Gdx.graphics.getDeltaTime()+bear_speed;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            currentFrame++;
            if(currentFrame >= MAX_FRAMES){
                currentFrame = 1;
            }
            bear.setRegion(textureAtlas.findRegion(Integer.toString(currentFrame/16+1)));
            bear_x+=Gdx.graphics.getDeltaTime()+bear_speed;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            currentFrame++;
            if(currentFrame >= MAX_FRAMES){
                currentFrame = 1;
            }
            bear.setRegion(textureAtlas.findRegion(Integer.toString(currentFrame/16+1)));
            bear.flip(true,false);
            bear_x-=Gdx.graphics.getDeltaTime()+bear_speed;
        }
        bear.setPosition(bear_x, bear_y);
        bear.draw(game.batch);

        game.batch.end();
    }

}
