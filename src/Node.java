public class Node {
    private int x;
    private int y;
    private int demandSize;

    public Node(int x, int y, int demandSize) {
        this.x = x;
        this.y = y;
        this.demandSize = demandSize;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDemandSize() {
        return demandSize;
    }

    public void setDemandSize(int demandSize) {
        this.demandSize = demandSize;
    }
}
