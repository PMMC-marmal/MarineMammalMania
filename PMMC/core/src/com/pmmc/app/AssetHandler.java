package com.pmmc.app;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

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
            seaLionButton = "buttons/sea_lion_button.png",
            seaLionButtonActive = "buttons/sea_lion_button_active.png",
            dolphinButton = "buttons/dolphin_button.png",
            dolphinButtonActive = "buttons/dolphin_button_active.png",
            killerWhaleButton = "buttons/killer_whale_button.png",
            killerWhaleButtonActive = "buttons/killer_whale_button_active.png",
            blueWhaleButton = "buttons/blue_whale_button.png",
            blueWhaleButtonActive = "buttons/blue_whale_button_active.png",
            polarBearButton = "buttons/polar_bear_button.png",
            polarBearButtonActive = "buttons/polar_bear_button_active.png",
            levelMenuBackground = "backgrounds/level_select_background.png",
            backArrow = "buttons/back_arrow.png",
            seaLionSprite = "sea_lion.png",
            dolphinSprite = "dolphin.png",
            killerWhaleSprite = "killer_whale.png",
            blueWhaleSprite = "blue_whale.png",
            polarBearSprite = "bear1.png",
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
            waterWithSand = "backgrounds/ocean-2.png",
            blur = "backgrounds/water-blur.png",

            icebergAddon = "obstacles/iceberg-background.png",
            iceberg1 = "obstacles/starting_ice_berg.png",
            iceberg2 = "obstacles/iceberg_medium.png",
            iceberg3 = "obstacles/iceberg_medium2.png",
            iceberg4 = "obstacles/iceberg_medium2.png",
            iceberg5 = "obstacles/iceberg_small.png",
            iceberg6 = "obstacles/iceberg_small2.png",

            polarBearWalking = "sprite_sheets/PolarBearWalkingSpriteSheet.atlas",
            polarBearSwimming = "sprite_sheets/PolarBearSwimmingSpriteSheet.atlas",
            seaLionWalking = "sprite_sheets/SeaLionWalkingSpriteSheet.atlas",
            seaLionSwimming = "sprite_sheets/SeaLionSwimmingSpriteSheet.atlas",

            polarFoodPop = "popups/polar_bear/food_popup.png",
            polarHabitatPop = "popups/polar_bear/habitat_popup.png",
            polarLifePop = "popups/polar_bear/life_popup.png",
            polarSocialPop = "popups/polar_bear/social_popup.png",
            polarThreatsPop = "popups/polar_bear/threats_popup.png",

            salmon = "food/chinook_salmon.png",
            herring = "food/herring.png",
            krill = "food/krill.png",
            seal = "food/ringed_seal.png",
            sardine = "food/sardine.png",
            squid = "food/squid.png",
            toxicSalmon = "food/toxic_chinook_salmon.png",
            toxicHerring = "food/toxic_herring.png",
            toxicKrill = "food/toxic_krill.png",
            toxicSeal = "food/toxic_ringed_seal.png",
            toxicSardine = "food/toxic_sardine.png",
            toxicSquid = "food/toxic_squid.png",
    // Quiz
            choiceButton = "quiz/choiceButton.png",
            choiceButtonActivated = "quiz/choiceButtonActivated.png",
            check = "quiz/check.png",
            x_mark = "quiz/x_mark.png"
    ;




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


        assetManager.load(waterWithSand,Texture.class);
        assetManager.load(blur,Texture.class);

        assetManager.load(polarFoodPop,Texture.class);
        assetManager.load(polarHabitatPop,Texture.class);
        assetManager.load(polarLifePop,Texture.class);
        assetManager.load(polarSocialPop,Texture.class);
        assetManager.load(polarThreatsPop,Texture.class);

        assetManager.load(iceberg1,Texture.class);
        assetManager.load(iceberg2,Texture.class);
        assetManager.load(iceberg3,Texture.class);
        assetManager.load(iceberg4,Texture.class);
        assetManager.load(iceberg5,Texture.class);
        assetManager.load(iceberg6,Texture.class);
        assetManager.load(icebergAddon, Texture.class);

        assetManager.load(polarBearWalking, TextureAtlas.class);
        assetManager.load(polarBearSwimming, TextureAtlas.class);
        assetManager.load(seaLionWalking, TextureAtlas.class);
        assetManager.load(seaLionSwimming, TextureAtlas.class);

        assetManager.load(salmon, Texture.class);
        assetManager.load(herring, Texture.class);
        assetManager.load(krill, Texture.class);
        assetManager.load(seal, Texture.class);
        assetManager.load(sardine, Texture.class);
        assetManager.load(squid, Texture.class);
        assetManager.load(toxicSalmon, Texture.class);
        assetManager.load(toxicHerring, Texture.class);
        assetManager.load(toxicKrill, Texture.class);
        assetManager.load(toxicSeal, Texture.class);
        assetManager.load(toxicSardine, Texture.class);
        assetManager.load(toxicSquid, Texture.class);

        assetManager.load(choiceButton, Texture.class);
        assetManager.load(choiceButtonActivated, Texture.class);
        assetManager.load(check, Texture.class);
        assetManager.load(x_mark, Texture.class);
    }

    public static void dispose(){
        assetManager.dispose();
    }
}
