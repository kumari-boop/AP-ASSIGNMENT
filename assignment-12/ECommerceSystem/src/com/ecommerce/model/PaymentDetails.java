package com.ecommerce.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PaymentDetails {
    private final Map<String, String> details;

    public PaymentDetails(Map<String, String> details) {
        this.details = new HashMap<>(details);
    }

    public String getDetail(String key) {
        return details.get(key);
    }

    public Map<String, String> getAllDetails() {
        return Collections.unmodifiableMap(details);
    }
}