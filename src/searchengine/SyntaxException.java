/**
 * 
 */
package searchengine;

/**
 * Signals that an unexpected token was found
 */
public class SyntaxException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public SyntaxException(String message) {
		super(message);
	}

}
