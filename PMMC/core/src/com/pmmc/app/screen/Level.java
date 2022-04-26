package com.pmmc.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.pmmc.app.AssetHandler;
import com.pmmc.app.GameLauncher;

import java.util.Random;

/**
 * Menu is a general abstract class that holds common procedures for the different menu screens.
 */

public abstract class Level extends AbstractScreen {
    public float gameWidth;
    public float gameHeight;
    private static final float PPM = 32 ;
    private static World world;
    private final Box2DDebugRenderer b2dr;
    private final Body player;
    int spacing = Gdx.graphics.getWidth()/3; // 1000
    final Stage stage;
    ScreenViewport viewPort;

    public Level(GameLauncher game) {
        super(game);

        // Setup camera to be in the center
        gameWidth = Gdx.graphics.getWidth();
        gameHeight = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(gameWidth, gameHeight);
        camera.setToOrtho(false, Gdx.graphics.getWidth() /2 , Gdx.graphics.getHeight() /2);

        world = new World(new Vector2(0,0f),false);
        b2dr = new Box2DDebugRenderer();

        player = createBox(0,0, 290,180,false,true);


        viewPort = new ScreenViewport(camera);
        stage = new Stage();
        stage.setViewport(viewPort);
    }

    public void Render(){
        update(Gdx.graphics.getDeltaTime());
        b2dr.render(world,camera.combined.scl(PPM));
    }

    private void update(float deltaTime) {
        world.step(1/60f,6,2);
        inputUpdate(deltaTime);
        cameraUpdate(deltaTime);
    }

    private void inputUpdate(float deltaTime) {
        int horizontalForce = 0;
        int verticalForce = 0;
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.getPosition().x > 0){
            horizontalForce -= 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            horizontalForce += 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            verticalForce -= 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            verticalForce += 1;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            player.applyForceToCenter(0,300,false);
        }
        player.setLinearVelocity(horizontalForce*10, verticalForce*10);
    }

    private void cameraUpdate(float deltaTime) {
        Vector3 position = camera.position;
        if (player.getPosition().x >= Gdx.graphics.getWidth()/2) {
            position.x = player.getPosition().x * PPM;
        }
        else{
            position.x = Gdx.graphics.getWidth()/2;
        }
//        if (player.getPosition().x >= Gdx.graphics.getWidth()/2) {
//            position.x = player.getPosition().x * PPM;
//        }
        position.y = player.getPosition().y * PPM;
        camera.position.set(position);
        camera.update();
    }

    public void renderBackground(Sprite background){
        stage.act();
        stage.getBatch().begin();
        generateBackground(background);
        stage.getBatch().end();
        stage.draw();
    }


    public void generateBackground(Sprite background){

        for (int i = 0; i < 30; i ++){
            stage.getBatch().draw(background, spacing * i, -1 * Gdx.graphics.getHeight() + 300, spacing, Gdx.graphics.getHeight());
            background.flip(true,false);
        }
    }

    public boolean[] generateObstacles(){
        boolean[] obstacles1 = {true, false ,true ,true, false ,false ,false, true, true ,false};
        boolean[] obstacles2 = {false, false ,false ,true, true ,false ,true, false, true ,true};
        boolean[] obstacles3 = {true, false ,true ,false, false ,true ,false, true, false ,true};
        boolean[] obstacles4 = {false, true ,false ,true, true ,false ,true, false, true ,false};
        boolean[] obstacles5 = {true, true ,false ,false, true ,false ,true, false, false ,true};
        boolean[][] arrays = {obstacles1,obstacles3,obstacles4,obstacles5};
        int rnd = new Random().nextInt(arrays.length);
        return arrays[rnd];
    }

    public void placeBox2DObstacles(int section, boolean[] obstacles){
        int i;
        if (section == 2){
            i = 10;
        }
        else if(section == 3){
            i = 20;
        }
        else{
            i = 0 ;
        }

        for (boolean o :obstacles){
            if (o){
                createBox(Gdx.graphics.getWidth()/3 * i + (Gdx.graphics.getWidth()/6),100,Gdx.graphics.getWidth()/3,Gdx.graphics.getHeight()/4,true,true);
            }
            i++;
        }
    }


    public void renderObstacles(int section, Sprite[] spritesArray, boolean[] obstacles){
        int i;
        stage.act();
        stage.getBatch().begin();
        if (section == 2){
            i = 10;
        }
        else if(section == 3){
            i = 20;
        }
        else{
            i = 0 ;
        }

        for (boolean o :obstacles){
            if (o){
                int rnd = new Random().nextInt(2);
                stage.getBatch().draw(spritesArray[rnd], Gdx.graphics.getWidth()/3 * i, 100, Gdx.graphics.getWidth()/3, Gdx.graphics.getHeight()/4);
            }
            i++;
        }

        stage.getBatch().end();
        stage.draw();
    }
    public static Body createBox(float x, float y, int width, int height, boolean isStatic, boolean fixedRotation) {
        Body pBody;
        BodyDef def = new BodyDef();

        if(isStatic)
            def.type = BodyDef.BodyType.StaticBody;
        else
            def.type = BodyDef.BodyType.DynamicBody;

        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = fixedRotation;
        pBody = world.createBody(def);
        pBody.setUserData("wall");

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM);


        pBody.createFixture(shape, 1.0f);
        shape.dispose();
        return pBody;
    }

    @Override
    public void resize(int width, int height){
        camera.setToOrtho(false, width, height);
        stage.getViewport().update(width, height);
    }

    @Override
    public void dispose(){
        world.dispose();
        b2dr.dispose();
    }

}
