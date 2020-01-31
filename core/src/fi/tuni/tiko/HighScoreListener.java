package fi.tuni.tiko;

import java.util.List;

public interface HighScoreListener {
    public void receiveHighscore(List<HighScoreEntry> highScores);
}
