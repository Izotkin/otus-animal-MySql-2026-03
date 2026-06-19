package ru.otus.animals;

public class Dog extends Animal {
    public Dog(String name, int age, double weight, Color color) {
        super(name, age, weight, color);
    }

    @Override
    public void say() {
        System.out.println("Гав");
    }
}