import java.util.*;

public class Graph {

    double adjMatrix[][];
    int numOfVehicles;
    Depot d;
    List<Customer> c;
    double dist;
    Queue<Integer> q;
    LinkedList<Customer> linkedList = new LinkedList<Customer>();
    ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();

    public Graph(Depot d, List<Customer> c){
        this.d =d;
        this.c = c;
        numOfVehicles = 0;
        q = new LinkedList<Integer>();
        adjMatrix = new double[d.numOfCustomers][d.numOfCustomers];
        //forming the graph
        for (int i = 0; i < d.numOfCustomers; i++) { //drawing the edge
            for (int j = 0; j < d.numOfCustomers; j++) {
                if(i==j) continue;
                if(i==0){
                    dist = Math.sqrt(Math.pow((d.xCoordinate-c.get(j-1).xCoordinate),2) + Math.pow((d.yCoordinate-c.get(j-1).yCoordinate),2));
                }else if(j==0){
                    dist = Math.sqrt(Math.pow((d.xCoordinate-c.get(i-1).xCoordinate),2) + Math.pow((d.yCoordinate-c.get(i-1).yCoordinate),2));
                }else{
                    dist = Math.sqrt(Math.pow(c.get(i-1).xCoordinate-c.get(j-1).xCoordinate,2) + Math.pow(c.get(i-1).yCoordinate-c.get(j-1).yCoordinate,2));//finding the distance between 2 nodes(Customer or Depot)
                }
                adjMatrix[i][j] = adjMatrix[j][i] = dist;
            }
        }
    }

    public void displayEdges(){
        for (int i = 0; i < adjMatrix.length; i++) {
            for (int j = 0; j < adjMatrix.length; j++) {
                System.out.print(adjMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public int getAdjUnvisitedVertex(int v){
        for (int i = 1; i <= c.size(); i++) {
            if(!c.get(i-1).wasVisited && adjMatrix[v][i]>0){
                return c.get(i-1).id;
            }
        }
        return -1;
    }

    public void bfs() {
        double shortestPath;
        int currentLoad;
        double routeCost;
        double tourCost=0;

        q.add(0);
        int v1;
        int v2;
        v1 = q.remove();
        while((v2=getAdjUnvisitedVertex(v1))!=-1){ //Queue = {1,2,3,4}
            c.get(v2-1).wasVisited=true;
            q.add(v2);
        }

        for (int i = 0; i < c.size(); i++) { //resetting all to false to create connections later
            c.get(i).wasVisited=false;
        }


        while(!completeVisited()){
            currentLoad=0;
            shortestPath=Double.POSITIVE_INFINITY;
            if(c.get(q.peek()-1).wasVisited){
                q.remove();
            }else{
                v2 = q.remove();
                c.get(v2-1).wasVisited = true;
                currentLoad+=c.get(v2-1).demandSize;
                linkedList.add(c.get(v2-1));
                int temp=0; //customer ID
                for (int i = 0; i < c.size(); i++) { //create a temporary shortest path here (e.g. 1->2, we know later it will be replaced by 3)
                    if (!c.get(i).wasVisited && (currentLoad + c.get(i).demandSize) <= d.maxCapacity) {
                        shortestPath = adjMatrix[v2][i+1];
                        temp = i+1;
                        break;
                    }
                }
                for (int k = 0; k < c.size(); k++) { //find the shortest path
                    if (!c.get(k).wasVisited && (currentLoad + c.get(k).demandSize) <= d.maxCapacity) {
                        if(shortestPath > adjMatrix[v2][k+1]){
                            shortestPath = adjMatrix[v2][k+1];
                            temp = k+1;
                        }
                    }
                }

                if(temp==0){//temp==0 means there is only one node(e.g. 0->4->0)
                    routeCost = computeRouteCost(linkedList);
                    vehicleList.add(new Vehicle(linkedList, routeCost,currentLoad));
                }else{//(e.g. 0->1->3->0)
                    c.get(temp-1).wasVisited=true;
                    currentLoad+=c.get(temp-1).demandSize;
                    linkedList.add(c.get(temp-1));
                    routeCost = computeRouteCost(linkedList);
                    vehicleList.add(new Vehicle(linkedList, routeCost,currentLoad));
                }
                tourCost+=routeCost;
                linkedList.clear();
            }
        }

        //display output
        System.out.println("Basic Simulation Tour" + "\nTour Cost: " + tourCost);
        displayVehicle(vehicleList);
    }

    public boolean completeVisited(){
        for (int i = 0; i < c.size(); i++) {
            if(!c.get(i).wasVisited) return false;
        }
        return true;
    }

    public double computeRouteCost(LinkedList<Customer> linkedList){
        double routeCost=0;
        routeCost+=adjMatrix[0][linkedList.getFirst().id];

        for (int i = 0; i < linkedList.size()-1; i++) {
            routeCost+=adjMatrix[linkedList.get(i).id][linkedList.get(i+1).id];

        }
        routeCost+=adjMatrix[linkedList.get(linkedList.size()-1).id][0];
        return routeCost;
    }

    public void displayVehicle(ArrayList<Vehicle> vehicleList){
        for (int i = 1; i <= vehicleList.size(); i++) {
            System.out.println("Vehicle " + i);
            System.out.println(vehicleList.get(i-1));
        }
    }

    public void greedySearch(){

    }

    public void MCTSearch(){

    }

}
