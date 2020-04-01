package self.edu.shopbiz.enums;

public enum CurseWords {

    SHIP("Ship"),
    MISS("Miss"),
    DUCK("Duck"),
    PUNT("Punt"),
    ROOSTER("Rooster"),
    MOTHER("Mother"),
    BITS("Bits");

    private final String value;

    CurseWords(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
