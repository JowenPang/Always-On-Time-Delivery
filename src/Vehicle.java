import java.util.ArrayList;
import java.util.LinkedList;

public class Vehicle {
    int capacity;
    double cost;
    LinkedList<Location> list;

    public Vehicle(LinkedList<Location> linkedList, double cost, int capacity){
        //I think the linked list for one vehicle which show the path should contain start from 0 and end with 0 too
        this.list = (LinkedList<Location>) linkedList.clone();
        this.cost = cost;
        this.capacity = capacity;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        //sb.append(0 + " -> ");
        for (int i = 0; i < list.size()-1; i++) {
            sb.append(list.get(i).id + " -> ");
        }
        sb.append(0);
        return sb + "\nCapacity: " + capacity + "\nCost: " + cost + "\n";
    }

}
