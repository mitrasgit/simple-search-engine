/**
 * 
 */
package searchengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A DocumentList where the document-term pairs have tfidf scores
 *
 */
public class TfidfDocumentList implements DocumentList {
	
	private final String term;
	// maps documents to their term frequency <docId>: <tf-idf>
	private List<Document> documentTermPairs = new ArrayList<Document>();
	
	public TfidfDocumentList(DocumentList docList, Double idf) {
		this.term = docList.getTerm();
		Document dt;
		for (int i=0; i<docList.size(); ++i) {
			dt = docList.get(i);
			documentTermPairs.add(new Document(dt.getDocId(), tfidf(dt.getScore(), idf)));
		}
	}
	
	private static Double tfidf(Double tf, Double idf) {
		return tf*idf;
	}
	
	@Override
	public String getTerm() {
		return term;
	}
	
	@Override
	public Document get(int index) {
		return documentTermPairs.get(index);
	}
	
	@Override
	public int size() {
		return documentTermPairs.size();
	}
	
	/**
	 * Sort the DocumentList in descending order based on tf-idf
	 */
	@Override
	public void sort() {
		Collections.sort(documentTermPairs, Collections.reverseOrder());
	}
	
	@Override
	public String toString() {
		return "TfidfDocumentList{ " + documentTermPairs.toString() + " }";
	}

}
