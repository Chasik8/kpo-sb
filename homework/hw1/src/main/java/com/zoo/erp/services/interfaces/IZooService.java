package com.zoo.erp.services.interfaces;

import com.zoo.erp.models.animals.base.Animal;
import com.zoo.erp.models.interfaces.IInventory;
import java.util.List;

public interface IZooService {
    boolean acceptAnimal(Animal animal);
    int getTotalFoodRequired();
    List<Animal> getPettingZooAnimals();
    List<IInventory> getInventoryList();
    void addInventory(IInventory item);
}