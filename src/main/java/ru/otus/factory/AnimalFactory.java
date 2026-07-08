package ru.otus.factory;

import ru.otus.animals.Animal;
import ru.otus.animals.Color;
import ru.otus.animals.birds.Duck;
import ru.otus.animals.Cat;
import ru.otus.animals.Dog;

public class AnimalFactory {
    public Animal create(AnimalType animalType, String name, int age, double weight, Color color, int id) {
        Animal animal;
        switch (animalType) {
            case CAT:
                animal = new Cat(name, age, weight, color);
                break;
            case DOG:
                animal = new Dog(name, age, weight, color);
                break;
            case DUCK:
                animal = new Duck(name, age, weight, color);
                break;
            default:
                throw new IllegalArgumentException("Неизвестный вид: " + animalType);
        }
        animal.setId(id);
        return animal;
    }
}