package com.ecommerce.notification;

import java.util.HashMap;
import java.util.Map;

public class NotificationTemplateService {
    private final Map<String, String> templates;

    public NotificationTemplateService() {
        templates = new HashMap<>();
        templates.put("order_confirmation",
                "Your order #${orderId} for $${amount} has been confirmed. Order type: ${orderType}");
        templates.put("payment_success",
                "Payment of $${amount} for order #${orderId} was successful.");
        templates.put("payment_failed",
                "Payment failed for order #${orderId}. Please try again.");
    }

    public String getFormattedMessage(String templateKey, Map<String, String> data) {
        String template = templates.get(templateKey);
        if (template == null) {
            return "";
        }

        String message = template;
        for (Map.Entry<String, String> entry : data.entrySet()) {
            message = message.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        return message;
    }
}