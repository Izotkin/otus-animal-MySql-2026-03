package ru.otus.factory;

import ru.otus.animals.*;
import ru.otus.animals.birds.Duck;

public class AnimalFactory {
    public static Animal createAnimal(AnimalType type, String name, int age, double weight, Color color) {
        switch (type) {
            case CAT:
                return new Cat(name, age, weight, color);
            case DOG:
                return new Dog(name, age, weight, color);
            case DUCK:
                return new Duck(name, age, weight, color);
            default:
                throw new IllegalArgumentException("Неизвестный тип животного: " + type);
        }
    }
}