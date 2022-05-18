package com.pmmc.app.character;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.pmmc.app.AssetHandler;

public class Dolphin extends CharacterAbstraction {

    public Dolphin() {
        super(AssetHandler.assetManager.get(AssetHandler.dolphinSwimming, TextureAtlas.class),
                AssetHandler.assetManager.get(AssetHandler.dolphinSwimming, TextureAtlas.class));
        setSpeed(20);
        setAirLossRate(1000);
        setFoodLossRate(500);
        setDamageRate(100);
        setJumpForce(15);
    }

    // Power related to polar bear (method here)

}
