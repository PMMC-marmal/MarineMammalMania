package com.pmmc.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.pmmc.app.AssetHandler;
import com.pmmc.app.GameLauncher;
import com.pmmc.app.transitions.FadeIn;
import com.pmmc.app.transitions.FadeOut;
import com.pmmc.app.transitions.TransitionEffect;

import java.util.ArrayList;

/**
 * Screen for level selection after the MainMenuScreen
 */
public class LevelMenuScreen extends Menu {

    private Sprite seaLionButton,
                    seaLionButtonActive,
                    dolphinButton,
                    dolphinButtonActive,
                    killerWhaleButton,
                    killerWhaleButtonActive,
                    blueWhaleButton,
                    blueWhaleButtonActive, polarBearButton,
                    polarBearButtonActive,
                    background,
                    backArrow,
                    seaLionSprite,
                    dolphinSprite,
                    killerWhaleSprite,
                    blueWhaleSprite,
                    polarBearSprite;

    private final float BUTTON_WIDTH = Gdx.graphics.getWidth() * 0.8f;
    private final float BUTTON_HEIGHT = Gdx.graphics.getHeight() * 0.15f;
    private final float BUTTON_SPACING = BUTTON_HEIGHT * 1.10f;

    public LevelMenuScreen(GameLauncher game){
        super(game);
    }

    @Override
    public void show(){
        // Buttons
        this.seaLionButton = new Sprite(AssetHandler.assetManager.get(AssetHandler.seaLionButton, Texture.class));
        this.seaLionButtonActive = new Sprite(AssetHandler.assetManager.get(AssetHandler.seaLionButtonActive, Texture.class));
        this.dolphinButton = new Sprite(AssetHandler.assetManager.get(AssetHandler.dolphinButton, Texture.class));
        this.dolphinButtonActive = new Sprite(AssetHandler.assetManager.get(AssetHandler.dolphinButtonActive, Texture.class));
        this.killerWhaleButton = new Sprite(AssetHandler.assetManager.get(AssetHandler.killerWhaleButton, Texture.class));
        this.killerWhaleButtonActive = new Sprite(AssetHandler.assetManager.get(AssetHandler.killerWhaleButtonActive, Texture.class));
        this.blueWhaleButton = new Sprite(AssetHandler.assetManager.get(AssetHandler.blueWhaleButton, Texture.class));
        this.blueWhaleButtonActive = new Sprite(AssetHandler.assetManager.get(AssetHandler.blueWhaleButtonActive, Texture.class));
        this.polarBearButton = new Sprite(AssetHandler.assetManager.get(AssetHandler.polarBearButton, Texture.class));
        this.polarBearButtonActive = new Sprite(AssetHandler.assetManager.get(AssetHandler.polarBearButtonActive, Texture.class));
        this.background = new Sprite(AssetHandler.assetManager.get(AssetHandler.levelMenuBackground, Texture.class));
        this.backArrow = new Sprite(AssetHandler.assetManager.get(AssetHandler.backArrow, Texture.class));

        // Characters
        this.seaLionSprite = new Sprite(AssetHandler.assetManager.get(AssetHandler.seaLionSprite, Texture.class));
        this.dolphinSprite = new Sprite(AssetHandler.assetManager.get(AssetHandler.dolphinSprite, Texture.class));
        this.killerWhaleSprite = new Sprite(AssetHandler.assetManager.get(AssetHandler.killerWhaleSprite, Texture.class));
        this.blueWhaleSprite = new Sprite(AssetHandler.assetManager.get(AssetHandler.blueWhaleSprite, Texture.class));
        this.polarBearSprite = new Sprite(AssetHandler.assetManager.get(AssetHandler.polarBearSprite, Texture.class));
    }

    @Override
    public void render(float delta){
        renderBackground(background);

        // Positional data for the button's coordinates
        float x = (Gdx.graphics.getWidth()/2f) - (BUTTON_WIDTH / 2f);
        float y_sea_lion = Gdx.graphics.getHeight() - BUTTON_SPACING*1.5f;

        // Back arrow width + height
        float backArrowWidth = Gdx.graphics.getWidth()/12f;
        float backArrowHeight = Gdx.graphics.getWidth()/14f;

        game.batch.begin();

        // Display back arrow. If clicked, return to main menu
        if(displayButton(backArrow, 0, Gdx.graphics.getHeight() - BUTTON_SPACING / 1.25f, backArrowWidth, backArrowHeight)){
            game.setScreen(new MainMenuScreen(game));
        }


        displayButton(seaLionButton, seaLionButtonActive, x, y_sea_lion, BUTTON_WIDTH, BUTTON_HEIGHT);
        displayButton(dolphinButton, dolphinButtonActive, x, y_sea_lion - BUTTON_SPACING, BUTTON_WIDTH, BUTTON_HEIGHT);
        displayButton(killerWhaleButton, killerWhaleButtonActive, x, y_sea_lion - BUTTON_SPACING*2, BUTTON_WIDTH, BUTTON_HEIGHT);
        if (displayButton(blueWhaleButton, blueWhaleButtonActive, x, y_sea_lion - BUTTON_SPACING*3, BUTTON_WIDTH, BUTTON_HEIGHT)){
        game.setScreen(new BlueWhaleLevel(game));
    }
        if(displayButton(polarBearButton, polarBearButtonActive, x, y_sea_lion - BUTTON_SPACING*4, BUTTON_WIDTH, BUTTON_HEIGHT)){
            Screen current = game.getScreen();
            Screen next = new PolarBearLevel(game);
            // not sure why but if next screen is not set beforehand, exception happens
            game.setScreen(next);
            ArrayList<TransitionEffect> effects = new ArrayList<TransitionEffect>();
            effects.add(new FadeOut(1));
            effects.add(new FadeIn(1));

            game.setScreen(new TransitionScreen(game, current, next, effects));
        }

        // Continue Button: If clicked, proceed to LevelSelectScreen

        // Draw characters on screen
        game.batch.draw(seaLionSprite,
                Gdx.graphics.getWidth() - Gdx.graphics.getWidth()/5f,
                Gdx.graphics.getHeight() - BUTTON_SPACING*1.5f,
                Gdx.graphics.getWidth()/5f,
                Gdx.graphics.getHeight()/6f
        );

        game.batch.draw(dolphinSprite,
                0,
                Gdx.graphics.getHeight() - (BUTTON_HEIGHT*2) - BUTTON_SPACING,
                Gdx.graphics.getWidth()/6f,
                Gdx.graphics.getHeight()/4f
        );

        game.batch.draw(killerWhaleSprite,
                Gdx.graphics.getWidth() - Gdx.graphics.getWidth()/6f,
                Gdx.graphics.getHeight() - (BUTTON_HEIGHT*3) - (BUTTON_SPACING*2),
                Gdx.graphics.getWidth()/5f,
                Gdx.graphics.getHeight()/3f
        );

        game.batch.draw(blueWhaleSprite,
                0 - Gdx.graphics.getWidth()/4f,
                BUTTON_HEIGHT*1.25f,
                Gdx.graphics.getWidth()/2f,
                Gdx.graphics.getHeight()/2f
        );

        game.batch.draw(polarBearSprite,
                Gdx.graphics.getWidth() - Gdx.graphics.getWidth()/4f,
                Gdx.graphics.getHeight()/20f,
                Gdx.graphics.getWidth()/6f,
                Gdx.graphics.getHeight()/5f
        );

        game.batch.end();
    }
}
