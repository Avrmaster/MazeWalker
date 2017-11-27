package ua.leskivproduction.kma.mazewalker;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ua.leskivproduction.kma.mazewalker.model.Maze;
import ua.leskivproduction.kma.mazewalker.utils.DummyInputProcessor;

public class Main extends ApplicationAdapter {
	private final int MAZE_WIDTH = 64;
	private final int MAZE_HEIGHT = 36;
	private final float SHUFFLE_TIME = 1;

	private MazeSolver mazeSolver;
	private MazeDrawer mazeDrawer;
	private MazeShuffler mazeShuffler;

	@Override
	public void create () {
		createNewMaze();

		Gdx.input.setInputProcessor(new DummyInputProcessor() {
			@Override
			public boolean keyDown(int keycode) {
				switch (keycode) {
					case Input.Keys.N:
						createNewMaze();
						break;
					case Input.Keys.O:
						if (mazeShuffler.done())
							mazeShuffler.resetObjective();
						break;
				}
				return true;
			}
		});

	}

	private void createNewMaze() {
		initMazeSystem(new Maze(MAZE_WIDTH, MAZE_HEIGHT));
	}

	private void initMazeSystem(Maze maze) {
		mazeShuffler = new MazeShuffler(maze, SHUFFLE_TIME);
		mazeSolver = new MazeSolver(maze);
		try {
			mazeDrawer.setMaze(maze);
		} catch (IllegalArgumentException | NullPointerException e) {
			mazeDrawer = new MazeDrawer(maze);
			int screenWidth = Gdx.graphics.getWidth();
			int screenHeight = Gdx.graphics.getHeight();
			mazeDrawer.setX(0).setY(0).setWidth(screenWidth).setHeight(screenHeight);
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		if (!mazeShuffler.done()) {
			mazeShuffler.update(Gdx.graphics.getDeltaTime());
		} else {
			if (mazeSolver.done()) {

			} else {

			}
		}
		mazeDrawer.draw(Gdx.graphics.getDeltaTime());

	}

}
