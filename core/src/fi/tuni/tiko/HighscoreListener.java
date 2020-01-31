package fi.tuni.tiko;

import java.util.List;

public interface HighscoreListener {
    public void receiveHighscore(List<HighscoreEntry> highscores);
}
