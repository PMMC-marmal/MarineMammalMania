package com.pmmc.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.pmmc.app.AssetHandler;
import com.pmmc.app.GameLauncher;

/**
 * Screen for level selection after the MainMenuScreen
 */
public class LevelMenuScreen extends Menu {

    private final float BUTTON_WIDTH = Gdx.graphics.getWidth() * 0.8f;
    private final float BUTTON_HEIGHT = Gdx.graphics.getHeight() * 0.15f;
    private final float BUTTON_SPACING = BUTTON_HEIGHT * 1.10f;
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

    public LevelMenuScreen(GameLauncher game) {
        super(game);
    }

    @Override
    public void show() {
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

        killerWhaleSprite.flip(true,false);
        polarBearSprite.flip(true,false);
    }

    @Override
    public void render(float delta) {
        renderBackground(background);

        // Positional data for the button's coordinates
        float x = (Gdx.graphics.getWidth() / 2f) - (BUTTON_WIDTH / 2f);
        float y_sea_lion = Gdx.graphics.getHeight() - BUTTON_SPACING * 1.5f;

        // Back arrow width + height
        float backArrowWidth = Gdx.graphics.getWidth() / 12f;
        float backArrowHeight = Gdx.graphics.getWidth() / 14f;

        game.batch.begin();

        // Display back arrow. If clicked, return to main menu
        if (displayButton(backArrow, 0, Gdx.graphics.getHeight() - BUTTON_SPACING / 1.25f, backArrowWidth, backArrowHeight)) {
            transitionScreen(new MainMenuScreen(game));
        }


        if (displayButton(seaLionButton, seaLionButtonActive, x, y_sea_lion, BUTTON_WIDTH, BUTTON_HEIGHT)) {
            transitionScreen(new SeaLionLevel(game));
        }
        if (displayButton(dolphinButton, dolphinButtonActive, x, y_sea_lion - BUTTON_SPACING, BUTTON_WIDTH, BUTTON_HEIGHT)) {
            transitionScreen(new DolphinLevel(game));
        }
        if (displayButton(killerWhaleButton, killerWhaleButtonActive, x, y_sea_lion - BUTTON_SPACING * 2, BUTTON_WIDTH, BUTTON_HEIGHT)) {
            transitionScreen(new OrcaLevel(game));
        }
        if (displayButton(blueWhaleButton, blueWhaleButtonActive, x, y_sea_lion - BUTTON_SPACING * 3, BUTTON_WIDTH, BUTTON_HEIGHT)) {
            transitionScreen(new BlueWhaleLevel(game));
        }
        if (displayButton(polarBearButton, polarBearButtonActive, x, y_sea_lion - BUTTON_SPACING * 4, BUTTON_WIDTH, BUTTON_HEIGHT)) {
            transitionScreen(new PolarBearLevel(game));
        }

        // Draw characters on screen
        game.batch.draw(seaLionSprite, Gdx.graphics.getWidth() *.80f, Gdx.graphics.getHeight()*.70f);

        game.batch.draw(dolphinSprite, Gdx.graphics.getWidth()*-.08f, Gdx.graphics.getHeight() *.60f);

        game.batch.draw(killerWhaleSprite, Gdx.graphics.getWidth()*.75f, Gdx.graphics.getHeight()*.37f);

        game.batch.draw(blueWhaleSprite, 0 - Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight()*.12f);

        game.batch.draw(polarBearSprite, Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight()*.08f);

        game.batch.end();
    }
}
