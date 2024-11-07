# Order Book System
This Java program simulates a simple order book system for managing buy and sell orders. It allows users to place orders, remove them, view active and removed orders, and see the best prices for buy and sell orders.

## Table of Contents
- [Classes](##Classes)
  - [Order](###Order)
  - [OrderBook](###OrderBook)
  - [Main](###Main)
- [Features](##Features)
- [Usage](##Usage)
- [Example Interaction](##ExampleInteraction)
  
## Classes
### Order
The Order class represents an individual order with the following fields:

id: Unique identifier for the order.
type: Type of the order ("Buy" or "Sell").
price: Price per unit of the order.
quantity: Quantity of units in the order.
The class includes a constructor to initialize an order and an overridden toString() method to display order information.

### OrderBook
The OrderBook class manages buy and sell orders in two PriorityQueues:
- buyOrders: Orders sorted by descending price to prioritize higher buy prices.
- sellOrders: Orders sorted by ascending price to prioritize lower sell prices.
#### Key Methods
- processOrder(Order order): Processes and adds new orders.
- addOrder(Order order): Adds a new buy or sell order to the appropriate queue.
- removeOrder(int orderId): Removes an order by ID, updating the removed orders list.
- removeOrderFromQueue(PriorityQueue<Order> queue, int orderId): Helper method to find and remove an order from a queue.
- displayBestPrices(): Displays the highest buy and lowest sell prices.
- printAllOrders(): Displays all active orders.
- printRemovedOrders(): Displays all removed orders.
- generateOrderId(): Generates a unique ID for each new order.
### Main
The Main class is the entry point of the program. It initializes an OrderBook and provides a menu-driven console interface for users to interact with the order book.

## Features
- Place a new order: Users can place buy or sell orders with specified price and quantity.
- Remove an order: Allows users to remove an order by its unique ID.
- View all orders: Displays all current active orders in the system.
- View removed orders: Shows a list of all removed orders.
- Display best prices: Automatically shows the best buy and sell prices whenever an order is added or removed.
## Usage
1. Clone or download this repository.
2. Compile and run the Main class.
```
javac Main.java
java Main
```
## Example Interaction
```
Welcome to the Order Book System!

Choose an action (1-5):
1 Place a new order
2 Remove an order by Id
3 View all orders
4 View removed orders
5 Exit
Your choice: 1

Order Type (Buy/Sell): Buy
Price: 22.50
Quantity: 150
Added Buy Order: Id: 6, Order: Buy, Price: 22.50, Quantity: 150

Current Best Prices:
Best Buy Order: Id: 6, Order: Buy, Price: 22.50, Quantity: 150
Best Sell Order: Id: 2, Order: Sell, Price: 25.00, Quantity: 200

Choose an action (1-5):
Your choice: 3

--- All Orders ---
Order Id  Order      Price      Quantity
001       Buy        20.00      100
002       Sell       25.00      200
006       Buy        22.50      150

Choose an action (1-5):
Your choice: 5
Exiting Order Book System.
```
