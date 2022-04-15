package com.pmmc.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.pmmc.app.AssetHandler;
import com.pmmc.app.GameLauncher;

import java.util.Random;

/**
 * Menu is a general abstract class that holds common procedures for the different menu screens.
 */

public abstract class Level extends AbstractScreen {
    int spacing = Gdx.graphics.getWidth()/3; // 1000
    final Stage stage;
    ScreenViewport viewPort;

    public Level(GameLauncher game) {
        super(game);

        // Setup camera to be in the center
        float gameWidth = Gdx.graphics.getWidth();
        float gameHeight = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(gameWidth, gameHeight);
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        viewPort = new ScreenViewport(camera);
        stage = new Stage();
        stage.setViewport(viewPort);
    }

    public void renderBackground(Sprite background){
        stage.act();
        stage.getBatch().begin();

        generateBackground(background);
        Sprite icebergBackground = new Sprite(AssetHandler.assetManager.get(AssetHandler.icebergAddon, Texture.class));;
        stage.getBatch().draw(icebergBackground, 0, Gdx.graphics.getHeight()/2 - 200, spacing * 4, Gdx.graphics.getHeight()/2);
        stage.getBatch().end();
        stage.draw();
    }


    public void generateBackground(Sprite background){

        for (int i = 0; i < 30; i ++){
            stage.getBatch().draw(background, spacing * i, -1 * Gdx.graphics.getHeight() + 300, spacing, Gdx.graphics.getHeight());
            background.flip(true,false);
        }
    }

    public boolean[] generateObstacles(){
        boolean[] obstacles1 = {true, false ,true ,true, false ,false ,false, true, true ,false};
        boolean[] obstacles2 = {false, false ,false ,true, true ,false ,true, false, true ,true};
        boolean[] obstacles3 = {true, false ,true ,false, false ,true ,false, true, false ,true};
        boolean[] obstacles4 = {false, true ,false ,true, true ,false ,true, false, true ,false};
        boolean[] obstacles5 = {true, true ,false ,false, true ,false ,true, false, false ,true};
        boolean[][] arrays = {obstacles1,obstacles3,obstacles4,obstacles5};
        int rnd = new Random().nextInt(arrays.length);
        return arrays[rnd];
    }

    public void renderObstacles(int section, Sprite[] spritesArray, boolean[] obstacles){
        int i;
        stage.act();
        stage.getBatch().begin();
        if (section == 2){
            i = 10;
        }
        else if(section == 3){
            i = 20;
        }
        else{
            i = 0 ;
        }

        for (boolean o :obstacles){
            if (o){
                int rnd = new Random().nextInt(2);
                stage.getBatch().draw(spritesArray[rnd], Gdx.graphics.getWidth()/3 * i, 100, Gdx.graphics.getWidth()/3, Gdx.graphics.getHeight()/4);
            }
            i++;
        }

        stage.getBatch().end();
        stage.draw();
    }

    @Override
    public void resize(int width, int height){
        camera.setToOrtho(false, width, height);
        stage.getViewport().update(width, height);
    }
}
