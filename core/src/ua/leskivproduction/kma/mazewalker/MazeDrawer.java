package ua.leskivproduction.kma.mazewalker;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static ua.leskivproduction.kma.mazewalker.utils.Lerper.*;
import ua.leskivproduction.kma.mazewalker.model.Maze;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public final class MazeDrawer {
    private Maze maze;
    private int x, y, width, height;
    private ShapeRenderer shapeRenderer;
    private Color[][] actualColors;

    List<Marker> markers = new LinkedList<>();
    private class Marker {
        float x, y;
        float radius;
        float goalRadius;
        Color color;
        Marker(float x, float y) {
            this.x = x;
            this.y = y;
        }
        void update(float deltaTime) {
            radius = lerp(radius, goalRadius, deltaTime);
        }
    }

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

    Maze.Objective drawnObjective;
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

        if (maze.hasObjective()) {
            if (drawnObjective == null || !maze.sameObjective(drawnObjective)) {
                drawnObjective = maze.getObjective();
                clearMarkers();
                addMarker(drawnObjective.startPoint.x, drawnObjective.startPoint.y, Color.BROWN);
                addMarker(drawnObjective.endPoint.x, drawnObjective.endPoint.y, Color.GOLD);
            }
        } else {
            clearMarkers();
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = markers.size()-1; i >= 0; i--) {
            Marker m = markers.get(i);
            m.update(deltaTime);
            if (m.radius < 0.1f) {
                markers.remove(i);
            } else {
                shapeRenderer.setColor(m.color);
                shapeRenderer.circle(m.x, m.y, m.radius);
            }
        }
        shapeRenderer.end();

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
        return (float)width/maze.width;
    }

    private float getCellHeight() {
        return (float)height/maze.height;
    }

    public void addMarker(int cellX, int cellY, Color color) {
        addMarker(cellX, cellY, color, max(getCellWidth(), getCellHeight())/2);
    }

    public void addMarker(int cellX, int cellY, Color color, float radius) {
        Marker newMarker = new Marker((cellX+0.5f)*getCellWidth(), (cellY+0.5f)*getCellHeight());
        newMarker.goalRadius = radius;
        newMarker.color = color;
        markers.add(newMarker);
    }

    public void clearMarkers() {
        for(Marker m : markers)
            m.goalRadius = 0;
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
