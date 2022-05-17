package com.pmmc.app.screen.quiz;

import static com.badlogic.gdx.graphics.GL20.GL_BLEND;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.pmmc.app.AssetHandler;
import com.pmmc.app.GameLauncher;
import com.pmmc.app.screen.LevelMenuScreen;
import com.pmmc.app.screen.Menu;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Quiz extends Menu {

    public int score;
    public int currentQuizQuestion;
    public Map<Integer, Question> questionBank;
    public Stage stage;
    public Sprite choice;
    public Sprite choiceActivated;
    public BitmapFont font;
    private float answerY;
    private int answerCorrect;
    private boolean timerRunning;

    public final float BUTTON_WIDTH = Gdx.graphics.getWidth() * 0.8f;
    public final float BUTTON_HEIGHT = Gdx.graphics.getHeight() * 0.15f;
    public final float x = (Gdx.graphics.getWidth()/2f) - (BUTTON_WIDTH / 2f);
    public final float firstY = Gdx.graphics.getHeight()/12f;

    public Quiz(GameLauncher game, HashMap<Integer, Question> questionBank) {
        super(game);
        this.score = 0;
        this.currentQuizQuestion = 1;
        this.questionBank = questionBank;
        this.stage = new Stage();
        this.choice = new Sprite(AssetHandler.assetManager.get(AssetHandler.choiceButton, Texture.class));
        this.choiceActivated = new Sprite(AssetHandler.assetManager.get(AssetHandler.choiceButtonActivated, Texture.class));
        this.font = generateFont(80, Color.WHITE);
        this.answerY = -1;
        this.answerCorrect = -1;
        this.timerRunning = false;

        // Randomize quiz options
        for (int i=1; i<=questionBank.size(); i++) {
            Collections.shuffle(questionBank.get(i).allChoices);
        }
    }

    public BitmapFont generateFont(int size, Color color){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/Gila-qZBPV.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameters = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameters.genMipMaps = true;
        parameters.color = color;
        parameters.size = (int)Math.ceil(size);
        parameters.magFilter = Texture.TextureFilter.MipMapLinearNearest;
        parameters.minFilter = Texture.TextureFilter.Nearest;
        generator.scaleForPixelHeight((int)Math.ceil(size));

        return generator.generateFont(parameters);
    }


    @Override
    public void render(float delta) {
        renderBackground(new Sprite(AssetHandler.assetManager.get(AssetHandler.levelMenuBackground, Texture.class)));

        if (currentQuizQuestion <= 5) {
            renderQuestionAndAnswers(currentQuizQuestion);
        }
        else{
            renderFinalScoreScreen();
            if (Gdx.input.justTouched()){
                game.setScreen(new LevelMenuScreen(game));
            }
        }
    }

    public void renderBackground(Sprite background){
        game.batch.begin();
        game.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();

        // Darken background
        Gdx.gl.glEnable(GL_BLEND);
        ShapeRenderer dimmer = new ShapeRenderer();
        dimmer.begin(ShapeRenderer.ShapeType.Filled);
        dimmer.setColor(0, 0, 0, 0.1f);
        dimmer.rect(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        dimmer.end();
        Gdx.gl.glDisable(GL_BLEND);
    }

    public void renderQuestionAndAnswers(int questionID){
        game.batch.begin();
        if(displayButton(choice, choiceActivated, x, firstY, BUTTON_WIDTH, BUTTON_HEIGHT)){
            // Check if this choice is the answer
            answerY = firstY;
            if(!timerRunning){checkAnswer(0, questionID);}
        }

        if(displayButton(choice, choiceActivated, x, firstY * 3, BUTTON_WIDTH, BUTTON_HEIGHT)){
            answerY = firstY * 3;
            if(!timerRunning){checkAnswer(1, questionID);}
        }

        if(displayButton(choice, choiceActivated, x, firstY * 5, BUTTON_WIDTH, BUTTON_HEIGHT)){
            answerY = firstY * 5;
            if(!timerRunning){checkAnswer(2, questionID);}
        }

        if(displayButton(choice, choiceActivated, x, firstY * 7, BUTTON_WIDTH, BUTTON_HEIGHT)){
            answerY = firstY * 7;
            if(!timerRunning){checkAnswer(3, questionID);}
        }

        // Draw question
        font.draw(game.batch, questionBank.get(questionID).question, x * 2/3, Gdx.graphics.getHeight() - firstY, Gdx.graphics.getWidth()-x, 200,true);

        // Draw answers over the buttons (randomized)
        font.draw(game.batch, questionBank.get(questionID).allChoices.get(0), x+50, (firstY * 1.5f) + BUTTON_HEIGHT/3);
        font.draw(game.batch, questionBank.get(questionID).allChoices.get(1), x+50, (firstY * 3.5f) + BUTTON_HEIGHT/3);
        font.draw(game.batch, questionBank.get(questionID).allChoices.get(2), x+50, (firstY * 5.5f) + BUTTON_HEIGHT/3);
        font.draw(game.batch, questionBank.get(questionID).allChoices.get(3), x+50, (firstY * 7.5f) + BUTTON_HEIGHT/3);

        if (answerY > -1 && answerCorrect == 1){
            game.batch.draw(new Sprite(AssetHandler.assetManager.get(AssetHandler.check, Texture.class)), (x*1.25f)+BUTTON_WIDTH, (answerY * 1.1f));
        }
        else if(answerY > -1 && answerCorrect == 0){
            game.batch.draw(new Sprite(AssetHandler.assetManager.get(AssetHandler.x_mark, Texture.class)), (x*1.25f)+BUTTON_WIDTH, (answerY * 1.1f));
        }

        game.batch.end();
    }

    public void checkAnswer(int choiceID, int questionID){
        if (questionBank.get(questionID).allChoices.get(choiceID).equals(questionBank.get(questionID).answer)){
            score++;
            answerCorrect = 1;
        }
        else{
            answerCorrect = 0;
        }
        timerRunning = true;
        Timer.schedule(renderAnswer, 1f);
    }

    public void renderFinalScoreScreen(){
        float scorePercent = ((float)score/5)*100;
        String finishLine = "Oh no! That's only " + (int)scorePercent + "%\nMake sure to try again!";
        if (score >= 4){finishLine = "Nice Job! That's " + (int)scorePercent + "%";}

        game.batch.begin();
        font.draw(game.batch, "You scored: " + score + "/5", x * 2/3, Gdx.graphics.getHeight() - firstY);
        font.draw(game.batch, finishLine, x * 2/3, Gdx.graphics.getHeight() - (firstY * 3));
        font.draw(game.batch, "Tap anywhere to continue...", x * 2/3, firstY);
        game.batch.end();

        // TODO: Save game state here for the level
    }

    private final Timer.Task renderAnswer = new Timer.Task() {
        @Override
        public void run() {
            currentQuizQuestion++;
            answerY = -1;
            answerCorrect = -1;
            timerRunning = false;
        }
    };
}
