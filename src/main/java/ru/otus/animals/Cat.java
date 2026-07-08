package ru.otus.animals;

import ru.otus.factory.AnimalType;

public class Cat extends Animal {
    public Cat(String name, int age, double weight, Color color) {
        super(name, age, weight, color);
    }

    @Override
    public void say() {
        System.out.println("Мяу");
    }

    @Override
    public AnimalType getAnimalType() {
        return AnimalType.CAT;
    }
}