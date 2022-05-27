package com.pmmc.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.pmmc.app.GameLauncher;
import com.pmmc.app.transitions.FadeIn;
import com.pmmc.app.transitions.FadeOut;
import com.pmmc.app.transitions.TransitionEffect;

import java.util.ArrayList;

/**
 * AbstractScreen is a template for game states
 */
public abstract class AbstractScreen implements Screen {
    public GameLauncher game;
    public OrthographicCamera camera;

    public AbstractScreen(GameLauncher game){
        this.game = game;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public void transitionScreen(Screen next){
        Screen current = game.getScreen();
        // not sure why but if next screen is not set beforehand, exception happens
        game.setScreen(next);
        ArrayList<TransitionEffect> effects = new ArrayList<TransitionEffect>();
        effects.add(new FadeOut(0.5f));
        effects.add(new FadeIn(0.5f));

        game.setScreen(new TransitionScreen(game, current, next, effects));
    }

    public BitmapFont generateFont(int size, Color color){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/Gila-qZBPV.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameters = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameters.genMipMaps = true;
        parameters.color = color;
        //parameters.size = (int)Math.ceil(size);
        parameters.size = size * Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
        parameters.magFilter = Texture.TextureFilter.MipMapLinearNearest;
        parameters.minFilter = Texture.TextureFilter.Nearest;
        generator.scaleForPixelHeight((int)Math.ceil(size));

        return generator.generateFont(parameters);
    }
}
