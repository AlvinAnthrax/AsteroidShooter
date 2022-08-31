package com.asteroid.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpaceEvader extends Game {
    public SpriteBatch batch;
    public BitmapFont font;

//    window size
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont(); // use libGDX's default Arial font
        this.setScreen(new MainMenuScreen(this));
    }
    public void render() {
        super.render(); // important!
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }

}
