package com.pmmc.app.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public abstract class CharacterAbstraction extends Sprite {

    private final int MAX_FRAMES = 64;

    private float speed;
    private int health;
    private int air;
    private int hunger;
    private int toxicity;
    private final int maxLevels;
    private int currentFrame;
    private float x_position;
    private float y_position;

    public Sprite character;
    // manages the sprites in a sprite sheet
    private TextureAtlas textureAtlas;



    public CharacterAbstraction(Sprite character, TextureAtlas textureAtlas){
        super(character);
        this.character = character;
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
    public void setTextureAtlas(TextureAtlas textureAtlas){this.textureAtlas = textureAtlas;}
    public void setX_position(float x_position){this.x_position = x_position;}
    public void setY_position(float y_position){this.y_position = y_position;}
    public float getX_position(){return x_position;}
    public float getY_position(){return y_position;}
    public void updateFrame(boolean horizontal, boolean flip){
        currentFrame++;
        if(currentFrame >= MAX_FRAMES){
            currentFrame = 1;
        }
        character.setRegion(textureAtlas.findRegion(Integer.toString(currentFrame/16+1)));

        if(horizontal) {
            if (flip) {
                character.flip(true, false);
                x_position -= Gdx.graphics.getDeltaTime() + speed;
            } else {
                x_position += Gdx.graphics.getDeltaTime() + speed;
            }
        }
        else{
            if(flip){
                y_position-=Gdx.graphics.getDeltaTime()+speed;
            }
            else{
                y_position+=Gdx.graphics.getDeltaTime()+speed;
            }
        }
    }

    public void setPosition(float x, float y){character.setPosition(x,y);}
    public void draw(SpriteBatch batch){character.draw(batch);}

}
