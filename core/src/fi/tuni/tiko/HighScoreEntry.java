package fi.tuni.tiko;

public class HighScoreEntry {
    private String name;
    private int score;

    // We need this no argument constructor for the json parsing!
    public HighScoreEntry() {
    }

    public HighScoreEntry(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
