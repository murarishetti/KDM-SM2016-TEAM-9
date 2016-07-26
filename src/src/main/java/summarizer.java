import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by chanti on 24-Jul-16.
 */
public class summarizer {
    public static void main() throws IOException {
        String url2 = "http://api.intellexer.com/summarize?apikey=1d5aadf2-c670-4680-afaf-680e71498e44&conceptsRestriction=7&returnedTopicsCount=2&summaryRestriction=7&textStreamLength=1000&url=http://www.nytimes.com/";
        URL url = new URL(url2);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        StringBuilder stringBuilder = new StringBuilder();
        InputStream is;
        is = urlConnection.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
            stringBuilder.append(line);
        }
        String response = stringBuilder.toString();
        System.out.println(response);
        is.close();
        urlConnection.disconnect();

        JSONObject obj = new JSONObject(response);
    }
}
