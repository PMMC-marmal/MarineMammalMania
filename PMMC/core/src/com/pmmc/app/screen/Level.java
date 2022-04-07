package com.pmmc.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.pmmc.app.GameLauncher;

public abstract class Level extends AbstractScreen{
    final Stage stage;
    ScreenViewport viewPort;

    public Level(GameLauncher game){
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
        stage.getBatch().draw(background, 0, 0);
        stage.getBatch().end();
        stage.draw();
    }

    public void displayPlayerUI(){

    }

    public void displayCharacter(){

    }

    public void displayTerrain(){

    }

    @Override
    public void resize(int width, int height){
        camera.setToOrtho(false, width, height);
        stage.getViewport().update(width, height);
    }
}
