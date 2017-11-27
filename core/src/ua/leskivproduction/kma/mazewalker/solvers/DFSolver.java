package ua.leskivproduction.kma.mazewalker.solvers;

import com.badlogic.gdx.graphics.Color;
import ua.leskivproduction.kma.mazewalker.model.Graph;
import ua.leskivproduction.kma.mazewalker.model.Maze;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public final class DFSolver extends MazeSolver {
    private Stack<Integer> verticesStack;
    private Maze.Objective objective;
    private Graph mazeGraph;

    private int initialV, currentV, goalV;
    private boolean[] visited;
    private int[] paths;

    public DFSolver(Maze maze) {
        this(maze, DEFAULT_STEP_TIME);
    }
    public DFSolver(Maze maze, float step_time) {
        super(maze, step_time);

        mazeGraph = maze.graph;
        objective = maze.getObjective();

        initialV = currentV = maze.getCellV(objective.startPoint);
        goalV = maze.getCellV(objective.endPoint);
        visited = new boolean[mazeGraph.V];
        paths = new int[mazeGraph.V];

        verticesStack = new Stack<>();
        verticesStack.push(initialV);
        visited[initialV] = true;
    }

    @Override
    protected boolean performStep() {
        if (currentV == goalV)
            return true;

        int newV = -1;
        for (int adj : mazeGraph.edges(currentV)) {
            if (!visited[adj]) {
                newV = adj;
                break;
            }
        }

        Point pos = maze.getCellPos(currentV);

        if (newV != -1) {
            paths[newV] = currentV;
            currentV = newV;
            verticesStack.push(newV);

            if (!pos.equals(objective.startPoint) && !pos.equals(objective.endPoint))
                maze.addMarker(pos.x, pos.y, Color.CHARTREUSE, 0.4f);
        } else {
            if (!pos.equals(objective.startPoint) && !pos.equals(objective.endPoint))
                maze.addMarker(pos.x, pos.y, Color.RED, 0.6f);

            if (verticesStack.isEmpty()) {
                solvable = false;
                return true; //done
            }
            currentV = verticesStack.pop();
        }

        visited[currentV] = true;

        if (currentV == goalV) {
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

}
