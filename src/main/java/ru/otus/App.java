package ru.otus;

import ru.otus.animals.Animal;
import ru.otus.animals.Color;
import ru.otus.factory.AnimalFactory;
import ru.otus.factory.AnimalType;

import java.util.List;
import java.util.Scanner;

public class App {
    private static DatabaseManager db = new DatabaseManager();

    public static void main(String[] args) {

        List<Animal> animals = db.loadAllAnimals();
        System.out.println("Загружено животных из БД: " + animals.size());

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("\nВведите команду (add/list/update/delete/filter/exit): ");
            String input = scanner.nextLine().trim().toUpperCase();

            switch (input) {
                case "ADD":
                    addAnimal(animals, scanner);
                    break;
                case "LIST":
                    listAnimals(animals);
                    break;
                case "UPDATE":
                    updateAnimal(animals, scanner);
                    break;
                case "DELETE":
                    deleteAnimal(animals, scanner);
                    break;
                case "FILTER":
                    filterAnimals(scanner);
                    break;
                case "EXIT":
                    System.out.println("Выход.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Неизвестная команда.");
            }
        }
    }

    private static void addAnimal(List<Animal> animals, Scanner scanner) {
        AnimalType animalType;
        while (true) {
            System.out.print("Какое животное (cat/dog/duck): ");
            String typeInput = scanner.nextLine().trim().toUpperCase();
            try {
                animalType = AnimalType.valueOf(typeInput);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Неизвестный тип.");
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
                System.out.println("Ошибка: введите число.");
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
                System.out.println("Неизвестный цвет.");
            }
        }

        Animal animal = new AnimalFactory().create(animalType, name, age, weight, color, 0);
        animals.add(animal);
        db.saveAnimal(animal);
        animal.say();
        System.out.println("Сохранено. ID: " + animal.getId());
    }

    private static void listAnimals(List<Animal> animals) {
        if (animals.isEmpty()) {
            System.out.println("Список животных пуст.");
            return;
        }
        System.out.println("Весь список:");
        for (Animal a : animals) {
            System.out.println(a.toString() + " (ID: " + a.getId() + ")");
        }
    }

    private static void updateAnimal(List<Animal> animals, Scanner scanner) {
        System.out.print("Введите ID: ");
        int id = Integer.parseInt(scanner.nextLine().trim());

        Animal animal = db.getAnimalById(id);
        if (animal == null) {
            System.out.println("Не найдено.");
            return;
        }

        System.out.println("Текущие данные: " + animal);
        System.out.println("Если изменения не требуются то оставить поле пустым.");

        System.out.print("Новое имя (" + animal.getName() + "): ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) name = animal.getName();

        System.out.print("Новый возраст (" + animal.getAge() + "): ");
        String ageStr = scanner.nextLine().trim();
        int age = ageStr.isEmpty() ? animal.getAge() : Integer.parseInt(ageStr);

        System.out.print("Новый вес (" + animal.getWeight() + "): ");
        String weightStr = scanner.nextLine().trim();
        double weight = weightStr.isEmpty() ? animal.getWeight() : Double.parseDouble(weightStr);

        System.out.print("Новый цвет (" + animal.getColor().getRussianName() + "): ");
        String colorStr = scanner.nextLine().trim().toUpperCase();
        String color = colorStr.isEmpty() ? animal.getColor().name() : colorStr;

        db.updateAnimal(id, name, age, weight, color);

        for (int i = 0; i < animals.size(); i++) {
            if (animals.get(i).getId() == id) {
                animals.set(i, new AnimalFactory().create(
                        animal.getAnimalType(), name, age, weight, Color.valueOf(color), id
                ));
                break;
            }
        }
    }

    private static void deleteAnimal(List<Animal> animals, Scanner scanner) {
        System.out.print("Введите ID: ");
        int id = Integer.parseInt(scanner.nextLine().trim());

        if (db.getAnimalById(id) == null) {
            System.out.println("Не найдено.");
            return;
        }

        db.deleteAnimal(id);
        animals.removeIf(a -> a.getId() == id);
    }

    private static void filterAnimals(Scanner scanner) {
        System.out.print("Тип (cat/dog/duck): ");
        String type = scanner.nextLine().trim().toUpperCase();

        List<Animal> filtered = db.loadAnimalsByType(type);
        if (filtered.isEmpty()) {
            System.out.println("Животных типа '" + type + "' нет.");
        } else {
            System.out.println("Список животных с типом " + type);
            for (Animal a : filtered) {
                System.out.println(a.toString() + " (ID: " + a.getId() + ")");
            }
        }
    }
}