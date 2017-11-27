package ua.leskivproduction.kma.mazewalker.solvers;

import com.badlogic.gdx.graphics.Color;
import ua.leskivproduction.kma.mazewalker.model.Graph;
import ua.leskivproduction.kma.mazewalker.model.Maze;

import java.util.ArrayList;
import java.util.List;
import java.awt.Point;

public abstract class MazeSolver {
    protected final static float STEP_TIME = 0.003f;
    private final static float SOLUTION_DRAW_TIME = 3;

    protected Maze maze;
    public MazeSolver(Maze maze) {
        this.maze = maze;

        mazeGraph = maze.graph;
        objective = maze.getObjective();

        initialV = currentV = maze.getCellV(objective.startPoint);
        goalV = maze.getCellV(objective.endPoint);

        visited = new boolean[mazeGraph.V];
        paths = new int[mazeGraph.V];
    }

    protected boolean solvable = true;
    protected int stepsPerformed;
    protected boolean done;
    protected float time;

    private boolean solutionDrawn;
    private float solutionDrawProgress;
    private int drawnPart;

    protected Maze.Objective objective;
    protected Graph mazeGraph;

    protected int initialV, currentV, goalV;
    protected boolean[] visited;
    protected int[] paths;

    public void update(float deltaTime) {
        time += deltaTime;

        int stepsByNow = (int)(time/STEP_TIME);
        while (!done && stepsPerformed < stepsByNow) {
            done = performStep();
            stepsPerformed++;
        }

        if (done && solvable && !solutionDrawn) {
            List<Point> solution = maze.solver.getSolution();
            solutionDrawProgress += deltaTime;
            int doneByNow;

            if (solutionDrawProgress > SOLUTION_DRAW_TIME)
                doneByNow = solution.size();
            else
                doneByNow = (int)(solution.size()*solutionDrawProgress/SOLUTION_DRAW_TIME);

            int added = 0;
            for (; drawnPart < doneByNow && added < 50; drawnPart++, added++) {
                Point p = solution.get(drawnPart);
                maze.addMarker(p.x, p.y, Color.BLUE, 0.5f, true, false);
            }

            if (drawnPart >= solution.size()) {
                solutionDrawProgress = 0;
                drawnPart = 0;
                solutionDrawn = true;
            }
        }
    }

    protected List<Point> solution;
    public List<Point> getSolution() {
        return solution;
    }

    protected boolean performStep() {
        Point pos = maze.getCellPos(currentV);

        if (!visited[currentV]) {
            if (!pos.equals(objective.startPoint) && !pos.equals(objective.endPoint))
                maze.addMarker(pos.x, pos.y, Color.CHARTREUSE, 0.4f, false, true);
            visited[currentV] = true;
        }

        spawn();

        if (isCollectionEmpty()) {
            solvable = false;
            return true; //done
        }

        currentV = removeFirst();

        if (currentV == goalV) {
            //solution found
            maze.clearMarkers();
            maze.updateObjectiveMarkers();

            solution = new ArrayList<>();
            int V = currentV;
            while (true) {
                V = paths[V];
                if (V != initialV)
                    solution.add(maze.getCellPos(V));
                else
                    break;
            }

        }

        return currentV == goalV;
    }

    protected abstract boolean isCollectionEmpty();
    protected abstract void spawn();
    protected abstract int removeFirst();

    public boolean done() {
        return done;
    }

    public boolean isSolutionDrawn() {
        return solutionDrawn;
    }

    public boolean solvable() {
        return solvable;
    }

}
