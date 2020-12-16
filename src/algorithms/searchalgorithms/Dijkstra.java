package algorithms.searchalgorithms;

import framework.MapPanel;

import java.awt.*;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Dijkstra extends PathAlgorithm {

    public Dijkstra(MapPanel panel, LinkedList<Point> list){
        super(panel, list);
    }

    @Override
    public void run(){

        long beforeTime;

        beforeTime = System.currentTimeMillis();

        PriorityQueue<Node> Q = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node nodeA, Node nodeB) {
                if(nodeA.getDistance() == null && nodeB.getDistance() != null) return -1;
                else if(nodeA.getDistance() != null && nodeB.getDistance() == null) return 1;
                else if(nodeA.getDistance() == nodeB.getDistance()) return 0;
                else return nodeA.getDistance() < nodeB.getDistance() ? -1 : 1;
            }
        });

        Q.add(this.start);

        LinkedList<Point> visited = new LinkedList<>();

        boolean isfinished = false;

        while(!Q.isEmpty() && !isfinished && !isInterrupted()){
            Node curr = Q.poll();
            visited.add(new Point(curr.getX(), curr.getY()));

            if(!curr.equals(start))
                update(50 - System.currentTimeMillis() - beforeTime, curr, 3);

            for(Node i: getNeighbours(curr)){
                relax(curr, i);
                if(isFinish(i)){
                    isfinished = true;
                    finishnode = i;
                }
                else if(!visited.contains(new Point(i.getX(), i.getY()))){
                    Q.remove(i);
                    Q.add(nodemap.get(i.getX()).get(i.getY()));

                    update(50 - System.currentTimeMillis() - beforeTime, i, 4);
                }
            }
        }

        paintPath(beforeTime);
    }

    @Override
    LinkedList<Node> getNeighbours(Node curr) {
        LinkedList<Node> neighbours = new LinkedList<>();

        for(int i = -1, j = 1; i < 2; i += 2, j -= 2){
            if(0 <= curr.getY() + i && curr.getY() + i < nodemap.get(curr.getX()).size()){
                Node tmp = nodemap.get(curr.getX()).get(curr.getY() + i);
                if(tmp.isLegalSuccessor(curr, start)){
                    neighbours.add(tmp);
                }
            }
            if(0 <= curr.getX() + j && curr.getX() + j < nodemap.size()){
                Node tmp = nodemap.get(curr.getX() + j).get(curr.getY());
                if(tmp.isLegalSuccessor(curr, start)){
                    neighbours.add(tmp);
                }
            }
        }

        return neighbours;
    }

}
