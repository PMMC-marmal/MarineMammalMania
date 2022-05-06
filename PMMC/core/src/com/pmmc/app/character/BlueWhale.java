package com.pmmc.app.character;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class BlueWhale extends CharacterAbstraction {

    public BlueWhale(Sprite character, TextureAtlas textureAtlas) {
        super(character, textureAtlas);
        setSpeed(6);
        setAirLossRate(1000);
        setFoodLossRate(500);
        setDamageRate(100);
    }

    // Power related to polar bear (method here)

}
