package com.zoo.erp.models.animals.base;

import com.zoo.erp.models.interfaces.IHerbivore;

public abstract class Herbo extends Animal implements IHerbivore {
    protected final int kindness;

    public Herbo(String name, int foodPerDay, int kindness) {
        super(name, foodPerDay);
        this.kindness = kindness;
    }

    @Override
    public int getKindness() {
        return kindness;
    }
}