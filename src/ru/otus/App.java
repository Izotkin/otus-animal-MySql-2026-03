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
                    AnimalType animalType;
                    while (true) {
                        System.out.print("Какое животное (cat/dog/duck): ");
                        String typeInput = scanner.nextLine().trim().toUpperCase();
                        try {
                            animalType = AnimalType.valueOf(typeInput);
                            break;
                        } catch (IllegalArgumentException e) {
                            System.out.println("Неизвестный тип животного. Используйте cat, dog или duck.");
                        }
                    }

                    System.out.print("Имя: ");
                    String name = scanner.nextLine().trim();

                    int age;
                    while (true) {
                        System.out.print("Возраст: ");
                        String ageInput = scanner.nextLine().trim();
                        try {
                            age = Integer.parseInt(ageInput);
                            if (age < 0) {
                                System.out.println("Ошибка: возраст не может быть отрицательным.");
                                continue;
                            }
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Ошибка: введите целое число.");
                        }
                    }

                    double weight;
                    while (true) {
                        System.out.print("Вес: ");
                        String weightInput = scanner.nextLine().trim();
                        try {
                            weight = Double.parseDouble(weightInput);
                            if (weight < 0) {
                                System.out.println("Ошибка: вес не может быть отрицательным.");
                                continue;
                            }
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Ошибка: введите число (например 4.5).");
                        }
                    }

                    Color color;
                    while (true) {
                        System.out.print("Цвет (BLACK, WHITE, GRAY, RED, BROWN, YELLOW, GREEN, BLUE): ");
                        String colorInput = scanner.nextLine().trim().toUpperCase();
                        try {
                            color = Color.valueOf(colorInput);
                            break;
                        } catch (IllegalArgumentException e) {
                            System.out.println("Неизвестный цвет. Используйте один из: BLACK, WHITE, GRAY, RED, BROWN, YELLOW, GREEN, BLUE.");
                        }
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