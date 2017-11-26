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
		config.width = screenSize.width*2/3;
		config.height = screenSize.height*2/3;

		new LwjglApplication(new Main(), config);
	}
}
