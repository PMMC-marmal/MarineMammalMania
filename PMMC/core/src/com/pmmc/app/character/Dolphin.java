package com.pmmc.app.character;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.pmmc.app.AssetHandler;

public class Dolphin extends CharacterAbstraction {

    public Dolphin() {
        super(AssetHandler.assetManager.get(AssetHandler.dolphinSwimming, TextureAtlas.class),
                AssetHandler.assetManager.get(AssetHandler.dolphinSwimming, TextureAtlas.class));
        setSpeed(20);
        setAirLossRate(500);
        setFoodLossRate(250);
        setDamageRate(100);
        setJumpForce(9);
    }

    // Power related to polar bear (method here)

}
