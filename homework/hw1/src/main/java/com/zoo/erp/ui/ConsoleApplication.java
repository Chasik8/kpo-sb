package com.zoo.erp.ui;

import com.google.inject.Inject;
import com.zoo.erp.models.animals.base.Animal;
import com.zoo.erp.models.animals.base.Herbo;
import com.zoo.erp.models.animals.species.*;
import com.zoo.erp.models.things.Computer;
import com.zoo.erp.models.things.Table;
import com.zoo.erp.services.interfaces.IZooService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleApplication {
    private final IZooService zooService;
    private final Scanner scanner;

    @Inject
    public ConsoleApplication(IZooService zooService) {
        this.zooService = zooService;
        this.scanner = new Scanner(System.in);

        seedData();
    }

    private void seedData() {
        System.out.println("\n--- Первоначальное заполнение зоопарка ---\n");
        zooService.acceptAnimal(new Tiger());
        zooService.acceptAnimal(new Rabbit());
        zooService.acceptAnimal(new Monkey());
        zooService.addInventory(new Table());
        zooService.addInventory(new Computer());
        System.out.println("\n--- Первоначальное заполнение завершено ---\n");
    }


    public void run() {
        while (true) {
            printMenu();
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                if (choice == 0) {
                    System.out.println("Завершение работы программы.");
                    break;
                }
                handleMenuChoice(choice);
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: Пожалуйста, введите число.");
                scanner.nextLine();
            }
        }
        scanner.close();
    }

    private void printMenu() {
        System.out.println("\n===== Меню ERP Системы Зоопарка =====");
        System.out.println("1. Добавить новое животное");
        System.out.println("2. Показать отчет: Общее кол-во еды в день");
        System.out.println("3. Показать отчет: Животные для контактного зоопарка");
        System.out.println("4. Показать отчет: Все на балансе (инвентаризация)");
        System.out.println("0. Выход");
        System.out.print("Выберите опцию: ");
    }

    private void handleMenuChoice(int choice) {
        switch (choice) {
            case 1:
                acceptNewAnimal();
                break;
            case 2:
                showTotalFoodReport();
                break;
            case 3:
                showPettingZooReport();
                break;
            case 4:
                showInventoryReport();
                break;
            default:
                System.out.println("Неверный выбор. Попробуйте снова.");
        }
    }

    private void acceptNewAnimal() {
        System.out.println("\nКакое животное добавить?");
        System.out.println("1. Тигр");
        System.out.println("2. Волк");
        System.out.println("3. Кролик");
        System.out.println("4. Обезьяна");
        System.out.print("Выберите вид: ");

        try {
            int animalChoice = scanner.nextInt();
            scanner.nextLine();
            Animal newAnimal = null;
            switch (animalChoice) {
                case 1: newAnimal = new Tiger(); break;
                case 2: newAnimal = new Wolf(); break;
                case 3: newAnimal = new Rabbit(); break;
                case 4: newAnimal = new Monkey(); break;
                default: System.out.println("Такого вида нет."); return;
            }
            zooService.acceptAnimal(newAnimal);
        } catch (InputMismatchException e) {
            System.out.println("Ошибка: Пожалуйста, введите число.");
            scanner.nextLine();
        }
    }

    private void showTotalFoodReport() {
        System.out.println("\n--- Отчет по потреблению еды ---");
        int totalFood = zooService.getTotalFoodRequired();
        System.out.printf("Всего животным требуется %d кг еды в день.\n", totalFood);
    }

    private void showPettingZooReport() {
        System.out.println("\n--- Отчет по контактному зоопарку ---");
        var pettingAnimals = zooService.getPettingZooAnimals();
        if (pettingAnimals.isEmpty()) {
            System.out.println("Животных, подходящих для контактного зоопарка, нет.");
        } else {
            System.out.println("Эти животные могут общаться с посетителями:");
            pettingAnimals.forEach(animal -> {
                Herbo herbo = (Herbo) animal;
                System.out.printf("- %s (Доброта: %d/10)\n", animal.getName(), herbo.getKindness());
            });
        }
    }

    private void showInventoryReport() {
        System.out.println("\n--- Инвентаризационный отчет ---");
        var inventoryList = zooService.getInventoryList();
        if (inventoryList.isEmpty()) {
            System.out.println("На балансе ничего нет.");
        } else {
            System.out.println("На балансе предприятия числятся:");
            inventoryList.forEach(item -> System.out.printf("- %s\n", item.toString()));
        }
    }
}