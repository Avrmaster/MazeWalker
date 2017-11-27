package ua.leskivproduction.kma.mazewalker;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import ua.leskivproduction.kma.mazewalker.model.Maze;
import ua.leskivproduction.kma.mazewalker.solvers.BFSolver;
import ua.leskivproduction.kma.mazewalker.solvers.DFSolver;
import ua.leskivproduction.kma.mazewalker.utils.DummyInputProcessor;

public class Main extends ApplicationAdapter {
    private final int MAZE_WIDTH = 128;
    private final int MAZE_HEIGHT = 72;
    private final float SHUFFLE_TIME = 2f;
    private final boolean AUTO_EPIC = true;

    private SpriteBatch spriteBatch;
    private Maze maze;
    private MazeDrawer mazeDrawer;
    private MazeShuffler mazeShuffler;
    private BitmapFont mainFont;

    private Music epicMusic, backgroundMusic;
    private final float BACKGROUND_VOLUME = 3f;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        mainFont = genFont("American Captain.ttf", Gdx.graphics.getHeight() / 10, Color.WHITE);

        Texture karp = new Texture("karp.jpg");
        TextureData textureData = karp.getTextureData();
        if (!textureData.isPrepared()) {
            textureData.prepare();
        }
        originalEpicPixmap = textureData.consumePixmap();

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background.mp3"));
        backgroundMusic.setVolume(BACKGROUND_VOLUME);
        epicMusic = Gdx.audio.newMusic(Gdx.files.internal("AssassinsCreed.mp3"));

        Gdx.input.setInputProcessor(new DummyInputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                switch (keycode) {
                    case Input.Keys.N:
                        if (!isEpic)
                            createNewMaze();
                        break;
                    case Input.Keys.O:
                        if (mazeShuffler.done())
                            mazeShuffler.newObjective();
                        break;
                    case Input.Keys.B:
                        if (maze.hasObjective()) {
                            maze.solveWith(BFSolver::new);
                        }
                        break;
                    case Input.Keys.D:
                        if (maze.hasObjective()) {
                            maze.solveWith(DFSolver::new);
                        }
                        break;

                    case Input.Keys.K:
                        if (mazeShuffler.done())
                            toggleEpic();
                        break;
                }
                return true;
            }
        });
        createNewMaze();
    }

    private boolean isEpic = false;
    private Pixmap originalEpicPixmap; //not resized
    private Pixmap epicPixmap;

    public void toggleEpic() {
        if (isEpic) {
            mazeDrawer.normalZoom();
            maze.clearCellsColors();
            epicMusic.stop();
            backgroundMusic.setVolume(BACKGROUND_VOLUME);
            isEpic = false;
            return;
        }

        mazeDrawer.zoomOut();

        for (int i = 0; i < epicPixmap.getWidth(); i++)
            for (int j = 0; j < epicPixmap.getHeight(); j++)
                maze.setColor(i, j, new Color(epicPixmap.getPixel(i, maze.height - 1 - j)));

        backgroundMusic.setVolume(0);
        epicMusic.play();
        epicMusic.setPosition(3);
        isEpic = true;
    }

    private BitmapFont genFont(String file, double size, Color color) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(file));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) size;
        parameter.color = color;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }

    private void createNewMaze() {
        initMazeSystem(new Maze(MAZE_WIDTH, MAZE_HEIGHT, BFSolver::new));
    }

    private void initMazeSystem(Maze maze) {
        this.maze = maze;
        mazeShuffler = new MazeShuffler(maze, SHUFFLE_TIME, false);

        epicPixmap = new Pixmap(maze.width, maze.height, originalEpicPixmap.getFormat());
        epicPixmap.setBlending(Pixmap.Blending.None);
        epicPixmap.drawPixmap(originalEpicPixmap,
                0, 0, originalEpicPixmap.getWidth(), originalEpicPixmap.getHeight(),
                0, 0, maze.width, maze.height);

        try {
            mazeDrawer.setMaze(maze);
        } catch (IllegalArgumentException | NullPointerException e) {
            mazeDrawer = new MazeDrawer(maze);
            int screenWidth = Gdx.graphics.getWidth();
            int screenHeight = Gdx.graphics.getHeight();

            float yOffset = mainFont.getCapHeight() + 3;
            float xOffset = yOffset * screenWidth / screenHeight;

            mazeDrawer.setX(xOffset).setY(yOffset).setWidth(screenWidth - 2 * xOffset).setHeight(screenHeight - 2 * yOffset);
        }
    }

    boolean epic_toggled = false;

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        if (!backgroundMusic.isPlaying())
            backgroundMusic.play();

        float deltaTime = Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            deltaTime /= 60;
        }

        if (!mazeShuffler.done()) {
            mazeShuffler.update(deltaTime);
        } else {
            if (maze.hasObjective()) {
                if (!maze.solver.done() || !(maze.solver.isSolutionDrawn())) {
                    maze.solver.update(deltaTime);
                }
                if (maze.solver.done()) {
                    if (!epic_toggled) {
                        if (AUTO_EPIC)
                            toggleEpic();
                        epic_toggled = true;
                    }
                } else epic_toggled = false;
            }
        }

        mazeDrawer.draw(Gdx.graphics.getDeltaTime());

        spriteBatch.begin();
        if (maze.hasObjective() && maze.solver.done())

        {

            if (maze.solver.solvable()) {
                mainFont.draw(spriteBatch, "Path length ( " + maze.solver.getClass().getSimpleName() + "): "
                                + maze.solver.getSolution().size(),
                        0, mainFont.getCapHeight());
            } else {
                mainFont.draw(spriteBatch, "Not solvable!", 0, mainFont.getCapHeight());
            }

        }
        spriteBatch.end();
    }

}
