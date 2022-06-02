package com.pmmc.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pmmc.app.screen.LevelMenuScreen;
import com.pmmc.app.screen.LoadingScreen;
import com.pmmc.app.screen.MainMenuScreen;

import com.pmmc.app.screen.PolarBearLevel;


public class GameLauncher extends Game {

	private MainMenuScreen mainMenuScreen;
	private LevelMenuScreen levelMenuScreen;

	private PolarBearLevel polarBearLevel;

	public SpriteBatch batch;
	public Music music;

	@Override
	public void create () {
		// Music
		music = Gdx.audio.newMusic(Gdx.files.internal("sounds/menu_music.mp3"));
		music.setLooping(true);
		music.setVolume(0.2f);
		music.play();

		this.mainMenuScreen = new MainMenuScreen(this);
		this.batch = new SpriteBatch();


		// Load assets WITH loading screen
		//setScreen(new LoadingScreen(this));

		// Load assets WITHOUT loading screen
		AssetHandler.load();
		AssetHandler.assetManager.finishLoading();
		setScreen(mainMenuScreen);

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
		music.dispose();
	}
}
