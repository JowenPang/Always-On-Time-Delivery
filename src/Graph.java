import java.util.*;

public class Graph {

    double adjMatrix[][];
    int numOfVehicles;
    Depot d;
    List<Location> c;  //depot at index 0 , customer continue 1-4
    double dist;
    Queue<Integer> q;
    LinkedList<Location> linkedList = new LinkedList<>();
    ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();

    public Graph(List<Location> c){
        this.c = c;
        d=(Depot)c.get(0);
        numOfVehicles = 0;
        q = new LinkedList<Integer>();
        adjMatrix = new double[c.size()][c.size()]; //store distance between every 2 node (customer and depot),customer and customer
        //forming the graph
        for (int i = 0; i < c.size(); i++) { //drawing the edge
            for (int j = 0; j < c.size(); j++) {
                if(i==j) continue;
                if(adjMatrix[i][j]!=0)
                    continue; //because save before
                dist = Math.sqrt(Math.pow(c.get(i).xCoordinate-c.get(j).xCoordinate,2) + Math.pow(c.get(i).yCoordinate-c.get(j).yCoordinate,2));

                adjMatrix[i][j] = adjMatrix[j][i] = dist;  //because is undirected edge
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
        for (int i = 1; i < c.size(); i++) {
            Customer cus=(Customer)c.get(i);  //Always need to downcast
            if(!cus.wasVisited && adjMatrix[v][i]>0){
                return c.get(i).id;
            }
        }
        return -1;
    }

    public void bfs() {
        double shortestPath;
        int currentLoad;
        double routeCost;
        double tourCost=0;

        q.add(1); //start from 1 , because Customer class start from index 1
        int v1=q.peek();
        int v2; // used to add to the queue when repeatedly call the getAdjUnvisitedVertex()
        while((v2=getAdjUnvisitedVertex(v1))!=-1){ //Queue = {1,2,3,4}
            ((Customer)(c.get(v2))).wasVisited=true;
            q.add(v2);
        }

        for (int i = 1; i < c.size(); i++) { //resetting all to false to create connections later
            ((Customer)(c.get(i))).wasVisited=false;
        }


        while(!completeVisited()){
            linkedList.clear();
            linkedList.add (c.get(0)); //add depot
            currentLoad=0;
            shortestPath=Double.POSITIVE_INFINITY;
            if( ((Customer)c.get(q.peek())).wasVisited){
                q.remove();
            }else{
                v2 = q.remove();
                ((Customer)c.get(v2)).wasVisited = true;
                currentLoad+=c.get(v2).demandSize;
                linkedList.add (c.get(v2));  //linkedList for the vehicle
                int temp=0; //customer ID
                for (int i = 1; i < c.size(); i++) { //create a temporary shortest path here (e.g. 1->2, we know later it will be replaced by 3)
                    Customer cus=(Customer) c.get(i);
                    if (!cus.wasVisited && (currentLoad + cus.demandSize) <= d.demandSize) {  //d.demandSize= depot MaximumCapacity
                        shortestPath = adjMatrix[v2][i];
                        temp = i;
                        break;
                    }
                }
                for (int k = 1; k < c.size(); k++) { //find the shortest path
                    Customer cus=(Customer) c.get(k);
                    if (!cus.wasVisited && (currentLoad + cus.demandSize) <= d.demandSize) {
                        if(shortestPath > adjMatrix[v2][k]){
                            shortestPath = adjMatrix[v2][k];
                            temp = k;
                        }
                    }
                }
                if(temp==0){//temp==0 means there is only one node(e.g. 0->4->0)
                    linkedList.add (c.get(0)); //add depot
                    routeCost = computeRouteCost(linkedList);
                    vehicleList.add(new Vehicle(linkedList, routeCost,currentLoad));
                }else{//(e.g. 0->1->3->0)
                    Customer cus=(Customer)c.get(temp);
                    cus.wasVisited=true;
                    currentLoad+=cus.demandSize;
                    linkedList.add(cus);
                    linkedList.add (c.get(0)); //add depot
                    routeCost = computeRouteCost(linkedList);
                    vehicleList.add(new Vehicle(linkedList, routeCost,currentLoad));
                }
                tourCost+=routeCost;

            }
        }

        //display output
        System.out.println("Basic Simulation Tour" + "\nTour Cost: " + tourCost);
        displayVehicle(vehicleList);
    }

    public boolean completeVisited(){
        for (int i = 1; i < c.size(); i++) {
            if(!((Customer)c.get(i)).wasVisited) return false;
        }
        return true;
    }

    public double computeRouteCost(LinkedList<Location> linkedList){
        //if linked list start and end with zero, we just need the for loop to calculate all routeCost
        //Eg: 0 -> 4 -> 1 -> 0
        // routeCost+=adjMatrix[x][y];
        double routeCost=0;
        //routeCost+=adjMatrix[0][linkedList.getFirst().id]; //starting from depot

        for (int i = 0; i < linkedList.size()-1; i++) {
            int x= linkedList.get(i).id;
            int y= linkedList.get(i+1).id;
            routeCost+=adjMatrix[x][y];//travel until last destination

        }
        //routeCost+=adjMatrix[linkedList.get(linkedList.size()-1).id][0];  // go back to depot
        return routeCost;
    }

    public void displayVehicle(ArrayList<Vehicle> vehicleList){
        for (int i = 1; i <= vehicleList.size(); i++) {
            System.out.println("Vehicle " + i);
            System.out.println(vehicleList.get(i-1));
        }
    }

    //Jowen's working on...
    public void greedySearch(){
        int i=0;
        while(!completeVisited()){  //if true , greedyList has been generated, can terminate
            double [] array=new double[adjMatrix.length]; //to compare their distance from i node
            for (int j = 0; j < adjMatrix.length; j++) {
                array[j]=adjMatrix[i][j];  //distance array from i
            }
            Arrays.sort(array); // to get ascending distance from i


        }

    }

    public void MCTSearch(){

    }

}
