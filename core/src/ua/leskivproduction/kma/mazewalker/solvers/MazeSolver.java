package ua.leskivproduction.kma.mazewalker.solvers;

import com.badlogic.gdx.graphics.Color;
import ua.leskivproduction.kma.mazewalker.model.Maze;

import java.util.List;
import java.awt.Point;

public abstract class MazeSolver {
    protected final static float DEFAULT_STEP_TIME = 0.01f;
    private final float STEP_TIME;

    protected Maze maze;
    public MazeSolver(Maze maze, final float step_time) {
        this.maze = maze;
        this.STEP_TIME = step_time;
    }

    protected boolean solvable = true;
    protected int stepsPerformed;
    protected boolean done;
    protected float time;
    protected boolean solutionDrawn;
    public void update(float deltaTime) {
        time += deltaTime;

        int stepsByNow = (int)(time/STEP_TIME);
        while (!done && stepsPerformed < stepsByNow) {
            done = performStep();
            stepsPerformed++;
        }

        if (done && solvable && !solutionDrawn) {
            maze.clearMarkers();
            maze.updateObjectiveMarkers();
            new Thread(() -> {
                for (Point p : getSolution()) {
                    maze.addMarker(p.x, p.y, Color.BLUE, 0.5f);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {

                    }
                }
                solutionDrawn = true;
            }).start();
        }
    }

    public abstract List<Point> getSolution();

    protected abstract boolean performStep();

    public boolean done() {
        return done;
    }

    public boolean solvable() {
        return solvable;
    }

}
