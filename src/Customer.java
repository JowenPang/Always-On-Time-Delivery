public class Customer {
    int xCoordinate;
    int yCoordinate;
    int demandSize;

    public Customer(int xCoordinate, int yCoordinate, int demandSize) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.demandSize = demandSize;
    }

    @Override
    public String toString() {
        return "[x-coordinate: " + xCoordinate + ", y-coordinate: " + yCoordinate +", demand size: " + demandSize + "]";
    }
}
