package com.pmmc.app;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class AssetHandler {
    public static final AssetManager assetManager = new AssetManager();

    // Main Menu
    public static final String
            continueButton = "main_menu/continue_button.png",
            continueButtonActive = "main_menu/continue_button_active.png",
            newGameButton = "main_menu/new_game_button.png",
            newGameButtonActive = "main_menu/new_game_button_active.png",
            mainMenuBackground = "main_menu/mainmenu_background.png",
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
            healthBarEmpty = "player-bars/health/healthbar_0.png",
            healthBar = "player-bars/health/health_bar.png",
            airBarEmpty = "player-bars/air/air_0.png",
            airBar = "player-bars/air/air_bar.png",
            hungerBarEmpty = "player-bars/food/hunger_0.png",
            hungerBar = "player-bars/food/hunger_bar.png",
            toxicBarEmpty = "player-bars/toxic/toxic_0.png",
            toxicBar = "player-bars/toxic/toxic_bar.png",

//            waterWithSand = "water-with-sand.png",
            waterWithSand = "ocean-2.png",
            blur = "water-blur.png",


            icebergAddon = "icebergs/iceberg-background.png",
            iceberg1 = "icebergs/starting_ice_berg.png",
            iceberg2 = "icebergs/iceberg_medium.png",
            iceberg3 = "icebergs/iceberg_medium2.png",
            iceberg4 = "icebergs/iceberg_medium2.png",
            iceberg5 = "icebergs/iceberg_small.png",
            iceberg6 = "icebergs/iceberg_small2.png",

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

        assetManager.load(healthBar, Texture.class);
        assetManager.load(healthBarEmpty, Texture.class);
        assetManager.load(airBar, Texture.class);
        assetManager.load(airBarEmpty, Texture.class);
        assetManager.load(hungerBar, Texture.class);
        assetManager.load(hungerBarEmpty, Texture.class);
        assetManager.load(toxicBar, Texture.class);
        assetManager.load(toxicBarEmpty, Texture.class);

        assetManager.load(testbear, Texture.class);
        assetManager.load(waterWithSand,Texture.class);
        assetManager.load(blur,Texture.class);

        assetManager.load(iceberg1,Texture.class);
        assetManager.load(iceberg2,Texture.class);
        assetManager.load(iceberg3,Texture.class);
        assetManager.load(iceberg4,Texture.class);
        assetManager.load(iceberg5,Texture.class);
        assetManager.load(iceberg6,Texture.class);
        assetManager.load(icebergAddon, Texture.class);
    }

    public static void dispose(){
        assetManager.dispose();
    }
}
