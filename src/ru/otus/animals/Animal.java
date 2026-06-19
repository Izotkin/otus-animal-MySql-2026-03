package ru.otus.animals;

public class Animal {
    private String name;
    private int age;
    private double weight;
    private Color color;

    public Animal(String name, int age, double weight, Color color) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.color = color;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }

    public void say() { System.out.println("Я говорю"); }
    public void go() { System.out.println("Я иду"); }
    public void drink() { System.out.println("Я пью"); }
    public void eat() { System.out.println("Я ем"); }

    @Override
    public String toString() {
        String yearWord;
        int lastDigit = age % 10;
        int lastTwoDigits = age % 100;
        if (lastTwoDigits >= 11 && lastTwoDigits <= 14) {
            yearWord = "лет";
        } else if (lastDigit == 1) {
            yearWord = "год";
        } else if (lastDigit >= 2 && lastDigit <= 4) {
            yearWord = "года";
        } else {
            yearWord = "лет";
        }
        return String.format("Привет! Меня зовут %s, мне %d %s, я вешу - %.0f кг, мой цвет - %s.",
                name, age, yearWord, weight, color.getRussianName());
    }
}