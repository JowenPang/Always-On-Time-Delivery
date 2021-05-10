import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        List<Customer> list = new ArrayList<Customer>();
        try {
            Scanner input = new Scanner(new FileInputStream("test.txt"));
            int numOfCustomers = input.nextInt();
            int maxCapacity = input.nextInt();
            Depot depot = new Depot(numOfCustomers, maxCapacity);
            while(input.hasNextLine()) { //check end of text file
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
    }




}
