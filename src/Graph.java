import java.util.*;

public class Graph {

    double adjMatrix[][];
    int numOfVehicles;
    Depot d;
    List<Location> c;  //depot at index 0 , customer continue 1-4
    double dist;
    Queue<Integer> q;
    LinkedList<Location> linkedList = new LinkedList<>();
    ArrayList<Vehicle> vehicleList = new ArrayList<>();
    double routeCost;
    double tourCost;

    public Graph(List<Location> c){
        this.c = c;
        d=(Depot)c.get(0);
        numOfVehicles = 0;
        q = new LinkedList<>();
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

    public ArrayList<Vehicle> bfs2(){
        List<Integer> adjList[]= new LinkedList[c.size()-1];  //adjacency list as different route combination
        double [] cost=new double[c.size()-1];      //cost of the adjList[i] route
        int [] accumulatedSize=new int[c.size()-1];

        boolean [][] visited = new boolean[c.size()-1][c.size()-1];
        //store visited customer for every customer
        //2D array because for each adjList, every customer must be visited

        for (int i = 0; i < adjList.length; i++) {
            adjList[i]=new LinkedList<>();
            adjList[i].add(0);   //every route will start from depot
        }

        for (int i = 0; i < adjList.length; i++) { //bfs first level
            adjList[i].add(i + 1);  //add every possible customer as first visit
            cost[i] += adjMatrix[0][i + 1];  //depot to first customer
            accumulatedSize[i]+=c.get(i+1).demandSize;
            visited[i][i]=true;  //first node visited
        }
        while(!completeArrayVisited(visited)) {  //everytime start this line means a new level of bfs
            int x;

            for (int i = 0; i < adjList.length; i++) {
                x=adjList[i].get(adjList[i].size()-1);  //get last element of list
                int minNode=0;
                double min=Double.POSITIVE_INFINITY;
                for (int j = 1; j < adjMatrix.length; j++) {  //check distance to adjacent node of customer i
                    if(visited[i][j-1])  //visited node in the list
                        continue;
                    if(adjMatrix[x][j]<min && accumulatedSize[i]+c.get(j).demandSize<= d.maximumCapacity ) {
                        min = adjMatrix[x][j];  //update the route with cheapest cost
                        minNode=j;
                    }

                }
                if(minNode!=0 ) {  //there exists a customer demand size that still can be add to the vehicle
                    cost[i] += min;
                    adjList[i].add(minNode);
                    accumulatedSize[i] += c.get(minNode).demandSize;
                    visited[i][minNode-1]=true;
                }
                else{  //no customer demand size can fit in the vehicle anymore, need new vehicle
                    accumulatedSize[i]=0;
                    cost[i] += adjMatrix[adjList[i].get(adjList[i].size()-1)][0]; //go back to depot
                    adjList[i].add(0);   //act as ending for previous route and also starting of next vehicle

                }
            }
            if(completeArrayVisited(visited)) {  //every customer visited, now return to depot and terminate
                for (int i = 0; i < adjList.length; i++) {
                    if(adjList[i].get(adjList[i].size() - 1)!=0) {
                        cost[i] += adjMatrix[adjList[i].get(adjList[i].size() - 1)][0];
                        adjList[i].add(0);
                    }
                }
                break;
            }
        }


        //System.out.println(Arrays.toString(adjList));
        //System.out.println(Arrays.toString(cost));
        double minCost=Double.POSITIVE_INFINITY;
        int minIndex=0;
        for (int i = 0; i < adjList.length; i++) {
            if(cost[i]<minCost){
                minCost=cost[i];
                minIndex=i;
            }
        }

        //System.out.println("Path = " + adjList[minIndex].toString());
        System.out.println("Basic Simulation Tour" + "\nTour Cost: " + minCost);
        int index=1;
            linkedList.clear();
            int  currentLoad=0;
            linkedList.add(c.get(0));
            for (int i = index; i < adjList[minIndex].size(); i++) {  //separate the minCost path into different vehicle
                if(adjList[minIndex].get(i) != 0) {
                    linkedList.add(c.get(adjList[minIndex].get(i)));
                    currentLoad+=c.get(adjList[minIndex].get(i)).demandSize;
                    continue;
                }
                index=i;
                linkedList.add(c.get(0));
                routeCost = computeRouteCost(linkedList);
                vehicleList.add(new Vehicle(linkedList, routeCost,currentLoad));
                linkedList.clear();
                currentLoad=0;
                linkedList.add(c.get(0));
            }


        return vehicleList;


    }

    public boolean completeArrayVisited(boolean [] [] visited){
        for (int i = 0; i < visited.length; i++){
            for(int j = 0; j <visited[0].length; j++) {
                if (!visited[i][j])
                    return false; //still have unvisited node

            }
        }
        return true;
    }

    public void bfs() {
        double shortestPath;
        int currentLoad;
        tourCost=0;

        q.add(1); //start from 1 , because Customer class start from index 1
        int v1=q.peek();//take the first customer
        int v2; //check the nodes adjacent to it, since this is a complete undirected graph, the nodes adjacent to customer 1 is 2,3,4
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
                    if (!cus.wasVisited && (currentLoad + cus.demandSize) <= d.maximumCapacity) {  //d.demandSize= depot MaximumCapacity
                        shortestPath = adjMatrix[v2][i];
                        temp = i;
                        break;
                    }
                }
                for (int k = 1; k < c.size(); k++) { //find the shortest path
                    Customer cus=(Customer) c.get(k);
                    if (!cus.wasVisited && (currentLoad + cus.demandSize) <= d.maximumCapacity) {
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

    public boolean completeVisited(){  //only checking is customer is visited , depot is excluded
        for (int i = 1; i < c.size(); i++) {
            if(!((Customer)c.get(i)).wasVisited) return false;
        }
        return true;
    }

    public double computeRouteCost(LinkedList<Location> linkedList){
        double routeCost=0;

        for (int i = 0; i < linkedList.size()-1; i++) {
            int x= linkedList.get(i).id;
            int y= linkedList.get(i+1).id;
            routeCost+=adjMatrix[x][y];
        }
      return routeCost;
    }

    public void displayVehicle(ArrayList<Vehicle> vehicleList){
        for (int i = 1; i <= vehicleList.size(); i++) {
            System.out.println("Vehicle " + i);
            System.out.println(vehicleList.get(i-1));
        }
    }

    public ArrayList<Vehicle> greedySearch(){
        int currentLoad;
        tourCost=0;
        ArrayList<Location> greedyList=new ArrayList<>();

        for (int i = 1; i < c.size(); i++) { //resetting all to false to create connections later
            ((Customer)(c.get(i))).wasVisited=false;
        }

        int i=0;  //start from depot
        while(!completeVisited()) {  //if true , greedyList has been generated, can terminate
            //double [] array=new double[adjMatrix.length];//to compare their distance from i node
            double shortest = Double.POSITIVE_INFINITY;
            Customer tempCustomer = (Customer) c.get(1);
            for (int j = 1; j < adjMatrix.length; j++) { //to get shortest distance
                Customer cus = (Customer) c.get(j);
                if (i == j)
                    continue;
                if(cus.wasVisited)
                    continue;
                if (adjMatrix[i][j] < shortest) { //greedy search only consider shortest distance between two node
                    shortest = adjMatrix[i][j];
                    tempCustomer = cus;
                }

            }
            shortest=Double.POSITIVE_INFINITY;
            greedyList.add(tempCustomer);
            tempCustomer.wasVisited=true;

            i = tempCustomer.id;
        }

        for ( i = 1; i < c.size(); i++) { //resetting all to false to create connections later
            ((Customer)(c.get(i))).wasVisited=false;
        }
        /*for ( i = 0; i < greedyList.size(); i++) {
            System.out.print(greedyList.get(i).id+" ");
        }*/
        tourCost=vehicleDistribution(greedyList);
        //display output
        System.out.println("Greedy Simulation Tour" + "\nTour Cost: " + tourCost);
        return vehicleList;
    }

    public ArrayList<Vehicle>  bestFirstSearch() {
        tourCost = 0;
        ArrayList<Integer> closed = new ArrayList<>();  //a list storing visited by not yet expand node
        ArrayList<Location> open = new ArrayList<>();   //a list storing visited and expanded node
        List<Double> distanceFromDepot= new ArrayList<>();  //a list storing straight line distance
        closed.add(0);
        for (int i = 1; i < adjMatrix[0].length; i++) {
            distanceFromDepot.add(adjMatrix[0][i]);
        }
       Collections.sort(distanceFromDepot);

        for (int i = 0; i < distanceFromDepot.size(); i++) {  //list that store location according to h(n)-> distance to goal node
            for (int j = 1; j < adjMatrix[0].length; j++) {
                if(adjMatrix[0][j]== distanceFromDepot.get(i)) {
                    closed.add(j);
                    break;
                }
            }
        }
        open.add(c.get(closed.get(0))); //start exploring with first node in open
        closed.remove(0);

        //f(n)=h(n)+g(n)
       while(!closed.isEmpty()){ //must travel until closed list is all visited
            List<Double> heuristicFunction=new ArrayList<>(); //first copy the distance to goal(depot)
            Location current=open.get(open.size()-1);
            int currentID=current.id;
            double min=Double.POSITIVE_INFINITY;
            int minIndex=0;
            for (int i = 0; i < distanceFromDepot.size(); i++) {
                double g= adjMatrix[currentID][closed.get(i)]+distanceFromDepot.get(i);   //distance from current node to next node
                heuristicFunction.add(g); //set the heuristic function
            }
            for (int i = 0; i < heuristicFunction.size(); i++) {
                if(heuristicFunction.get(i)<min){
                    min=heuristicFunction.get(i);
                    minIndex=i;
               }
            }
            open.add(c.get(closed.get(minIndex)));
            closed.remove(minIndex);
            distanceFromDepot.remove(minIndex);
        }
        open.remove(0);
       /* for (int i = 0; i < open.size(); i++) {
            System.out.print(open.get(i).id+" ");
        }*/

        tourCost=vehicleDistribution(open);
        //display output
        System.out.println("Best First Search Simulation Tour" + "\nTour Cost: " + tourCost);
        return vehicleList;

        //jowen
        //-----
        //boon
        /*
        from what i understand,
        if your code is sth like this:
            expected output:
                0 -> 1 -> 4 -> 0
                0 -> 2 -> 3 -> 0
            AND this method send this [1, 4, 2, 3] into vehicleDistribution method
        then my code correct liao lo, yayy
        */

        /*tourCost = 0;
        ArrayList<Location> closed = new ArrayList<>(); //a list storing visited by not yet expand node
        ArrayList<Location> open = new ArrayList<>(); //a list storing visited and expanded node
        List<Double> h = new ArrayList<>(); //a list storing straight line distance from current node to goal node, h(n)
        for (int i = 0; i < adjMatrix[0].length; i++) {
            h.add(adjMatrix[0][i]);
        }
        for (int i = 0; i < h.size(); i++) {  //list that store location, according to h(n)
            closed.add(c.get(i));
        }

        open.add(closed.remove(0)); //start exploring with first node in open
        h.remove(0);
        //f(n) = h(n)
        int currentRouteCapacity = 0;
        while (!closed.isEmpty()) {
            double hMIN = Double.POSITIVE_INFINITY;
            int closedID = -1; //-1 means nextStop not found
            for (int i = 0; i < closed.size(); i++) { //to find best possible nextStop
                //condition 1: lowest h(n) among latest list of closed
                //condition 2: if add this nextStop does not exceed maximumCapacity
                if (h.get(i) < hMIN && currentRouteCapacity + closed.get(i).demandSize <= d.maximumCapacity) {
                    hMIN = h.get(i);
                    closedID = i;
                }
            }
            if (closedID != -1) {
                currentRouteCapacity += closed.get(closedID).demandSize;
                open.add(closed.remove(closedID));
                h.remove(closedID);
            }
            else { //new route, reset data of route
                currentRouteCapacity = 0;
            }
        }
        open.remove(0);

        tourCost=vehicleDistribution(open);
        //display output
        System.out.println("Best First Search Simulation Tour" + "\nTour Cost: " + tourCost);
        return vehicleList;*/
    }

    public ArrayList<Vehicle>  AStarSearch() {
//        tourCost = 0;
//        ArrayList<Location> closed = new ArrayList<>();  //a list storing visited by not yet expand node
//        ArrayList<Location> open = new ArrayList<>();   //a list storing visited and expanded node
//        List<Double> g= new ArrayList<>();  //a list storing straight line distance, h(n)
//        for (int i = 0; i < adjMatrix[0].length; i++) {
//            g.add(adjMatrix[0][i]);
//        }
//        for (int i = 0; i < g.size(); i++) {  //list that store location according to h(n)-> distance to goal node
//            closed.add(c.get(i));
//        }
//
//        open.add(closed.remove(0)); //start exploring with first node in open
//        double startToCurrent=0;
//        //f(n)=h(n)+g(n)
//        while(!closed.isEmpty()){ //must travel until closed list is all visited
//            List<Double> f=new ArrayList<>(); //first copy the distance to goal(depot)
//            Location current=open.get(open.size()-1); //expand the node chosen
//            int currentID=current.id;
//            double min=Double.POSITIVE_INFINITY;
//            int minIndex=0;
//            for (int i = 1; i <=closed.size(); i++) {
//                double h= startToCurrent+ adjMatrix[currentID][closed.get(i-1).id]+ g.get(i);   //distance from current node to next node
//                f.add(h); //set the heuristic function
//            }
//            for (int i = 0; i < f.size(); i++) {
//                if(f.get(i)<min){
//                    min=f.get(i);
//                    minIndex=i;
//                }
//            }
//            startToCurrent+=adjMatrix[currentID][closed.get(minIndex).id];
//            open.add(closed.remove(minIndex));//the node is being visited and expanded, remove from closed
//            g.remove(minIndex);
//        }
//        open.remove(0);
//        /*for (int i = 0; i < open.size(); i++) {
//            System.out.print(open.get(i).id+" ");
//        }*/
//
//        tourCost=vehicleDistribution(open);
//        //display output
//        System.out.println("A Star Search Simulation Tour" + "\nTour Cost: " + tourCost);
//        return vehicleList;

        //jowen
        //----------
        //boon

        tourCost = 0;
        ArrayList<Location> open = new ArrayList<>(); //keeps all nodes that are discovered but not yet expanded
//        ArrayList<Location> closed = new ArrayList<>(); //keeps all nodes that are expanded
        ArrayList<Location> result = new ArrayList<>();
        //store c in open
        for (int i = 0; i<c.size(); i++) {
            open.add(c.get(i));
        }

        //f(n) = g(n) + h(n)
        //g(n) = actual cost from start to current
        //h(n) = estimated cost from current to goal in straight line
        double f = 0;
        double g = 0;
        ArrayList<Double> h = new ArrayList<>(); //list that keeps h(n) values of all nodes
        //store values of h(n)
        for (int i = 0; i< open.size(); i++) {
            h.add(adjMatrix[i][0]);
        }

        result.add(open.remove(0)); //add depot to start
        h.remove(0);
        Location currentStop = result.get(result.size() - 1);

        int maxCapacity = d.maximumCapacity;
        int currentRouteCapacity = 0;

        while (!open.isEmpty()) {
            double fMIN = Double.POSITIVE_INFINITY;

            int openID = -1; //-1 means nextStop not found
            double gTemp = g + 0; //temporary g(n) until nextStop
            for (int i = 0; i<open.size(); i++) { //to find best possible nextStop
                Location nextStop = open.get(i);
                gTemp = g + adjMatrix[currentStop.id][nextStop.id];
                f = gTemp + h.get(i);

                //condition 1: lowest f(n) among latest list of open
                //condition 2: if add this nextStop does not exceed maximumCapacity
                if (f < fMIN && currentRouteCapacity + open.get(i).demandSize <= maxCapacity) {
                    fMIN = f;
                    openID = i;
                }
            }
            if (openID != -1) {
                result.add(open.remove(openID));
                h.remove(openID);
                currentStop = result.get(result.size() - 1); //refresh currentStop
                currentRouteCapacity += result.get(result.size() - 1).demandSize;
                g = gTemp; //update g(n) until nextStop
            }
            else { //new route, reset data of route
                currentStop = result.get(0); //restart currentStop at depot, but not added into (ArrayList) result
                currentRouteCapacity = 0;
                f = 0;
                g = 0;
            }
        }
        result.remove(0);

        tourCost=vehicleDistribution(result);
        //display output
        System.out.println("A* Star Search Simulation Tour" + "\nTour Cost: " + tourCost);
        return vehicleList;
    }

    public double vehicleDistribution(ArrayList<Location> list){
        tourCost=0;
        linkedList.clear();
        vehicleList.clear();

        for (int i = 1; i < c.size(); i++) { //resetting all to false to create connections later
            ((Customer)(c.get(i))).wasVisited=false;
        }
        int i=0;
        while(!completeVisited()) {  //for loop to form different combination of customer , makesure all customer are visited
            //suppose is while there are no node unvisited
            linkedList.clear();
            int currentLoad = 0;
            double tempShortestNode=adjMatrix[0][0];  //distance depot to depot = zero
            int tempShortestNodeIndex=0;
            while (i < list.size()) {
                //traverse through greedy list to end to check if there if any combination can be formed, which capacity won't be overload
                if (currentLoad + list.get(i).demandSize<= d.maximumCapacity  && !((Customer)c.get(i+1)).wasVisited) {
                    double shortestFirstNode = adjMatrix[0][list.get(i).id];
                    if (shortestFirstNode < tempShortestNode) {
                        linkedList.add(tempShortestNodeIndex, list.get(i));
                    } else if (linkedList.size() >= 2) {
                        linkedList.add(tempShortestNodeIndex + 1, list.get(i));
                    } else
                        linkedList.add(list.get(i)); //add Customer at the end , but we need to formed the route starting from location closest to the depot
                    ((Customer)c.get(i+1)).wasVisited=true;
                    tempShortestNode = shortestFirstNode;
                    tempShortestNodeIndex = linkedList.indexOf(list.get(i));
                    currentLoad += list.get(i).demandSize;
                }
                i++;
            }

            linkedList.addFirst(c.get(0)); //add depot
            linkedList.add(c.get(0));//complete the path with return to depot
            routeCost = computeRouteCost(linkedList);
            vehicleList.add(new Vehicle(linkedList, routeCost,currentLoad));

            tourCost+=routeCost;
            linkedList.clear();
            i=0;
        }
        return tourCost;
    }

    public void MCTSearch(){

    }

}
