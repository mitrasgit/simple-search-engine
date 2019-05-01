/**
 * 
 */
package searchengine;

/**
 * A DocumentList keeps track of a list of documents associated with 
 * a term in the inverted index. (DocTerms)
 *
 */
public interface DocumentList {
	
	/**
	 * Get the term associated with this list of documents
	 */
	public String getTerm();
	
	/**
	 * Get the DocTerm stored at an index
	 */
	public Document get(int index);
	
	/**
	 * Get the number of documents in this list
	 */
	public int size();
	
	/**
	 * Sort the DocumentList in descending order based on the DocTerm scores
	 */
	public void sort();
	
	/**
	 * Get a String representation of the object
	 */
	public String toString();

}
