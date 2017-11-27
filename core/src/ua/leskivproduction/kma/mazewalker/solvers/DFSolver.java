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
    protected boolean isCollectionEmpty() {
        return verticesStack.isEmpty();
    }

    @Override
    protected void spawn() {
        for (int adj : mazeGraph.edges(currentV)) {
            if (!visited[adj]) {
                verticesStack.push(adj);
                paths[adj] = currentV;
            }
        }
    }

    @Override
    protected int removeFirst() {
        return verticesStack.pop();
    }


}
