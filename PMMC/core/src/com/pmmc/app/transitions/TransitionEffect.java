package com.pmmc.app.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public abstract class TransitionEffect {
    float elapsedTime = 0;
    float duration;

    TransitionEffect(float duration) {
        this.duration = duration;
    };

    protected float getAlpha() {
        return elapsedTime/duration;
    };

    public void update(float delta) {
        elapsedTime += delta;
    };

    public void render(Screen current, Screen next){
    };

    public boolean isFinished() {
        if(getAlpha() >= 1)
            return true;
        return false;
    };
}
