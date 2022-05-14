package com.pmmc.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.pmmc.app.GameLauncher;
import com.pmmc.app.levelbackgrounds.ScrollingBackground;


/**
 * MainMenu: The first screen presented to the user
 */

public class TestLevel extends Level {

    private ScrollingBackground background;

    public TestLevel(final GameLauncher game){
        super(game);
    }

    @Override
    public void show(){
        this.background = new ScrollingBackground();
    }

    @Override
    public void render(float delta){
        // Set default background to black
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        this.background.updateAndRender(delta, game.batch);
        game.batch.end();
    }

}
