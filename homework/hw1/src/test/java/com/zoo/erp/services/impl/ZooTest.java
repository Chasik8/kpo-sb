package com.zoo.erp.services.impl;

import com.zoo.erp.models.animals.base.Animal;
import com.zoo.erp.models.animals.species.Rabbit;
import com.zoo.erp.models.animals.species.Tiger;
import com.zoo.erp.models.animals.species.Wolf;
import com.zoo.erp.models.things.Table;
import com.zoo.erp.services.interfaces.IVeterinaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ZooTest {


    @Mock
    private IVeterinaryService mockVetService;


    @InjectMocks
    private Zoo zoo;

    private Animal healthyTiger;
    private Animal unhealthyWolf;
    private Animal friendlyRabbit;

    @BeforeEach
    void setUp() {

        healthyTiger = new Tiger();
        unhealthyWolf = new Wolf();
        friendlyRabbit = new Rabbit();
    }

    @Test
    @DisplayName("Прием здорового животного в зоопарк")
    void acceptAnimal_shouldAddAnimal_whenHealthy() {

        when(mockVetService.checkHealth(any(Animal.class))).thenReturn(true);

        boolean result = zoo.acceptAnimal(healthyTiger);

        assertTrue(result, "Здоровое животное должно быть принято");
        assertEquals(1, zoo.getInventoryList().size(), "В инвентаре должна быть одна запись");
        assertEquals(healthyTiger, zoo.getInventoryList().get(0), "Принятое животное должно быть в списке");
    }

    @Test
    @DisplayName("Отказ в приеме больного животного")
    void acceptAnimal_shouldNotAddAnimal_whenUnhealthy() {

        when(mockVetService.checkHealth(unhealthyWolf)).thenReturn(false);

        boolean result = zoo.acceptAnimal(unhealthyWolf);

        assertFalse(result, "Больное животное не должно быть принято");
        assertTrue(zoo.getInventoryList().isEmpty(), "Инвентарный список должен быть пустым");
    }

    @Test
    @DisplayName("Подсчет общего количества еды")
    void getTotalFoodRequired_shouldReturnCorrectSum() {
        when(mockVetService.checkHealth(any(Animal.class))).thenReturn(true);

        zoo.acceptAnimal(healthyTiger);
        zoo.acceptAnimal(friendlyRabbit);

        int totalFood = zoo.getTotalFoodRequired();

        assertEquals(12, totalFood, "Общее количество еды должно быть 10 + 2 = 12");
    }

    @Test
    @DisplayName("Формирование списка для контактного зоопарка")
    void getPettingZooAnimals_shouldReturnOnlyKindHerbivores() {
        when(mockVetService.checkHealth(any(Animal.class))).thenReturn(true);

        zoo.acceptAnimal(healthyTiger);
        zoo.acceptAnimal(friendlyRabbit);

        List<Animal> pettingAnimals = zoo.getPettingZooAnimals();

        assertEquals(1, pettingAnimals.size(), "В контактном зоопарке должен быть только один зверь");
        assertEquals(friendlyRabbit, pettingAnimals.get(0), "Это должен быть кролик");
    }

    @Test
    @DisplayName("Инвентаризация животных и вещей")
    void getInventoryList_shouldContainAnimalsAndThings() {
        when(mockVetService.checkHealth(any(Animal.class))).thenReturn(true);
        Table table = new Table();

        zoo.acceptAnimal(healthyTiger);
        zoo.addInventory(table);

        var inventory = zoo.getInventoryList();

        assertEquals(2, inventory.size(), "В инвентарном списке должно быть 2 объекта");
        assertTrue(inventory.contains(healthyTiger), "Список должен содержать тигра");
        assertTrue(inventory.contains(table), "Список должен содержать стол");
    }
}