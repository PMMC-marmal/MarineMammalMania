package com.pmmc.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pmmc.app.screen.LevelMenuScreen;
import com.pmmc.app.screen.MainMenuScreen;
<<<<<<< Updated upstream
import com.pmmc.app.screen.TestLevel;
=======
import com.pmmc.app.screen.TestScreen;
>>>>>>> Stashed changes

public class GameLauncher extends Game {

	private MainMenuScreen mainMenuScreen;
	private LevelMenuScreen levelMenuScreen;
<<<<<<< Updated upstream
	private TestLevel testLevel;
=======
	private TestScreen testScreen;

>>>>>>> Stashed changes
	public SpriteBatch batch;

	@Override
	public void create () {
		this.mainMenuScreen = new MainMenuScreen(this);
		this.levelMenuScreen = new LevelMenuScreen(this);
<<<<<<< Updated upstream
		this.testLevel = new TestLevel(this);
=======
		this.testScreen = new TestScreen(this);
>>>>>>> Stashed changes
		this.batch = new SpriteBatch();

		// Load assets
		AssetHandler.load();
		AssetHandler.assetManager.finishLoading();

<<<<<<< Updated upstream
		this.setScreen(testLevel);
=======
		this.setScreen(testScreen);
>>>>>>> Stashed changes
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		mainMenuScreen.dispose();
		levelMenuScreen.dispose();
		testLevel.dispose();
		AssetHandler.dispose();
	}
}
