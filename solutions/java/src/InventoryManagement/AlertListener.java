package InventoryManagement;

public interface AlertListener {
    void onLowStock(String warehouseId, String productId, int currentQuantity);
}


