package InventoryManagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Warehouse {
    private final String id;
    private final Map<String, Integer> inventory;
    private final Map<String, List<AlertConfig>> alertConfigs;

    public Warehouse(String id) {
        this.id = id;
        this.inventory = new HashMap<>();
        this.alertConfigs = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void addStock(String productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        List<AlertToFire> alertsToFire = null;

        synchronized (this) {
            int currentQty = inventory.getOrDefault(productId, 0);
            int newQty = currentQty + quantity;
            inventory.put(productId, newQty);

            alertsToFire = getAlertsToFire(productId, currentQty, newQty);
        }

        if (alertsToFire != null) {
            fireAlerts(alertsToFire);
        }
    }

    public boolean removeStock(String productId, int quantity) {
        List<AlertToFire> alertsToFire = null;

        synchronized (this) {
            if (quantity <= 0) {
                return false;
            }

            int currentQty = inventory.getOrDefault(productId, 0);
            if (currentQty - quantity < 0) {
                return false;
            }

            int newQty = currentQty - quantity;
            inventory.put(productId, newQty);

            alertsToFire = getAlertsToFire(productId, currentQty, newQty);
        }

        if (alertsToFire != null) {
            fireAlerts(alertsToFire);
        }

        return true;
    }

    public synchronized int getStock(String productId) {
        return inventory.getOrDefault(productId, 0);
    }

    public synchronized boolean checkAvailability(String productId, int quantity) {
        if (quantity <= 0) {
            return false;
        }
        int currentQty = inventory.getOrDefault(productId, 0);
        return currentQty >= quantity;
    }

    public synchronized void setLowStockAlert(String productId, int threshold, AlertListener listener) {
        if (threshold <= 0) {
            throw new IllegalArgumentException("Threshold must be positive");
        }
        if (listener == null) {
            throw new IllegalArgumentException("Listener cannot be null");
        }

        alertConfigs.computeIfAbsent(productId, k -> new ArrayList<>());
        alertConfigs.get(productId).add(new AlertConfig(threshold, listener));
    }

    // Must be called while holding lock
    int getStockInternal(String productId) {
        return inventory.getOrDefault(productId, 0);
    }

    // Must be called while holding lock
    void setStockInternal(String productId, int quantity) {
        inventory.put(productId, quantity);
    }

    private List<AlertToFire> getAlertsToFire(String productId, int previousQty, int newQty) {
        List<AlertConfig> configs = alertConfigs.get(productId);
        if (configs == null) {
            return null;
        }

        List<AlertToFire> alertsToFire = new ArrayList<>();

        for (AlertConfig config : configs) {
            if (previousQty >= config.getThreshold() && newQty < config.getThreshold()) {
                alertsToFire.add(new AlertToFire(config.getListener(), productId, newQty));
            }
        }

        return alertsToFire.isEmpty() ? null : alertsToFire;
    }

    private void fireAlerts(List<AlertToFire> alerts) {
        for (AlertToFire alert : alerts) {
            alert.listener.onLowStock(id, alert.productId, alert.quantity);
        }
    }

    private static class AlertToFire {
        final AlertListener listener;
        final String productId;
        final int quantity;

        AlertToFire(AlertListener listener, String productId, int quantity) {
            this.listener = listener;
            this.productId = productId;
            this.quantity = quantity;
        }
    }
}


