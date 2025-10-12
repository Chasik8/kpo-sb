package com.zoo.erp.services.interfaces;

import com.zoo.erp.models.animals.base.Animal;

public interface IVeterinaryService {
    boolean checkHealth(Animal animal);
}