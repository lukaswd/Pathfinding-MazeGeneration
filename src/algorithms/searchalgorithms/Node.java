package algorithms.searchalgorithms;

public class Node {

    private int x;
    private int y;
    private Integer distance;
    private Node predecessor;
    private boolean wall;

    public Node(int x, int y, Integer distance, Node predecessor, boolean iswall){
        this.x = x;
        this.y = y;
        this.distance = distance;
        this.predecessor = predecessor;
        this.wall = iswall;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Integer getDistance() {
        return distance;
    }

    public Node getPredecessor() {
        return predecessor;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public void setPredecessor(Node predecessor) {
        this.predecessor = predecessor;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isWall() {
        return wall;
    }

    public void printNode(){
        System.out.print("[" + x + " , " + y + "] ");
    }

    public boolean isLegalSuccessor(Node node, Node start){
        return this != null && !this.isWall() && !this.sameas(node) && !this.sameas(start);
    }

    public boolean sameas(Node node){
        return x == node.x && y == node.y;
    }
}
