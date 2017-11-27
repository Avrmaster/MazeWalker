package ua.leskivproduction.kma.mazewalker.solvers;

import com.badlogic.gdx.graphics.Color;
import ua.leskivproduction.kma.mazewalker.model.Graph;
import ua.leskivproduction.kma.mazewalker.model.Maze;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BFSolver extends MazeSolver {
    private Queue<Integer> verticesQueue;
    private Maze.Objective objective;
    private Graph mazeGraph;

    private int initialV, currentV, goalV;
    private boolean[] visited;
    private int[] paths;

    public BFSolver(Maze maze) {
        this(maze, DEFAULT_STEP_TIME);
    }



    public BFSolver(Maze maze, float step_time) {
        super(maze, step_time);

        mazeGraph = maze.graph;
        objective = maze.getObjective();

        initialV = currentV = maze.getCellV(objective.startPoint);
        goalV = maze.getCellV(objective.endPoint);
        visited = new boolean[mazeGraph.V];
        paths = new int[mazeGraph.V];

        visited[initialV] = true;
        verticesQueue = new ConcurrentLinkedQueue<>();
        verticesQueue.add(initialV);
    }

    @Override
    protected boolean performStep() {
//        if (currentV == goalV)
//            return true;
//
//        int newV = -1;
//        for (int adj : mazeGraph.edges(currentV)) {
//            if (!visited[adj]) {
//                newV = adj;
//                break;
//            }
//        }
//
//        Point pos = maze.getCellPos(currentV);
//
//        if (newV != -1) {
//            paths[newV] = currentV;
//            currentV = newV;
//            verticesQueue.add(newV);
//
//            if (!pos.equals(objective.startPoint) && !pos.equals(objective.endPoint))
//                maze.addMarker(pos.x, pos.y, com.badlogic.gdx.graphics.Color.CHARTREUSE, 0.4f);
//        } else {
//            if (!pos.equals(objective.startPoint) && !pos.equals(objective.endPoint))
//                maze.addMarker(pos.x, pos.y, Color.RED, 0.6f);
//
//            if (verticesQueue.isEmpty()) {
//                solvable = false;
//                return true; //done
//            }
//            currentV = verticesQueue.poll();
//        }
//
//        visited[currentV] = true;
//
//        return currentV == goalV;
        return false;
    }

}
