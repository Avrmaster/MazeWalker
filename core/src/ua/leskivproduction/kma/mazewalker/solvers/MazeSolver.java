package ua.leskivproduction.kma.mazewalker.solvers;

import com.badlogic.gdx.graphics.Color;
import ua.leskivproduction.kma.mazewalker.model.Maze;

import java.util.List;
import java.awt.Point;

public abstract class MazeSolver {
    protected final static float DEFAULT_STEP_TIME = 0.002f;
    private final float STEP_TIME;
    private final static float SOLUTION_DRAW_TIME = 1;

    protected Maze maze;
    public MazeSolver(Maze maze, final float step_time) {
        this.maze = maze;
        this.STEP_TIME = step_time;
    }

    protected boolean solvable = true;
    protected int stepsPerformed;
    protected boolean done;
    protected float time;

    private boolean solutionDrawn;
    private float solutionDrawProgress;
    private int drawnPart;

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

            for (; drawnPart < doneByNow; drawnPart++) {
                Point p = solution.get(drawnPart);
                maze.addMarker(p.x, p.y, Color.BLUE, 0.5f);
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

    protected abstract boolean performStep();

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
