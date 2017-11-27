package ua.leskivproduction.kma.mazewalker;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
    private ShapeRenderer shapeRenderer;

    private Stack<Point> shuffleStack;

    public MazeShuffler(Maze maze, float shuffleTime) {
        if (maze.width*maze.height<2)
            throw new IllegalArgumentException("This maze cannot be shuffled! It's too small.");
        this.maze = maze;
        this.shuffleTime = shuffleTime;
        shapeRenderer = new ShapeRenderer();
        totalStepsCnt = maze.width*maze.height;
        visited = new boolean[maze.width][maze.height];

        curPoint = new Point(0, 0);
        visited[curPoint.x][curPoint.y] = true;
        shuffleStack = new Stack<>();
    }

    private float time;
    private Point curPoint;
    private int performedSteps;
    public void update(float deltaTime) {
        time += deltaTime;
        int goalStepsCnt = Math.min(totalStepsCnt, (int)((time/shuffleTime)*totalStepsCnt));
        while (performedSteps < goalStepsCnt) {

            List<Point> neighbours = unvisitedNeighbours(curPoint);
            if (neighbours.size() > 0) {
                int randInd = (int)(neighbours.size()*Math.random());
                Point newPoint = neighbours.get(randInd);
                shuffleStack.push(curPoint);
                maze.setColor(curPoint.x, curPoint.y, Color.GREEN);

                maze.open(curPoint.x, curPoint.y, newPoint.x, newPoint.y);

                curPoint = newPoint;
                visited[curPoint.x][curPoint.y] = true;
                performedSteps++;

                maze.setColor(curPoint.x, curPoint.y, Color.RED);
            } else {
                curPoint = shuffleStack.pop();
                System.out.println(curPoint);
            }
        }
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

    private boolean hasUnvisited() {
        for (int i = 0; i < maze.width; i++) {
            for (int j = 0; j < maze.height; j++) {
                if (!visited[i][j])
                    return true;
            }
        }
        return false;
    }

}
