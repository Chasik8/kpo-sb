package com.zoo.erp.di;

import com.google.inject.AbstractModule;
import com.zoo.erp.services.impl.VeterinaryClinic;
import com.zoo.erp.services.impl.Zoo;
import com.zoo.erp.services.interfaces.IVeterinaryService;
import com.zoo.erp.services.interfaces.IZooService;

public class AppModule extends AbstractModule {
    @Override
    protected void configure() {

        bind(IVeterinaryService.class).to(VeterinaryClinic.class);
        bind(IZooService.class).to(Zoo.class);
    }
}