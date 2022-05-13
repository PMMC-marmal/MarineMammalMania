package com.pmmc.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pmmc.app.screen.LevelMenuScreen;
import com.pmmc.app.screen.MainMenuScreen;
import com.pmmc.app.screen.TestLevel;
import com.pmmc.app.screen.TestScreen;

import com.pmmc.app.screen.PolarBearLevel;


public class GameLauncher extends Game {

	private MainMenuScreen mainMenuScreen;
	private LevelMenuScreen levelMenuScreen;

	private PolarBearLevel polarBearLevel;

	public SpriteBatch batch;


	@Override
	public void create () {

		this.mainMenuScreen = new MainMenuScreen(this);
		this.levelMenuScreen = new LevelMenuScreen(this);
		this.polarBearLevel = new PolarBearLevel(this);
		this.batch = new SpriteBatch();

		// Load assets
		AssetHandler.load();
		AssetHandler.assetManager.finishLoading();

		this.setScreen(this.polarBearLevel);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		mainMenuScreen.dispose();
		levelMenuScreen.dispose();
		polarBearLevel.dispose();
		AssetHandler.dispose();
	}
}
