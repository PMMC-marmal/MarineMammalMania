package com.pmmc.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pmmc.app.screen.LevelSelectScreen;
import com.pmmc.app.screen.MainMenuScreen;

public class GameLauncher extends Game {

	private MainMenuScreen mainMenuScreen;
	private LevelSelectScreen levelSelectScreen;

	public SpriteBatch batch;

	@Override
	public void create () {
		this.mainMenuScreen = new MainMenuScreen(this);
		this.levelSelectScreen = new LevelSelectScreen(this);
		this.batch = new SpriteBatch();

		this.setScreen(mainMenuScreen);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		mainMenuScreen.dispose();
	}
}
