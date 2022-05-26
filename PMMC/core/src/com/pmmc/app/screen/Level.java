package com.pmmc.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.pmmc.app.AssetHandler;
import com.pmmc.app.GameLauncher;
import com.pmmc.app.character.B2dContactListener;
import com.pmmc.app.character.CharacterAbstraction;
import com.pmmc.app.screen.quiz.Question;
import com.pmmc.app.screen.quiz.Quiz;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

/**
 * LEVEL is a general abstract class that holds common procedures for the different level screens.
 */

public abstract class Level extends AbstractScreen {
    public static final float PPM = 32;
    private static final float SCALE = 2;
    private static World world;
    final Stage stage;
    public Body player2d;
    public Fixture contactor = null;
    public Fixture contacting = null;
    public float idealGameWidth = 1600;
    public float idealGameHeight = 1000;
    public CharacterAbstraction player;
    public boolean isTouchingObstacle = false;
    public int preySpawnHeight;
    public boolean preyDespawnable = false;
    public boolean toggle = true;
    public boolean gameOverLost = false;
    public boolean waterPrey = false;
    public int spacing;
    public int preyWidth;
    public int preyHeight;
    public int boatYAxis;
    public int preySpeed;
    ExtendViewport extendViewport;
    private Body endGoal;
    private int worldSize = 24000;
    private int jumpforce;
    private final ArrayList<Body> prey = new ArrayList<>();
    private final ArrayList<Body> trashes = new ArrayList<>();
    private final ArrayList<Body> oil = new ArrayList<>();
    private boolean atSurface = false;
    private boolean waterWorld = false;
    private boolean boatStrike = false;
    private boolean oilSpill = false;
    private Sprite oilSprite;
    private boolean trash = false;
    private Sprite trashSprite;
    private Sprite boatModel;
    private Body boat2D;
    private boolean predator;
    private Sprite predatorSprite;
    private Body predator2D;
    private boolean predatorSpriteFlipped;
    private int oceanDepth = 1200;
    private boolean directionLeft = false;
    private boolean gameOverWon = false;
    private int minNumPrey = 5;
    private boolean jumpedGulp;
    public boolean[] obstacles2;
    public boolean[] obstacles3;

    private ArrayList<Sound> soundBank;
    private boolean soundTimerRunning;

    private int predatorScale = 1;
    private boolean boatRunning;

    public Level(GameLauncher game, ArrayList<Sound> soundBank) {
        super(game);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, idealGameWidth, idealGameHeight);

        world = new World(new Vector2(0, -10f * PPM), false);
        world.setContactListener(new B2dContactListener(this));

        extendViewport = new ExtendViewport(idealGameWidth, idealGameHeight, camera);
        stage = new Stage();
        stage.setViewport(extendViewport);

        changeMusic("sounds/tropical_music.mp3", 0.7f);

