public enum Language {

    English("English", 1),
    Polish("Polish",2),
    Italian("Italian", 3);

    private final String name;
    private final int number;

    Language(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
