package com.asteroid.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;


import java.util.Iterator;
import java.util.Random;

public class BackUpData extends ApplicationAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Texture spaceShipImage;
    private Texture ammoImage;
    private Texture asteroid1;
    private Texture asteroid2;
    private Texture background;
    private Music spaceMusic;
    private Sound laserShoot;
    private Sound hitBySmallAsteroid;
    private ExtendViewport viewport;
    private Rectangle spaceShip;
    private ShapeRenderer SR;
    private Array<Rectangle> ammos;
    private Array<Rectangle> asteroids;
    private Array<Rectangle> asteroids2;
    private long lastDropTimeAsteroid1;
    private long lastDropTimeAsteroid2;
    private long lastFire;

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        ammoImage = new Texture(Gdx.files.internal("Ammo.png"));
        spaceShipImage = new Texture(Gdx.files.internal("Space Ship.png"));
        asteroid1 = new Texture(Gdx.files.internal("Asteroid1.png"));
        asteroid2 = new Texture(Gdx.files.internal("Asteroid2.png"));
        spaceMusic = Gdx.audio.newMusic(Gdx.files.internal("Gaze.mp3"));
        laserShoot = Gdx.audio.newSound(Gdx.files.internal("Laser Shoot.mp3"));
        hitBySmallAsteroid = Gdx.audio.newSound(Gdx.files.internal("Damaged.wav"));
        viewport = new ExtendViewport(800, 600, camera);
        background = new Texture(Gdx.files.internal("space bg.jpg"));
        spaceShip = new Rectangle();
        spaceShip.x = 800 / 2 - 64 / 2;
        spaceShip.y = 20;
        spaceShip.height = 64;
        spaceShip.width = 64;
        spaceMusic.setLooping(true);
        spaceMusic.play();
        asteroids = new Array<Rectangle>();
        asteroids2 = new Array<Rectangle>();
        ammos = new Array<Rectangle>();
        SR = new ShapeRenderer();
        spawnAsteroid();
        spawnAsteroid2();
        shotBullet();

    }

    private void spawnAsteroid() {
        Rectangle asteroid = new Rectangle();
        asteroid.x = MathUtils.random(0, 800 - 64);
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
        ammo.x = spaceShip.x + (spaceShip.getWidth() / 2);
        ammo.y = spaceShip.y;
        ammo.width = 8;
        ammo.height = 16;
        ammos.add(ammo);
        lastFire = TimeUtils.nanoTime();

    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);

        batch.setProjectionMatrix(camera.combined);
    }


    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        SR.begin(ShapeRenderer.ShapeType.Filled);
        SR.setColor(Color.BLUE);

        batch.begin();
        batch.draw(background, 0, 0);
        for (Rectangle asteroid : asteroids) {
            //SR.rect(asteroid.x,asteroid.y,asteroid.width,asteroid.height);
            batch.draw(asteroid1, asteroid.x, asteroid.y, asteroid.width, asteroid.height);

        }

        for (Rectangle asteroid : asteroids2) {
            //SR.rect(asteroid.x,asteroid.y,asteroid.width,asteroid.height);
            batch.draw(asteroid2, asteroid.x, asteroid.y, asteroid.width, asteroid.height);

        }

        for (Rectangle ammo : ammos) {
            //SR.rect(asteroid.x,asteroid.y,asteroid.width,asteroid.height);
            batch.draw(ammoImage, ammo.x, ammo.y, ammo.width, ammo.height);

        }


        batch.draw(spaceShipImage, spaceShip.x, spaceShip.y, 64, 64);
        //SR.rect(spaceShip.x, spaceShip.y,64,64);
        SR.end();
        batch.end();

        if (spaceShip.x < 0) spaceShip.x = 0;
        if (spaceShip.x >= 800) spaceShip.x = 800 - 64;
        if (spaceShip.y < 0) spaceShip.y = 0;
        if (spaceShip.y >= 600) spaceShip.x = 600 - 64;


        if (Gdx.input.isKeyPressed(Input.Keys.A)) spaceShip.x -= 200 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.D)) spaceShip.x += 200 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.W)) spaceShip.y += 200 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.S)) spaceShip.y -= 200 * Gdx.graphics.getDeltaTime();


        if (TimeUtils.nanoTime() - lastDropTimeAsteroid1 > 1000000000 / 4) spawnAsteroid();
        for (Iterator<Rectangle> iterAsteroids = asteroids.iterator(); iterAsteroids.hasNext(); ) {
            Rectangle asteroid = iterAsteroids.next();


            asteroid.y -= 100 * Gdx.graphics.getDeltaTime();

            if (asteroid.y + 100 < 0) iterAsteroids.remove();
            if (asteroid.overlaps(spaceShip)) {
                hitBySmallAsteroid.play();
                iterAsteroids.remove();
            }
        }
        if (TimeUtils.nanoTime() - lastDropTimeAsteroid2 > 2000000000) spawnAsteroid2();
        for (Iterator<Rectangle> iterAsteroids2 = asteroids2.iterator(); iterAsteroids2.hasNext(); ) {
            Rectangle asteroid = iterAsteroids2.next();
            asteroid.y -= 100 * Gdx.graphics.getDeltaTime();
            if (asteroid.y + 100 < 0) iterAsteroids2.remove();
            if (asteroid.overlaps(spaceShip)) {
                hitBySmallAsteroid.play();
                iterAsteroids2.remove();
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (TimeUtils.nanoTime() - lastFire > 750000000) shotBullet();
        }
//        1000000000

        ;

        for (Iterator<Rectangle> itterAmmo = ammos.iterator(); itterAmmo.hasNext(); ) {
            Rectangle ammo = itterAmmo.next();

            ammo.y += 200 * Gdx.graphics.getDeltaTime();
            try {
                for (Iterator<Rectangle> iterAsteroids = asteroids.iterator(); iterAsteroids.hasNext(); ) {
                    Rectangle asteroid = iterAsteroids.next();

                    if (asteroid.y + 100 < 0) iterAsteroids.remove();
                    if (asteroid.overlaps(spaceShip)) {
                        hitBySmallAsteroid.play();
                        iterAsteroids.remove();
                    }
                    if (ammo.overlaps(asteroid)) {
                        iterAsteroids.remove();
                        itterAmmo.remove();

                    }
                }


                if (ammo.y > 600) itterAmmo.remove();
            } catch (ArrayIndexOutOfBoundsException e) {
                e.getMessage();

            }

            for (Iterator<Rectangle> iterAsteroids2 = asteroids2.iterator(); iterAsteroids2.hasNext(); ) {
                Rectangle asteroid = iterAsteroids2.next();

                if (asteroid.y + 100 < 0) iterAsteroids2.remove();
                if (asteroid.overlaps(spaceShip)) {
                    hitBySmallAsteroid.play();
                    iterAsteroids2.remove();
                }
                int counterAsteroid2 = 0;
                if (counterAsteroid2 < 3) {
                    if (ammo.overlaps(asteroid)) {
                        counterAsteroid2++;
                        if (counterAsteroid2 == 3) {
                            counterAsteroid2 = 0;
                            iterAsteroids2.remove();
                        }
                    }
                }
            }
            System.out.println(ammos.size);

        }
    }

    @Override
    public void dispose() {

        batch.dispose();
        spaceShipImage.dispose();
        ammoImage.dispose();
        asteroid1.dispose();
        asteroid2.dispose();
        laserShoot.dispose();
        spaceMusic.dispose();
        background.dispose();
        hitBySmallAsteroid.dispose();
        SR.dispose();

    }
}
