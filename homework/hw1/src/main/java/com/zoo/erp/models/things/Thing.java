package com.zoo.erp.models.things;

import com.zoo.erp.models.interfaces.IInventory;

public abstract class Thing implements IInventory {
    private static int counter = 5000;
    protected final String name;
    protected final int inventoryNumber;

    public Thing(String name) {
        this.name = name;
        this.inventoryNumber = counter++;
    }

    @Override
    public int getInventoryNumber() {
        return inventoryNumber;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("%s (инв. %d)", name, inventoryNumber);
    }
}