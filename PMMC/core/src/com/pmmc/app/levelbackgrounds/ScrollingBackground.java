package com.pmmc.app.levelbackgrounds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pmmc.app.AssetHandler;

public class ScrollingBackground {

    public static final int DEFAULT_SPEED = 200;
    public static final int ACCELERATION = 50;
    public static final int GOAL_REACH_ACCELERATION = 200;

    Texture image;
    float x1, x2;
    int speed;//In pixels / second
    int goalSpeed;
    boolean speedFixed;

    public ScrollingBackground () {
        image = new Texture(AssetHandler.mainMenuBackground);

        x1 = 0;
        x2 = Gdx.graphics.getWidth();
        speed = 0;
        goalSpeed = DEFAULT_SPEED;
        speedFixed = true;
    }

    public void updateAndRender (float deltaTime, SpriteBatch batch) {
        //Speed adjustment to reach goal
        if (speed < goalSpeed) {
            speed += GOAL_REACH_ACCELERATION * deltaTime;
            if (speed > goalSpeed)
                speed = goalSpeed;
        } else if (speed > goalSpeed) {
            speed -= GOAL_REACH_ACCELERATION * deltaTime;
            if (speed < goalSpeed)
                speed = goalSpeed;
        }

        if (!speedFixed)
            speed += ACCELERATION * deltaTime;

        x1 -= speed * deltaTime;
        x2 -= speed * deltaTime;

        //if image reached the bottom of screen and is not visible, put it back on top
        if (x1 + Gdx.graphics.getWidth() <= 0)
            x1 = x2 + Gdx.graphics.getWidth();

        if (x2 + Gdx.graphics.getWidth() <= 0)
            x2 = x1 + Gdx.graphics.getWidth();

        //Render
        batch.draw(image, x1, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(image, x2, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void setSpeed (int goalSpeed) {
        this.goalSpeed = goalSpeed;
    }

    public void setSpeedFixed (boolean speedFixed) {
        this.speedFixed = speedFixed;
    }

}