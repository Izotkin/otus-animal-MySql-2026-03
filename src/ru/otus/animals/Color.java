package ru.otus.animals;

public enum Color {
    BLACK("черный"),
    WHITE("белый"),
    GRAY("серый"),
    RED("рыжий"),
    BROWN("коричневый"),
    YELLOW("желтый"),
    GREEN("зеленый"),
    BLUE("голубой");

    private final String russianName;

    Color(String russianName) {
        this.russianName = russianName;
    }

    public String getRussianName() {
        return russianName;
    }
}
