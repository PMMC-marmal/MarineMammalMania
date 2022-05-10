package com.pmmc.app.character;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.pmmc.app.AssetHandler;

public class PolarBear extends CharacterAbstraction {

    public PolarBear() {
        super(AssetHandler.assetManager.get(AssetHandler.seaLionWalking, TextureAtlas.class),
                AssetHandler.assetManager.get(AssetHandler.seaLionSwimming, TextureAtlas.class));
        setSpeed(10);
        setAirLossRate(200);
        setFoodLossRate(300);
        setDamageRate(100);
    }

    // Power related to polar bear (method here)

}
