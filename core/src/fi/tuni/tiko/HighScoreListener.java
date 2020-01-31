package fi.tuni.tiko;

import java.util.List;

/**
 * The HighScore part of the game needs to implement this interface.
 */
public interface HighScoreListener {
    /**
     * receiveHighScore is called in the parent class once the high score data
     * has been downloaded from the server. It is passed using this method.
     * @param highScores the loaded highScoreEntry data in a List
     */
    public void receiveHighScore(List<HighScoreEntry> highScores);

    public void receiveConfirmationOnSend(boolean success);
}
