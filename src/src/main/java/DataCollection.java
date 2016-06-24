import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by chanti on 24-Jun-16.
 */
public class DataCollection {
    public static String main() throws IOException {
        String fileName = "gTrends.txt";
        String url2 = "https://www.google.com/trends/api/stories/latest?cat=m&fi=15&fs=15&geo=US&ri=300&rs=15&tz=300";
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
        String response = stringBuilder.toString().substring(4);
        is.close();
        urlConnection.disconnect();

        JSONObject obj = new JSONObject(response);
        JSONArray jArray = obj.getJSONObject("storySummaries").getJSONArray("trendingStories").getJSONObject(1).getJSONArray("articles");
        System.out.println(jArray);

        FileWriter fileWriter = new FileWriter(fileName);

        // Always wrap FileWriter in BufferedWriter.
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        for(int i = 0;i < jArray.length();i++) {
            JSONObject j = jArray.getJSONObject(i);
           bufferedWriter.write(j.toString());
        }
        bufferedWriter.close();

        return fileName;
    }
}
