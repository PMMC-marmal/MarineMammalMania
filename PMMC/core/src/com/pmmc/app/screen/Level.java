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
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.pmmc.app.AssetHandler;
import com.pmmc.app.GameLauncher;
import com.pmmc.app.character.B2dContactListener;
import com.pmmc.app.character.CharacterAbstraction;

import java.util.ArrayList;
import java.util.Random;

/**
 * Menu is a general abstract class that holds common procedures for the different menu screens.
 */

public abstract class Level extends AbstractScreen {
    private static final float PPM = 32;
    private static final float SCALE = 2;
    private static World world;
    public final Body player2d;
    public Fixture contactor;
    public Fixture contacting;
    final Stage stage;
    private final Box2DDebugRenderer b2dr;
    public float gameWidth;
    public float gameHeight;
    public CharacterAbstraction player;
    public boolean isSwimming;
    public boolean isTouchingIceBerg;
    int spacing = Gdx.graphics.getWidth() / 3;
    ScreenViewport viewPort;
    private int jumpforce;
    private ArrayList<Body> prey;

    public Level(GameLauncher game) {
        super(game);

        // Setup camera to be in the center
        gameWidth = Gdx.graphics.getWidth();
        gameHeight = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth() / SCALE, Gdx.graphics.getHeight() / SCALE);

        world = new World(new Vector2(0, 0f), false);
        world.setContactListener(new B2dContactListener(this));
        b2dr = new Box2DDebugRenderer();

        player2d = createBox(200, 200, 290, 180, false, true, "Player");
        contactor = null;
        contacting = null;
        prey = new ArrayList<>();

        viewPort = new ScreenViewport(camera);
        stage = new Stage();
        stage.setViewport(viewPort);
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

    public Body makePolygonShapeBody(Vector2[] vertices, float x, float y){
        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.type = BodyDef.BodyType.StaticBody;
        boxBodyDef.position.set(x / PPM, y / PPM);
        boxBodyDef.fixedRotation = true;
        Body boxBody = world.createBody(boxBodyDef);
        boxBody.setUserData("IceBerg");
        PolygonShape polygon = new PolygonShape();
        polygon.set(vertices);
        boxBody.createFixture(polygon,1.0f);
        polygon.dispose();
        return boxBody;
    }

    public void Render() {
        update(Gdx.graphics.getDeltaTime());
    }

    private void update(float deltaTime) {
        world.step(deltaTime, 6, 2);
        if (player2d.getPosition().y * PPM > 200) {
            world.setGravity(new Vector2(0, -10f*PPM));
            isSwimming = false;
            player.setTimeInWater(0);
            player.incrementTimeOutWater();
        } else {
            world.setGravity(new Vector2(0, 0f));
            isSwimming = true;
            player.incrementTimeInWater();
            player.setTimeOutWater(0);
        }
        player.incrementTimeSinceFood();
        inputUpdate(deltaTime);
        playerUpdate(deltaTime);
        preyUpdate(deltaTime);
        cameraUpdate(deltaTime);
        stage.getBatch().setProjectionMatrix(camera.combined);
    }

    private void playerUpdate(float deltaTime) {
        setPlayerParams(deltaTime);
        getPlayerParams();
    }

    private void setPlayerParams(float deltaTime) {
        if (player.getAir() ==0 || player.getHunger() == 0 ){
            player.incrementTimeSinceDamage();
            if (player.getTimeSinceDamage() % player.getDamageRate() == 0){
                player.setHealth(player.getHealth() -1 );
            }
        }
        if (isSwimming) {
            if (player.getTimeInWater() % player.getAirLossRate() == 0){
                player.setAir(player.getAir() - 1);
            }
        } else {
            if (player.getTimeOutWater()  % (player.getAirLossRate()/5) == 0){
                player.setAir(player.getAir() + 1);
            }
        }
        if (player.getTimeSinceFood() % player.getFoodLossRate() == 0){
            player.setHunger(player.getHunger() - 1);
        }
        if (contacting != null && contactor != null) {
            if (prey.contains(contacting.getBody()) && contactor.getBody().getUserData().equals("Player")) {
                if(contacting.getBody().getUserData().equals("toxic food")){
                    player.incrementToxicity();
                    if (player.getToxicity() > 5) player.setHealth(player.getHealth() -1 );
                }
                player.incrementHunger();
                prey.remove(contacting.getBody());
                world.destroyBody(contacting.getBody());
                //delete food
            }
        }
    }

