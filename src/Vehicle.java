import java.util.ArrayList;
import java.util.LinkedList;

public class Vehicle {
    int capacity;
    double cost;
    LinkedList<Customer> list;

    public Vehicle(LinkedList<Customer> linkedList, double cost, int capacity){
        this.list = (LinkedList<Customer>) linkedList.clone();
        this.cost = cost;
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(0 + " -> ");
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i).getId() + " -> ");
        }
        sb.append(0);
        return sb + "\nCapacity: " + capacity + "\nCost: " + cost;
    }

}
