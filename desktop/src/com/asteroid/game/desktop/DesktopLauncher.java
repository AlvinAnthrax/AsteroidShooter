package com.asteroid.game.desktop;

import com.asteroid.game.BackUpData;
import com.asteroid.game.MainData;
import com.asteroid.game.SpaceEvader;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Space Evaders";
		config.width =800;
		config.height= 600;
		config.foregroundFPS=60;
		new LwjglApplication(new SpaceEvader(), config);
	}
}
