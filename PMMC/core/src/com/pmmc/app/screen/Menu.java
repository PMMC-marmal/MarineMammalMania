package com.pmmc.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.pmmc.app.GameLauncher;

/**
 * Menu is a general abstract class that holds common procedures for the different menu screens.
 */

public abstract class Menu extends AbstractScreen {

    final Stage stage;
    ScreenViewport viewPort;

    public Menu(GameLauncher game) {
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
        stage.getBatch().draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getBatch().end();
        stage.draw();
    }

    public boolean displayButton(Sprite button, Sprite buttonActive, float x, float y, float buttonWidth, float buttonHeight){
        // Input coordinate system is flipped from the graphical coordinate system, must flip the y to match
        float yConverted = Gdx.graphics.getHeight() - y - buttonHeight;

        if ((Gdx.input.getX() > x) && (Gdx.input.getX() < (x + buttonWidth)) &&
                (Gdx.input.getY() > yConverted) && (Gdx.input.getY() < (yConverted + buttonHeight))
        ){
            game.batch.draw(buttonActive, x, y, buttonWidth, buttonHeight);

            // Return true if button is clicked
            return Gdx.input.isTouched();
        }
        else{
            game.batch.draw(button, x, y, buttonWidth, buttonHeight);
        }
        return false;
    }

    public boolean displayButton(Sprite button, float x, float y, float buttonWidth, float buttonHeight){
        // Input coordinate system is flipped from the graphical coordinate system, must flip the y to match
        float yConverted = Gdx.graphics.getHeight() - y - buttonHeight;

        game.batch.draw(button, x, y, buttonWidth, buttonHeight);

        // Return true if button is clicked
        return (Gdx.input.getX() > x) && (Gdx.input.getX() < (x + buttonWidth)) &&
                (Gdx.input.getY() > yConverted) && (Gdx.input.getY() < (yConverted + buttonHeight)
                && Gdx.input.isTouched());
    }

    @Override
    public void resize(int width, int height){
        camera.setToOrtho(false, width, height);
        stage.getViewport().update(width, height);
    }
}
