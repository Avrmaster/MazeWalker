package ua.leskivproduction.kma.mazewalker;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import ua.leskivproduction.kma.mazewalker.model.Maze;


public class MazeDrawer {
    protected final Maze maze;
    protected int x, y, width, height;
    protected ShapeRenderer shapeRenderer;



    public MazeDrawer(Maze maze) {
        this.maze = maze;
        shapeRenderer = new ShapeRenderer();
    }

    public void draw(SpriteBatch batch) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0.2f, 0.8f, 0.1f, 1));

        rect(x, y, width, height, 3);


        shapeRenderer.end();
    }

    private void rect(int x, int y, int width, int height, int lineWidth) {
        shapeRenderer.rectLine(x, y, x, y+height, lineWidth);
        shapeRenderer.rectLine(x+width, y, x+width, y+height, lineWidth);
        shapeRenderer.rectLine(x, y, x+width, y, lineWidth);
        shapeRenderer.rectLine(x, y+height, x+width, y+height, lineWidth);
    }

    public MazeDrawer setX(int x) {
        this.x = x;
        return this;
    }

    public MazeDrawer setY(int y) {
        this.y = y;
        return this;
    }

    public MazeDrawer setWidth(int width) {
        this.width = width;
        return this;
    }

    public MazeDrawer setHeight(int height) {
        this.height = height;
        return this;
    }
}
