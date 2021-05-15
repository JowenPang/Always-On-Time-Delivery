import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static List<Customer> list = new ArrayList<Customer>();
    static Depot depot;
    public static void main(String[] args) {
        try {
            Scanner input = new Scanner(new FileInputStream("test.txt"));
            int numOfCustomers = input.nextInt();
            int maxCapacity = input.nextInt();
            int xCoordinate = input.nextInt();
            int yCoordinate = input.nextInt();
            depot = new Depot(numOfCustomers, maxCapacity, xCoordinate,yCoordinate);
            input.nextLine();
            while(input.hasNextLine()) {
                int x = input.nextInt();
                int y = input.nextInt();
                int demandSize = input.nextInt();
                list.add(new Customer(x,y,demandSize));
            }
            input.close();
        }
        catch(FileNotFoundException e) {
            System.out.println("File was not found");
        }
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }

        Graph graph = new Graph(depot, list);
//        graph.displayEdges();
        graph.bfs();

    }
}
