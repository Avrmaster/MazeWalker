package ua.leskivproduction.kma.mazewalker;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import static java.lang.Math.max;

import java.util.List;

import static ua.leskivproduction.kma.mazewalker.utils.Lerper.*;
import ua.leskivproduction.kma.mazewalker.model.Maze;

public final class MazeDrawer {
    private Maze maze;
    private float x, y, width, height;
    private ShapeRenderer shapeRenderer;
    private Color[][] actualColors;

    public MazeDrawer(Maze maze) {
        this.maze = maze;
        shapeRenderer = new ShapeRenderer();
        actualColors = new Color[maze.width][maze.height];
        for (int i = 0; i < maze.width; i++) {
            for (int j = 0; j < maze.height; j++) {
                actualColors[i][j] = new Color(maze.getColor(i, j));
            }
        }
    }

    public void setMaze(Maze maze) {
        if (this.maze.width == maze.width &&
                this.maze.height == maze.height) {
            this.maze = maze;
        } else
            throw new IllegalArgumentException("New maze must have same size!");
    }

    public void draw(float deltaTime) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0.2f, 0.8f, 0.1f, 1));

        rect(x, y, width, height, 3);
        float cellWidth = getCellWidth();
        float cellHeight = getCellHeight();
        float cellThickness = max(1f, cellWidth/20);
        for (int i = 0; i < maze.width; i++) {
            for (int j = 0; j < maze.height; j++) {
                lerp(actualColors[i][j], maze.getColor(i, j), deltaTime);
                shapeRenderer.setColor(actualColors[i][j]);
                shapeRenderer.rect(x+i*cellWidth, y+j*cellHeight, cellWidth, cellHeight);

                shapeRenderer.setColor(0.4f, 0.4f, 0.3f, 1);
                try {
                    if (!maze.isOpened(i-1, j, i, j)) {
                        wall(i-1, j, i, j, cellThickness);
                    }
                } catch (IllegalArgumentException e) {}//it's ok
                try {
                    if (!maze.isOpened(i, j-1, i, j)) {
                        wall(i, j-1, i, j, cellThickness);
                    }
                } catch (IllegalArgumentException e) {}//it's ok
            }
        }
        shapeRenderer.end();


        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        List<Maze.Marker> markers = maze.markers();
        for (int i = markers.size()-1; i >= 0; i--) {
            Maze.Marker m = markers.get(i);
            m.update(deltaTime);
            if (m.radius < 0.05f && m.goalRadius < 0.05f) {
                markers.remove(i);
            } else {
                shapeRenderer.setColor(m.color);
                ellipse(x+(m.pos.x+0.5f)*cellWidth, y+(m.pos.y+0.5f)*cellHeight,
                        m.radius*cellWidth, m.radius*cellHeight);
            }
        }
        shapeRenderer.end();

    }

    private void ellipse(float centerX, float centerY, float width, float height) {
        shapeRenderer.ellipse(centerX-width/2, centerY-height/2, width, height);
    }

    private void wall(float x1, float y1, float x2, float y2, float wallThickness) {
        float cellWidth = getCellWidth();
        float cellHeight = getCellHeight();

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

    private float getCellWidth() {
        return width/maze.width;
    }

    private float getCellHeight() {
        return height/maze.height;
    }

    public MazeDrawer setX(float x) {
        this.x = x;
        return this;
    }

    public MazeDrawer setY(float y) {
        this.y = y;
        return this;
    }

    public MazeDrawer setWidth(float width) {
        this.width = width;
        return this;
    }

    public MazeDrawer setHeight(float height) {
        this.height = height;
        return this;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
