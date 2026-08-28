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

    /*
    FIXME Follow-up: 2

    Both Warehouse and Transfer implement InventoryHolder
    interface InventoryHolder:
        + addStock(productId, quantity) -> void
        + removeStock(productId, quantity) -> boolean
        + getStock(productId) -> int
        + checkAvailability(productId, quantity) -> boolean

    class Transfer implements InventoryHolder:
        - id: string
        - productId: string
        - quantity: int
        - fromWarehouseId: string
        - toWarehouseId: string
        - createdAt: timestamp

        + Transfer(id, productId, quantity, fromWarehouseId, toWarehouseId)
        + getStock(productId) -> int
        + getFromWarehouse() -> string
        + getToWarehouse() -> string

    initiateTransfer(productId, fromWarehouseId, toWarehouseId, quantity)
        fromWarehouse = warehouses[fromWarehouseId]
        toWarehouse = warehouses[toWarehouseId]

        if !fromWarehouse.removeStock(productId, quantity)
            return null  // Insufficient stock

        // Create transfer to hold the inventory during shipment
        transfer = Transfer(generateId(), productId, quantity, fromWarehouseId, toWarehouseId)
        transfers[transfer.id] = transfer

        return transfer.id

    completeTransfer(transferId)
        transfer = transfers[transferId]
        if transfer == null
            return false

        toWarehouse = warehouses[transfer.toWarehouseId]

        // Move inventory from transfer to destination
        toWarehouse.addStock(transfer.productId, transfer.quantity)

        // Remove the transfer object
        transfers.remove(transferId)
        return true
     */

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



