package com.pmmc.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.pmmc.app.AssetHandler;
import com.pmmc.app.GameLauncher;
import com.pmmc.app.transitions.FadeIn;
import com.pmmc.app.transitions.FadeOut;
import com.pmmc.app.transitions.TransitionEffect;

import java.util.ArrayList;


/**
 * MainMenu: The first screen presented to the user
 */

public class MainMenuScreen extends Menu {

    private Sprite continueButton,
            continueButtonActive,
            newGameButton,
            newGameButtonActive,
            background;

    private static final int BUTTON_WIDTH = Gdx.graphics.getWidth()/4;
    private static final int BUTTON_HEIGHT = Gdx.graphics.getHeight()/12;

    public MainMenuScreen(final GameLauncher game){
        super(game);
    }

    @Override
    public void show(){
        this.continueButton = new Sprite(AssetHandler.assetManager.get(AssetHandler.continueButton, Texture.class));
        this.continueButtonActive = new Sprite(AssetHandler.assetManager.get(AssetHandler.continueButtonActive, Texture.class));
        this.newGameButton = new Sprite(AssetHandler.assetManager.get(AssetHandler.newGameButton, Texture.class));
        this.newGameButtonActive = new Sprite(AssetHandler.assetManager.get(AssetHandler.newGameButtonActive, Texture.class));
        this.background = new Sprite(AssetHandler.assetManager.get(AssetHandler.mainMenuBackground, Texture.class));
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
            transitionScreen(new LevelMenuScreen(game));
        }

        // New Game Button: If clicked, restart progress and proceed to LevelSelectScreen
        if (displayButton(newGameButton, newGameButtonActive, x, y_new, BUTTON_WIDTH, BUTTON_HEIGHT)){
            resetProgress();
            transitionScreen(new LevelMenuScreen(game));
        }

        game.batch.end();
    }

    public void resetProgress(){
        Preferences pref = Gdx.app.getPreferences("Quiz Results");
        pref.clear();
        pref.flush();
    }

    @Override
    public void dispose() {
        super.dispose();
        Gdx.input.setInputProcessor(null);
    }
}
