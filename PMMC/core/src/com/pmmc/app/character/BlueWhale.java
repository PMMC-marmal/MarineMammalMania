package com.pmmc.app.character;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.pmmc.app.AssetHandler;

public class BlueWhale extends CharacterAbstraction {

    public BlueWhale() {
        super(AssetHandler.assetManager.get(AssetHandler.polarBearWalking, TextureAtlas.class),
                AssetHandler.assetManager.get(AssetHandler.polarBearSwimming, TextureAtlas.class));
        setSpeed(6);
        setAirLossRate(1000);
        setFoodLossRate(500);
        setDamageRate(100);
    }

    // Power related to polar bear (method here)

}
