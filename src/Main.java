import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static List<Location> list = new ArrayList<>();
    //static Depot depot;
    public static void main(String[] args) {
        try {
            Scanner input = new Scanner(new FileInputStream("test.txt"));
            int numOfCustomers = input.nextInt();
            int maxCapacity = input.nextInt();
            int xCoordinate = input.nextInt();
            int yCoordinate = input.nextInt();
            list.add(new Depot(numOfCustomers, maxCapacity, xCoordinate,yCoordinate)); //instantiate Depot
            input.nextLine();
            while(input.hasNextLine()) {     //instantiate Customer
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
        for (int i = 1; i < list.size(); i++) {  //print only customer
            System.out.println(list.get(i));
        }

        Graph graph = new Graph(list);
        //graph.displayEdges();

        System.out.println(graph.dfs());
        //graph.bfs2();
        System.out.println("\n\n--------------------------------------------------------\n\n");
        //System.out.println(graph.greedySearch());
        System.out.println("\n\n--------------------------------------------------------\n\n");
        //System.out.println(graph.bestFirstSearch());

        //System.out.println(graph.bestFirstSearch2());
        System.out.println("\n\n--------------------------------------------------------\n\n");
        //System.out.println(graph.AStarSearch());

    }
}
