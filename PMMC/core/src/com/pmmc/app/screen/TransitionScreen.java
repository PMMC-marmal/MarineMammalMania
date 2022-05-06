package com.pmmc.app.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.pmmc.app.transitions.TransitionEffect;

import java.util.ArrayList;

public class TransitionScreen implements Screen {

    Game game;
    Screen current;
    Screen next;
    int currentTransition;
    ArrayList<TransitionEffect> transitionEffects;

    public TransitionScreen(Game game, Screen current, Screen next, ArrayList<TransitionEffect> transitionEffects){
        this.game = game;
        this.current = current;
        this.next = next;
        this.transitionEffects = transitionEffects;
        this.currentTransition = 0;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // if more than 1 transition effect
        if (currentTransition >= transitionEffects.size()) {
            game.setScreen(next);
            return;
        }
        transitionEffects.get(currentTransition).update(Gdx.graphics.getDeltaTime());
        transitionEffects.get(currentTransition).render(this.current, this.next);

        // once current effect ends move to next if there is one
        if (transitionEffects.get(currentTransition).isFinished())
            currentTransition++;
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
}
