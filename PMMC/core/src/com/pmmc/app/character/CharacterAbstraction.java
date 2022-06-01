package com.pmmc.app.character;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public abstract class CharacterAbstraction extends Sprite {

    private final int maxLevels;
    private int timeInWater;
    private int timeOutWater;
    private int timeSinceFood;
    private int timeSinceDamage;
    private int damageRate;
    private boolean flipped = true;
    private float speed;
    private int health;
    private int air;
    private int jumpForce;
    private int airLossRate;
    private int foodLossRate;
    private int hunger;
    private int toxicity;
    private int currentFrame;
    private boolean isSwimming;
    // manages the sprites in a sprite sheet
    private TextureAtlas textureAtlas;
    private final TextureAtlas walkingAtlas;
    private final TextureAtlas swimmingAtlas;


    public CharacterAbstraction(TextureAtlas walkingAtlas, TextureAtlas swimmingAtlas) {
        super(walkingAtlas.findRegion("1"));
        timeInWater = 0;
        timeOutWater = 0;
        speed = 3.0f;
        health = 5;
        air = 5;
        hunger = 5;
        toxicity = 0;
        maxLevels = 5;
        isSwimming = false;
        this.textureAtlas = walkingAtlas;
        this.walkingAtlas = walkingAtlas;
        this.swimmingAtlas = swimmingAtlas;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        if (health <= maxLevels && health >= 0) {
            this.health = health;
        }
    }

    public int getAir() {
        return air;
    }

    public void setAir(int air) {
        if (air <= maxLevels && air >= 0) {
            this.air = air;
        }
    }

    public int getHunger() {
        return hunger;
    }

    public void setHunger(int hunger) {
        if (hunger < maxLevels && hunger >= 0) {
            this.hunger = hunger;
        }
    }

    public int getToxicity() {
        return toxicity;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        if (speed >= 0) {
            this.speed = speed;
        }
    }

    public boolean getSwimming() {
        return isSwimming;
    }

    public void setSwimming(boolean swimming) {
        if (swimming) {
            this.textureAtlas = swimmingAtlas;
        } else {
            this.textureAtlas = walkingAtlas;
        }
        this.isSwimming = swimming;
    }

    public void updateFrame(boolean horizontal, boolean flip, boolean death) {

        currentFrame++;
        if (currentFrame >= textureAtlas.getRegions().size * 16) {
            currentFrame = 1;
        }
//        System.out.println(currentFrame / 16 + 1);
        setRegion(textureAtlas.findRegion(Integer.toString(currentFrame / 16 + 1)));

        if (death) {
            flip(false, true);
        }
        if (flipped) {
            flip(true, false);

        }
        if (horizontal) {
            flipped = flip;
        }
    }

    public void setPosition(float x, float y) {
        setPosition(x, y);
    }

    public int getAirLossRate() {
        return airLossRate;
    }

    public void setAirLossRate(int airLossRate) {
        this.airLossRate = airLossRate;
    }

    public int getFoodLossRate() {
        return foodLossRate;
    }

    public void setFoodLossRate(int foodLossRate) {
        this.foodLossRate = foodLossRate;
    }

    public int getTimeInWater() {
        return timeInWater;
    }

    public void setTimeInWater(int timeInWater) {
        this.timeInWater = timeInWater;
    }

    public int getTimeOutWater() {
        return timeOutWater;
    }

    public void setTimeOutWater(int timeOutWater) {
        this.timeOutWater = timeOutWater;
    }

    public void incrementTimeInWater() {
        this.timeInWater++;
    }

    public void incrementTimeOutWater() {
        this.timeOutWater++;

    }

    public int getTimeSinceFood() {
        return timeSinceFood;
    }

    public void setTimeSinceFood(int timeSinceFood) {
        this.timeSinceFood = timeSinceFood;
    }

    public void incrementTimeSinceFood() {
        this.timeSinceFood++;

    }

    public int getDamageRate() {
        return damageRate;
    }

    public void setDamageRate(int damageRate) {
        this.damageRate = damageRate;
    }

    public int getTimeSinceDamage() {
        return timeSinceDamage;
    }

    public void incrementTimeSinceDamage() {
        this.timeSinceDamage++;

    }

    public void incrementToxicity() {
        if (toxicity < maxLevels) this.toxicity++;
    }

    public void incrementHunger() {
        if (hunger < maxLevels) this.hunger++;
    }

    public int getJumpForce() {
        return jumpForce;
    }

    public int getMaxLevels() {
        return maxLevels;
    }

    public void setJumpForce(int jumpForce) {
        this.jumpForce = jumpForce;
    }
}
