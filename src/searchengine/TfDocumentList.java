/**
 * 
 */
package searchengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * List of documents that contains a word.
 * Keeps track of the term frequency (tf = the number of times that term t occurs in document d)
 *
 */
public class TfDocumentList implements DocumentList {
	
	private final String term;
	// maps documents to their term frequency <docId>: <tf>
	private List<Document> documentTermPairs = new ArrayList<Document>();
	
	public TfDocumentList(String term) {
		this.term = term;
	}
	
	public TfDocumentList(TfDocumentList other) {
		this.term = other.getTerm();
		this.documentTermPairs = other.documentTermPairs;
	}
	
	/**
	 * Add a document to the list and update the term frequencies.
	 * @param docTerm the document-term pair to add
	 */
	public void add(Document docTerm) {
		int index = documentTermPairs.indexOf(docTerm);
		if (index == -1) {
			// Add a <document, term> pair to the list
			documentTermPairs.add(docTerm);
		} else {
			// Update the term frequency
			Double oldTf = documentTermPairs.get(index).getScore();
			docTerm.setScore(oldTf + docTerm.getScore());
			documentTermPairs.set(index, docTerm);
		}
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
	 * Sort the DocumentList in descending order based on the term frequency (tf)
	 */
	@Override
	public void sort() {
		Collections.sort(documentTermPairs, Collections.reverseOrder());
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i=0; i < documentTermPairs.size(); ++i) {
			if (i > 0)
				sb.append(", ");
			sb.append(documentTermPairs.get(i).toString());
		}
		sb.append("]");
		return sb.toString();
	}
	
}
