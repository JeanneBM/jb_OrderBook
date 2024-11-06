import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Scanner;

class Order {
    String id;
    String type;
    double price;
    int quantity;
    

    public Order(String id, String type, double price, int quantity) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format("Id: %s, Order: %s, Price: %.2f, Quantity: %d", id, type, price, quantity);
    }
}

class OrderBook {
    public PriorityQueue<Order> buyOrders = new PriorityQueue<>(Comparator.comparingDouble((Order o) -> -o.price));
    public PriorityQueue<Order> sellOrders = new PriorityQueue<>(Comparator.comparingDouble(o -> o.price));
    public int orderIdCounter = 1; // Counter to auto-generate order IDs

    // Process a new order (attempt to match or add to order book)
    public void processOrder(Order order) {
        if (order.type.equalsIgnoreCase("Buy")) {
            matchOrder(order, sellOrders, "Sell");
        } else if (order.type.equalsIgnoreCase("Sell")) {
            matchOrder(order, buyOrders, "Buy");
        } else {
            System.out.println("Invalid order type: " + order.type);
            return;
        }

        if (order.quantity > 0) {
            addOrder(order);
        }
    }

    public void matchOrder(Order order, PriorityQueue<Order> oppositeQueue, String oppositeType) {
        while (order.quantity > 0 && !oppositeQueue.isEmpty()) {
            Order bestMatch = oppositeQueue.peek();

            if ((order.type.equals("Buy") && order.price >= bestMatch.price) ||
                (order.type.equals("Sell") && order.price <= bestMatch.price)) {

                int matchedQuantity = Math.min(order.quantity, bestMatch.quantity);
                System.out.println("Matching " + order + " with " + bestMatch + " for quantity " + matchedQuantity);

                order.quantity -= matchedQuantity;
                bestMatch.quantity -= matchedQuantity;

                if (bestMatch.quantity == 0) {
                    oppositeQueue.poll();
                }
            } else {
                break;
            }
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

    public void removeOrder(String orderId) {
        boolean removed = removeOrderFromQueue(buyOrders, orderId) || removeOrderFromQueue(sellOrders, orderId);
        if (removed) {
            System.out.println("Removed Order ID: " + orderId);
        } else {
            System.out.println("Order ID not found: " + orderId);
        }
        displayBestPrices();
    }

    public boolean removeOrderFromQueue(PriorityQueue<Order> queue, String orderId) {
        Iterator<Order> iterator = queue.iterator();
        while (iterator.hasNext()) {
            Order order = iterator.next();
            if (order.id.equals(orderId)) {
                iterator.remove();
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

        if (buyOrders.isEmpty() && sellOrders.isEmpty()) {
            System.out.println("No orders in the order book.");
            return;
        }

        // Print Buy Orders
        for (Order order : buyOrders) {
            System.out.printf("%-10s %-10s %-10.2f %-10d\n", order.id, order.type, order.price, order.quantity);
        }

        // Print Sell Orders
        for (Order order : sellOrders) {
            System.out.printf("%-10s %-10s %-10.2f %-10d\n", order.id, order.type, order.price, order.quantity);
        }
    }

    public String generateOrderId() {
        return String.format("%03d", orderIdCounter++);
    }
}

public class Main {
    public static void main(String[] args) {
        OrderBook orderBook = new OrderBook();

        // Adding Default Orders
        orderBook.addOrder(new Order("001", "Buy", 20.00, 100));
        orderBook.addOrder(new Order("002", "Sell", 25.00, 200));
        orderBook.addOrder(new Order("004", "Buy", 23.00, 70));
        orderBook.addOrder(new Order("005", "Sell", 28.00, 100));
        
        
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

                    String orderId = orderBook.generateOrderId();
                    Order order = new Order(orderId, type, price, quantity);
                    orderBook.processOrder(order);
                    break;

                case "2":
                    System.out.print("Enter the Order Id to remove: ");
                    String removeId = scanner.nextLine();
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
