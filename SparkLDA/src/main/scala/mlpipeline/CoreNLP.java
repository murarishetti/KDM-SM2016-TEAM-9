package mlpipeline;

import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;

import java.util.List;

/**
 * Created by vamshirajarikam on 7/8/16.
 */
public class CoreNLP {
    public static String returnLemma(String sentence) {

        Document doc = new Document(sentence);
        String lemma="";
        for (Sentence sent : doc.sentences()) {

            List<String> l=sent.lemmas();
            for (int i = 0; i < l.size() ; i++) {
                lemma += l.get(i) + " ";
            }
        }

        return lemma;
    }

}
