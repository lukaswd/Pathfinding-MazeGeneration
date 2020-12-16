package algorithms.searchalgorithms;

import framework.MapPanel;

import java.awt.*;
import java.util.LinkedList;

public class BreathFirstSearch extends PathAlgorithm {

    public BreathFirstSearch(MapPanel panel, LinkedList<Point> list){
        super(panel, list);
    }

    @Override
    public void run() {
        long beforeTime;

        beforeTime = System.currentTimeMillis();

        LinkedList<Node> Q = new LinkedList<>();
        Q.add(nodemap.get(start.getX()).get(start.getY()));

        boolean isfinished = false;

        while(!Q.isEmpty() && !isfinished && !isInterrupted()){
            Node curr = Q.remove();

            if(!curr.equals(start))
                update(50 - System.currentTimeMillis() - beforeTime, curr, 3);

            for(Node i: getNeighbours(curr)){
                if(isFinish(i)) {
                    finishnode = i;
                    isfinished = true;
                }
                else {
                    Q.add(i);

                    update(50 - System.currentTimeMillis() - beforeTime, i, 4);
                }
            }
        }

        paintPath(beforeTime);
    }

}
