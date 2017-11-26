package ua.leskivproduction.kma.mazewalker.model;

import static java.lang.Math.abs;

public final class Maze {
    private final Graph graph;
    private final int width, height;

    public Maze(int width, int height) {
        graph = new UndirectedGraph(width*height);
        this.width = width;
        this.height = height;
    }

    public boolean isOpened(int x1, int y1, int x2, int y2) {
        checkBoundaries(x1, y1, x2, y2);
        checkAdjustments(x1, y1, x2, y2);
        return graph.hasEdge(getCellV(x1, y1), getCellV(x2, y2));
    }

    public void open(int x1, int y1, int x2, int y2) {
        checkBoundaries(x1, y1, x2, y2);
        checkAdjustments(x1, y1, x2, y2);
        graph.addEdge(getCellV(x1, y1), getCellV(x2, y2));
    }


    private int getCellV(int x, int y) {
        return y*width + x;
    }

    private void checkBoundaries(int x1, int y1, int x2, int y2) {
        if (x1 < 0 || x1 >= width ||
                x2 < 0 || x2 >= width ||
                y1 < 0 || y1 >= height ||
                y2 < 0 || y2 >= height)
            throw new IllegalArgumentException("Given cell is out of bounds!");
    }

    private void checkAdjustments(int x1, int y1, int x2, int y2) {
        if ((abs(x1-x2) == 1 && y1 == y2) || (abs(y1-y2) == 1 && x1 == x2)) {
            return;
        }
        throw new IllegalArgumentException("Cells must be adjustment!");
    }

}
