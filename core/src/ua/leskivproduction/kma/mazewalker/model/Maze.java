package ua.leskivproduction.kma.mazewalker.model;

import com.badlogic.gdx.graphics.Color;
import ua.leskivproduction.kma.mazewalker.solvers.BFSolver;
import ua.leskivproduction.kma.mazewalker.solvers.MazeSolver;
import ua.leskivproduction.kma.mazewalker.solvers.SolverFactory;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.floorMod;
import static ua.leskivproduction.kma.mazewalker.utils.Lerper.lerp;

public final class Maze {
    private final static Point START_POINT = new Point(0, 0);
    public final static int MAX_MARKERS_COUNT = 900;

    public final Graph graph;
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

    public MazeSolver solver;

    private List<Marker> markers = new LinkedList<>();
    public class Marker {
        public boolean removable;
        public Point pos;
        public float radius;
        public float goalRadius;
        public Color color;
        private Marker(Point pos) {
            this.pos = new Point(pos);
        }
        private Marker(int x, int y) {
            this.pos = new Point(x, y);
        }
        public void update(float deltaTime) {
            if (Math.abs(radius-goalRadius) > 0.01f)
                radius = lerp(radius, goalRadius, 4*deltaTime);
            else
                radius = goalRadius;
        }
    }

    private SolverFactory solverFactory;

    public Maze(int width, int height, SolverFactory solverFactory) {
        this.solverFactory = solverFactory;

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

    public void addMarker(int cellX, int cellY, Color color, boolean important) {
        addMarker(cellX, cellY, color, 1, important);
    }

    public void addMarker(int cellX, int cellY, Color color, float radius, boolean important) {
        removeAllMarkersAt(cellX, cellY);
        Marker newMarker = new Marker(cellX, cellY);
        newMarker.goalRadius = radius;
        newMarker.color = color;
        newMarker.removable = !important;
        markers.add(newMarker);

        int i = 0;
        while (markers.size() > MAX_MARKERS_COUNT && i < markers.size()) {
            if (markers.get(i).removable) {
                markers.remove(i);
            } else i++;
        }
    }

    public void removeLastMarker() {
        if (markers.size() > 0)
            markers.remove(markers.size()-1);
    }

    public void removeAllMarkersAt(Point p) {
        removeAllMarkersAt(p.x, p.y);
    }
    public void removeAllMarkersAt(int cellX, int cellY) {
        for (int i = markers.size()-1; i >= 0; i--) {
            Marker m = markers.get(i);
            if (m.pos.x == cellX && m.pos.y == cellY)
                markers.remove(i);
        }
    }

    public void clearMarkers() {
        for(Marker m : markers)
            m.goalRadius = 0;
    }

    public List<Marker> markers() {
        return markers;
    }

    public void setObjective(Point endPoint) {
        setObjective(START_POINT, endPoint);
    }

    public void setObjective(Point startPoint, Point endPoint) {
        objective = new Objective(startPoint, endPoint);
        clearMarkers();
        updateObjectiveMarkers();
        solver = solverFactory.genSolver(this);
    }

    public void updateObjectiveMarkers() {
        int startX = objective.startPoint.x;
        int startY = objective.startPoint.y;
        int endX = objective.endPoint.x;
        int endY = objective.endPoint.y;

        removeAllMarkersAt(startX, startY);
        removeAllMarkersAt(endX, endY);

        addMarker(startX, startY, Color.BROWN, true);
        addMarker(endX, endY, Color.GOLD, true);
    }

    public Objective getObjective() {
        return new Objective(this.objective);
    }

    public boolean hasObjective() {
        return objective != null;
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

    public int getCellV(int x, int y) {
        return y*width + x;
    }

    public int getCellV(Point p) {
        return getCellV(p.x, p.y);
    }

    public Point getCellPos(int V) {
        return new Point(V%width, V/width);
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
