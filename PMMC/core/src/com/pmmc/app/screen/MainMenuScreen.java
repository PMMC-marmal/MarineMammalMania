package com.pmmc.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.pmmc.app.GameLauncher;


/**
 * MainMenu: The first screen presented to the user
 */

public class MainMenuScreen extends Menu {

    private final Texture continueButton;
    private final Texture continueButtonActive;
    private final Texture newGameButton;
    private final Texture newGameButtonActive;
    private final Texture background;

    private static final int BUTTON_WIDTH = Gdx.graphics.getWidth()/4;
    private static final int BUTTON_HEIGHT = Gdx.graphics.getHeight()/12;

    public MainMenuScreen(final GameLauncher game){
        super(game);
        this.continueButton = new Texture("continue_button.png");
        this.continueButtonActive = new Texture("continue_button_active.png");
        this.newGameButton = new Texture("new_game_button.png");
        this.newGameButtonActive = new Texture("new_game_button_active.png");
        this.background = new Texture("mainmenu_background.png");
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
        renderBackground(background);

        game.batch.begin();

        // Continue Button: If clicked, proceed to LevelSelectScreen
        if(displayButton(continueButton, continueButtonActive, x, y_cont, BUTTON_WIDTH, BUTTON_HEIGHT)){
            game.setScreen(new LevelMenuScreen(game));
        }

        // New Game Button: If clicked, restart progress and proceed to LevelSelectScreen
        if (displayButton(newGameButton, newGameButtonActive, x, y_new, BUTTON_WIDTH, BUTTON_HEIGHT)){
            game.setScreen(new LevelMenuScreen(game));
        }

        game.batch.end();
    }

}
