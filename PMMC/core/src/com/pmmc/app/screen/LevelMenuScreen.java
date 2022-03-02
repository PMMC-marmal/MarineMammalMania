package com.pmmc.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.pmmc.app.GameLauncher;

/**
 * Screen for level selection after the MainMenuScreen
 */
public class LevelMenuScreen extends Menu {

    private final Texture seaLionButton;
    private final Texture seaLionButtonActive;
    private final Texture dolphinButton;
    private final Texture dolphinButtonActive;
    private final Texture killerWhaleButton;
    private final Texture killerWhaleButtonActive;
    private final Texture blueWhaleButton;
    private final Texture blueWhaleButtonActive;
    private final Texture polarBearButton;
    private final Texture polarBearButtonActive;
    private final Texture background;
    private final Texture backArrow;

    private final Texture seaLionTexture;
    private final Texture dolphinTexture;
    private final Texture killerWhaleTexture;
    private final Texture blueWhaleTexture;
    private final Texture polarBearTexture;

    private final float BUTTON_WIDTH = Gdx.graphics.getWidth() * 0.8f;
    private final float BUTTON_HEIGHT = Gdx.graphics.getHeight() * 0.15f;
    private final float BUTTON_SPACING = BUTTON_HEIGHT * 1.10f;

    public LevelMenuScreen(GameLauncher game){
        super(game);
        this.seaLionButton = new Texture("sea_lion_button.png");
        this.seaLionButtonActive = new Texture("sea_lion_button_active.png");
        this.dolphinButton = new Texture("dolphin_button.png");
        this.dolphinButtonActive = new Texture("dolphin_button_active.png");
        this.killerWhaleButton = new Texture("killer_whale_button.png");
        this.killerWhaleButtonActive = new Texture("killer_whale_button_active.png");
        this.blueWhaleButton = new Texture("blue_whale_button.png");
        this.blueWhaleButtonActive = new Texture("blue_whale_button_active.png");
        this.polarBearButton = new Texture("polar_bear_button.png");
        this.polarBearButtonActive = new Texture("polar_bear_button_active.png");
        this.background = new Texture("level_select_background.png");
        this.backArrow = new Texture("back_arrow.png");

        this.seaLionTexture = new Texture("sea_lion.png");
        this.dolphinTexture = new Texture("dolphin.png");
        this.killerWhaleTexture = new Texture("killer_whale.png");
        this.blueWhaleTexture = new Texture("blue_whale.png");
        this.polarBearTexture = new Texture("polar_bear.png");
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
        displayButton(blueWhaleButton, blueWhaleButtonActive, x, y_sea_lion - BUTTON_SPACING*3, BUTTON_WIDTH, BUTTON_HEIGHT);
        displayButton(polarBearButton, polarBearButtonActive, x, y_sea_lion - BUTTON_SPACING*4, BUTTON_WIDTH, BUTTON_HEIGHT);

        // Draw characters on screen
        game.batch.draw(seaLionTexture,
                Gdx.graphics.getWidth() - Gdx.graphics.getWidth()/5f,
                Gdx.graphics.getHeight() - BUTTON_SPACING*1.5f,
                Gdx.graphics.getWidth()/5f,
                Gdx.graphics.getHeight()/6f
        );

        game.batch.draw(dolphinTexture,
                0,
                Gdx.graphics.getHeight() - (BUTTON_HEIGHT*2) - BUTTON_SPACING,
                Gdx.graphics.getWidth()/6f,
                Gdx.graphics.getHeight()/4f
        );

        game.batch.draw(killerWhaleTexture,
                Gdx.graphics.getWidth() - Gdx.graphics.getWidth()/6f,
                Gdx.graphics.getHeight() - (BUTTON_HEIGHT*3) - (BUTTON_SPACING*2),
                Gdx.graphics.getWidth()/5f,
                Gdx.graphics.getHeight()/3f
        );

        game.batch.draw(blueWhaleTexture,
                0 - Gdx.graphics.getWidth()/4f,
                BUTTON_HEIGHT*1.25f,
                Gdx.graphics.getWidth()/2f,
                Gdx.graphics.getHeight()/2f
        );

        game.batch.draw(polarBearTexture,
                Gdx.graphics.getWidth() - Gdx.graphics.getWidth()/4f,
                Gdx.graphics.getHeight()/20f,
                Gdx.graphics.getWidth()/6f,
                Gdx.graphics.getHeight()/5f
        );

        game.batch.end();
    }
}
