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
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.pmmc.app.AssetHandler;
import com.pmmc.app.GameLauncher;
import com.pmmc.app.character.B2dContactListener;
import com.pmmc.app.character.CharacterAbstraction;

import java.util.ArrayList;
import java.util.Random;

/**
 * LEVEL is a general abstract class that holds common procedures for the different level screens.
 */

public abstract class Level extends AbstractScreen {
    public static final float PPM = 32;
    private static final float SCALE = 2;
    private static World world;
    public Body player2d;
    final Stage stage;
    private final Body endGoal;
    private final Box2DDebugRenderer b2dr;
    public Fixture contactor;
    public Fixture contacting;
    public float idealGameWidth = 1600;
    public float idealGameHeight = 1000;
    public CharacterAbstraction player;
    public boolean isTouchingObstacle;
    public int preySpawnHeight;
    public boolean preyDespawnable;
    public boolean toggle;
    public boolean gameOverLost;
    public boolean waterPrey;
    FitViewport fitViewport;
    FillViewport fillViewport;
    ExtendViewport extendViewport;
    ScreenViewport viewPort;
    private int worldSize = 24000;
    public int spacing;
    private int jumpforce;
    private ArrayList<Body> prey;
    private boolean atSurface;

    private boolean waterWorld;
    private boolean boatStrike;
    private Sprite boatModel;
    private Body boat2D;
    private int oceanDepth;
    private boolean directionLeft;
    private boolean boatFlipped = false;

    public Level(GameLauncher game) {
        super(game);

        // Setup camera to be in the center

        camera = new OrthographicCamera();
        camera.setToOrtho(false, idealGameWidth, idealGameHeight);

        world = new World(new Vector2(0, -10f * PPM), false);
        world.setContactListener(new B2dContactListener(this));
        b2dr = new Box2DDebugRenderer();


        contactor = null;
        contacting = null;
        prey = new ArrayList<>();

        endGoal = createBox(worldSize - 500, 200, 250, 150, false, true, "End Goal");

        toggle = true;
        gameOverLost = false;

        viewPort = new ScreenViewport(camera);
        extendViewport = new ExtendViewport(idealGameWidth, idealGameHeight, camera);
        fillViewport = new FillViewport(idealGameWidth, idealGameHeight, camera);
        fitViewport = new FitViewport(idealGameWidth, idealGameHeight, camera);
        new StretchViewport(idealGameWidth, idealGameHeight, camera);
        stage = new Stage();
        stage.setViewport(extendViewport);
    }

    public static Body createBox(float x, float y, int width, int height, boolean isStatic, boolean fixedRotation, String b2dType) {
        Body pBody;
        BodyDef def = new BodyDef();

        if (isStatic)
            def.type = BodyDef.BodyType.StaticBody;
        else
            def.type = BodyDef.BodyType.DynamicBody;

        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = fixedRotation;
        pBody = world.createBody(def);
        pBody.setUserData(b2dType);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM);

