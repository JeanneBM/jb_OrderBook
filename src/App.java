import java.util.*;
import java.util.Collections;

class Order {
    int id;
    String type;
    double price;
    int quantity;

    public Order(int id, String type, double price, int quantity) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format("Id: %d, Order: %s, Price: %.2f, Quantity: %d", id, type, price, quantity);
    }
}

class OrderBook {
    public PriorityQueue<Order> buyOrders = new PriorityQueue<>(Comparator.comparingDouble((Order o) -> -o.price));
    public PriorityQueue<Order> sellOrders = new PriorityQueue<>(Comparator.comparingDouble(o -> o.price));
    public int orderIdCounter = 6; // Start generating order IDs from 6

    // Process a new order (just add it to the order book)
    public void processOrder(Order order) {
        if (order.quantity > 0) {
            addOrder(order);
        }
    }

    public void addOrder(Order order) {
        if (order.type.equalsIgnoreCase("Buy")) {
            buyOrders.add(order);
            System.out.println("Added Buy Order: " + order);
        } else if (order.type.equalsIgnoreCase("Sell")) {
            sellOrders.add(order);
            System.out.println("Added Sell Order: " + order);
        }
        displayBestPrices();
    }

    public void removeOrder(int orderId) {
        boolean removed = removeOrderFromQueue(buyOrders, orderId) || removeOrderFromQueue(sellOrders, orderId);
        if (removed) {
            System.out.println("Removed Order ID: " + orderId);
        } else {
            System.out.println("Order ID not found: " + orderId);
        }
        displayBestPrices();
    }

    public boolean removeOrderFromQueue(PriorityQueue<Order> queue, int orderId) {
        Iterator<Order> iterator = queue.iterator();
        while (iterator.hasNext()) {
            Order order = iterator.next();
            if (order.id == orderId) { // Use == for primitive int comparison
                iterator.remove(); // Remove the current item safely
                return true;
            }
        }
        return false;
    }

    public void displayBestPrices() {
        System.out.println("\nCurrent Best Prices:");
        System.out.println("Best Buy Order: " + (buyOrders.peek() != null ? buyOrders.peek() : "None"));
        System.out.println("Best Sell Order: " + (sellOrders.peek() != null ? sellOrders.peek() : "None"));
    }

    public void printAllOrders() {
        System.out.println("\n--- All Orders ---");
        System.out.printf("%-10s %-10s %-10s %-10s\n", "Order Id", "Order", "Price", "Quantity");

        // Combine and sort all orders by ID
        List<Order> allOrders = new ArrayList<>();
        allOrders.addAll(buyOrders);
        allOrders.addAll(sellOrders);
        allOrders.sort(Comparator.comparingInt(order -> order.id));

        if (allOrders.isEmpty()) {
            System.out.println("No orders in the order book.");
            return;
        }

        // Print each order in the sorted list
        for (Order order : allOrders) {
            System.out.printf("%03d       %-10s %-10.2f %-10d\n", order.id, order.type, order.price, order.quantity);
        }
    }

    public int generateOrderId() {
        return orderIdCounter++;  // Increment after returning the current value
    }
}

public class Main {
    public static void main(String[] args) {
        OrderBook orderBook = new OrderBook();

        // Adding Default Orders
        orderBook.addOrder(new Order(1, "Buy", 20.00, 100));
        orderBook.addOrder(new Order(2, "Sell", 25.00, 200));
        orderBook.addOrder(new Order(4, "Buy", 23.00, 70));
        orderBook.addOrder(new Order(5, "Sell", 28.00, 100));

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Order Book System!");

        while (true) {
            System.out.println("\nChoose an action (1-4):");
            System.out.println("1 Place a new order");
            System.out.println("2 Remove an order by Id");
            System.out.println("3 View all orders");
            System.out.println("4 Exit");
            System.out.print("Your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Order Type (Buy/Sell): ");
                    String type = scanner.nextLine();

                    System.out.print("Price: ");
                    double price = Double.parseDouble(scanner.nextLine());

                    System.out.print("Quantity: ");
                    int quantity = Integer.parseInt(scanner.nextLine());

                    int orderId = orderBook.generateOrderId();  // This will start from 6
                    Order order = new Order(orderId, type, price, quantity);
                    orderBook.processOrder(order);
                    break;

                case "2":
                    System.out.print("Enter the Order Id to remove: ");
                    int removeId = Integer.parseInt(scanner.nextLine()); // Convert the input to an int
                    orderBook.removeOrder(removeId);
                    break;

                case "3":
                    orderBook.printAllOrders();
                    break;

                case "4":
                    System.out.println("Exiting Order Book System.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
