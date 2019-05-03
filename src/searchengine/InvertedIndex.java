package searchengine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Inverted index
 * Data structure for searching the documents in the data base
 *
 */
public class InvertedIndex {
	
	// index that maps words to documents, format: <term>: <document id>
	private Map<String, TfDocumentList> index = new HashMap<String, TfDocumentList>();
	private Set<String> docIds = new HashSet<String>();
	
	/**
	 * Add a (word, document) pair to the index
	 * @param term A word in the document
	 * @param docID The document identifier, e.g. document name
	 */
	public void insert(String term, Document docTerm) {
		// Add to index
		if (index.containsKey(term)) {
			TfDocumentList docs = index.get(term);
			docs.add(docTerm);
			index.put(term, docs);
		} else {
			TfDocumentList docs = new TfDocumentList(term);
			docs.add(docTerm);
			index.put(term, docs);
		}
		// Keep track of the number of documents
		docIds.add(docTerm.getDocId());
	}
	
	/**
	 * Get all documents that contain the term
	 * @param term the word to search for
	 * @return A list of documents with their tf scores
	 */
	public TfDocumentList getDocumentsTf(String term) {
		TfDocumentList documents = index.get(term);
		if (documents == null) {
			documents = new TfDocumentList(term);
		}
		return documents;
	}
	
	/**
	 * Get a DocumentList with the documents that contain a term (with tf scores)
	 * @param term the word to search for
	 * @return A list of documents with their tf-idf scores
	 */
	public TfidfDocumentList getDocumentsTfidf(String term) {
		TfDocumentList documentsTf = index.get(term);
		if (documentsTf == null) {
			documentsTf = new TfDocumentList(term);
		}
		Double idf = calcIdf(term);
		return new TfidfDocumentList(documentsTf, idf);
	}
	
	public Integer numberOfDocuments() {
		return docIds.size();
	}
	
	public boolean hasDocId(String docId) {
		return docIds.contains(docId);
	}
	
	/**
	 * Calculates the idf for the specified term
	 * @param term the word/token for which to evaluate the idf
	 * @return the idf score which equals log(#documents in index / #documents containing term)
	 */
	public Double calcIdf(String term) {
		// idf = log(N/nTerm), 
		// avoid division by zero by setting nTerm = 1!
		int nTerm = 1;
		if (index.containsKey(term)) {
			nTerm = index.get(term).size();
		}
		Double idf = Math.log10((double) numberOfDocuments() / nTerm);
		System.out.println(String.format("calc idf: term = %s, N = %d, n_t = %d --> idf = %f", 
				term, numberOfDocuments(), nTerm, idf));
		return idf;
	}
	
	@Override
	public String toString() {
		return index.toString();
	}

}
