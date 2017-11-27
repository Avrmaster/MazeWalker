package ua.leskivproduction.kma.mazewalker.solvers;

import ua.leskivproduction.kma.mazewalker.model.Maze;
import java.util.Stack;


public final class DFSolver extends MazeSolver {
    private Stack<Integer> verticesStack;

    public DFSolver(Maze maze) {
        super(maze);

        verticesStack = new Stack<>();
        verticesStack.push(initialV);
        visited[initialV] = true;
    }

    @Override
    protected void insertVertex(int newV) {
        verticesStack.push(newV);
    }

    @Override
    protected boolean isCollectionEmpty() {
        return verticesStack.isEmpty();
    }

    @Override
    protected int removeFirst() {
        return verticesStack.pop();
    }


}
