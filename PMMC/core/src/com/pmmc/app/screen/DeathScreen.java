package com.pmmc.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.pmmc.app.AssetHandler;
import com.pmmc.app.GameLauncher;
import com.pmmc.app.character.CharacterAbstraction;

import java.util.ArrayList;

public class DeathScreen extends Menu {

    private CharacterAbstraction player;
    private BitmapFont font;

    public final float x = (Gdx.graphics.getWidth()/2f) - ((Gdx.graphics.getWidth() * 0.8f) / 2f);

    public DeathScreen(GameLauncher game, CharacterAbstraction player) {
        super(game);
        this.player = player;
        this.font = generateFont(35, Color.WHITE);
    }

    @Override
    public void render(float delta) {
        renderBackground(new Sprite(AssetHandler.assetManager.get(AssetHandler.levelMenuBackground, Texture.class)));
        renderDeathMessage();

        if (Gdx.input.justTouched()){
            transitionScreen(new LevelMenuScreen(game));
        }

    }

    public void renderDeathMessage(){
        ArrayList<String> reasons = new ArrayList<>();
        float y = Gdx.graphics.getHeight()/12f;

        if (player.getAir() == 0){
            reasons.add("Air: Reach the surface to get some air!");
        }
        if (player.getHunger() == 0){
            reasons.add("Hunger: Make sure to eat enough food!");
        }
        if (player.getToxicity() == 5){
            reasons.add("Toxicity: Avoid toxic food");
            reasons.add("(Unless you really need it!)");
        }
        if (reasons.isEmpty()){
            reasons.add("Hazards: Watch out for threats!");
        }

        game.batch.begin();
        font.draw(game.batch, "Oh no, you died from", x * 1/3, Gdx.graphics.getHeight() - y);
        for (int i=0; i<reasons.size(); i++){
            font.draw(game.batch, reasons.get(i), x * 2/3, Gdx.graphics.getHeight() - (y * (i+3)));
        }
        font.draw(game.batch, "Tap anywhere to continue...", x * 2/3, y);
        game.batch.end();
    }


}
