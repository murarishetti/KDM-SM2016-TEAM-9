import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class summarize {
    public static void main() throws IOException {
       // String url2 = "http://api.intellexer.com/summarize?apikey=bbbfb162-ee2e-4c5b-8945-45ded0377a98&url=http%3A%2F%2Fapi.nytimes.com/svc/search/v2/articlesearch.json?api-key=1069bc25bff24ebf8cf3dbae1133e000&q=iphone7/&summaryRestriction=7&returnedTopicsCount=2&loadConceptsTree=true&loadNamedEntityTree=true&usePercentRestriction=true&conceptsRestriction=7&structure=general&fullTextTrees=true&textStreamLength=1000&useCache=false&wrapConcepts=true";
/*
        HttpClient client2 = new DefaultHttpClient();

        HttpGet request = new HttpGet("http://api.intellexer.com/recognizeNe?apikey=bbbfb162-ee2e-4c5b-8945-45ded0377a98&url=http%3A%2F%2Fwww.nytimes.com/pages/technology/index.html?action=click&pgtype=Homepage&region=TopBar&module=HPMiniNav&contentCollection=Tech&WT.nav=page&loadNamedEntities=true&loadRelationsTree=true&loadSentences=true");
        HttpResponse response = client2.execute(request);
        BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
        String line = " ";
        StringBuilder s = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
            s.append(line);
        }
        String output = s.toString();
        JSONObject j = new JSONObject(output);
        JSONArray jArray = j.getJSONArray("items");

        String result = "";

        for (int i = 0;i < jArray.length();i++) {
            result = result + jArray.optJSONObject(i).getString("text");
        }
        System.out.print( result);*/

        HttpClient client = new DefaultHttpClient();

        HttpGet request = new HttpGet("http://api.intellexer.com/summarize?apikey=bbbfb162-ee2e-4c5b-8945-45ded0377a98&url=http%3A%2F%2Fwww.nytimes.com/pages/technology/index.html?action=click&pgtype=Homepage&region=TopBar&module=HPMiniNav&contentCollection=Tech&WT.nav=page/&summaryRestriction=7&returnedTopicsCount=2&loadConceptsTree=true&loadNamedEntityTree=true&usePercentRestriction=true&conceptsRestriction=7&structure=general&fullTextTrees=true&textStreamLength=1000&useCache=false&wrapConcepts=true");
        HttpResponse response = client.execute(request);
        BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
        String line = " ";
        StringBuilder s = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
            s.append(line);
        }
        String output = s.toString();
        JSONObject j = new JSONObject(output);
        JSONArray jArray = j.getJSONArray("items");

        String result = "";

        for (int i = 0;i < jArray.length();i++) {
            result = result + jArray.optJSONObject(i).getString("text") + "\n";
        }
        System.out.print( result);
    }
}
