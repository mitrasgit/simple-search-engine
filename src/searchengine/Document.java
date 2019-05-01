/**
 * 
 */
package searchengine;


/**
 * Object that represents a term-document pair. 
 * It is associated with the term frequency in the document (tf).
 */
public class Document implements Comparable<Document>{
	
	private String docId;
	private Double score;
	
	/**
	 * 
	 * @param docId
	 * @param score
	 */
	public Document(String docId, Double score) {
		this.docId = docId;
		this.score = score;
	}
	
	public String getDocId() {
		return docId;
	}
	
	public Double getScore() {
		return score;
	}
	
	public void setScore(Double score) {
		this.score = score;
	}
	
	/**
	 * Check if two DocTerms are equal
	 * @param other the other DocTerm to be compared
	 * @return True if both DocTerms have equal document ID and term pair, 
	 * False if either of the values are different
	 */
	@Override
	public boolean equals(Object obj) {
		boolean eq = false;
		if (this == obj) {
			eq = true;
		}
		else if (obj instanceof Document) {
			Document dt = (Document) obj;
			eq = docId.equals(dt.getDocId());
		}
		return eq;
	}
	
	/**
	 * Compare two DocTerm's scores
	 * @param other the other DocTerm to be compared
	 * @return 0 if the scores are equal, < 0 if this DocTerm has a lower score than the argument,
	 * and > 0 if this DocTerm has a higher score than the argument
	 */
	@Override
	public int compareTo(Document other) {
		return this.score.compareTo(other.getScore());
	}
	
	@Override
	public String toString() {
		return "DocTerm{docId=" + docId + ", score=" + score + "}";
	}

}
