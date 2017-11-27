package ua.leskivproduction.kma.mazewalker.solvers;

import ua.leskivproduction.kma.mazewalker.model.Maze;

import java.awt.*;
import java.util.List;

public class BFSolver extends MazeSolver {

    public BFSolver(Maze maze) {
        this(maze, DEFAULT_STEP_TIME);
    }

    public BFSolver(Maze maze, float step_time) {
        super(maze, step_time);

    }

    @Override
    public List<Point> getSolution() {
        return null;
    }

    @Override
    protected boolean performStep() {
        return false;
    }
}
