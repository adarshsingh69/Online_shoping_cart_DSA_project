import java.io.*;
import java.util.*;

public class OnlineShoppingCartSystem {

    // Product class
    static class Product {
        String id;
        String name;
        int price;

        Product(String id, String name, int price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }
    }

    // Cart Node for custom Linked List
    static class CartNode {
        Product product;
        int quantity;
        CartNode next;

        CartNode(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }
    }

    // Products stored in HashMap
    static HashMap<String, Product> products = new HashMap<>();

    // Custom Linked List cart
    static CartNode head = null;

    // Stack to remove last added item
    static Stack<CartNode> addedItems = new Stack<>();

    static Scanner sc = new Scanner(System.in);

    // Load products from products.txt
    static void loadProducts() {
        try {
            File file = new File("products.txt");

            // Create default file if it does not exist
            if (!file.exists()) {
                PrintWriter writer = new PrintWriter(file);
                writer.println("P101,Notebook,50");
                writer.println("P102,Pen,10");
                writer.println("P103,Bag,800");
                writer.close();
            }

            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {
                String[] data = fileScanner.nextLine().split(",");

                if (data.length == 3) {
                    products.put(data[0],
                            new Product(data[0], data[1],
                                    Integer.parseInt(data[2])));
                }
            }

            fileScanner.close();

        } catch (Exception e) {
            System.out.println("Error loading products.");
        }
    }

    // View products
    static void viewProducts() {
        System.out.println("\nAvailable Products:");

        for (Product p : products.values()) {
            System.out.println(p.id + " - " + p.name + " - " + p.price);
        }
    }

    // Add product to cart
    static void addToCart() {
        System.out.print("Enter Product ID: ");
        String id = sc.nextLine().toUpperCase();

        Product product = products.get(id);

        if (product == null) {
            System.out.println("Product " + id + " not found.");
            return;
        }

        System.out.print("Enter Quantity: ");

        int quantity;

        try {
            quantity = Integer.parseInt(sc.nextLine());

            if (quantity <= 0) {
                System.out.println("Quantity must be a positive whole number.");
                return;
            }

        } catch (Exception e) {
            System.out.println("Quantity must be a positive whole number.");
            return;
        }

        CartNode newNode = new CartNode(product, quantity);

        // Add to custom Linked List
        if (head == null) {
            head = newNode;
        } else {
            CartNode temp = head;

            while (temp.next != null) {
                temp = temp.next;
            }

            temp.next = newNode;
        }

        // Push last added item into Stack
        addedItems.push(newNode);

        System.out.println("Added: " + product.name + " x" + quantity
                + " = " + (product.price * quantity));
    }

    // View cart
    static void viewCart() {
        if (head == null) {
            System.out.println("Your cart is empty.");
            System.out.println("Total: 0");
            return;
        }

        CartNode temp = head;
        int total = 0;

        System.out.println("\nYour Cart:");

        while (temp != null) {
            int itemTotal = temp.product.price * temp.quantity;

            System.out.println(temp.product.name + " x" + temp.quantity
                    + " = " + itemTotal);

            total += itemTotal;
            temp = temp.next;
        }

        System.out.println("Total: " + total);
    }

    // Remove last added item using Stack
    static void removeLast() {
        if (addedItems.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        CartNode lastAdded = addedItems.pop();

        if (head == lastAdded) {
            head = null;
        } else {
            CartNode temp = head;

            while (temp.next != lastAdded) {
                temp = temp.next;
            }

            temp.next = lastAdded.next;
        }

        System.out.println("Removed: " + lastAdded.product.name);
    }

    // Sort cart by item total - high to low
    static void sortCart() {
        if (head == null || head.next == null) {
            System.out.println("Cart sorted.");
            return;
        }

        for (CartNode i = head; i != null; i = i.next) {
            for (CartNode j = i.next; j != null; j = j.next) {

                int total1 = i.product.price * i.quantity;
                int total2 = j.product.price * j.quantity;

                if (total1 < total2) {
                    Product tempProduct = i.product;
                    int tempQuantity = i.quantity;

                    i.product = j.product;
                    i.quantity = j.quantity;

                    j.product = tempProduct;
                    j.quantity = tempQuantity;
                }
            }
        }

        System.out.println("Cart sorted by item total (high to low).");
    }

    // Checkout
    static void checkout() {
        if (head == null) {
            System.out.println("Your cart is empty.");
            return;
        }

        int total = 0;
        CartNode temp = head;

        while (temp != null) {
            total += temp.product.price * temp.quantity;
            temp = temp.next;
        }

        System.out.println("Order placed. Total: " + total);

        // Clear cart after checkout
        head = null;
        addedItems.clear();
    }

    // Save products on exit
    static void saveProducts() {
        try {
            PrintWriter writer = new PrintWriter("products.txt");

            for (Product p : products.values()) {
                writer.println(p.id + "," + p.name + "," + p.price);
            }

            writer.close();

        } catch (Exception e) {
            System.out.println("Error saving products.");
        }
    }

    public static void main(String[] args) {

        loadProducts();

        int choice;

        do {
            System.out.println("\n--- Online Shopping Cart System ---");
            System.out.println("1. View Products");
            System.out.println("2. Add to Cart");
            System.out.println("3. View Cart");
            System.out.println("4. Remove Last Added Item");
            System.out.println("5. Sort by Item Total (High to Low)");
            System.out.println("6. Checkout");
            System.out.println("7. Exit");

            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                choice = 0;
            }

            switch (choice) {
                case 1:
                    viewProducts();
                    break;

                case 2:
                    addToCart();
                    break;

                case 3:
                    viewCart();
                    break;

                case 4:
                    removeLast();
                    break;

                case 5:
                    sortCart();
                    viewCart();
                    break;

                case 6:
                    checkout();
                    break;

                case 7:
                    saveProducts();
                    System.out.println("Thank you!");
                    break;

                default:
                    System.out.println("Invalid option.");
            }

        } while (choice != 7);

        sc.close();
    }
}