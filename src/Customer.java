public class Customer {
    int xCoordinate;
    int yCoordinate;
    int demandSize;
    int id;
    static int serial =1;
    boolean wasVisited;

    public Customer(int xCoordinate, int yCoordinate, int demandSize) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.demandSize = demandSize;
        wasVisited = false;
        id = serial++; //auto increment the ID whenever an instance of customer is created
    }

    public int getId() {
        return id;
    }

    public void setWasVisited(boolean wasVisited) {
        this.wasVisited = wasVisited;
    }

    @Override
    public String toString() {
        return "[ id: " + id+ " x-coordinate: " + xCoordinate + ", y-coordinate: " + yCoordinate +", demand size: " + demandSize + "]";
    }
}