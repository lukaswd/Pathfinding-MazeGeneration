package algorithms.mazegeneration;

import framework.MapPanel;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

public class DepthFirstSearch extends GenerationAlgorithm {

    public DepthFirstSearch(MapPanel panel) {
        super(panel);
    }

    @Override
    public void run() {
        initMap();

        long beforeTime;
        beforeTime = System.currentTimeMillis();

        Stack<Point> cells = new Stack<>();
        LinkedList<Point> visited = new LinkedList<>();
        Random random = new Random();

        cells.push(start);

        while(!cells.isEmpty()){
            Point curr = cells.pop();

            LinkedList<Point> neighbours = new LinkedList<>();
            for(Point p: getNeighbours(curr)){
                if(!visited.contains(p)) neighbours.add(p);
            }

            if(!neighbours.isEmpty()){
                cells.push(curr);
                Point chosen = neighbours.get(random.nextInt(neighbours.size()));
                Point wall = getInBetween(curr, chosen);
                visited.add(chosen);
                visited.add(wall);
                cells.push(chosen);
                update(50 - System.currentTimeMillis() - beforeTime, wall, 0);
                update(50 - System.currentTimeMillis() - beforeTime, chosen, 0);
            }
        }
    }

    private Point getInBetween(Point curr, Point chosen){
        if(curr.x < chosen.x) return new Point(curr.x + 1, curr.y);
        else if(curr.x > chosen.x) return new Point(curr.x - 1, curr.y);
        else if(curr.y < chosen.y) return new Point(curr.x, curr.y + 1);
        else return new Point(curr.x, curr.y - 1);
    }
}
