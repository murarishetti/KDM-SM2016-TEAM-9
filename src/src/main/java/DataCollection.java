import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.*;

/**
 * Created by chanti on 24-Jun-16.
 */
public class DataCollection {
    public static String main() throws IOException {
        String fileName = "gTrends.txt";

        String url2 = "https://api.nytimes.com/svc/search/v2/articlesearch.json?api-key=1069bc25bff24ebf8cf3dbae1133e000&q=kansascity&sort=newest&fl=lead_paragraph&page=0";
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
        JSONArray jArray = obj.getJSONObject("response").getJSONArray("docs");
        FileWriter fileWriter = new FileWriter(fileName);

        // Always wrap FileWriter in BufferedWriter.
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        System.out.print(jArray.length());
        for (int i = 0;i < jArray.length();i++) {
            JSONObject result = jArray.getJSONObject(i);
            String output = result.toString().substring(19);
            output = output.substring(0, output.length()-2);
            System.out.println(i);
            System.out.print(output);

            bufferedWriter.write(output);
            bufferedWriter.write("\n");
        }

        bufferedWriter.close();

        return fileName;
    }
}
