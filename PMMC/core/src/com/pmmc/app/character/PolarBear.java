package com.pmmc.app.character;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class PolarBear extends CharacterAbstraction {

    public PolarBear(Sprite character, TextureAtlas textureAtlas) {
        super(character, textureAtlas);
        setSpeed(10);
        setAirLossRate(200);
        setFoodLossRate(300);
        setDamageRate(100);
    }

    // Power related to polar bear (method here)

}
