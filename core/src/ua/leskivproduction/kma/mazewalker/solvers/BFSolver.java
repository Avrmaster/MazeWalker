package ua.leskivproduction.kma.mazewalker.solvers;

import ua.leskivproduction.kma.mazewalker.model.Maze;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BFSolver extends MazeSolver {
    private Queue<Integer> verticesQueue;

    public BFSolver(Maze maze) {
        super(maze);

        visited[initialV] = true;
        verticesQueue = new ConcurrentLinkedQueue<>();
        verticesQueue.add(initialV);

    }

    @Override
    protected void insertVertex(int newV) {
        verticesQueue.add(newV);
    }

    @Override
    protected boolean isCollectionEmpty() {
        return verticesQueue.isEmpty();
    }

    @Override
    protected int removeFirst() {
        return verticesQueue.poll();
    }


}
