package com.ecommerce;

import com.ecommerce.model.*;
import com.ecommerce.notification.*;
import com.ecommerce.order.*;
import com.ecommerce.payment.*;
import com.ecommerce.service.*;
import com.ecommerce.storage.*;
import java.io.File;
import java.util.*;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("--------------------------------------------");
        System.out.println("  E-COMMERCE ORDER SYSTEM");
        System.out.println("   Vikas@kumar@engineer");
        System.out.println("--------------------------------------------\n");

        // Fixed: use File.separator so it works on Windows, Mac, and Linux
        String desktopPath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "ECommerceOrders";
        IOrderRepository orderRepository = new FileOrderRepository(desktopPath);

        boolean continueShopping = true;

        while (continueShopping) {
            processNewOrder(orderRepository);

            System.out.print("\nPlace another order? (yes/no): ");
            String choice = scanner.nextLine().toLowerCase();
            continueShopping = choice.equals("yes") || choice.equals("y");
        }

        System.out.println("\n--------------------------------------------");
        System.out.println("  THANK YOU FOR SHOPPING WITH US!");
        System.out.println("--------------------------------------------");

        scanner.close();
    }

    private static void processNewOrder(IOrderRepository orderRepository) {
        System.out.println("\n--- NEW ORDER ---\n");

        System.out.print("Enter Customer ID: ");
        String customerId = scanner.nextLine();

        System.out.print("Enter Customer Email: ");
        String email = scanner.nextLine();

        System.out.print("Enter Customer Phone: ");
        String phone = scanner.nextLine();

        OrderType orderType = selectOrderType();

        Map<OrderType, IOrderProcessor> orderProcessors = new HashMap<>();
        orderProcessors.put(OrderType.REGULAR, new RegularOrderProcessor());
        orderProcessors.put(OrderType.DISCOUNTED, new DiscountedOrderProcessor());
        orderProcessors.put(OrderType.PRIORITY, new PriorityOrderProcessor());

        List<OrderItem> items = addItems();

        if (items.isEmpty()) {
            System.out.println("Order cancelled.");
            return;
        }

        NotificationTemplateService templateService = new NotificationTemplateService();
        NotificationService notificationService = new NotificationService(
                new ArrayList<>(), templateService);

        OrderService orderService = new OrderService(
                orderRepository, notificationService, orderProcessors);

        OrderRequest request = new OrderRequest(
                customerId, items, orderType, email, phone, "device-" + customerId);

        OrderResult result = orderService.createOrder(request);

        if (!result.isSuccess()) {
            System.out.println("Order failed: " + result.getMessage());
            return;
        }

        System.out.println("\n✅ Order Created Successfully!");
        System.out.println("Order ID: " + result.getOrder().getOrderId());
        System.out.println("Order Type: " + result.getOrder().getOrderType());
        System.out.println("Total Amount: $" + result.getOrder().getTotalAmount());

        boolean paymentSuccess = processPayment(orderService, result.getOrder().getOrderId());

        if (paymentSuccess) {
            System.out.println("\n🎉 ORDER COMPLETED SUCCESSFULLY!");
            sendOrderNotification(result.getOrder(), email, phone, "device-" + customerId);
        }
    }

    private static void sendOrderNotification(Order order, String email, String phone, String deviceToken) {
        System.out.println("\n--- Send Order Confirmation ---");
        System.out.println("1. SMS Only");
        System.out.println("2. Email Only");
        System.out.println("3. Push Notification Only");
        System.out.println("4. SMS + Email");
        System.out.println("5. SMS + Push");
        System.out.println("6. Email + Push");
        System.out.println("7. All (SMS + Email + Push)");
        System.out.println("8. No Notification");
        System.out.print("Enter choice (1-8): ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        List<INotificationSender> selectedSenders = new ArrayList<>();
        String selectedChannels = "";

        switch (choice) {
            case 1:
                selectedSenders.add(new SMSNotification());
                selectedChannels = "SMS";
                break;
            case 2:
                selectedSenders.add(new EmailNotification());
                selectedChannels = "Email";
                break;
            case 3:
                selectedSenders.add(new PushNotification());
                selectedChannels = "Push Notification";
                break;
            case 4:
                selectedSenders.add(new SMSNotification());
                selectedSenders.add(new EmailNotification());
                selectedChannels = "SMS + Email";
                break;
            case 5:
                selectedSenders.add(new SMSNotification());
                selectedSenders.add(new PushNotification());
                selectedChannels = "SMS + Push";
                break;
            case 6:
                selectedSenders.add(new EmailNotification());
                selectedSenders.add(new PushNotification());
                selectedChannels = "Email + Push";
                break;
            case 7:
                selectedSenders.add(new EmailNotification());
                selectedSenders.add(new SMSNotification());
                selectedSenders.add(new PushNotification());
                selectedChannels = "SMS + Email + Push";
                break;
            case 8:
                System.out.println("No notification sent.");
                return;
            default:
                selectedSenders.add(new SMSNotification());
                selectedChannels = "SMS (default)";
        }

        NotificationTemplateService templateService = new NotificationTemplateService();

        for (INotificationSender sender : selectedSenders) {
            if (sender.isAvailable()) {
                String recipient = "";
                if (sender instanceof EmailNotification) recipient = email;
                if (sender instanceof SMSNotification) recipient = phone;
                if (sender instanceof PushNotification) recipient = deviceToken;

                Map<String, String> templateData = new HashMap<>();
                templateData.put("orderId", order.getOrderId());
                templateData.put("amount", String.valueOf(order.getTotalAmount()));
                templateData.put("orderType", order.getOrderType().toString());

                String message = templateService.getFormattedMessage("order_confirmation", templateData);
                sender.sendNotification(recipient, "Order Confirmed", message);
            }
        }

        System.out.println("✅ Confirmation sent via: " + selectedChannels);
    }

    private static OrderType selectOrderType() {
        System.out.println("\n--- Select Order Type ---");
        System.out.println("1. Regular Order");
        System.out.println("2. Discounted Order (10% off)");
        System.out.println("3. Priority Order (10% extra)");
        System.out.print("Enter choice (1-3): ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 2: return OrderType.DISCOUNTED;
            case 3: return OrderType.PRIORITY;
            default: return OrderType.REGULAR;
        }
    }

    private static List<OrderItem> addItems() {
        List<OrderItem> items = new ArrayList<>();
        Map<String, Product> availableProducts = getAvailableProducts();

        System.out.println("\n--- Available Products ---");
        for (Product p : availableProducts.values()) {
            System.out.println(p);
        }

        boolean addingItems = true;

        while (addingItems) {
            System.out.print("\nEnter Product ID (or 'done' to finish): ");
            String productId = scanner.nextLine();

            if (productId.equalsIgnoreCase("done")) {
                addingItems = false;
                continue;
            }

            Product product = availableProducts.get(productId);
            if (product == null) {
                System.out.println("Invalid Product ID!");
                continue;
            }

            System.out.print("Enter quantity: ");
            int quantity = scanner.nextInt();
            scanner.nextLine();

            items.add(new OrderItem(product.id, product.name, product.price, quantity));
            System.out.println("Added: " + product.name + " x" + quantity);
        }

        return items;
    }

    private static Map<String, Product> getAvailableProducts() {
        Map<String, Product> products = new HashMap<>();
        products.put("P001", new Product("P001", "Laptop", 999.99));
        products.put("P002", new Product("P002", "Mouse", 29.99));
        products.put("P003", new Product("P003", "Keyboard", 59.99));
        products.put("P004", new Product("P004", "Monitor", 299.99));
        products.put("P005", new Product("P005", "Headphones", 149.99));
        products.put("P006", new Product("P006", "USB Cable", 9.99));
        products.put("P007", new Product("P007", "Smartphone", 699.99));
        products.put("P008", new Product("P008", "Tablet", 399.99));
        return products;
    }

    private static boolean processPayment(OrderService orderService, String orderId) {
        System.out.println("\n--- Payment ---");
        System.out.println("1. Credit Card");
        System.out.println("2. UPI");
        System.out.println("3. Wallet");
        System.out.print("Enter choice (1-3): ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        IPaymentMethod paymentMethod;
        Map<String, String> details = new HashMap<>();

        switch (choice) {
            case 1:
                paymentMethod = new CreditCardPayment();
                System.out.print("Card Number (16 digits): ");
                details.put("cardNumber", scanner.nextLine());
                System.out.print("CVV (3 digits): ");
                details.put("cvv", scanner.nextLine());
                System.out.print("Expiry (MM/YY): ");
                details.put("expiryDate", scanner.nextLine());
                break;

            case 2:
                paymentMethod = new UPIPayment();
                System.out.print("UPI ID: ");
                details.put("upiId", scanner.nextLine());
                break;

            case 3:
                paymentMethod = new WalletPayment();
                System.out.print("Wallet ID: ");
                details.put("walletId", scanner.nextLine());
                break;

            default:
                paymentMethod = new CreditCardPayment();
                details.put("cardNumber", "1234567890123456");
                details.put("cvv", "123");
                details.put("expiryDate", "12/25");
        }

        OrderResult paymentResult = orderService.processOrderPayment(
                orderId, paymentMethod, new PaymentDetails(details));

        if (paymentResult.isSuccess()) {
            System.out.println("\n✅ Payment Successful!");
            System.out.println("Transaction ID: " + paymentResult.getPaymentResult().getTransactionId());
            return true;
        } else {
            System.out.println("\n❌ Payment Failed!");
            return false;
        }
    }
}

class Product {
    String id;
    String name;
    double price;

    Product(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return id + " - " + name + " ($" + price + ")";
    }
}
