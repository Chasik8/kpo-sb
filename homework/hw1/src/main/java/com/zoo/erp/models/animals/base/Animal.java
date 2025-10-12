package com.zoo.erp.models.animals.base;

import com.zoo.erp.models.interfaces.IAlive;
import com.zoo.erp.models.interfaces.IInventory;

public abstract class Animal implements IAlive, IInventory {
    private static int counter = 1000;
    protected final String name;
    protected final int inventoryNumber;
    protected final int foodPerDay;

    public Animal(String name, int foodPerDay) {
        this.name = name;
        this.foodPerDay = foodPerDay;
        this.inventoryNumber = counter++;
    }

    @Override
    public int getFoodPerDay() {
        return foodPerDay;
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