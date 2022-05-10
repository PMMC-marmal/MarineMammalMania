package com.pmmc.app.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pmmc.app.GameLauncher;
import com.pmmc.app.transitions.FadeIn;
import com.pmmc.app.transitions.FadeOut;
import com.pmmc.app.transitions.TransitionEffect;

import java.util.ArrayList;

/**
 * AbstractScreen is a template for game states
 */
public abstract class AbstractScreen implements Screen {
    GameLauncher game;
    OrthographicCamera camera;

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
        effects.add(new FadeOut(1));
        effects.add(new FadeIn(1));

        game.setScreen(new TransitionScreen(game, current, next, effects));
    }
}
