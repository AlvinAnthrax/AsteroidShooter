package com.asteroid.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameOver implements Screen {

    final SpaceEvader game;

    private static final int replay_width = 240;
    private static final int replay_height = 52;
    private static final int quit_width = 100;
    private static final int quit_height = 35;
    private static final int replay_X = 100;
    private static final int replay_Y = 150;
    private static final int quit_X = 100;
    private static final int quit_Y = 100;

    OrthographicCamera camera;
    Texture GameOver;
    Texture ReplayActive;
    Texture ReplayInactive;
    Texture quitActive;
    Texture quitInactive;
    Texture background;
    int x;

    public GameOver(final SpaceEvader game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        GameOver = new Texture("GameOver.png");
        ReplayActive = new Texture("Replay.png");
        ReplayInactive = new Texture("Replay2.png");
        quitActive = new Texture("Quit.png");
        quitInactive = new Texture("Quit2.png");
        background = new Texture("space bg.jpg");



    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        game.batch.draw(background,0,0);
        game.batch.draw(GameOver, 180, 400, 445, 64);

//        Replay Button
        x = SpaceEvader.WIDTH / 2 - replay_width / 2;
        if (Gdx.input.getX() < x + replay_width && Gdx.input.getX() > x && SpaceEvader.HEIGHT - Gdx.input.getY() < replay_Y + replay_height && SpaceEvader.HEIGHT - Gdx.input.getY() > replay_Y) {
            game.batch.draw(ReplayActive, x, replay_Y , replay_width, replay_height);
            if(Gdx.input.isTouched()){
                this.dispose();
                game.setScreen(new GameScreen(game));
            }
        }else{
            game.batch.draw(ReplayInactive, x, replay_Y , replay_width, replay_height);
        }

//        Quit Button
        x = SpaceEvader.WIDTH / 2 - quit_width / 2;
        if (Gdx.input.getX() < x + quit_width && Gdx.input.getX() > x && SpaceEvader.HEIGHT - Gdx.input.getY() < quit_Y + quit_height && SpaceEvader.HEIGHT - Gdx.input.getY() > quit_Y) {
            game.batch.draw(quitActive, x, quit_Y , quit_width, quit_height);
            if(Gdx.input.isTouched()){
                Gdx.app.exit();
            }
        }else{
            game.batch.draw(quitInactive, x, quit_Y , quit_width, quit_height);
        }


//        game.font.draw(game.batch, "You Dead!!! ", 350, 300);
//        game.font.draw(game.batch, "Tap anywhere to Re-Try!", 350, 250);
        game.batch.end();

//        if (Gdx.input.isTouched()) {
//            game.setScreen(new MainMenuScreen(game));
//            dispose();
//        }

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
        background.dispose();
        ReplayActive.dispose();
        ReplayInactive.dispose();
        quitActive.dispose();
        quitInactive.dispose();
    }
}
