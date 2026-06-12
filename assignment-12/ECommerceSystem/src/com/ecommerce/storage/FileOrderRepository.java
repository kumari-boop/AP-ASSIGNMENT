package com.ecommerce.storage;

import com.ecommerce.model.Order;
import com.ecommerce.model.OrderItem;
import com.ecommerce.model.OrderStatus;
import com.ecommerce.model.OrderType;
import java.io.*;
import java.util.*;

public class FileOrderRepository implements IOrderRepository {
    private final String folderPath;
    // Memory cache to find orders quickly
    private final Map<String, Order> cache;

    public FileOrderRepository(String folderPath) {
        this.folderPath = folderPath;
        this.cache = new HashMap<>();

        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        System.out.println("✅ File Storage Ready: " + folderPath);
    }

    @Override
    public void save(Order order) {
        try {
            // Fixed: use File.separator instead of hardcoded "\\" so it works on all OS
            File file = new File(folderPath + File.separator + order.getOrderId() + ".txt");
            PrintWriter writer = new PrintWriter(file);

            writer.println("========== ORDER DETAILS ==========");
            writer.println("Order ID: " + order.getOrderId());
            writer.println("Customer ID: " + order.getCustomerId());
            writer.println("Order Type: " + order.getOrderType());
            writer.println("Status: " + order.getStatus());
            writer.println("Date: " + order.getCreatedAt());
            writer.println("Total Amount: $" + order.getTotalAmount());
            writer.println("");
            writer.println("--- Items ---");
            for (OrderItem item : order.getItems()) {
                writer.println(item.getProductName() + " x" + item.getQuantity() +
                        " - $" + (item.getPrice() * item.getQuantity()));
            }
            writer.println("===================================");
            writer.close();

            // Also save in cache
            cache.put(order.getOrderId(), order);

            System.out.println("📁 Order saved to: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("❌ Save Error: " + e.getMessage());
        }
    }

    @Override
    public Optional<Order> findById(String orderId) {
        // Check cache first
        if (cache.containsKey(orderId)) {
            System.out.println("📂 Order found in cache: " + orderId);
            return Optional.of(cache.get(orderId));
        }

        // Check file
        File file = new File(folderPath + File.separator + orderId + ".txt");
        if (file.exists()) {
            System.out.println("📂 Order file found: " + file.getAbsolutePath());
            try {
                Order order = readOrderFromFile(file);
                if (order != null) {
                    cache.put(orderId, order);
                    return Optional.of(order);
                }
            } catch (Exception e) {
                System.out.println("Error reading: " + e.getMessage());
            }
        }

        System.out.println("❌ Order not found: " + orderId);
        return Optional.empty();
    }

    private Order readOrderFromFile(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String orderId = null;
            String customerId = null;
            OrderType orderType = OrderType.REGULAR;
            OrderStatus status = OrderStatus.CREATED;
            List<OrderItem> items = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Order ID: ")) {
                    orderId = line.substring(10).trim();
                } else if (line.startsWith("Customer ID: ")) {
                    customerId = line.substring(13).trim();
                } else if (line.startsWith("Order Type: ")) {
                    String type = line.substring(12).trim();
                    orderType = OrderType.valueOf(type);
                } else if (line.startsWith("Status: ")) {
                    String stat = line.substring(8).trim();
                    status = OrderStatus.valueOf(stat);
                }
            }
            reader.close();

            if (orderId != null && customerId != null) {
                return new Order(orderId, customerId, items, orderType);
            }
        } catch (Exception e) {
            System.out.println("Parse error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Order> findByCustomerId(String customerId) {
        List<Order> result = new ArrayList<>();
        for (Order order : cache.values()) {
            if (order.getCustomerId().equals(customerId)) {
                result.add(order);
            }
        }
        return result;
    }

    @Override
    public void update(Order order) {
        cache.put(order.getOrderId(), order);
        save(order);
    }

    @Override
    public void delete(String orderId) {
        cache.remove(orderId);
        File file = new File(folderPath + File.separator + orderId + ".txt");
        if (file.delete()) {
            System.out.println("🗑️ Order deleted: " + orderId);
        }
    }
}
