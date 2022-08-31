package com.asteroid.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

import java.awt.*;

public class MainMenuScreen implements Screen {

    final SpaceEvader game;

    private static final int play_width = 120;
    private static final int play_height = 35;
    private static final int quit_width = 100;
    private static final int quit_height = 35;
    private static final int play_X = 100;
    private static final int play_Y = 150;
    private static final int quit_X = 100;
    private static final int quit_Y = 100;

    OrthographicCamera camera;
    Texture playActive;
    Texture playInactive;
    Texture quitActive;
    Texture quitInactive;
    Texture background;
    Texture Title;
    Rectangle Play;
    ShapeRenderer SR;
    int x;

    public MainMenuScreen(final SpaceEvader game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);

//        Texture
        playActive = new Texture("Play.png");
        playInactive = new Texture("Play2.png");
        quitActive = new Texture("Quit.png");
        quitInactive = new Texture("Quit2.png");
        background = new Texture("space bg.jpg");
        Title = new Texture("Title.png");
        Play = new Rectangle();
        SR = new ShapeRenderer();
        Play.x =800/2 - 120/2;
        Play.y= 600/3 ;

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(background,0,0);
        game.batch.draw(Title, 80,450);

//      Play button
        x = SpaceEvader.WIDTH / 2 - play_width / 2;
        if (Gdx.input.getX() < x + play_width && Gdx.input.getX() > x && SpaceEvader.HEIGHT - Gdx.input.getY() < play_Y + play_height && SpaceEvader.HEIGHT - Gdx.input.getY() > play_Y) {
            game.batch.draw(playActive, x, play_Y , play_width, play_height);
            if(Gdx.input.isTouched()){
                this.dispose();
                game.setScreen(new GameScreen(game));
            }
        }else{
            game.batch.draw(playInactive, x, play_Y , play_width, play_height);
        }

//      Quit Button
        x = SpaceEvader.WIDTH / 2 - quit_width / 2;
        if (Gdx.input.getX() < x + quit_width && Gdx.input.getX() > x && SpaceEvader.HEIGHT - Gdx.input.getY() < quit_Y + quit_height && SpaceEvader.HEIGHT - Gdx.input.getY() > quit_Y) {
            game.batch.draw(quitActive, x, quit_Y , quit_width, quit_height);
            if(Gdx.input.isTouched()){
                Gdx.app.exit();
            }
        }else{
            game.batch.draw(quitInactive, x, quit_Y , quit_width, quit_height);
        }
        game.batch.end();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        playActive.dispose();
        playInactive.dispose();
        quitActive.dispose();
        quitInactive.dispose();
        Title.dispose();

    }
}