    private void getPlayerParams(){
        if( player.getHealth() == 0) {
            player.updateFrame(false, false, true);
        }

        System.out.println("Player health: "+ player.getHealth() +" PLayer air: " + player.getAir() + " Player hunger: " + player.getHunger() + " Player toxicity: " + player.getToxicity());
    }

    public void renderHealthBars(){
        drawHealthBar(player.getHealth(),
                new Sprite(AssetHandler.assetManager.get(AssetHandler.healthBar, Texture.class)),
                new Sprite(AssetHandler.assetManager.get(AssetHandler.healthBarEmpty, Texture.class)),
                -Gdx.graphics.getWidth()*0.5f,
                Gdx.graphics.getHeight()*0.38f
        );
        drawHealthBar(player.getAir(),
                new Sprite(AssetHandler.assetManager.get(AssetHandler.airBar, Texture.class)),
                new Sprite(AssetHandler.assetManager.get(AssetHandler.airBarEmpty, Texture.class)),
                -Gdx.graphics.getWidth()*0.5f,
                Gdx.graphics.getHeight()*0.25f
        );
        drawHealthBar(player.getHunger(),
                new Sprite(AssetHandler.assetManager.get(AssetHandler.hungerBar, Texture.class)),
                new Sprite(AssetHandler.assetManager.get(AssetHandler.hungerBarEmpty, Texture.class)),
                Gdx.graphics.getWidth()*0.22f,
                Gdx.graphics.getHeight()*0.38f
        );
        drawHealthBar(player.getToxicity(),
                new Sprite(AssetHandler.assetManager.get(AssetHandler.toxicBar, Texture.class)),
                new Sprite(AssetHandler.assetManager.get(AssetHandler.toxicBarEmpty, Texture.class)),
                Gdx.graphics.getWidth()*0.22f,
                Gdx.graphics.getHeight()*0.25f
        );

    }

    private void drawHealthBar(int barValue, Sprite bar, Sprite emptyBar, float x_offset, float y_offset){
        stage.act();
        stage.getBatch().begin();
        // Positioning
        Vector3 barPosition = camera.position;
        barPosition.set(barPosition.x + x_offset, barPosition.y + y_offset, 0);

        switch (barValue){
            case 1:
                stage.getBatch().draw(bar, barPosition.x, barPosition.y, bar.getWidth()*0.22f, bar.getHeight()*0.7f);
                break;
            case 2:
                stage.getBatch().draw(bar, barPosition.x, barPosition.y, bar.getWidth()*0.34f, bar.getHeight()*0.7f);
                break;
            case 3:
                stage.getBatch().draw(bar, barPosition.x, barPosition.y, bar.getWidth()*0.46f, bar.getHeight()*0.7f);
                break;
            case 4:
                stage.getBatch().draw(bar, barPosition.x, barPosition.y, bar.getWidth()*0.58f, bar.getHeight()*0.7f);
                break;
            case 5:
                stage.getBatch().draw(bar, barPosition.x, barPosition.y, bar.getWidth()*0.7f, bar.getHeight()*0.7f);
                break;
            default:
                break;
        }
        stage.getBatch().draw(emptyBar, barPosition.x, barPosition.y, bar.getWidth()*0.7f, bar.getHeight()*0.7f);
        stage.getBatch().end();
        stage.draw();
    }

    private void preyUpdate(float deltaTime){
        if (prey.isEmpty()){
            addPrey(2, generateObstacles(2),300, 300, 150);
            addPrey(3, generateObstacles(2),300, 300, 150);
        }
        ArrayList<Body> toRemove = new ArrayList<>();
        for (Body p : prey){

            if (isSwimming){
                p.setLinearVelocity(0.5f,0);
            }
            else {
                p.setLinearVelocity(2, 0);
            }
            if (p.getPosition().y * PPM < 0){
                toRemove.add(p);
                world.destroyBody(p);
            }
        }
        for ( Body p : toRemove){
            prey.remove(p);
        }
    }

