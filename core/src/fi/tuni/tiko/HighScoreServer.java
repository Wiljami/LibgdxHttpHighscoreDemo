package fi.tuni.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.util.ArrayList;

/**
 * To get this work on an android device add following line to
 * AndroidManifest.xml:
 * <uses-permission android:name="android.permission.INTERNET" />
 * above tag <application
 */
public class HighScoreServer {
    /**
     * url of where you get your high score data. What you get depends on the
     * server. In this democase you will receive a json file that will contain
     * up to 10 high score entries ordered by highest score first.
     */
    private static String getUrl;

    /**
     * url where you send your high score entries. The server will then handle
     * the entry data. You should not have to worry whether the new high score
     * gets to the top10 etc. Server takes care of that.
     */
    private static String postUrl;

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
        request.setUrl(getUrl);
        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void handleHttpResponse (Net.HttpResponse httpResponse) {
                String r = httpResponse.getResultAsString();
                Json json = new Json();
                ArrayList<HighScoreEntry> highScores =
                        json.fromJson(ArrayList.class, HighScoreEntry.class, r);
                Gdx.app.log("HighScoreServer", "Fetch: success");
                source.receiveHighScore(highScores);
            }

            @Override
            public void failed (Throwable t) {
                if (verbose)
                    Gdx.app.error("HighScoreServer",
                            "Fetch: failed");
                source.failedToRetrieveHighScores(t);
            }

            @Override
            public void cancelled () {
                if (verbose)
                    Gdx.app.log("HighScoreServer", "Fetch: cancelled");
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
        request.setUrl(postUrl);
        request.setHeader("Content-type", "application/json");
        request.setContent(content);

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                if (verbose)
                    Gdx.app.log("HighScoreServer", "Send: success");
                source.receiveConfirmationOnSend();
            }

            @Override
            public void failed(Throwable t) {
                if (verbose)
                    Gdx.app.error("HighScoreServer",
                            "Send: failed", t);
                source.failedToSendHighScore(t);
            }

            @Override
            public void cancelled() {
                if (verbose)
                    Gdx.app.log("HighScoreServer", "Send: cancelled");
            }
        });
    }

    public static void setGetUrl(String getUrl) {
        HighScoreServer.getUrl = getUrl;
    }

    public static void setPostUrl(String postUrl) {
        HighScoreServer.postUrl = postUrl;
    }
}
