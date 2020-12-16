package algorithms.searchalgorithms;

import framework.MapPanel;

import java.awt.*;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.PriorityQueue;

public class AStar extends PathAlgorithm {

    public AStar(MapPanel panel, LinkedList<Point> list){
        super(panel, list);
    }

    @Override
    public void run() {
        long beforeTime;
        beforeTime = System.currentTimeMillis();

        PriorityQueue<Pair<Node>> openlist = new PriorityQueue<>(new Comparator<Pair<Node>>() {
            @Override
            public int compare(Pair<Node> node1, Pair<Node> node2) {
                if(node1.getValue() == null && node2.getValue() != null) return -1;
                else if(node1.getValue() != null && node2.getValue() == null) return 1;
                else if(node1.getValue() == node2.getValue()) return 0;
                else return node1.getValue() < node2.getValue() ? -1 : 1;
            }
        });

        openlist.add(new Pair<>(start, 0));
        boolean isfinished = false;

        while(!openlist.isEmpty() && !isfinished && !isInterrupted()){
            Pair<Node> curr = openlist.poll();

            if(!curr.object.equals(start))
                update(50 - System.currentTimeMillis() - beforeTime, curr.getObject(), 3);

            LinkedList<Node> neighbours = getNeighbours(curr);
            for(Node i: neighbours){
                if(isFinish(i)){
                    isfinished = true;
                    finishnode = i;
                }
                else {
                    if (!openlist.contains(i)) {
                        openlist.add(new Pair<>(i, calcValue(i)));
                        update(50 - System.currentTimeMillis() - beforeTime, i, 4);
                    }
                    else {
                        if (relax(curr.getObject(), i)) {
                            openlist.remove(i);
                            openlist.add(new Pair<>(i, calcValue(i)));
                        }
                    }
                }
            }
        }

        paintPath(beforeTime);
    }

    private int calcValue(Node node){
        return node.getDistance() + square(finish.x - node.getX()) + square(finish.y - node.getY());
    }

    private int square(int x){
        return x * x;
    }

    private LinkedList<Node> getNeighbours(Pair<Node> nodepair){
        return super.getNeighbours(nodepair.getObject());
    }

    private class Pair<T> {

        T object;
        Integer value;

        public Pair(T object, Integer value){
            this.object = object;
            this.value = value;
        }

        public T getObject(){
            return object;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair<?> pair = (Pair<?>) o;
            return Objects.equals(object, pair.object);
        }

        @Override
        public int hashCode() {
            return Objects.hash(object, value);
        }
    }

}
