package uet.oop.bomberman.utils;

public enum EGameLevel {
    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard"),
    BACK("Back (B)");

    private final String level;

    EGameLevel(String level) {
        this.level = level;
    }

    public String getStringLevel() {
        return this.level;
    }
}
