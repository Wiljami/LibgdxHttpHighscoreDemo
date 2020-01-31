package fi.tuni.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.util.ArrayList;

public class HighScoreServer {
    /**
     * url of where you get your high score data. What you get depends on the
     * server. In this democase you will receive a json file that will contain
     * up to 10 high score entries ordered by highest score first.
     */
    private static final String GETURL =
            "https://highscore-demo.herokuapp.com/get/";

    /**
     * url where you send your high score entries. The server will then handle
     * the entry data. You should not have to worry whether the new high score
     * gets to the top10 etc. Server takes care of that.
     */
    private static final String POSTURL =
            "https://highscore-demo.herokuapp.com/add/";

    /**
     * If verbose is true, this class will print out messages to the Gdx log.
     */
    private static boolean verbose = true;

    /**
     * fetchHighScores gets high score entries from the server.
     *
     * This gets high score entries from a server, converts the json file to
     * List of HighScoreEntries and sends it back to the source.
     * @param source source class implementing HighScoreListener
     */
    public static void fetchHighScores(final HighScoreListener source) {
        Net.HttpRequest request = new Net.HttpRequest(HttpMethods.GET);
        request.setUrl(GETURL);
        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void handleHttpResponse (Net.HttpResponse httpResponse) {
                String r = httpResponse.getResultAsString();
                Json json = new Json();
                ArrayList<HighScoreEntry> highScores =
                        json.fromJson(ArrayList.class, HighScoreEntry.class, r);
                source.receiveHighScore(highScores);
            }

            @Override
            public void failed (Throwable t) {
                if (verbose)
                    Gdx.app.error("HighScoreServer",
                            "GET: something went wrong");
                source.failedToRetrieveHighScores(t);
            }

            @Override
            public void cancelled () {
                if (verbose)
                    Gdx.app.log("HighScoreServer", "GET: cancelled");
            }

        });
    }

    /**
     * sendHighScore entry sends new high score data to the server
     *
     * It will then send a confirmation of success back to the source.
     * @param highScore The new HighScoreEntry data to be sent to the server.
     * @param source source class implementing HighScoreListener
     */
    public static void sendNewHighScore(HighScoreEntry highScore,
                                        final HighScoreListener source) {
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);

        String content = json.toJson(highScore);

        Net.HttpRequest request = new Net.HttpRequest(HttpMethods.POST);
        request.setUrl(POSTURL);
        request.setHeader("Content-type", "application/json");
        request.setContent(content);

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                if (verbose)
                    Gdx.app.log("HighScoreServer", "POST: success");
                source.receiveConfirmationOnSend();
            }

            @Override
            public void failed(Throwable t) {
                if (verbose)
                    Gdx.app.error("HighScoreServer",
                            "POST: something went wrong", t);
                source.failedToSendHighScore(t);
            }

            @Override
            public void cancelled() {
                if (verbose)
                    Gdx.app.log("HighScoreServer", "POST: cancelled");
            }
        });
    }
}
