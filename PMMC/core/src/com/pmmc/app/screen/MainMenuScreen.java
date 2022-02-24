package com.pmmc.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.pmmc.app.GameLauncher;

import org.graalvm.compiler.phases.common.NodeCounterPhase;

/**
 * MainMenu: The first screen presented to the user
 */

public class MainMenuScreen extends AbstractScreen{

    private Texture continueButton;
    private Texture continueButtonActive;
    private Texture newGameButton;
    private Texture newGameButtonActive;
    private Texture background;

    private Stage stage;
    private ScreenViewport viewPort;

    private static final int BUTTON_WIDTH = Gdx.graphics.getWidth()/4;
    private static final int BUTTON_HEIGHT = Gdx.graphics.getHeight()/12;

    public MainMenuScreen(final GameLauncher game){
        super(game);
        this.continueButton = new Texture("continue_button.png");
        this.continueButtonActive = new Texture("continue_button_active.png");
        this.newGameButton = new Texture("new_game_button.png");
        this.newGameButtonActive = new Texture("new_game_button_active.png");
        this.background = new Texture("mainmenu_background.png");

        // Setup camera to be in the center
        float gameWidth = Gdx.graphics.getWidth();
        float gameHeight = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(gameWidth, gameHeight);
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        viewPort = new ScreenViewport(camera);
        stage = new Stage();
        stage.setViewport(viewPort);
    }

    public void renderBackground(){
        stage.act();
        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getBatch().end();
        stage.draw();
    }

    @Override
    public void resize(int width, int height){
        camera.setToOrtho(false, width, height);
        stage.getViewport().update(width, height);
    }

    @Override
    public void render(float delta){
        // Set default background to black
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Positional data for the button's coordinates
        float x = (Gdx.graphics.getWidth()/2f) - (BUTTON_WIDTH / 2f);
        float y_cont = (Gdx.graphics.getHeight()/2f) - (BUTTON_HEIGHT / 2f);
        float y_new = (Gdx.graphics.getHeight()/2f) - (BUTTON_HEIGHT * 2);

        // Add background
        renderBackground();

        game.batch.begin();

        // Continue Button
        if ((Gdx.input.getX() > x) && (Gdx.input.getX() < (x + BUTTON_WIDTH)) &&
                (Gdx.input.getY() > y_cont) && (Gdx.input.getY() < (y_cont + BUTTON_HEIGHT))
        ){
            game.batch.draw(continueButtonActive, x, y_cont, BUTTON_WIDTH, BUTTON_HEIGHT);

            // If clicked...
            if (Gdx.input.isTouched()){
                // Navigate to level select screen
                game.setScreen(new LevelSelectScreen(game));
            }
        }
        else{
            game.batch.draw(continueButton, x, y_cont, BUTTON_WIDTH, BUTTON_HEIGHT);
        }

        // New Game Button
        if ((Gdx.input.getX() > x) && (Gdx.input.getX() < (x + BUTTON_WIDTH)) &&
                (Gdx.input.getY() < (Gdx.graphics.getHeight() - y_new)) && (Gdx.input.getY() > (Gdx.graphics.getHeight() - y_new - BUTTON_HEIGHT))
        ){
            game.batch.draw(newGameButtonActive, x, y_new, BUTTON_WIDTH, BUTTON_HEIGHT);

            // If clicked...
            if (Gdx.input.isTouched()){
                // Erase previous data, then navigate to level select screen
                game.setScreen(new LevelSelectScreen(game));
            }
        }
        else{
            game.batch.draw(newGameButton, x, y_new, BUTTON_WIDTH, BUTTON_HEIGHT);
        }

        game.batch.end();
    }

}
