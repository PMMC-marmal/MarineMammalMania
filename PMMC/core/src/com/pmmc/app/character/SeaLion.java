package com.pmmc.app.character;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.pmmc.app.AssetHandler;

public class SeaLion extends CharacterAbstraction {

    public SeaLion() {
        super(AssetHandler.assetManager.get(AssetHandler.seaLionWalking, TextureAtlas.class),
                AssetHandler.assetManager.get(AssetHandler.seaLionSwimming, TextureAtlas.class));
        setSpeed(18);
        setAirLossRate(200);
        setFoodLossRate(300);
        setDamageRate(100);
        setJumpForce(5);
    }

    // Power related to polar bear (method here)

}
