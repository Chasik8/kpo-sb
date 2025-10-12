package com.zoo.erp.services.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.zoo.erp.models.animals.base.Animal;
import com.zoo.erp.models.animals.base.Herbo;
import com.zoo.erp.models.interfaces.IAlive;
import com.zoo.erp.models.interfaces.IHerbivore;
import com.zoo.erp.models.interfaces.IInventory;
import com.zoo.erp.services.interfaces.IVeterinaryService;
import com.zoo.erp.services.interfaces.IZooService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class Zoo implements IZooService {
    private final List<Animal> animals = new ArrayList<>();
    private final List<IInventory> inventoryItems = new ArrayList<>();
    private final IVeterinaryService vetService;

    @Inject
    public Zoo(IVeterinaryService vetService) {
        this.vetService = vetService;
        System.out.println("ERP система зоопарка запущена.");
    }

    @Override
    public boolean acceptAnimal(Animal animal) {
        if (vetService.checkHealth(animal)) {
            animals.add(animal);
            inventoryItems.add(animal);
            System.out.printf("Животное '%s' принято в зоопарк с инвентарным номером %d.\n", animal.getName(), animal.getInventoryNumber());
            return true;
        } else {
            System.out.printf("Животное '%s' не прошло медосмотр.\n", animal.getName());
            return false;
        }
    }

    @Override
    public void addInventory(IInventory item) {
        inventoryItems.add(item);
        System.out.printf("Вещь '%s' поставлена на баланс с инвентарным номером %d.\n", item.getName(), item.getInventoryNumber());
    }

    @Override
    public int getTotalFoodRequired() {
        return animals.stream()
                .mapToInt(IAlive::getFoodPerDay)
                .sum();
    }

    @Override
    public List<Animal> getPettingZooAnimals() {
        return animals.stream()
                .filter(animal -> animal instanceof IHerbivore)
                .map(animal -> (Herbo) animal)
                .filter(herbo -> herbo.getKindness() > 5)
                .collect(Collectors.toList());
    }

    @Override
    public List<IInventory> getInventoryList() {
        return new ArrayList<>(inventoryItems);
    }
}