package com.pmmc.app.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class CharacterAbstraction extends Sprite {

    private float speed;
    private int health;
    private int air;
    private int hunger;
    private int toxicity;
    private final int maxLevels;

    private Texture stillImage;


    public CharacterAbstraction(Sprite character, String stillImagePath){
        super(character);
        speed = 50.0f;
        health = 5;
        air = 5;
        hunger = 5;
        toxicity = 0;
        maxLevels = 5;
        stillImage = new Texture(stillImagePath);
    }

    public int getHealth() {
        return health;
    }
    public int getAir() {
        return air;
    }
    public int getHunger() {
        return hunger;
    }
    public int getToxicity() {
        return toxicity;
    }
    public void setHealth(int health) {
        if (health <= maxLevels && health >= 0){this.health = health;}
    }
    public void setAir(int air) {
        if(air <= maxLevels && air >= 0){this.air = air;}
    }
    public void setHunger(int hunger) {
        if(hunger <= maxLevels && hunger >= 0){this.hunger = hunger;}
    }
    public void setToxicity(int toxicity) {
        if(toxicity <= maxLevels && toxicity >= 0){this.toxicity = toxicity;}
    }
    public void setSpeed(float speed){
        if (speed >= 0){this.speed = speed;}
    }
    public void setStillImage(String path){
        this.stillImage = new Texture(path);
    }
}
