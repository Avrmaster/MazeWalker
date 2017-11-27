package ua.leskivproduction.kma.mazewalker;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import ua.leskivproduction.kma.mazewalker.model.Maze;
import ua.leskivproduction.kma.mazewalker.utils.DummyInputProcessor;

public class Main extends ApplicationAdapter {
	private final int MAZE_WIDTH = 64;
	private final int MAZE_HEIGHT = 36;
	private final float SHUFFLE_TIME = 2f;

	private SpriteBatch spriteBatch;
	private Maze maze;
	private MazeDrawer mazeDrawer;
	private MazeShuffler mazeShuffler;
	private BitmapFont mainFont;

	Texture img;

	@Override
	public void create () {
		createNewMaze();

		spriteBatch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		mainFont = genFont("American Captain.ttf", Gdx.graphics.getHeight()/10, Color.WHITE);


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

	private BitmapFont genFont(String file, double size, Color color) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(file));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = (int)size;
		parameter.color = color;
		BitmapFont font = generator.generateFont(parameter);
		generator.dispose();
		return font;
	}

	private void createNewMaze() {
		initMazeSystem(new Maze(MAZE_WIDTH, MAZE_HEIGHT));
	}

	private void initMazeSystem(Maze maze) {
		this.maze = maze;
		mazeShuffler = new MazeShuffler(maze, SHUFFLE_TIME, false);

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

//		Gdx.gl.glEnable(GL20.GL_BLEND);
//		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		float deltaTime = Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			deltaTime /= 20;
		}

		if (!mazeShuffler.done()) {
			mazeShuffler.update(deltaTime);
		} else {
			if (maze.hasObjective() &&
					!maze.solver.done()) {
				maze.solver.update(deltaTime);
			}
		}

		mazeDrawer.draw(Gdx.graphics.getDeltaTime());

		spriteBatch.begin();
		if (maze.hasObjective() && maze.solver.done()) {
			
			if (maze.solver.solvable()) {
				mainFont.draw(spriteBatch, "Path length: "+maze.solver.getSolution().size(),
						0, mainFont.getCapHeight());
			} else {
				mainFont.draw(spriteBatch, "Not solvable!", 0, mainFont.getCapHeight());
			}

		}
		spriteBatch.end();
	}

}
