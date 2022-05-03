package com.pmmc.app.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public abstract class CharacterAbstraction extends Sprite {

    private final int MAX_FRAMES = 64;
    private int timeInWater;
    private int timeOutWater;
    private int timeSinceFood;
    private int timeSinceDamage;
    private int damageRate;
    private boolean flipped = true;
    private final int maxLevels;
    public Sprite character;
    private float speed;
    private int health;
    private int air;
    private int airLossRate;
    private int foodLossRate;
    private int hunger;
    private int toxicity;
    private int currentFrame;
    private float x_position;
    private float y_position;
    // manages the sprites in a sprite sheet
    private TextureAtlas textureAtlas;


    public CharacterAbstraction(Sprite character, TextureAtlas textureAtlas) {
        super(character);
        timeInWater = 0;
        timeOutWater = 0;
        speed = 3.0f;
        health = 5;
        air = 5;
        hunger = 5;
        toxicity = 0;
        maxLevels = 5;
        x_position = 0;
        y_position = 0;
        this.textureAtlas = textureAtlas;
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
        if (hunger <= maxLevels && hunger >= 0) {
            this.hunger = hunger;
        }
    }

    public int getToxicity() {
        return toxicity;
    }

    public void setToxicity(int toxicity) {
        if (toxicity <= maxLevels && toxicity >= 0) {
            this.toxicity = toxicity;
        }
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        if (speed >= 0) {
            this.speed = speed;
        }
    }

    public void setTextureAtlas(TextureAtlas textureAtlas) {
        this.textureAtlas = textureAtlas;
    }

    public float getX_position() {
        return x_position;
    }

    public void setX_position(float x_position) {
        this.x_position = x_position;
    }

    public float getY_position() {
        return y_position;
    }

    public void setY_position(float y_position) {
        this.y_position = y_position;
    }

    public void updateFrame(boolean horizontal, boolean flip, boolean death) {

        currentFrame++;
        if (currentFrame >= MAX_FRAMES) {
            currentFrame = 1;
        }
            setRegion(textureAtlas.findRegion(Integer.toString(currentFrame / 16 + 1)));

        if (death){
            flip(false, true);
        }
        if (flipped) {
            flip(true, false);

        }
        if (horizontal){
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

    public int getTimeOutWater() {
        return timeOutWater;
    }

    public void incrementTimeInWater(){
        this.timeInWater ++;
    }
    public void incrementTimeOutWater(){
        this.timeOutWater ++;

    }

    public void setTimeInWater(int timeInWater) {
        this.timeInWater = timeInWater;
    }

    public void setTimeOutWater(int timeOutWater) {
        this.timeOutWater = timeOutWater;
    }

    public int getTimeSinceFood() {
        return timeSinceFood;
    }

    public void incrementTimeSinceFood(){
        this.timeSinceFood ++;

    }

    public void setTimeSinceFood(int timeSinceFood) {
        this.timeSinceFood = timeSinceFood;
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

    public void incrementTimeSinceDamage(){
        this.timeSinceDamage ++;

    }

    public void setTimeSinceDamage(int timeSinceDamage) {
        this.timeSinceDamage = timeSinceDamage;
    }

    public void incrementToxicity(){
        this.toxicity++;
    }

    public void incrementHunger(){
        this.hunger++;
    }
}
