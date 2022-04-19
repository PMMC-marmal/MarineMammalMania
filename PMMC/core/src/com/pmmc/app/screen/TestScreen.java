package com.pmmc.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
    private TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("PolarBearWalkingSpriteSheet.atlas"));
    // camera
    private OrthographicCamera camera;
    private Sprite background;

    public TestScreen(final GameLauncher game){
        super(game);
    }

    @Override
    public void show(){
        // camera screen size
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.background = new Sprite(AssetHandler.assetManager.get(AssetHandler.levelMenuBackground, Texture.class));
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
        // updates the camera
        camera.update();
        // links camera to screen
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        // adjusted due to the background being drawn at 0,0
        game.batch.draw(background, -Gdx.graphics.getWidth()/2,-Gdx.graphics.getHeight()/2,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // get user key inputs
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            bear.updateFrame(false,false);
            // moves camera
            camera.translate(0, bear.getSpeed());
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            bear.updateFrame(false,true);
            camera.translate(0, -bear.getSpeed());
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            bear.updateFrame(true,false);
            camera.translate(bear.getSpeed(), 0);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            bear.updateFrame(true, true);
            camera.translate(-bear.getSpeed(), 0);
        }

        // get user touch/mouse inputs
        if(Gdx.input.isTouched()){
            float xTouchPixels = Gdx.input.getX();
            float yTouchPixels = Gdx.input.getY();
            // move up
            if(yTouchPixels < 2*Gdx.graphics.getHeight()/3) {
                bear.updateFrame(false, false);
                camera.translate(0, bear.getSpeed());
            }
            // move down
            if(yTouchPixels > Gdx.graphics.getHeight()/3) {
                bear.updateFrame(false, true);
                camera.translate(0, -bear.getSpeed());
            }
            // move right
            if(xTouchPixels > Gdx.graphics.getWidth()/2){
                bear.updateFrame(true,false);
                camera.translate(bear.getSpeed(), 0);
            }
            //move left
            if(xTouchPixels < Gdx.graphics.getWidth()/2) {
                bear.updateFrame(true, true);
                camera.translate(-bear.getSpeed(), 0);
            }
        }

        bear.setPosition(bear.getX_position(), bear.getY_position());
        bear.draw(game.batch);

        game.batch.end();
    }
}