    private void inputUpdate(float deltaTime) {
        int verticalForce;
        if (jumpforce != 0){
            verticalForce = jumpforce;
            jumpforce --;
        }else{
            verticalForce = 0;
        }
        int horizontalForce = 0;
        float speed = player.getSpeed() ;

//        System.out.println("x:" +player2d.getPosition().x * PPM+ " y:" + player2d.getPosition().y * PPM);
        // keyboard input
        if ((Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A) )&& player2d.getPosition().x > 0) {
            player.updateFrame(true,true, false);
            horizontalForce -= 1;
        }
        if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D) ) && player2d.getPosition().x * PPM< 15000) {
            player.updateFrame(true,false, false);
            horizontalForce += 1;
        }
        if ((Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S) ) && (player2d.getPosition().y * PPM > -700)) {
            player.updateFrame(false,false, false);
            verticalForce -= 1;
        }

        if ((Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W) ) && isSwimming){
            if(isTouchingIceBerg && (player2d.getPosition().y * PPM > 190))
            {
                player.updateFrame(false,false, false); // REPLACE WITH JUMP ANIMATION
                this.jumpforce = 7;

            }
            else {
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
        /*if(Gdx.input.isTouched()){
            float xTouchPixels = Gdx.input.getX();
            float yTouchPixels = Gdx.input.getY();
            //move left
            if(xTouchPixels < Gdx.graphics.getWidth()/2 && player2d.getPosition().x > 0) {
                player.updateFrame(true, true);
                horizontalForce -= 1;
            }
            // move right
            if(xTouchPixels > Gdx.graphics.getWidth()/2 && player2d.getPosition().x * PPM< 15000) {
                player.updateFrame(true, false);
                horizontalForce += 1;
            }
            // move down
            if(yTouchPixels > Gdx.graphics.getHeight()/3 && (player2d.getPosition().y * PPM > -700)) {
                player.updateFrame(false, true);
                verticalForce -= 1;
            }
            // move up
            if(yTouchPixels < 2*Gdx.graphics.getHeight()/3 && (player2d.getPosition().y * PPM < 200)) {
                player.updateFrame(false, false);
                verticalForce += 1;
            }
        }*/
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
    public void renderPrey2D(Sprite food) {
        stage.act();
        stage.getBatch().begin();
        for (Body p: prey){
            food.setRotation(p.getAngle());
            stage.getBatch().draw(food, p.getPosition().x * PPM - 150, p.getPosition().y * PPM, 300,300);
        }
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
            int x = (Gdx.graphics.getWidth() / 3 ) * 5;
            createBox(Gdx.graphics.getWidth() / 3 * i + (x/2), 100, x, Gdx.graphics.getHeight() / 4, true, true, "IceBerg");
            createBox(x + (x/2), 175 , x, 100, true, true,"IceBerg");
        }
        else if(obstacles.length == 10) {
            for (boolean o : obstacles) {
                if (o) {
//                    createBox(Gdx.graphics.getWidth() / 3 * i + (Gdx.graphics.getWidth() / 6), 100, Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 4, true, true);
                    makePolygonShapeBody(new Vector2[]{new Vector2(0,0 ),new Vector2((Gdx.graphics.getWidth() / 3)/PPM, 0),new Vector2((Gdx.graphics.getWidth() / 6)/PPM, -200/PPM)} ,Gdx.graphics.getWidth() / 3 * i , 225);
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

    public void addPrey(int section, boolean[] obstacles, int y, int width, int height){
        int i;
        if (section == 2) {
            i = 10;
        } else if (section == 3) {
            i = 20;
        } else {
            i = 0;
        }
        String[] toxicOptions = {"toxic food", "food"};
        int rnd = new Random().nextInt(toxicOptions.length);
        if(obstacles.length == 10) {
            for (boolean o : obstacles) {
                if (o) {
//                    createBox(Gdx.graphics.getWidth() / 3 * i + (Gdx.graphics.getWidth() / 6), 100, Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 4, true, true);
                    prey.add(createBox(Gdx.graphics.getWidth() / 3 * i , y, width,height, false, false,toxicOptions[rnd]));
                }
                i++;
            }
        }
        else{
            prey.add(createBox(4200,150, 300,300, true, true,"food"));
        }
    }
    public void resetPrey(){
        prey.clear();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width/SCALE, height/SCALE);
        stage.getViewport().update(width, height);
    }

    @Override
    public void dispose() {
        world.dispose();
        b2dr.dispose();
    }

}
