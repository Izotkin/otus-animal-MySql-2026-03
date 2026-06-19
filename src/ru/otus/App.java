package ru.otus;

import ru.otus.animals.*;
import ru.otus.factory.AnimalFactory;
import ru.otus.factory.AnimalType;
import java.util.ArrayList;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        ArrayList<Animal> animals = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Введите команду (add/list/exit): ");
            String input = scanner.nextLine().trim().toUpperCase();

            Command command;
            try {
                command = Command.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Неизвестная команда. Используйте add, list или exit.");
                continue;
            }

            switch (command) {
                case ADD:
                    System.out.print("Какое животное (cat/dog/duck): ");
                    String typeInput = scanner.nextLine().trim().toUpperCase();
                    AnimalType animalType;
                    try {
                        animalType = AnimalType.valueOf(typeInput);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Неизвестный тип животного. Используйте cat, dog или duck.");
                        continue;
                    }

                    System.out.print("Имя: ");
                    String name = scanner.nextLine().trim();
                    System.out.print("Возраст: ");
                    int age = Integer.parseInt(scanner.nextLine().trim());
                    System.out.print("Вес: ");
                    double weight = Double.parseDouble(scanner.nextLine().trim());
                    System.out.print("Цвет (BLACK, WHITE, GRAY, RED, BROWN, YELLOW, GREEN, BLUE): ");
                    String colorInput = scanner.nextLine().trim().toUpperCase();
                    Color color;
                    try {
                        color = Color.valueOf(colorInput);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Неизвестный цвет. Используйте один из: BLACK, WHITE, GRAY, RED, BROWN, YELLOW, GREEN, BLUE.");
                        continue;
                    }

                    Animal animal = AnimalFactory.createAnimal(
                            animalType, name, age, weight, color
                    );


                    animals.add(animal);
                    animal.say();
                    break;

                case LIST:
                    if (animals.isEmpty()) {
                        System.out.println("Список животных пуст.");
                    } else {
                        for (Animal a : animals) {
                            System.out.println(a.toString());
                        }
                    }
                    break;

                case EXIT:
                    System.out.println("Выход из программы.");
                    scanner.close();
                    return;
            }
        }
    }
}