package algorithms.mazegeneration;

import algorithms.Algorithm;
import framework.MapPanel;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public abstract class GenerationAlgorithm extends Algorithm {

    Point start;

    int columns;
    int rows;

    public GenerationAlgorithm(MapPanel panel){
        super(panel);

        panel.reset();

        columns = panel.getColumns();
        rows = panel.getRows();

        Random random = new Random();
        start = new Point(random.nextInt(columns / 2) * 2, random.nextInt(rows / 2) * 2);
    }

    /**
     * Initiates the map with making every cell a wall
     */
    void initMap(){
        for(int i = 0; i < columns; i++){
            for(int j = 0; j < rows; j++){
                panel.setMapPoint(i, j, 1);
            }
        }

        panel.repaint();
    }

    void update(long sleep, Point point, int value){
        if(!isInterrupted()) {
            panel.setMapPoint(point.x, point.y, value);
            panel.repaint();

            if (sleep < 0)
                sleep = 2;
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }
        }
    }

    /**
     * Gets the neigbhour cells of the given point
     *
     * @param curr
     * @return List of neighbour cells
     */
    LinkedList<Point> getNeighbours(Point curr) {
        LinkedList<Point> neighbours = new LinkedList<>();

        for(int i = -2; i < 3; i += 4){
            if(0 <= curr.x + i && curr.x + i < columns){
                neighbours.add(new Point(curr.x + i, curr.y));
            }
            if(0 <= curr.y + i && curr.y + i < rows){
                neighbours.add(new Point(curr.x, curr.y + i));
            }
        }

        return neighbours;
    }
}
