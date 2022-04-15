package com.pmmc.app;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class AssetHandler {
    public static final AssetManager assetManager = new AssetManager();

    // Main Menu
    public static final String
            continueButton = "continue_button.png",
            continueButtonActive = "continue_button_active.png",
            newGameButton = "new_game_button.png",
            newGameButtonActive = "new_game_button_active.png",
            mainMenuBackground = "mainmenu_background.png",
    // Level Select
            seaLionButton = "sea_lion_button.png",
            seaLionButtonActive = "sea_lion_button_active.png",
            dolphinButton = "dolphin_button.png",
            dolphinButtonActive = "dolphin_button_active.png",
            killerWhaleButton = "killer_whale_button.png",
            killerWhaleButtonActive = "killer_whale_button_active.png",
            blueWhaleButton = "blue_whale_button.png",
            blueWhaleButtonActive = "blue_whale_button_active.png",
            polarBearButton = "polar_bear_button.png",
            polarBearButtonActive = "polar_bear_button_active.png",
            levelMenuBackground = "level_select_background.png",
            backArrow = "back_arrow.png",
            seaLionSprite = "sea_lion.png",
            dolphinSprite = "dolphin.png",
            killerWhaleSprite = "killer_whale.png",
            blueWhaleSprite = "blue_whale.png",
            polarBearSprite = "polar_bear.png",

    //polarbear
//            waterWithSand = "water-with-sand.png",
            waterWithSand = "ocean-2.png",
            icebergAddon = "iceberg-top.png",

    iceberg2 = "iceberg2.png",
            //Iceberge3 = "iceberge3.png",
            //Iceberge4 = "iceberge4.png";

            testbear = "bear-1.png";


    public static void load(){
        assetManager.load(continueButton, Texture.class);
        assetManager.load(continueButtonActive, Texture.class);
        assetManager.load(newGameButton, Texture.class);
        assetManager.load(newGameButtonActive, Texture.class);
        assetManager.load(mainMenuBackground, Texture.class);

        assetManager.load(seaLionButton, Texture.class);
        assetManager.load(seaLionButtonActive, Texture.class);
        assetManager.load(dolphinButton, Texture.class);
        assetManager.load(dolphinButtonActive, Texture.class);
        assetManager.load(killerWhaleButton, Texture.class);
        assetManager.load(killerWhaleButtonActive, Texture.class);
        assetManager.load(blueWhaleButton, Texture.class);
        assetManager.load(blueWhaleButtonActive, Texture.class);
        assetManager.load(polarBearButton, Texture.class);
        assetManager.load(polarBearButtonActive, Texture.class);
        assetManager.load(levelMenuBackground, Texture.class);
        assetManager.load(backArrow, Texture.class);
        assetManager.load(seaLionSprite, Texture.class);
        assetManager.load(dolphinSprite, Texture.class);
        assetManager.load(killerWhaleSprite, Texture.class);
        assetManager.load(blueWhaleSprite, Texture.class);
        assetManager.load(polarBearSprite, Texture.class);

        assetManager.load(testbear, Texture.class);
        assetManager.load(waterWithSand,Texture.class);
        assetManager.load(iceberg2,Texture.class);
        assetManager.load(icebergAddon, Texture.class);
    }

    public static void dispose(){
        assetManager.dispose();
    }
}
