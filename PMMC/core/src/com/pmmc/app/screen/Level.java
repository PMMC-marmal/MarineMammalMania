package com.pmmc.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.pmmc.app.GameLauncher;
import com.pmmc.app.character.CharacterAbstraction;

import java.util.ArrayList;
import java.util.Random;

/**
 * Menu is a general abstract class that holds common procedures for the different menu screens.
 */

public abstract class Level extends AbstractScreen {
    private static final float PPM = 32;
    private static World world;
    public final Body player2d;
    final Stage stage;
    private final Box2DDebugRenderer b2dr;
    public float gameWidth;
    public float gameHeight;
    public CharacterAbstraction player;
    int spacing = Gdx.graphics.getWidth() / 3;
    ScreenViewport viewPort;

    public Level(GameLauncher game) {
        super(game);

        // Setup camera to be in the center
        gameWidth = Gdx.graphics.getWidth();
        gameHeight = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(gameWidth, gameHeight);
        camera.setToOrtho(false, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);

        world = new World(new Vector2(0, 0f), false);
        b2dr = new Box2DDebugRenderer();

        player2d = createBox(200, 200, 290, 180, false, true);

        viewPort = new ScreenViewport(camera);
        stage = new Stage();
        stage.setViewport(viewPort);
    }

    public static Body createBox(float x, float y, int width, int height, boolean isStatic, boolean fixedRotation) {
        Body pBody;
        BodyDef def = new BodyDef();

        if (isStatic)
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

    public void Render() {
        update(Gdx.graphics.getDeltaTime());
    }

    private void update(float deltaTime) {
        world.step(1 / 60f, 6, 2);
        if (player2d.getPosition().y * PPM > 200) {
            world.setGravity(new Vector2(0, -9.8f * PPM));
        } else {
            world.setGravity(new Vector2(0, 0));
        }
        inputUpdate(deltaTime);
        cameraUpdate(deltaTime);
    }

    private void inputUpdate(float deltaTime) {
        int horizontalForce = 0;
        int verticalForce = 0;
        float speed = player.getSpeed() ;

//        System.out.println("x:" + player2d.getPosition().x * PPM + " y:" + player2d.getPosition().y * PPM);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player2d.getPosition().x > 0) {
            horizontalForce -= 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player2d.getPosition().x * PPM< 15000) {
            horizontalForce += 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && (player2d.getPosition().y * PPM > -700)) {
            verticalForce -= 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && (player2d.getPosition().y * PPM < 200)) {
            verticalForce += 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            speed = speed * 3;
        }
        player2d.setLinearVelocity(horizontalForce * speed, verticalForce * speed);
    }

    private void cameraUpdate(float deltaTime) {
        Vector3 position = camera.position;
        if (player2d.getPosition().x * PPM < Gdx.graphics.getWidth() / 2){
            position.x = Gdx.graphics.getWidth() / 2;
        } else if (player2d.getPosition().x * PPM > 14250) {
            position.x = 14250;
        } else {
            position.x = player2d.getPosition().x * PPM;
        }
        if (player2d.getPosition().y * PPM >= -200) {
            position.y = player2d.getPosition().y * PPM;
        } else {
            position.y = -200;
        }

        camera.position.set(position);
        camera.update();
    }

    public void renderPlayer2D() {
        stage.act();
        stage.getBatch().begin();
        stage.getBatch().draw(player, player2d.getPosition().x * PPM - (player.getWidth() / 2), player2d.getPosition().y * PPM);
        stage.getBatch().end();
        stage.draw();
    }

    public void setPlayer(CharacterAbstraction player) {
        this.player = player;
    }

    public void renderBackground(Sprite background) {
        stage.act();
        stage.getBatch().begin();
        generateBackground(background);
        stage.getBatch().end();
        stage.draw();
    }

    public void generateBackground(Sprite background) {

        for (int i = 0; i < 30; i++) {
            stage.getBatch().draw(background, spacing * i, -1 * Gdx.graphics.getHeight() + 300, spacing, Gdx.graphics.getHeight());
            background.flip(true, false);
        }
    }

    public boolean[] generateObstacles(int Section) {
        if (Section == 2){
            boolean[] obstacles1 = {true, false, true, true, false, false, false, true, true, false};
            boolean[] obstacles2 = {false, false, false, true, true, false, true, false, true, true};
            boolean[] obstacles3 = {true, false, true, false, false, true, false, true, false, true};
            boolean[] obstacles4 = {false, true, false, true, true, false, true, false, true, false};
            boolean[] obstacles5 = {true, true, false, false, true, false, true, false, false, true};
            boolean[][] arrays = {obstacles1, obstacles2, obstacles3, obstacles4, obstacles5};
            int rnd = new Random().nextInt(arrays.length);
            return arrays[rnd];
        } else if (Section == 3){
            boolean[] obstacles6 = {false, false, true, false, false, false, false, false, false, true};
            boolean[] obstacles7 = {false, false, false, true, false, false, false, false, false, true};
            boolean[] obstacles8 = {false, false, false, false, true, false, false, false, false, true};
            boolean[] obstacles9 = {false, false, false, false, false, true, false, false, false, true};
            boolean[] obstacles10 = {false, false, false, false, false, false, true, false, false, true};
            boolean[][] arrays = {obstacles6, obstacles7, obstacles8, obstacles9, obstacles10};
            int rnd = new Random().nextInt(arrays.length);
            return arrays[rnd];
        }
        else {
            return new boolean[]{true};
        }
    }

    public void placeBox2DObstacles(int section, boolean[] obstacles) {
        int i;
        if (section == 2) {
            i = 10;
        } else if (section == 3) {
            i = 20;
        } else {
            i = 0;
        }
        if (obstacles.length == 1){
            int x = (Gdx.graphics.getWidth() / 3 ) * 10;
            System.out.println(x);
            createBox(Gdx.graphics.getWidth() / 3 * i + (x/2), 100, x, Gdx.graphics.getHeight() / 4, true, true);
        }
        else if(obstacles.length == 10) {
            for (boolean o : obstacles) {
                if (o) {
                    createBox(Gdx.graphics.getWidth() / 3 * i + (Gdx.graphics.getWidth() / 6), 100, Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 4, true, true);
                }
                i++;
            }
        }
    }

    public void renderObstacles(int section, ArrayList<Sprite> choices, boolean[] obstacles, int yAxis) {
        int i;
        int j = 0;
        stage.act();
        stage.getBatch().begin();
        if (section == 2) {
            i = 10;
        } else if (section == 3) {
            i = 20;
        } else {
            i = 0;
        }
        if (obstacles.length == 1){
            stage.getBatch().draw(choices.get(0), Gdx.graphics.getWidth() / 3 * i, -650, (Gdx.graphics.getWidth() / 3 )* 10, choices.get(0).getHeight());
        }
        else if(obstacles.length == 10) {
            for (boolean o : obstacles) {
                if (o) {
                    stage.getBatch().draw(choices.get(j), Gdx.graphics.getWidth() / 3 * i, yAxis, Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 4);
                    j++;
                }
                i++;
            }
        }

        stage.getBatch().end();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        stage.getViewport().update(width, height);
    }

    @Override
    public void dispose() {
        world.dispose();
        b2dr.dispose();
    }

}
