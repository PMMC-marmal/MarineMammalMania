package com.pmmc.app.character;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.pmmc.app.AssetHandler;

public class Orca extends CharacterAbstraction {

    public Orca() {
        super(AssetHandler.assetManager.get(AssetHandler.orcaSwimming, TextureAtlas.class),
                AssetHandler.assetManager.get(AssetHandler.orcaSwimming, TextureAtlas.class));
        setSpeed(15);
        setAirLossRate(500);
        setFoodLossRate(500);
        setDamageRate(100);
        setJumpForce(7);
    }

    // Power related to polar bear (method here)

}
