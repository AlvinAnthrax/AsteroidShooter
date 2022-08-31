package com.asteroid.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.Iterator;

public class GameScreen implements Screen {

    final SpaceEvader game;

    OrthographicCamera camera;
    //    Texture
    Texture spaceShipImage;
    Texture spaceShipImage2;
    Texture ammo;
    Texture asteroid1;
    Texture asteroid2;
    Texture background;
    Texture ammoImage;
    //    Audio
    Music spaceMusic;
    Sound laserShoot;
    Sound hitBySmallAsteroid;
    Sound hitAsteroid;
    ExtendViewport viewport;
    //    Rectangle
    Rectangle spaceShip;
    Rectangle spaceShip2;
    ShapeRenderer SR;
    Array<Rectangle> ammos;
    Array<Rectangle> asteroids;
    Array<Rectangle> asteroids2;
    //
    long lastDropTimeAsteroid1;
    long lastDropTimeAsteroid2;
    private long lastFire;
    int Score;
    int count;
    float Life = 5f;
    boolean isPaused = false;
    float time = 0; // timer to store current elapsed time
    int speed = 0;

    public GameScreen(final SpaceEvader game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        viewport = new ExtendViewport(800, 600, camera);

//        Texture
        ammoImage = new Texture(Gdx.files.internal("Ammo.png"));
        spaceShipImage = new Texture(Gdx.files.internal("Space Ship.png"));
        asteroid1 = new Texture(Gdx.files.internal("Asteroid1.png"));
        asteroid2 = new Texture(Gdx.files.internal("Asteroid2.png"));
        spaceMusic = Gdx.audio.newMusic(Gdx.files.internal("Gaze.mp3"));
        laserShoot = Gdx.audio.newSound(Gdx.files.internal("Laser Shoot.mp3"));
        hitAsteroid = Gdx.audio.newSound(Gdx.files.internal("Bullet Hit Meteor.mp3"));
        hitBySmallAsteroid = Gdx.audio.newSound(Gdx.files.internal("Damaged.wav"));
        background = new Texture(Gdx.files.internal("space bg.jpg"));

//        Spaceship Rectangle
        spaceShip = new Rectangle();
        spaceShip.x = 800 / 2 - 64 / 2;
        spaceShip.y = 20;
        spaceShip.height = 64;
        spaceShip.width = 64;

//        Music
        spaceMusic.setLooping(true);
        spaceMusic.play();
//        Rectangle
        asteroids = new Array<Rectangle>();
        asteroids2 = new Array<Rectangle>();
        ammos = new Array<Rectangle>();
        SR = new ShapeRenderer();
//        Spaceship 2
        spaceShipImage2 = new Texture(Gdx.files.internal("Space Ship.png"));
        spaceShip2 = new Rectangle();
        spaceShip2.x = 800 / 4 - 48 / 2;
        spaceShip2.y = 575;
        spaceShip2.height = 48;
        spaceShip2.width = 48;
        count = 0;

        spawnAsteroid();
        spawnAsteroid2();

    }

    private void spawnAsteroid() {
        Rectangle asteroid = new Rectangle();
        asteroid.x = MathUtils.random(0, 800 - 100);
        asteroid.y = 600;
        asteroid.width = 40;
        asteroid.height = 40;
        asteroids.add(asteroid);
        lastDropTimeAsteroid1 = TimeUtils.nanoTime();
    }

    private void spawnAsteroid2() {
        Rectangle asteroid = new Rectangle();
        asteroid.x = MathUtils.random(0, 800 - 100);
        asteroid.y = 600;
        asteroid.width = 110;
        asteroid.height = 90;
        asteroids2.add(asteroid);
        lastDropTimeAsteroid2 = TimeUtils.nanoTime();
    }

    private void shotBullet() {
        Rectangle ammo = new Rectangle();
        ammo.x = spaceShip.x + (spaceShip.getWidth() / 2) - 9;
        ammo.y = spaceShip.y + (spaceShip.getHeight() / 2) + 10;
        ammo.width = 20;
        ammo.height = 20;
        ammos.add(ammo);
        lastFire = TimeUtils.nanoTime();
        laserShoot.play(10);

    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        game.batch.setProjectionMatrix(camera.combined);
    }


    @Override
    public void show() {
        spaceMusic.play();
    }

