package com.zoo.erp.services.impl;

import com.zoo.erp.models.animals.base.Animal;
import com.zoo.erp.services.interfaces.IVeterinaryService;
import java.util.Random;

public class VeterinaryClinic implements IVeterinaryService {
    private final Random random = new Random();

    @Override
    public boolean checkHealth(Animal animal) {
        System.out.printf("Ветеринарная клиника: Проверяем здоровье животного '%s'.\n", animal.getName());

        boolean isHealthy = random.nextDouble() < 0.7;
        System.out.println(isHealthy ? "Результат: Здоров." : "Результат: Требуется лечение, не принимаем.");
        return isHealthy;
    }
}