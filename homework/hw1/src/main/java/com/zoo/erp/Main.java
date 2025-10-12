package com.zoo.erp;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.zoo.erp.di.AppModule;
import com.zoo.erp.ui.ConsoleApplication;

public class Main {
    public static void main(String[] args) {

        Injector injector = Guice.createInjector(new AppModule());



        ConsoleApplication app = injector.getInstance(ConsoleApplication.class);


        app.run();
    }
}