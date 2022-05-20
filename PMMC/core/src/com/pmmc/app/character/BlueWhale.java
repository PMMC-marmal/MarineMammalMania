package com.pmmc.app.character;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.pmmc.app.AssetHandler;

public class BlueWhale extends CharacterAbstraction {

    public BlueWhale() {
        super(AssetHandler.assetManager.get(AssetHandler.whaleSwimming, TextureAtlas.class),
                AssetHandler.assetManager.get(AssetHandler.whaleSwimming, TextureAtlas.class));
        setSpeed(10);
        setAirLossRate(1000);
        setFoodLossRate(200);
        setDamageRate(100);
        setJumpForce(8);
    }

    // Power related to polar bear (method here)

}
