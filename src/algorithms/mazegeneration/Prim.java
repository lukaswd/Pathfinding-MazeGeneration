package algorithms.mazegeneration;

import framework.MapPanel;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public class Prim extends GenerationAlgorithm {

    private LinkedList<Point> visited;
    long beforeTime;

    public Prim(MapPanel panel) {
        super(panel);
        System.out.println(start.toString());
    }

    @Override
    public void run() {
        initMap();

        beforeTime = System.currentTimeMillis();

        Random random = new Random();

        update(50 - System.currentTimeMillis() - beforeTime, start, 0);

        LinkedList<Point> walls = getNeighbours(start);

        visited = new LinkedList<>();
        visited.add(start);

        while(!walls.isEmpty() && !isInterrupted()){
            Point currwall = walls.remove(random.nextInt(walls.size()));

            Point canidate = iscanidate(currwall);

            if(canidate != null){
                for(Point p: getNeighbours(canidate)){
                    if(!walls.contains(p) && !visited.contains(p)) walls.add(p);
                }
            }
        }
    }

    LinkedList<Point> getNeighbours(Point curr){
        LinkedList<Point> neighbours = new LinkedList<>();

        for(int i = -1; i < 2; i += 2){
            if(0 <= curr.x + i && curr.x + i < columns){
                neighbours.add(new Point(curr.x + i, curr.y));
            }
            if(0 <= curr.y + i && curr.y + i < rows){
                neighbours.add(new Point(curr.x, curr.y + i));
            }
        }

        return neighbours;
    }

    private Point iscanidate(Point currwall){
        LinkedList<Point> neighbours = getNeighbours(currwall);
        int i = 0;

        Point canidate = new Point();

        for(Point p: neighbours){

            Point oppositeOfP = new Point(currwall.x + (currwall.x - p.x), currwall.y + (currwall.y - p.y));
            if(visited.contains(p) && visited.contains(oppositeOfP)){
                return null;
            }
            else if(visited.contains(p)) {
                i++;
                canidate = new Point(2 * currwall.x - p.x, 2 * currwall.y - p.y);
            }
        }

        if(i == 1){
            update(50 - System.currentTimeMillis() - beforeTime, currwall, 0);
            update(50 - System.currentTimeMillis() - beforeTime, canidate, 0);

            visited.add(currwall);
            visited.add(canidate);
            return canidate;
        }
        else return null;
    }
}
