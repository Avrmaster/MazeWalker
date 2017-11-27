package ua.leskivproduction.kma.mazewalker.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ua.leskivproduction.kma.mazewalker.Main;

import java.awt.*;

//!!!Don't forget to set working directory to MazeWalker/core/assets!!!
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		config.useGL30 = true;
		config.useHDPI = true;
		config.samples = 6;

//		config.width = screenSize.width*3/4;
//		config.height = screenSize.height*3/4;

		config.width = screenSize.width;
		config.height = screenSize.height;
		config.fullscreen = true;

		new LwjglApplication(new Main(), config);
	}
}
