package ua.leskivproduction.kma.mazewalker;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ua.leskivproduction.kma.mazewalker.model.Maze;

public class Main extends ApplicationAdapter {
	SpriteBatch batch;
	Maze maze;
	MazeDrawer mazeDrawer;

	@Override
	public void create () {
		batch = new SpriteBatch();

		maze = new Maze(10, 10);
		mazeDrawer = new MazeDrawer(maze);

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		int screenWidth = Gdx.graphics.getWidth();
		int screenHeight = Gdx.graphics.getHeight();

		mazeDrawer.setX(screenWidth/6).setY(screenHeight/6).setWidth(screenWidth*2/3).setHeight(screenHeight*2/3);
		mazeDrawer.draw(batch);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
