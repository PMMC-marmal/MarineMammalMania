package com.pmmc.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.pmmc.app.AssetHandler;
import com.pmmc.app.GameLauncher;


public class GameOverScreen extends Menu{

    private Sprite continueButton,
            continueButtonActive,
            exitButton,
            exitButtonActive,
            background;

    private static final int BUTTON_WIDTH = Gdx.graphics.getWidth()/4;
    private static final int BUTTON_HEIGHT = Gdx.graphics.getHeight()/12;

    public GameOverScreen(GameLauncher game) { super(game); }

    @Override
    public void show(){
        this.continueButton = new Sprite(AssetHandler.assetManager.get(AssetHandler.continueButton, Texture.class));
        this.continueButtonActive = new Sprite(AssetHandler.assetManager.get(AssetHandler.continueButtonActive, Texture.class));
        this.exitButton = new Sprite(AssetHandler.assetManager.get(AssetHandler.newGameButton, Texture.class));
        this.exitButtonActive = new Sprite(AssetHandler.assetManager.get(AssetHandler.newGameButtonActive, Texture.class));
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

        // Continue Button: If clicked, return to Previous Level Screen
        if(displayButton(continueButton, continueButtonActive, x, y_cont, BUTTON_WIDTH, BUTTON_HEIGHT)){
            transitionScreen(new LevelMenuScreen(game));
        }

        // Exit Button: If clicked, return to LevelSelectScreen
        if (displayButton(exitButton, exitButtonActive, x, y_new, BUTTON_WIDTH, BUTTON_HEIGHT)){
            transitionScreen(new LevelMenuScreen(game));
        }

        game.batch.end();
    }
}