        pBody.createFixture(shape, 1.0f);
        shape.dispose();
        return pBody;
    }

    public Body makePolygonShapeBody(Vector2[] vertices, float x, float y, String type) {
        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.type = BodyDef.BodyType.StaticBody;
        boxBodyDef.position.set(x / PPM, y / PPM);
        boxBodyDef.fixedRotation = true;
        Body boxBody = world.createBody(boxBodyDef);
        boxBody.setUserData(type);
        PolygonShape polygon = new PolygonShape();
        polygon.set(vertices);
        boxBody.createFixture(polygon, 1.0f);
        polygon.dispose();
        return boxBody;
    }

    public void Render() {
        update(Gdx.graphics.getDeltaTime());
    }

    private void update(float deltaTime) {
        if (gameOverLost) {
            transitionScreen(new LevelMenuScreen(game));
        }
        world.step(deltaTime, 6, 2);
        if (player2d.getPosition().y * PPM > 0) {
            atSurface = false;
            player2d.setGravityScale(1);
            player.setSwimming(false);
            player.setTimeInWater(0);
            player.incrementTimeOutWater();
        } else if (player2d.getPosition().y * PPM > -10) {
            atSurface = true;
            player.setTimeInWater(0);
            player.incrementTimeOutWater();
        } else {
            atSurface = false;
            player2d.setGravityScale(0);
            player.setSwimming(true);
            player.incrementTimeInWater();
            player.setTimeOutWater(0);
        }

        player.incrementTimeSinceFood();
        inputUpdate(deltaTime);
        playerUpdate(deltaTime);
        preyUpdate(deltaTime);
        hazardsUpdate(deltaTime);
        cameraUpdate(deltaTime);
        stage.getBatch().setProjectionMatrix(camera.combined);
    }

    private void hazardsUpdate(float deltaTime) {
        if (boatStrike && boatModel != null){
            if (boat2D == null){
                if (atSurface){
                    if (player2d.getPosition().x * PPM < worldSize/2){
                        if (!boatFlipped){
                            System.out.println("GOING LEFT");
                            boatModel.flip(true,false);
                            boatFlipped = true;
                        }
                        boat2D = createBox(worldSize-1,0,500,5,false,true,"Boat");
                        directionLeft = true;
                    }
                    else {
                        if (boatFlipped){
                            boatModel.flip(true,false);
                            boatFlipped = false;
                        }
                        boat2D = createBox(1000,0,500,5,false,true,"Boat");
                        directionLeft = false;
                    }
                    boat2D.setGravityScale(0);

                }
            }else if (boat2D.getUserData().equals("Boat")){

                if (boat2D.getPosition().x < 0 || boat2D.getPosition().x * PPM > worldSize){
                    world.destroyBody(boat2D);
                    boat2D = null;
                }
                else{
                    if (!directionLeft){
                        boat2D.setLinearVelocity(200,0);
                    }else {
                        boat2D.setLinearVelocity(-200,0);

                    }
                }
            }
        }
    }



    private void playerUpdate(float deltaTime) {
        setPlayerParams(deltaTime);
        getPlayerParams();
    }

    private void setPlayerParams(float deltaTime) {
        if (boatStrike){
            if (contacting != null && contactor != null){
                if ((contacting.getBody().getUserData().equals("Boat") && contactor.getBody().getUserData().equals("Player") ) || (contactor.getBody().getUserData().equals("Boat") && contacting.getBody().getUserData().equals("Player"))){
                    player.setHealth(player.getHealth() - 1);
                    player2d.setLinearVelocity(0,1000*PPM);
                    world.destroyBody(boat2D);
                    boat2D = null;
                }
            }
        }
        if (player.getAir() == 0 || player.getHunger() == 0) {
            player.incrementTimeSinceDamage();
            if (player.getTimeSinceDamage() % player.getDamageRate() == 0) {
                player.setHealth(player.getHealth() - 1);
            }
        }
        if (player.getSwimming() && !atSurface) {
            if (player.getTimeInWater() % player.getAirLossRate() == 0) {
                player.setAir(player.getAir() - 1);
            }
        } else {
            if (player.getTimeOutWater() % (player.getAirLossRate() / 5) == 0) {
                player.setAir(player.getAir() + 1);
            }
        }
        if (player.getTimeSinceFood() % player.getFoodLossRate() == 0) {
            player.setHunger(player.getHunger() - 1);
        }
        if (contacting != null && contactor != null) {
            if (prey.contains(contacting.getBody()) && contactor.getBody().getUserData().equals("Player")) {
                if (contacting.getBody().getUserData().equals("toxic food")) {
                    player.incrementToxicity();
                    if (player.getToxicity() > 4) player.setHealth(player.getHealth() - 1);
                    player.setTimeSinceFood(1);
                }
                player.incrementHunger();
                prey.remove(contacting.getBody());
                world.destroyBody(contacting.getBody());
                //delete food
            }
        }
    }

    private void getPlayerParams() {
        if (player.getHealth() == 0) {
            gameOverLost = true;
            player.updateFrame(false, false, true);
        }

        //System.out.println("Player health: "+ player.getHealth() +" PLayer air: " + player.getAir() + " Player hunger: " + player.getHunger() + " Player toxicity: " + player.getToxicity());
    }

    public void renderHealthBars() {
        drawHealthBar(player.getHealth(),
                new Sprite(AssetHandler.assetManager.get(AssetHandler.healthBar, Texture.class)),
                new Sprite(AssetHandler.assetManager.get(AssetHandler.healthBarEmpty, Texture.class)),
                -camera.viewportWidth * 0.49f,
                camera.viewportHeight * 0.36f
        );
        drawHealthBar(player.getAir(),
                new Sprite(AssetHandler.assetManager.get(AssetHandler.airBar, Texture.class)),
                new Sprite(AssetHandler.assetManager.get(AssetHandler.airBarEmpty, Texture.class)),
                -camera.viewportWidth * 0.49f,
                camera.viewportHeight * 0.23f
        );
        drawHealthBar(player.getHunger(),
                new Sprite(AssetHandler.assetManager.get(AssetHandler.hungerBar, Texture.class)),
                new Sprite(AssetHandler.assetManager.get(AssetHandler.hungerBarEmpty, Texture.class)),
                camera.viewportWidth * 0.18f,
                camera.viewportHeight * 0.36f
        );
        drawHealthBar(player.getToxicity(),
                new Sprite(AssetHandler.assetManager.get(AssetHandler.toxicBar, Texture.class)),
                new Sprite(AssetHandler.assetManager.get(AssetHandler.toxicBarEmpty, Texture.class)),
                camera.viewportWidth * 0.18f,
                camera.viewportHeight * 0.23f
        );

    }

    private void drawHealthBar(int barValue, Sprite bar, Sprite emptyBar, float x_offset, float y_offset) {
        stage.act();
        stage.getBatch().begin();
        // Positioning
        Vector3 barPosition = camera.position;
        barPosition.set(barPosition.x + x_offset, barPosition.y + y_offset, 0);

        switch (barValue) {
            case 1:
                stage.getBatch().draw(bar, barPosition.x+50, barPosition.y, bar.getWidth() * 0.10f-50, bar.getHeight() / 2);
                break;
            case 2:
                stage.getBatch().draw(bar, barPosition.x+50, barPosition.y, bar.getWidth() * 0.20f-50, bar.getHeight() / 2);
                break;
            case 3:
                stage.getBatch().draw(bar, barPosition.x+50, barPosition.y, bar.getWidth() * 0.30f-50, bar.getHeight() / 2);
                break;
            case 4:
                stage.getBatch().draw(bar, barPosition.x+50, barPosition.y, bar.getWidth() * 0.40f-50, bar.getHeight() / 2);
                break;
            case 5:
                stage.getBatch().draw(bar, barPosition.x+50, barPosition.y, bar.getWidth() * 0.50f-50, bar.getHeight() / 2);
                break;
            default:
                break;
        }
        stage.getBatch().draw(emptyBar, barPosition.x, barPosition.y, bar.getWidth() * 0.5f, bar.getHeight() / 2);
        stage.getBatch().end();
        stage.draw();
    }

    private void preyUpdate(float deltaTime) {
        ArrayList<Body> toRemove = new ArrayList<>();
        if (waterPrey){
            if (prey.size()<5) {
                addPrey(2, generateObstacles(2), 300, 150, !waterPrey);
                addPrey(3, generateObstacles(2), 300, 150, !waterPrey);
            }
            for (Body p : prey) {
                if (inPlayerView(p.getPosition())) {
                    int xforce = new Random().nextInt(20) - 10;
                    int yforce = new Random().nextInt(10) - 5;
                    if ((p.getPosition().x * PPM > worldSize && xforce > 0) || (p.getPosition().x * PPM < 0 && xforce < 0))
                        xforce = xforce * -1;
                    if ((p.getPosition().y * PPM > 0 && yforce > 0) || (p.getPosition().y * PPM < - oceanDepth && yforce < 0))
                        yforce = yforce * -1;
                    p.setLinearVelocity(p.getLinearVelocity().x + xforce, p.getLinearVelocity().y + yforce);
                } else p.setLinearVelocity(5, 1);
            }
        }
        else {
            if (contactor != null && contacting != null) {
                if (prey.contains(contactor.getBody()) && prey.contains(contacting.getBody())) {
                    toRemove.add(prey.get(prey.indexOf(contactor.getBody())));
                    world.destroyBody(prey.get(prey.indexOf(contactor.getBody())));
                }
            }
            if (prey.isEmpty()) {
                addPrey(2, generateObstacles(2), 300, 150, !waterPrey);
                addPrey(3, generateObstacles(2), 300, 150, !waterPrey);
            }

            for (Body p : prey) {

                    if (player.getSwimming()) {
                        p.setLinearVelocity(1, 0);

                    } else {
                        p.setLinearVelocity(6, 0);
                    }
                    if (p.getPosition().y * PPM < preySpawnHeight - 200 && preyDespawnable) {
                        toRemove.add(p);
                        world.destroyBody(p);
                    }

            }

        }
        for (Body p : toRemove) {
            prey.remove(p);
        }
    }

    private void inputUpdate(float deltaTime) {
        int verticalForce;
        if (jumpforce != 0) {
            verticalForce = jumpforce;
            jumpforce--;
        } else {
            verticalForce = 0;
        }
        int horizontalForce = 0;
        float speed = player.getSpeed();

//        System.out.println("x:" +player2d.getPosition().x * PPM+ " y:" + player2d.getPosition().y * PPM);
        // keyboard input
        if ((Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) && player2d.getPosition().x > 0) {

                player.updateFrame(true, true, false);
                horizontalForce -= 1;

        }
        if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) && player2d.getPosition().x * PPM < worldSize) {

                player.updateFrame(true, false, false);
                horizontalForce += 1;

        }
        if ((Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) && (player2d.getPosition().y * PPM > -oceanDepth)) {
            player.updateFrame(false, false, false);
            verticalForce -= 1;
        }

        if ((Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) && player.getSwimming()) {
            if ((isTouchingObstacle && (player2d.getPosition().y * PPM > -10)) || (waterWorld && atSurface)) {
                player.updateFrame(false, false, false); // REPLACE WITH JUMP ANIMATION
                this.jumpforce = player.getJumpForce();

            } else if (player2d.getPosition().y * PPM < -10) {
                player.updateFrame(false, false, false);

                verticalForce += 1;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            speed = speed * 3;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new LevelMenuScreen(game));
        }

        // touch input
        if (Gdx.input.isTouched()) {

            float xTouchPixels = Gdx.input.getX();
            float yTouchPixels = Gdx.input.getY();
            if (xTouchPixels < Gdx.graphics.getWidth() / 3 && player2d.getPosition().x > 0) {
                player.updateFrame(true, true, false);
                horizontalForce -= 1;
            }
            if (xTouchPixels > 2 * Gdx.graphics.getWidth() / 3 && player2d.getPosition().x * PPM < worldSize) {
                player.updateFrame(true, false, false);
                horizontalForce += 1;
            }
            if (yTouchPixels > 3 * Gdx.graphics.getHeight() / 4 && (player2d.getPosition().y * PPM > -oceanDepth)) {
                player.updateFrame(false, false, false);
                verticalForce -= 1;
            }

            if (yTouchPixels < 2 * Gdx.graphics.getHeight() / 4 && player.getSwimming()) {
                if (isTouchingObstacle && (player2d.getPosition().y * PPM > -10)) {
                    player.updateFrame(false, false, false); // REPLACE WITH JUMP ANIMATION
                    this.jumpforce = 7;

                } else if (player2d.getPosition().y * PPM > -10) {
                    player.updateFrame(false, false, false);

                    verticalForce += 1;
                }
            }
        }
        player2d.setLinearVelocity(horizontalForce * speed, verticalForce * speed);
    }

    private void cameraUpdate(float deltaTime) {
        Vector3 position = camera.position;
        if (player2d.getPosition().x * PPM < idealGameWidth / 2){
            position.x = idealGameWidth / 2;
        } else if (player2d.getPosition().x * PPM > worldSize-idealGameWidth / 2) {
            position.x = worldSize-idealGameWidth / 2;
        } else {
        position.x = player2d.getPosition().x * PPM;
        }
        if (player2d.getPosition().y * PPM < -oceanDepth+(idealGameHeight/2)) {
            position.y = -oceanDepth+(idealGameHeight/2);
        } else {
            position.y = player2d.getPosition().y * PPM;;
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

    public void renderEndGoal2D(Sprite bear) {
        stage.act();
        stage.getBatch().begin();
        stage.getBatch().draw(bear, endGoal.getPosition().x * PPM - (bear.getWidth() / 2), endGoal.getPosition().y * PPM);
        stage.getBatch().end();
        stage.draw();
    }

    public void renderPrey2D(Sprite food, Sprite toxicFood, int width,int height) {
        stage.act();
        stage.getBatch().begin();
        for (Body p : prey) {
            if (p.getUserData().equals("toxic food")) {
                stage.getBatch().draw(toxicFood, p.getPosition().x * PPM - (width/2), p.getPosition().y * PPM, width, height);
            } else {
                stage.getBatch().draw(food, p.getPosition().x * PPM - (width/2), p.getPosition().y * PPM, width, height);
            }
        }
        stage.getBatch().end();
        stage.draw();
    }
    public void renderBoat() {
        if (boat2D != null) {
            stage.act();
            stage.getBatch().begin();
            System.out.println(boat2D.getPosition().x * PPM + " : " + boat2D.getPosition().y * PPM);

            stage.getBatch().draw(boatModel, boat2D.getPosition().x * PPM - (boatModel.getWidth() / 2), boat2D.getPosition().y * PPM);
            stage.getBatch().end();
            stage.draw();
        }
    }

    public void renderCustom(Sprite img, int x, float y, float width, float height) {
        stage.act();
        stage.getBatch().begin();
        stage.getBatch().draw(img, x , y,width,height);
        stage.getBatch().end();
        stage.draw();
    }

    public void renderBackground(Sprite background) {
        int h = oceanDepth;
        stage.act();
        stage.getBatch().begin();
        for (int i = -2; i < worldSize / (h/2) + h; i++) {
            stage.getBatch().draw(background, (h/2) * i, -1 * oceanDepth-50 , h/2, h+150);
            background.flip(true, false);
        }
        stage.getBatch().end();
        stage.draw();
    }

    public boolean[][] renderPopUps(boolean[][] seen, Vector2[] locations, ArrayList<Sprite> popups){
        stage.act();
        stage.getBatch().begin();
        for (int i = 0; i< seen[0].length;i++){
            if (!seen[0][i]) {
                if (inPlayerView(new Vector2(locations[i].x/PPM,locations[i].y/PPM))) {
                    stage.getBatch().draw(popups.get(i), locations[i].x, locations[i].y);
                    seen[0][i] = true;
                    seen[1][i] = true;
                }
            } else if (seen[1][i]){
                if (inPlayerView(new Vector2(locations[i].x/PPM,locations[i].y/PPM))) {
                    stage.getBatch().draw(popups.get(i), locations[i].x, locations[i].y);
                    seen[0][i] = true;
                    seen[1][i] = true;
                }
                else{
                    seen[0][i] = true;
                    seen[1][i] = false;
                }
            }
        }
        stage.getBatch().end();
        stage.draw();
        return seen;
    }

    public boolean[] generateObstacles(int Section) {
        if (Section == 2) {
            boolean[] obstacles1 = {true, false, true, true, false, false, false, true, true, false};
            boolean[] obstacles2 = {false, false, false, true, true, false, true, false, true, true};
            boolean[] obstacles3 = {true, false, true, false, false, true, false, true, false, true};
            boolean[] obstacles4 = {false, true, false, true, true, false, true, false, true, false};
            boolean[] obstacles5 = {true, true, false, false, true, false, true, false, false, true};
            boolean[][] arrays = {obstacles1, obstacles2, obstacles3, obstacles4, obstacles5};
            int rnd = new Random().nextInt(arrays.length);
            return arrays[rnd];
        } else if (Section == 3) {
            boolean[] obstacles6 = {false, false, true, false, false, false, false, false, false, true};
            boolean[] obstacles7 = {false, false, false, true, false, false, false, false, false, true};
            boolean[] obstacles8 = {false, false, false, false, true, false, false, false, false, true};
            boolean[] obstacles9 = {false, false, false, false, false, true, false, false, false, true};
            boolean[] obstacles10 = {false, false, false, false, false, false, true, false, false, true};
            boolean[][] arrays = {obstacles6, obstacles7, obstacles8, obstacles9, obstacles10};
            int rnd = new Random().nextInt(arrays.length);
            return arrays[rnd];
        } else {
            return new boolean[]{true};
        }
    }

    public void placeBox2DObstacles(int section, Vector2[] shape, boolean[] obstacles, int yAxis, boolean doubleWidth, String type) {
        int i;
        if (section == 2) {
            i = 10;
        } else if (section == 3) {
            i = 30;
        } else {
            i = 0;
        }
        if (section == 1) {
            int x = spacing * 4;
            createBox(x / 2 + spacing, 0 - 100, x, 200, true, true, type);
            createBox(x + (x / 2) + spacing, 0 - 50, x, 100, true, true, type);
        }
        else {
            for (boolean o : obstacles) {
                if (o) {
                    makePolygonShapeBody(shape, spacing * i, yAxis, type);
                }
                if (doubleWidth) {
                    i++;
                }
                i++;
            }
        }
    }

    public void renderObstacles(int section, ArrayList<Sprite> choices, boolean[] obstacles, int yAxis, int height) {
        int i;
        int j = 0;
        if (section == 2) {
            i = 10;
        } else if (section == 3) {
            i = 30;
        } else {
            i = 0;
        }
        stage.act();
        stage.getBatch().begin();
        if (section == 1) {
            i++;
            stage.getBatch().draw(choices.get(0), spacing * i, -860, (spacing) * 8, choices.get(0).getHeight());
        } else if (section == 2) {
            for (boolean o : obstacles) {
                if (o) {
                    stage.getBatch().draw(choices.get(j), spacing * i, yAxis - 250, (spacing) * 2, height);
                    j++;
                }
                i++;
                i++;
            }
        } else if (section == 3) {
            for (boolean o : obstacles) {
                if (o) {
                    stage.getBatch().draw(choices.get(j), spacing * i, yAxis - 250, (spacing), height);
                    j++;
                }
                i++;
            }
        }

        stage.getBatch().end();
        stage.draw();
    }

    public void addPrey(int section, boolean[] obstacles, int width, int height, boolean gravityAffected) {
        int i;
        if (section == 2) {
            i = 10;
        } else if (section == 3) {
            i = 30;
        } else {
            i = 0;
        }
        String[] toxicOptions = {"toxic food", "food","food","food"};
        int rnd = new Random().nextInt(toxicOptions.length);
        if (obstacles.length == 10) {
            for (boolean o : obstacles) {
                int xpos = (worldSize/40) * i;
                if (o && !inPlayerView(new Vector2(xpos / PPM, preySpawnHeight / PPM))) {
                    Body p = createBox(xpos, preySpawnHeight, width, height, false, false, toxicOptions[rnd]);
                    if (!gravityAffected)
                        p.setGravityScale(0);
                    prey.add(p);
                }
                i++;
                i++;
            }
        }
        if (toggle) {
            prey.add(createBox(4600, preySpawnHeight - 50, 300, 300, true, false, "food"));
            toggle = false;
        }
    }

    public boolean inPlayerView(Vector2 pos) {
        float playerposx = player2d.getPosition().x * PPM;
        float playerposy = player2d.getPosition().y * PPM;
        float rbound = playerposx + idealGameWidth / 2;
        float tbound = playerposy + idealGameHeight / 2;
        float lbound = playerposx - idealGameWidth / 2;
        float bbound = playerposy - idealGameHeight / 2;
        return (lbound < pos.x * PPM && pos.x * PPM < rbound) || (bbound < pos.x * PPM && pos.x * PPM < tbound);
    }

    public void setPlayer(CharacterAbstraction player) {
        if (toggle) {
            this.player = player;
            player2d = createBox(spacing, 200, (int) player.getWidth(), (int) player.getHeight(), false, true, "Player");
        }
    }

    public int getWorldSize() {
        return worldSize;
    }

    public void setWorldSize(int worldSize) {
        this.worldSize = worldSize;
    }

    public int getOceanDepth() {
        return oceanDepth;
    }

    public void setOceanDepth(int oceanDepth) {
        this.oceanDepth = oceanDepth;
    }

    public int getSpacing() {
        return spacing;
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }

    public boolean isBoatStrike() {
        return boatStrike;
    }

    public void setBoatStrike(boolean boatStrike) {
        this.boatStrike = boatStrike;
    }

    public Sprite getBoatModel() {
        return boatModel;
    }

    public void setBoatModel(Sprite boatModel) {
        this.boatModel = boatModel;
    }

    public boolean isWaterWorld() {
        return waterWorld;
    }

    public void setWaterWorld(boolean waterWorld) {
        this.waterWorld = waterWorld;
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width / SCALE, height / SCALE);
        stage.getViewport().update(width, height);
    }

    @Override
    public void dispose() {
        world.dispose();
        b2dr.dispose();
    }

}