        this.soundBank = soundBank;
        soundTimerRunning = false;
    }

    public static Body createBox(float x, float y, int width, int height, boolean isStatic, boolean fixedRotation, String b2dType) {
        Body pBody;
        BodyDef def = new BodyDef();
        def.type = (isStatic) ? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;
        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = fixedRotation;
        pBody = world.createBody(def);
        pBody.setUserData(b2dType);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2f / PPM, height / 2f / PPM);

        pBody.createFixture(shape, 1.0f);
        shape.dispose();
        return pBody;
    }

    public Body makePolygonShapeBody(Vector2[] vertices, float x, float y, boolean isStatic, String type) {
        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.type = (isStatic) ? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;
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
            transitionScreen(new DeathScreen(game, player));
            changeMusic("sounds/menu_music.mp3", 0.5f);
        }
        if (gameOverWon){
            game.setScreen(new Quiz(game, generateQuestionBank(), this.getClass().toString().substring(26)));
        }
        world.step(deltaTime, 6, 2);
        if (player2d.getPosition().y * PPM > 0) {
            atSurface = false;
            player2d.setGravityScale(1);
            player.setSwimming(false);
            player.setTimeInWater(0);
            player.incrementTimeOutWater();
        } else if (player2d.getPosition().y * PPM > -50) {
            atSurface = true;
            player.setSwimming(true);
            player.incrementTimeOutWater();
        } else {
            atSurface = false;
            player2d.setGravityScale(0);
            player.setSwimming(true);
            player.incrementTimeInWater();
            player.setTimeOutWater(0);
        }

        player.incrementTimeSinceFood();
        inputUpdate();
        playerUpdate();
        preyUpdate();
        hazardsUpdate();
        cameraUpdate();
        stage.getBatch().setProjectionMatrix(camera.combined);

        // Make noise
        if(!soundTimerRunning){Timer.schedule(makeAnimalSound, 7f);}
        soundTimerRunning = true;
    }

    private void inputUpdate() {
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
            changeMusic("sounds/menu_music.mp3", 0.5f);
        }

        // touch input
        if (Gdx.input.isTouched()) {

            float xTouchPixels = Gdx.input.getX();
            float yTouchPixels = Gdx.input.getY();
            if (xTouchPixels < Gdx.graphics.getWidth() / 3f && player2d.getPosition().x > 0) {
                player.updateFrame(true, true, false);
                horizontalForce -= 1;
            }
            if (xTouchPixels > 2 * (Gdx.graphics.getWidth() / 3f) && player2d.getPosition().x * PPM < worldSize) {
                player.updateFrame(true, false, false);
                horizontalForce += 1;
            }
            if (yTouchPixels > 3 * Gdx.graphics.getHeight() / 4f && (player2d.getPosition().y * PPM > -oceanDepth)) {
                player.updateFrame(false, false, false);
                verticalForce -= 1;
            }

            if (yTouchPixels < 2 * (Gdx.graphics.getHeight() / 4f) && player.getSwimming()) {
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

    private void playerUpdate() {
        setPlayerParams();
        getPlayerParams();
    }

    private void setPlayerParams() {
        if (boatStrike) {
            if (contacting != null && contactor != null) {
                if ((contacting.getBody().getUserData().equals("Boat") && contactor.getBody().getUserData().equals("Player")) || (contactor.getBody().getUserData().equals("Boat") && contacting.getBody().getUserData().equals("Player"))) {
                    player.setHealth(player.getHealth() - 1);
                    player2d.applyForceToCenter(0, -500000 * PPM, true);
                    world.destroyBody(boat2D);
                    boat2D = null;
                }
            }
        }
        if (oilSpill) {
            if (contacting != null && contactor != null) {
                if ((contacting.getBody().getUserData().equals("Oil") && contactor.getBody().getUserData().equals("Player")) || (contactor.getBody().getUserData().equals("Oil") && contacting.getBody().getUserData().equals("Player"))) {
                    player.setHealth(player.getHealth() - 1);
                    player2d.setLinearVelocity(0, -500000 * PPM);
                }
            }
        }
        if (trash) {
            if (contacting != null && contactor != null) {
                if ((contacting.getBody().getUserData().equals("Trash") && contactor.getBody().getUserData().equals("Player"))) {
                    player.setHealth(player.getHealth() - 1);
                    player.incrementToxicity();
                    trashes.remove(contacting.getBody());
                    world.destroyBody(contacting.getBody());
                } else if (contactor.getBody().getUserData().equals("Trash") && contacting.getBody().getUserData().equals("Player")){
                    player.setHealth(player.getHealth() - 1);
                    player.incrementToxicity();
                    trashes.remove(contactor.getBody());
                    world.destroyBody(contactor.getBody());
                }
            }
        }
        if (predator){
            if (contacting != null && contactor != null) {
                if ((contacting.getBody().getUserData().equals("Predator") && contactor.getBody().getUserData().equals("Player")) || (contactor.getBody().getUserData().equals("Predator") && contacting.getBody().getUserData().equals("Player"))) {
                    player.setHealth(player.getHealth() - 1);
                    world.destroyBody(predator2D);
                    predator2D = null;

                }
            }
        }
        if (contacting != null && contactor != null) {
            if ((contacting.getBody().getUserData().equals("End Goal") && contactor.getBody().getUserData().equals("Player")) || (contactor.getBody().getUserData().equals("End Goal") && contacting.getBody().getUserData().equals("Player"))) {
                gameOverWon = true;
            }
        }
        if (player.getAir() == 0 || player.getHunger() == 0) {
            player.incrementTimeSinceDamage();
            if (player.getTimeSinceDamage() % player.getDamageRate() == 0) {
                player.setHealth(player.getHealth() - 1);
            }
        }
        if (player.getAir() == 5 && player.getHunger() == 5) {
            player.incrementTimeSinceDamage();
            if (player.getTimeSinceDamage() % player.getDamageRate() == 0) {
                if (player.getHealth() <= player.getMaxLevels()- player.getToxicity())
                player.setHealth(player.getHealth() + 1);
            }
        }
        if (player.getSwimming() && !atSurface) {
            //In the water
            if (player.getTimeInWater() % player.getAirLossRate() == 0) {
                player.setAir(player.getAir() - 1);
                jumpedGulp = false;
            }
        } else if (player.getSwimming() && atSurface) {
            //at surface
            if (player.getTimeOutWater() % (player.getAirLossRate() / 5) == 0) {
                player.setAir(player.getAir() + 1);
                jumpedGulp = false;
            }
        }  else if (!player.getSwimming() && !atSurface) {
            //out of water
                if (player.getTimeOutWater() % (player.getAirLossRate() / 5) == 0) {
                    player.setAir(player.getAir() + 1);
                }
                if (!jumpedGulp){
                    player.setAir(player.getAir() + 1);
                    jumpedGulp = true;
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
                }
                player.incrementHunger();
                player.setTimeSinceFood(0);
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

    private void preyUpdate() {
        ArrayList<Body> toRemove = new ArrayList<>();
        if (contactor != null && contacting != null) {
            if (contactor.getBody().getUserData().equals("Oil") && prey.contains(contacting.getBody())) {
                toRemove.add(prey.get(prey.indexOf(contacting.getBody())));
                world.destroyBody(prey.get(prey.indexOf(contacting.getBody())));
            } else if (contacting.getBody().getUserData().equals("Oil") && prey.contains(contactor.getBody())){
                toRemove.add(prey.get(prey.indexOf(contactor.getBody())));
                world.destroyBody(prey.get(prey.indexOf(contactor.getBody())));
            }
        }
        if (waterPrey) {
            if (prey.size() < minNumPrey) {
                addPrey(2, generateObstacles(2), preyWidth, preyHeight, !waterPrey);
                addPrey(3, generateObstacles(3), preyWidth, preyHeight, !waterPrey);
            }
            for (Body p : prey) {
                 if (inPlayerView(p.getPosition())) {
                    int xforce = new Random().nextInt(preySpeed) - (preySpeed / 2);
                    int yforce = new Random().nextInt(preySpeed / 2) - (preySpeed / 4);
                    if ((p.getPosition().x * PPM > worldSize && xforce > 0) || (p.getPosition().x * PPM < 0 && xforce < 0))
                        xforce = xforce * -1;
                    if ((p.getPosition().y * PPM > 0 && yforce > 0) || (p.getPosition().y * PPM < -oceanDepth && yforce < 0))
                        yforce = yforce * -1;
                    p.setLinearVelocity(p.getLinearVelocity().x + xforce, p.getLinearVelocity().y + yforce);
                }
                  else {
                     p.setLinearVelocity(0, 0);
                 }
            }
        } else {
            if (contactor != null && contacting != null) {
                if (prey.contains(contactor.getBody()) && prey.contains(contacting.getBody())) {
                    toRemove.add(prey.get(prey.indexOf(contactor.getBody())));
                    world.destroyBody(prey.get(prey.indexOf(contactor.getBody())));
                }
            }
            for (Body p : prey) {
                if (p.getPosition().x * PPM > worldSize || p.getPosition().x * PPM < 0 || p.getPosition().y * PPM < -oceanDepth ) {
                    toRemove.add(p);
                    world.destroyBody(p);
                }
                p.setGravityScale(p.getPosition().y * PPM < 0 ? 0 : 1);
                if (inPlayerView(p.getPosition())){
                    p.setLinearVelocity(p.getPosition().y * PPM < 0 ? preySpeed : 8, p.getPosition().y * PPM < 0 ? -2 : 0);
                }
            }
            if (prey.size() < minNumPrey) {
                addPrey(2, generateObstacles(2), preyWidth, preyHeight, !waterPrey);
                addPrey(3, generateObstacles(3), preyWidth, preyHeight, !waterPrey);
            }
        }
        for (Body p : toRemove) {
            prey.remove(p);
        }
    }

    private void hazardsUpdate() {
        if (boatStrike && boatModel != null) {
            if (boat2D == null) {
                if (atSurface) {
                    if (player2d.getPosition().x * PPM < worldSize / 2f) {
                        boat2D = (boatYAxis != 0) ? makePolygonShapeBody(new Vector2[]{new Vector2(0, 0), new Vector2(500 / PPM, 0), new Vector2(133 / PPM, -250 / PPM)}, worldSize - (spacing*3), 0, false, "Boat") : createBox(worldSize - (spacing*3), 0, 500, 5, false, true, "Boat");
                        directionLeft = true;
                    } else {
                        boat2D = (boatYAxis != 0) ? makePolygonShapeBody(new Vector2[]{new Vector2(0, 0), new Vector2(500 / PPM, 0), new Vector2(350 / PPM, -250 / PPM)}, (spacing*3), 0, false, "Boat") : createBox((spacing*3), 0, 500, 5, false, true, "Boat");
                        directionLeft = false;
                    }
                    boat2D.setGravityScale(0);
                }
            } else if (boat2D.getUserData().equals("Boat")) {

                if ((boat2D.getPosition().x * PPM) < spacing || (boat2D.getPosition().x * PPM) > (worldSize - spacing) || (boatRunning && boat2D.getInertia() == 0 )) {
                    world.destroyBody(boat2D);
                    boat2D = null;
                    boatRunning = false;
                } else {
                    int xForce = (!directionLeft) ? 100 : -100;
                    float yForce;
                    if (boat2D.getPosition().y > 0) {
                        yForce = -boat2D.getPosition().y * PPM;
                    } else if (boat2D.getPosition().y < 0) {
                        yForce = boat2D.getPosition().y * PPM;
                    } else {
                        yForce = 0;
                    }
                    boat2D.setLinearVelocity(xForce, yForce);
                    boatRunning = true;

                }
            }
        }
        if (oilSpill && oilSprite != null) {
            if (oil.size() < 3) {
                if (obstacles2 != null && obstacles3 != null){
                int n = new Random().nextInt(30)+10;
                int i = n%10;
                int xpos = (worldSize / 40) * n;
                if (n < 30 && !obstacles2[i]){

                    Body p = createBox(xpos, 10, (int) oilSprite.getWidth(), 5, true, true, "Oil");
                    System.out.println(p);
                    oil.add(p);
                } else if (n >= 30 && !obstacles3[i]){
                    Body p = createBox(xpos, 10, (int) oilSprite.getWidth(), 5, true, true, "Oil");
                    System.out.println(p);
                    oil.add(p);
                }
                } else {
                    Body p = createBox(new Random().nextInt(worldSize - (4*spacing)) + (2*spacing), 10, (int) oilSprite.getWidth(), 5, true, true, "Oil");
                    oil.add(p);
                }

            }
        }
        if (trash && trashSprite != null) {
            if (trashes.size() < 5) {
                Body p = createBox(new Random().nextInt(worldSize - (4*spacing)) + (2*spacing), -new Random().nextInt(oceanDepth), (int) trashSprite.getWidth(), (int) trashSprite.getHeight(), false, true, "Trash");
                p.setGravityScale(0);
                trashes.add(p);
            } else {
                for (Body t : trashes) {
                    int xforce = new Random().nextInt(4) - (2);
                    int yforce = new Random().nextInt(2) - (1);
                    if ((t.getPosition().x * PPM > worldSize && xforce > 0) || (t.getPosition().x * PPM < 0 && xforce < 0))
                        xforce = xforce * -1;
                    if ((t.getPosition().y * PPM > 0 && yforce > 0) || (t.getPosition().y * PPM < -oceanDepth && yforce < 0))
                        yforce = yforce * -1;
                    t.setLinearVelocity(xforce, yforce);
                }
            }
        }
        if (predator && predatorSprite != null){
            if (predator2D == null) {
                Body p = createBox(new Random().nextInt(worldSize - (4*spacing)) + (2*spacing), -new Random().nextInt(oceanDepth), (int) predatorSprite.getWidth() * predatorScale, (int) predatorSprite.getHeight() * predatorScale, false, true, "Predator");
                p.setGravityScale(0);
                predator2D = p;
            } else {

                int xforce = (int) (new Random().nextInt((int) (player.getSpeed())) - (player.getSpeed()/2));
                int yforce = (int) (new Random().nextInt((int) (player.getSpeed())) - (player.getSpeed()/2));
                if ((player2d.getPosition().x < predator2D.getPosition().x && xforce > 0 )|| (player2d.getPosition().x > predator2D.getPosition().x && xforce < 0 )){
                    xforce = xforce * -1;
                }
                if ((player2d.getPosition().y < predator2D.getPosition().y && yforce > 0) || (player2d.getPosition().y > predator2D.getPosition().y && yforce < 0)){
                    yforce = yforce * -1;
                }
                predatorSpriteFlipped = player2d.getPosition().x < predator2D.getPosition().x;
                if (yforce > 0 && predator2D.getPosition().y * PPM > -(predatorSprite.getHeight()/2)){
                    yforce = 0;
                }
                predator2D.setLinearVelocity(xforce, yforce);

            }
        }
    }

    private void cameraUpdate() {
        Vector3 position = camera.position;
        position.x = (player2d.getPosition().x * PPM < idealGameWidth / 2) ? idealGameWidth / 2 : Math.min(player2d.getPosition().x * PPM, worldSize - idealGameWidth / 2);
        position.y = Math.max(player2d.getPosition().y * PPM, -oceanDepth + (idealGameHeight / 2));
        camera.position.set(position);
        camera.update();
    }

    protected abstract HashMap<Integer, Question> generateQuestionBank();

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
                stage.getBatch().draw(bar, barPosition.x, barPosition.y, bar.getWidth() * 0.20f, bar.getHeight() / 2);
                break;
            case 2:
                stage.getBatch().draw(bar, barPosition.x, barPosition.y, bar.getWidth() * 0.28f, bar.getHeight() / 2);
                break;
            case 3:
                stage.getBatch().draw(bar, barPosition.x, barPosition.y, bar.getWidth() * 0.36f, bar.getHeight() / 2);
                break;
            case 4:
                stage.getBatch().draw(bar, barPosition.x, barPosition.y, bar.getWidth() * 0.42f, bar.getHeight() / 2);
                break;
            case 5:
                stage.getBatch().draw(bar, barPosition.x, barPosition.y, bar.getWidth() * 0.50f, bar.getHeight() / 2);
                break;
            default:
                break;
        }
        stage.getBatch().draw(emptyBar, barPosition.x, barPosition.y, bar.getWidth() * 0.5f, bar.getHeight() / 2);
        stage.getBatch().end();
        stage.draw();
    }

    public void renderPlayer2D() {
        stage.act();
        stage.getBatch().begin();
        stage.getBatch().draw(player, player2d.getPosition().x * PPM - (player.getWidth() / 2), player2d.getPosition().y * PPM - (player.getHeight() / 2));
        stage.getBatch().end();
        stage.draw();
    }

    public void renderEndGoal2D(Sprite mammal) {
        stage.act();
        stage.getBatch().begin();
        stage.getBatch().draw(mammal, endGoal.getPosition().x * PPM - (mammal.getWidth() / 2), endGoal.getPosition().y * PPM - (mammal.getHeight() / 2));
        stage.getBatch().end();
        stage.draw();
    }

    public void renderPrey2D(Sprite food, Sprite toxicFood) {
        stage.act();
        stage.getBatch().begin();
        for (Body p : prey) {
            if (p.getUserData().equals("toxic food")) {
                stage.getBatch().draw(toxicFood, p.getPosition().x * PPM - (preyWidth / 2f), p.getPosition().y * PPM - (preyHeight / 2f), preyWidth, preyHeight);
            } else {
                stage.getBatch().draw(food, p.getPosition().x * PPM - (preyWidth / 2f), p.getPosition().y * PPM - (preyHeight / 2f), preyWidth, preyHeight);
            }
        }
        stage.getBatch().end();
        stage.draw();
    }

    public void renderOil() {
        if (oilSpill) {
            stage.act();
            stage.getBatch().begin();
            for (Body b : oil) {
                stage.getBatch().draw(oilSprite, b.getPosition().x * PPM - (oilSprite.getWidth() / 2), b.getPosition().y * PPM - (oilSprite.getHeight()));
            }
            stage.getBatch().end();
            stage.draw();
        }
    }

    public void renderTrash() {
        if (trash) {
            stage.act();
            stage.getBatch().begin();
            for (Body b : trashes) {
                stage.getBatch().draw(trashSprite, b.getPosition().x * PPM - (trashSprite.getWidth() / 2), b.getPosition().y * PPM - (trashSprite.getHeight() / 2));
            }
            stage.getBatch().end();
            stage.draw();
        }
    }

    public void renderBoat() {
        if (boat2D != null) {
            if (directionLeft){
                boatModel.flip(true,false);
            }
            stage.act();
            stage.getBatch().begin();
            if (boatYAxis != 0) {
                stage.getBatch().draw(boatModel, boat2D.getPosition().x * PPM - (boatModel.getWidth() / 2), boat2D.getPosition().y * PPM - (boatModel.getHeight() / 2));
            } else {
                stage.getBatch().draw(boatModel, boat2D.getPosition().x * PPM - (boatModel.getWidth() / 2), boat2D.getPosition().y * PPM - 20);
            }
            stage.getBatch().end();
            stage.draw();
            if (directionLeft){
                boatModel.flip(true,false);
            }
        }
    }

    public void renderCustom(Sprite img, int x, float y, float width, float height) {
        stage.act();
        stage.getBatch().begin();
        stage.getBatch().draw(img, x, y, width, height);
        stage.getBatch().end();
        stage.draw();
    }

    public void renderBackground(Sprite background, int yAxis, int scale) {
        stage.act();
        stage.getBatch().begin();
        for (int i = -2; i < worldSize / (oceanDepth / 2) + 2; i++) {
            if (scale != 1){
                stage.getBatch().draw(background, (oceanDepth / 2f) * i, yAxis, (oceanDepth / 2f ) * scale, ((oceanDepth / 2f ) * scale) * (background.getHeight()/background.getWidth()));
                for (int n = 0; n < scale - 1 ; n++){
                    i++;
                }
            }
            else {
                stage.getBatch().draw(background, (oceanDepth / 2f) * i, yAxis, oceanDepth / 2f, oceanDepth + 50);
            }
            background.flip(true, false);
        }
        stage.getBatch().end();
        stage.draw();
    }

    public boolean[][] renderPopUps(boolean[][] seen, Vector2[] locations, ArrayList<Sprite> popups) {
        stage.act();
        stage.getBatch().begin();
        for (int i = 0; i < seen[0].length; i++) {
            if (!seen[0][i]) {
                if (inPlayerView(new Vector2(locations[i].x / PPM, locations[i].y / PPM))) {
                    stage.getBatch().draw(popups.get(i), locations[i].x, locations[i].y);
                    seen[0][i] = true;
                    seen[1][i] = true;
                }
            } else if (seen[1][i]) {
                if (inPlayerView(new Vector2(locations[i].x / PPM, locations[i].y / PPM))) {
                    stage.getBatch().draw(popups.get(i), locations[i].x, locations[i].y);
                    seen[0][i] = true;
                    seen[1][i] = true;
                } else {
                    seen[0][i] = true;
                    seen[1][i] = false;
                }
            }
        }
        stage.getBatch().end();
        stage.draw();
        return seen;
    }

    public void renderObstacles(int section, ArrayList<Sprite> choices, boolean[] obstacles, int yAxis, int height, boolean doubleWidth) {
        int i;
        int j = 0;
        int width = spacing;
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
            stage.getBatch().draw(choices.get(0), spacing * i, yAxis - choices.get(0).getHeight(), (spacing) * 8, choices.get(0).getHeight());
        } else {
            if (doubleWidth) {
                width = (spacing) * 2;
            }
            for (boolean o : obstacles) {

                if (o) {
                    stage.getBatch().draw(choices.get(j), spacing * i, yAxis - 250, width, height);
                }
                if (doubleWidth) {
                    i++;
                }
                i++;
            }
        }
        stage.getBatch().end();
        stage.draw();
    }

    public void renderPredator() {
        if (predator2D != null) {
            if (predatorSpriteFlipped) {
                predatorSprite.flip(true, false);
            }
            stage.act();
            stage.getBatch().begin();
            stage.getBatch().draw(predatorSprite, predator2D.getPosition().x * PPM - ((predatorSprite.getWidth() * predatorScale) / 2), predator2D.getPosition().y * PPM - ((predatorSprite.getHeight() * predatorScale) / 2), predatorSprite.getWidth() * predatorScale, predatorSprite.getHeight() * predatorScale);
            stage.getBatch().end();
            stage.draw();
            if (predatorSpriteFlipped) {
                predatorSprite.flip(true, false);
            }
        }
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
            boolean[] obstacles6 = {false, false, true, false, false, false, false, false, false, false};
            boolean[] obstacles7 = {false, false, false, true, false, false, false, false, false, false};
            boolean[] obstacles8 = {false, false, false, false, true, false, false, false, false, false};
            boolean[] obstacles9 = {false, false, false, false, false, true, false, false, false, false};
            boolean[] obstacles10 = {false, false, false, false, false, false, true, false, false, false};
            boolean[][] arrays = {obstacles6, obstacles7, obstacles8, obstacles9, obstacles10};
            int rnd = new Random().nextInt(arrays.length);
            return arrays[rnd];
        } else if (Section == 4) {
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
            createBox(x / 2f + spacing, -100, x, 200, true, true, type);
            createBox(x + (x / 2f) + spacing, -50, x, 100, true, true, type);
        } else {

            for (boolean o : obstacles) {
                if (o) {
                    makePolygonShapeBody(shape, spacing * i, yAxis, true, type);
                }
                if (doubleWidth) {
                    i++;
                }
                i++;
            }
        }
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
        String[] toxicOptions = {"toxic food", "food", "food", "food"};
        int rnd = new Random().nextInt(toxicOptions.length);
        if (obstacles.length == 10) {
            for (boolean o : obstacles) {
                int xpos = (worldSize / 40) * i;
                if (o && !inPlayerView(new Vector2(xpos / PPM, preySpawnHeight / PPM))) {
                    Body p = createBox(xpos, (waterPrey) ? preySpawnHeight - (new Random().nextInt(600) - 300 ): preySpawnHeight , width, height, false, false, toxicOptions[rnd]);
                    if (!gravityAffected)
                        p.setGravityScale(0);
                    prey.add(p);
                }
                if (section == 2){
                    i++;
                }
                i++;
            }
        }
        if (toggle) {
            prey.add(createBox(spacing* 7.5f, preySpawnHeight - 50, 300, 300, true, false, "food"));
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

    public void setEndGoal(Sprite endGoalSprite, int height) {
        if (toggle) {
            endGoal = createBox(worldSize - spacing/2f, height, (int) endGoalSprite.getWidth(), (int) endGoalSprite.getHeight(), true, true, "End Goal");
        }
    }

    public Body getEndGoal() {
        return endGoal;
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

    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }

    public void setBoatStrike(boolean boatStrike) {
        this.boatStrike = boatStrike;
    }

    public void setBoatModel(Sprite boatModel) {
        this.boatModel = boatModel;
    }

    public void setBoatYAxis(int boatYAxis) {
        this.boatYAxis = boatYAxis;
    }

    public void setWaterWorld(boolean waterWorld) {
        this.waterWorld = waterWorld;
    }

    public void setOilSpill(boolean oilSpill) {
        this.oilSpill = oilSpill;
    }

    public void setTrash(boolean trash) {
        this.trash = trash;
    }

    public void setOilSprite(Sprite oilSprite) {
        this.oilSprite = oilSprite;
    }

    public void setTrashSprite(Sprite trashSprite) {
        this.trashSprite = trashSprite;
    }

    public void setMinNumPrey(int minNumPrey) {
        this.minNumPrey = minNumPrey;
    }

    public void setPredator(boolean predator) {
        this.predator = predator;
    }

    public void setPredatorSprite(Sprite predatorSprite) {
        this.predatorSprite = predatorSprite;
    }

    private void changeMusic(String musicPath, float volume){
        game.music.stop();
        game.music = Gdx.audio.newMusic(Gdx.files.internal(musicPath));
        game.music.setLooping(true);
        game.music.setVolume(volume);
        game.music.play();
    }

    private final Timer.Task makeAnimalSound = new Timer.Task() {
        @Override
        public void run() {
            Collections.shuffle(soundBank);
            soundBank.get(0).play();
            soundTimerRunning = false;
        }
    };

    public void setPredatorScale(int predatorScale) {
        this.predatorScale = predatorScale;
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width / SCALE, height / SCALE);
        stage.getViewport().update(width, height);
    }

    @Override
    public void dispose() {
        world.dispose();
        makeAnimalSound.cancel();
    }

}
