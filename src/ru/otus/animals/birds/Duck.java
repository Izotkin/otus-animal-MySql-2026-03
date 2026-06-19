package ru.otus.animals.birds;

import ru.otus.animals.Animal;
import ru.otus.animals.Color;

public class Duck extends Animal implements Flying {
    public Duck(String name, int age, double weight, Color color) {
        super(name, age, weight, color);
    }

    @Override
    public void say() {
        System.out.println("Кря");
    }

    @Override
    public void fly() {
        System.out.println("Я лечу");
    }
}