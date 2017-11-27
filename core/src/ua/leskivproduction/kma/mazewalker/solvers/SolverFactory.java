package ua.leskivproduction.kma.mazewalker.solvers;

import ua.leskivproduction.kma.mazewalker.model.Maze;

public interface SolverFactory {
    MazeSolver genSolver(Maze maze);
}
