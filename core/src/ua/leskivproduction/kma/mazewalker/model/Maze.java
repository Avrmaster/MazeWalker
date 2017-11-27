package ua.leskivproduction.kma.mazewalker.model;

import com.badlogic.gdx.graphics.Color;

import java.awt.*;

import static java.lang.Math.abs;

public final class Maze {
    private final static Point START_POINT = new Point(0, 0);

    private final Graph graph;
    public final int width, height;
    private Color[][] colors;



    public class Objective {
        public final Point startPoint, endPoint;
        public Objective(Point startPoint, Point endPoint) {
            checkBoundaries(startPoint.x, startPoint.y);
            checkBoundaries(endPoint.x, endPoint.y);
            this.startPoint = startPoint;
            this.endPoint = endPoint;
        }

        public Objective(Objective another) {
            this.startPoint = new Point(another.startPoint);
            this.endPoint = new Point(another.endPoint);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null)
                return false;
            if (obj.getClass() != this.getClass())
                return false;
            Objective another = (Objective)obj;
            return this.startPoint.equals(another.startPoint) &&
                    this.endPoint.equals(another.endPoint);
        }

        @Override
        public int hashCode() {
            return startPoint.hashCode()*11+endPoint.hashCode();
        }
    }
    private Objective objective;

    public Maze(int width, int height) {
        graph = new UndirectedGraph(width*height);
        this.width = width;
        this.height = height;
        this.colors = new Color[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                colors[i][j] = new Color();
            }
        }
    }

    public void setObjective(Point endPoint) {
        setObjective(START_POINT, endPoint);
    }

    public void setObjective(Point startPoint, Point endPoint) {
        objective = new Objective(startPoint, endPoint);
    }

    public boolean hasObjective() {
        return objective != null;
    }

    public boolean sameObjective(Objective another) {
        return this.objective.equals(another);
    }

    public Objective getObjective() {
        return new Objective(this.objective);
    }

    public boolean isOpened(int x1, int y1, int x2, int y2) {
        checkBoundaries(x1, y1);
        checkBoundaries(x2, y2);
        checkAdjustments(x1, y1, x2, y2);
        return graph.hasEdge(getCellV(x1, y1), getCellV(x2, y2));
    }

    public void open(int x1, int y1, int x2, int y2) {
        checkBoundaries(x1, y1);
        checkBoundaries(x2, y2);
        checkAdjustments(x1, y1, x2, y2);
        graph.addEdge(getCellV(x1, y1), getCellV(x2, y2));
    }

    public void setColor(int x, int y, Color color) {
        colors[x][y] = color;
    }

    public void setColor(int x, int y, float r, float g, float b, float a) {
        colors[x][y].a = a;
        colors[x][y].r = r;
        colors[x][y].g = g;
        colors[x][y].b = b;
    }

    public Color getColor(int x, int y) {
        return colors[x][y];
    }

    private int getCellV(int x, int y) {
        return y*width + x;
    }

    private void checkBoundaries(int x, int y) {
        if (x < 0 || x >= width ||
                y < 0 || y >= height)
            throw new IllegalArgumentException("Given cell is out of bounds!");
    }

    private void checkAdjustments(int x1, int y1, int x2, int y2) {
        if ((abs(x1-x2) == 1 && y1 == y2) || (abs(y1-y2) == 1 && x1 == x2)) {
            return;
        }
        throw new IllegalArgumentException("Cells must be adjustment!");
    }

}
