package InventoryManagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryManager {
    private final Map<String, Warehouse> warehouses;

    public InventoryManager(List<String> warehouseIds) {
        this.warehouses = new HashMap<>();
        for (String id : warehouseIds) {
            warehouses.put(id, new Warehouse(id));
        }
    }

    public void addStock(String warehouseId, String productId, int quantity) {
        Warehouse warehouse = warehouses.get(warehouseId);
        if (warehouse == null) {
            throw new IllegalArgumentException("Warehouse " + warehouseId + " not found");
        }
        warehouse.addStock(productId, quantity);
    }

    public boolean removeStock(String warehouseId, String productId, int quantity) {
        Warehouse warehouse = warehouses.get(warehouseId);
        if (warehouse == null) {
            return false;
        }
        return warehouse.removeStock(productId, quantity);
    }

    public boolean transfer(String productId, String fromWarehouseId, String toWarehouseId, int quantity) {
        if (quantity <= 0) {
            return false;
        }

        if (fromWarehouseId.equals(toWarehouseId)) {
            return false;
        }

        Warehouse fromWarehouse = warehouses.get(fromWarehouseId);
        Warehouse toWarehouse = warehouses.get(toWarehouseId);

        if (fromWarehouse == null || toWarehouse == null) {
            return false;
        }

        // Lock in consistent order to prevent deadlock
        String firstId = fromWarehouseId.compareTo(toWarehouseId) < 0 ? fromWarehouseId : toWarehouseId;
        String secondId = fromWarehouseId.compareTo(toWarehouseId) < 0 ? toWarehouseId : fromWarehouseId;
        Warehouse firstLock = warehouses.get(firstId);
        Warehouse secondLock = warehouses.get(secondId);

        // Java's synchronized is reentrant, so we can call removeStock/addStock
        // which will re-acquire the same locks
        synchronized (firstLock) {
            synchronized (secondLock) {
                if (!fromWarehouse.removeStock(productId, quantity)) {
                    return false;
                }
                toWarehouse.addStock(productId, quantity);
            }
        }

        return true;
    }

    public List<String> getWarehousesWithAvailability(String productId, int quantity) {
        List<String> available = new ArrayList<>();
        for (Map.Entry<String, Warehouse> entry : warehouses.entrySet()) {
            if (entry.getValue().checkAvailability(productId, quantity)) {
                available.add(entry.getKey());
            }
        }
        return available;
    }

    public void setLowStockAlert(String warehouseId, String productId, int threshold, AlertListener listener) {
        Warehouse warehouse = warehouses.get(warehouseId);
        if (warehouse == null) {
            throw new IllegalArgumentException("Warehouse " + warehouseId + " not found");
        }
        warehouse.setLowStockAlert(productId, threshold, listener);
    }
}



