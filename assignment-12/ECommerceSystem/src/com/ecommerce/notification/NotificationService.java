package com.ecommerce.notification;

import com.ecommerce.model.Order;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationService {
    private final List<INotificationSender> notificationSenders;
    private final NotificationTemplateService templateService;

    public NotificationService(List<INotificationSender> notificationSenders,
                               NotificationTemplateService templateService) {
        this.notificationSenders = notificationSenders;
        this.templateService = templateService;
    }

    public void sendOrderConfirmation(Order order, String customerEmail,
                                      String customerPhone, String deviceToken) {
        Map<String, String> templateData = new HashMap<>();
        templateData.put("orderId", order.getOrderId());
        templateData.put("amount", String.valueOf(order.getTotalAmount()));
        templateData.put("orderType", order.getOrderType().toString());

        for (INotificationSender sender : notificationSenders) {
            if (sender.isAvailable()) {
                String recipient = getRecipientForSender(sender, customerEmail,
                        customerPhone, deviceToken);
                if (recipient != null) {
                    String message = templateService.getFormattedMessage("order_confirmation",
                            templateData);
                    sender.sendNotification(recipient, "Order Confirmed", message);
                }
            }
        }
    }

    private String getRecipientForSender(INotificationSender sender,
                                         String email, String phone, String deviceToken) {
        if (sender instanceof EmailNotification) return email;
        if (sender instanceof SMSNotification) return phone;
        if (sender instanceof PushNotification) return deviceToken;
        return null;
    }
}