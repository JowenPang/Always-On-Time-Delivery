public class Depot extends Location{
    int numOfCustomers;
    int maximumCapacity;

    public Depot(int numOfCustomers, int maxCapacity, int xCoordinate, int yCoordinate) {
        super(xCoordinate, yCoordinate, maxCapacity);
        this.numOfCustomers = numOfCustomers;
        maxCapacity=super.demandSize;
    }

    public int getId() {
        return id;
    }
}