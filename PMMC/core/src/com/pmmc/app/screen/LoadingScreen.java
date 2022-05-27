package com.pmmc.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.pmmc.app.AssetHandler;
import com.pmmc.app.GameLauncher;

public class LoadingScreen extends AbstractScreen {

    Sprite loadingScreen;
    public BitmapFont font;


    public LoadingScreen(GameLauncher game) {
        super(game);
        AssetHandler.load();
        loadingScreen = new Sprite(new Texture("backgrounds/level_select_background.png"));
        this.font = generateFont(40,Color.WHITE);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        game.batch.begin();
        game.batch.draw(loadingScreen, 0, 0);
        font.draw(game.batch, "Loading...", (Gdx.graphics.getWidth()/20f), (Gdx.graphics.getHeight()/12f));
        game.batch.end();
        if(AssetHandler.assetManager.update()){
            game.setScreen(new MainMenuScreen(game));
        }
    }
}
