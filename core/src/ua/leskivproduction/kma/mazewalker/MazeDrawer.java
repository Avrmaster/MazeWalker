package ua.leskivproduction.kma.mazewalker;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import static java.lang.Math.max;
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
        float cellWidth = (float)width/maze.width;
        float cellHeight = (float)height/maze.height;
        float cellThickness = 1.4f;
        for (int i = 0; i < maze.width; i++) {
            for (int j = 0; j < maze.height; j++) {
                shapeRenderer.setColor(maze.getColor(i, j));
                shapeRenderer.rect(x+i*cellWidth, y+j*cellHeight, cellWidth, cellHeight);

                shapeRenderer.setColor(0.4f, 0.4f, 0.3f, 1);
                try {
                    if (!maze.isOpened(i-1, j, i, j)) {
                        wall(i-1, j, i, j, cellWidth, cellHeight, cellThickness);
                    }
                } catch (IllegalArgumentException e) {}//it's ok
                try {
                    if (!maze.isOpened(i, j-1, i, j)) {
                        wall(i, j-1, i, j, cellWidth, cellHeight, cellThickness);
                    }
                } catch (IllegalArgumentException e) {}//it's ok
            }
        }

        shapeRenderer.end();
    }

    private void wall(float x1, float y1, float x2, float y2, float cellWidth, float cellHeight, float wallThickness) {
        if (x1 == x2)
            shapeRenderer.rect(x+x1*cellWidth, y+max(y1, y2)*cellHeight, cellWidth, wallThickness);
        else if (y1 == y2)
            shapeRenderer.rect(x+max(x1, x2)*cellWidth, y+y1*cellHeight, wallThickness, cellHeight);
    }

    private void rect(float x, float y, float width, float height, float lineWidth) {
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
