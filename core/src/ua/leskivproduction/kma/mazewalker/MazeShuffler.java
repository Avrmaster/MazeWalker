package ua.leskivproduction.kma.mazewalker;

import com.badlogic.gdx.graphics.Color;

import java.awt.*;
import java.util.LinkedList;
import java.util.Stack;
import java.util.List;

import ua.leskivproduction.kma.mazewalker.model.Maze;

public final class MazeShuffler {

    private Maze maze;
    private float shuffleTime;
    private int totalStepsCnt;
    private boolean visited[][];

    private Stack<Point> shuffleStack;

    public MazeShuffler(Maze maze, float shuffleTime) {
        if (maze.width*maze.height<2)
            throw new IllegalArgumentException("This maze cannot be shuffled! It's too small.");
        this.maze = maze;
        this.shuffleTime = shuffleTime;
        totalStepsCnt = maze.width*maze.height;
        visited = new boolean[maze.width][maze.height];

        shuffleStack = new Stack<>();
        shuffleStack.push(new Point(0, 0));
    }

    private float time;
    private Point curPoint;
    private int performedSteps;
    private boolean cleared;

    public void update(float deltaTime) {
        time += deltaTime;
        int goalStepsCnt = Math.min(totalStepsCnt, (int)((time/shuffleTime)*totalStepsCnt));
        while (performedSteps < goalStepsCnt && !shuffleStack.isEmpty()) {
            if (curPoint == null)
                curPoint = shuffleStack.pop();

            List<Point> neighbours = unvisitedNeighbours(curPoint);
            if (neighbours.size() > 0) {
                int randInd = (int)(neighbours.size()*Math.random());
                Point newPoint = neighbours.get(randInd);
                shuffleStack.push(curPoint);
                maze.setColor(curPoint.x, curPoint.y, new Color(0, (float)(0.8+Math.random()*0.2), 0, 1));

                maze.open(curPoint.x, curPoint.y, newPoint.x, newPoint.y);

                curPoint = newPoint;
                visited[curPoint.x][curPoint.y] = true;
                performedSteps++;

                maze.setColor(curPoint.x, curPoint.y, Color.RED);
            } else {
                curPoint = shuffleStack.pop();
                maze.setColor(curPoint.x, curPoint.y, Color.GREEN);
            }
        }

        if (done() && !cleared) {
            for (int i = 0; i < maze.width; i++) {
                for (int j = 0; j < maze.height; j++) {
                    maze.setColor(i, j, Color.BLACK);
                }
            }
            cleared = true;
            shuffleStack.clear();
            resetObjective();
        }

    }

    public void resetObjective() {
        maze.setObjective(new Point((int)(maze.width*Math.random()), (int)(maze.height*Math.random())));
    }

    public boolean done() {
        for (int i = 0; i < maze.width; i++) {
            for (int j = 0; j < maze.height; j++) {
                if (!visited[i][j])
                    return false;
            }
        }
        return true;
    }

    public List<Point> unvisitedNeighbours(Point p) {
        int x = p.x, y = p.y;
        List<Point> pointList = new LinkedList<>();
        for (int i = 0; i < 4; i++) {
            try {
                switch (i) {
                    case 0:
                        if (!visited[x-1][y])
                            pointList.add(new Point(x-1, y));
                        break;
                    case 1:
                        if (!visited[x+1][y])
                            pointList.add(new Point(x+1, y));
                        break;
                    case 2:
                        if (!visited[x][y-1])
                            pointList.add(new Point(x, y-1));
                        break;
                    case 3:
                        if (!visited[x][y+1])
                            pointList.add(new Point(x, y+1));
                        break;
                }
            } catch (IndexOutOfBoundsException e) {}
        }
        return pointList;
    }

}
