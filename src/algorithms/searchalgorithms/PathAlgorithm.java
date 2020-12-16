package algorithms.searchalgorithms;

import algorithms.Algorithm;
import framework.MapPanel;

import java.awt.*;
import java.util.LinkedList;

public abstract class PathAlgorithm extends Algorithm {

    LinkedList<LinkedList<Node>> nodemap;

    Node start;
    Point finish;

    Node finishnode;

    public PathAlgorithm(MapPanel panel, LinkedList<Point> startandfinish){

        super(panel);

        panel.resetPath();
        panel.setOldMap(panel.getMap());

        start = new Node(startandfinish.get(0).x, startandfinish.get(0).y, 0, null, false);
        finish = startandfinish.get(1);

        initNodeMap(panel.getMap());
    }

    /**
     * Initiates the NodeMap and setts every Nodes distamce to null (except the start node = 0) and its predessecor to null
     */
    void initNodeMap(int[][] map){
        nodemap = new LinkedList<>();

        for(int i = 0; i < map.length; i++){

            LinkedList<Node> colum = new LinkedList<>();

            for(int j = 0; j < map[i].length; j++){
                if(start.getX() == i && start.getY() == j) colum.add(this.start);
                else colum.add(new Node(i,j ,null, null, map[i][j] == 1));
            }

            nodemap.add(colum);
        }

    }

    /**
     * Creates a List off all neighbours of the current node
     *
     * @param curr
     * @return List of Neighbours
     */
    LinkedList<Node> getNeighbours(Node curr) {
        LinkedList<Node> neighbours = new LinkedList<>();

        for(int i = -1, j = 1; i < 2; i += 2, j -= 2){
            if(0 <= curr.getY() + i && curr.getY() + i < nodemap.get(curr.getX()).size()){
                Node tmp = nodemap.get(curr.getX()).get(curr.getY() + i);
                if(tmp.isLegalSuccessor(curr, start) && tmp.getPredecessor() == null){
                    tmp.setPredecessor(curr);
                    tmp.setDistance(curr.getDistance() + 1);
                    neighbours.add(tmp);
                }
            }
            if(0 <= curr.getX() + j && curr.getX() + j < nodemap.size()){
                Node tmp = nodemap.get(curr.getX() + j).get(curr.getY());
                if(tmp.isLegalSuccessor(curr, start) && tmp.getPredecessor() == null){
                    tmp.setPredecessor(curr);
                    tmp.setDistance(curr.getDistance() + 1);
                    neighbours.add(tmp);
                }
            }
        }

        return neighbours;
    }

    boolean relax(Node curr, Node i){
        if(i.getDistance() == null || curr.getDistance() + 1 < i.getDistance()){
            nodemap.get(i.getX()).get(i.getY()).setPredecessor(curr);
            nodemap.get(i.getX()).get(i.getY()).setDistance(curr.getDistance() + 1);
            return true;
        }

        return false;
    }

    boolean isFinish(Node node){
        return node.getX() == finish.x && node.getY() == finish.y;
    }

    void update(long sleep, Node node, int value){
        if(!isInterrupted()){
            panel.setMapPoint(node.getX(), node.getY(), value);
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

    void paintPath(long beforeTime){
        if(!isInterrupted()) {
            Node curr = finishnode.getPredecessor();

            while (curr.getPredecessor() != null && !curr.equals(start)) {
                update(50 - System.currentTimeMillis() - beforeTime, curr, 5);
                curr = curr.getPredecessor();
            }
        }
    }

}