    @Override
    public void render(float delta) {
//        Life Setting
        if (Life <= 0) {
            game.setScreen(new GameOver(game));
        }
        if (Life > 0) {
            ScreenUtils.clear(0, 0, 0, 1);
//            Camera
            camera.update();
            game.batch.setProjectionMatrix(camera.combined);

            SR.begin(ShapeRenderer.ShapeType.Filled);
            SR.setColor(Color.BLUE);

            game.batch.begin();

            game.batch.draw(background, 0, 0);

//            Rectangle
            for (Rectangle ammo : ammos) {
                //SR.rect(asteroid.x,asteroid.y,asteroid.width,asteroid.height);
                game.batch.draw(ammoImage, ammo.x, ammo.y, ammo.width, ammo.height);

            }
            for (Rectangle asteroid : asteroids) {
                //SR.rect(asteroid.x,asteroid.y,asteroid.width,asteroid.height);
                game.batch.draw(asteroid1, asteroid.x, asteroid.y, asteroid.width, asteroid.height);

            }

            for (Rectangle asteroid : asteroids2) {
                //SR.rect(asteroid.x,asteroid.y,asteroid.width,asteroid.height);
                game.batch.draw(asteroid2, asteroid.x, asteroid.y, asteroid.width, asteroid.height);

            }


//            Render assets
            game.batch.draw(spaceShipImage, spaceShip.x, spaceShip.y, 64, 64);
            game.batch.draw(spaceShipImage2, 25, 500, 48, 48);
            game.font.draw(game.batch, " x " + Life, 80, 535);
            game.font.draw(game.batch, "Score :  " + Score, 25, 575);
            //SR.rect(spaceShip.x, spaceShip.y,64,64);
            SR.end();
            game.batch.end();

//            Controls
            speed = 200;

            if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))
                spaceShip.x -= speed * Gdx.graphics.getDeltaTime();
            if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                spaceShip.x += speed * Gdx.graphics.getDeltaTime();
            if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP))
                spaceShip.y += speed * Gdx.graphics.getDeltaTime();
            if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN))
                spaceShip.y -= speed * Gdx.graphics.getDeltaTime();
            if (spaceShip.x < 0) spaceShip.x = 5;
            if (spaceShip.x >= 736) spaceShip.x = 736 - 5;
            if (spaceShip.y < 0) spaceShip.y = 5;
            if (spaceShip.y >= 543) spaceShip.y = 543 - 5;


//              Spawn Asteroid 1
            if (TimeUtils.nanoTime() - lastDropTimeAsteroid1 > 1000000000 / 2) spawnAsteroid();
            for (Iterator<Rectangle> iter = asteroids.iterator(); iter.hasNext(); ) {
                Rectangle asteroid = iter.next();
                asteroid.y -= 100 * Gdx.graphics.getDeltaTime();
                if (asteroid.y + 100 < 0) iter.remove();
                if (asteroid.overlaps(spaceShip)) {
                    Life -= 0.5f;
                    hitBySmallAsteroid.play();
                    iter.remove();
                }
            }

            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                if (TimeUtils.nanoTime() - lastFire > 350000000) shotBullet();

            }

//            Spawn Asteroid 2
            if (Score >= 100) {
                if (TimeUtils.nanoTime() - lastDropTimeAsteroid2 > 2000000000) spawnAsteroid2();

                for (Iterator<Rectangle> iter = asteroids2.iterator(); iter.hasNext(); ) {
                    Rectangle asteroid = iter.next();
                    asteroid.y -= 100 * Gdx.graphics.getDeltaTime();
                    if (asteroid.y + 100 < 0) iter.remove();
                    if (asteroid.overlaps(spaceShip)) {
                        Life -= 1;
                        hitBySmallAsteroid.play();
                        iter.remove();
                    }
                }
            }
//            Bullet Ammo
            for (Iterator<Rectangle> itterAmmo = ammos.iterator(); itterAmmo.hasNext(); ) {
                try {
                    Rectangle ammo = itterAmmo.next();
                    ammo.y += 200 * Gdx.graphics.getDeltaTime();
                    for (Iterator<Rectangle> iterAsteroids = asteroids.iterator(); iterAsteroids.hasNext(); ) {
                        Rectangle asteroid = iterAsteroids.next();

                        if (asteroid.y + 100 < 0) iterAsteroids.remove();
                        if (ammo.overlaps(asteroid)) {
                            Score += 10;
                            iterAsteroids.remove();
                            itterAmmo.remove();

                        } else if (asteroid.overlaps(spaceShip)) {
                            hitBySmallAsteroid.play();
                            iterAsteroids.remove();
                        }


                    }
                    for (Iterator<Rectangle> iter = asteroids2.iterator(); iter.hasNext(); ) {
                        Rectangle asteroid = iter.next();
                        if (asteroid.y + 100 < 0) iter.remove();
                        else if (ammo.overlaps(asteroid)) {
                            itterAmmo.remove();
                            hitAsteroid.play();
                        }
                        if (asteroid.overlaps(spaceShip)) {
                            hitBySmallAsteroid.play();
                            iter.remove();
                        }

                    }


                    if (ammo.y > 600) itterAmmo.remove();
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.getMessage();

                }
            }
        }
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
        SR.dispose();
        game.batch.dispose();
        spaceShipImage.dispose();
        spaceShipImage2.dispose();
        ammo.dispose();
        asteroid1.dispose();
        asteroid2.dispose();
        laserShoot.dispose();
        spaceMusic.dispose();
        background.dispose();
        hitBySmallAsteroid.dispose();
        hitAsteroid.dispose();
        SR.dispose();
    }
}
