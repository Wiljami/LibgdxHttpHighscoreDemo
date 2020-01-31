package fi.tuni.tiko;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.List;

/**
 * MainClass is just for demo purposes in this project.
 */
public class MainClass extends ApplicationAdapter implements HighScoreListener {
	private SpriteBatch batch;
	private OrthographicCamera camera;

	private int height = 800;
	private int width = 600;

	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, width, height);
		createFont();
		HighScoreServer.fetchHighScores(this);
	}

	private void createFont() {
		FreeTypeFontGenerator fontGenerator =
				new FreeTypeFontGenerator(Gdx.files.internal("CheyenneSansNovus-Regular.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter =
				new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 24;
		parameter.color = Color.BLACK;
		font = fontGenerator.generateFont(parameter);
	}

	private void sendScore() {
		HighScoreEntry score = new HighScoreEntry("Kalle", 10000);
		HighScoreServer.sendNewHighScore(score, this);
	}


	private BitmapFont font;

	private String textToDraw = "";

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);

		batch.begin();

		font.draw(batch, textToDraw, 20, height-50);

		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
	}

	private void buildNewString(List<HighScoreEntry> scores) {
		StringBuilder string = new StringBuilder();
		string.append("Highscores:\n");
		if (scores == null) {
			textToDraw = string.toString();
		} else {
			for (HighScoreEntry e : scores) {
				string.append(e.getScore()).append("  ").append(e.getName()).append("\n");
			}
			textToDraw = string.toString();
		}
	}

	@Override
	public void receiveHighScore(List<HighScoreEntry> highScores) {
		Gdx.app.log("MainClass", "Received new high scores successfully");
		buildNewString(highScores);
	}

	@Override
	public void receiveConfirmationOnSend() {
		Gdx.app.log("MainClass", "Sent a new high entry successfully");
		HighScoreServer.fetchHighScores(this);
	}

	@Override
	public void failedToRetrieveHighScores(Throwable t) {
		Gdx.app.error("MainClass",
				"Something went wrong while getting high scores", t);
	}

	@Override
	public void failedToSendHighScore(Throwable t) {
		Gdx.app.error("MainClass",
				"Something went wrong while sending a high score entry", t);
	}
}
