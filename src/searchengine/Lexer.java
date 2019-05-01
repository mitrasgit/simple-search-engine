/**
 * 
 */
package searchengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Converts a sequence of characters to tokens
 * A token is a sub-sequence of characters with a special meaning, 
 * for example a word, email adress, or punctuation.
 * 
 * The "query language" supports 
 * 1. Addition of a file:
 * 	ADD filename.txt a sequence of text for the file content
 * 2. One-word query
 * 	GET query
 * 3. Terminate the search engine
 * 	EXIT
 * 4. TODO select a subset of the documents
 * 	SELECT * or SELECT doc1.txt doc2.txt
 *
 */
public class Lexer {
	
	// Build the list of tokens in a field variable, for "complex" queries with 
	// punctuation etc we want to find the tokens recursively!
	List<Token> tokens;
	HashMap<TokenType, String> regexMap = new HashMap<TokenType, String>();
	
	public Lexer() {
		regexMap.put(TokenType.FILENAME, "\\w+\\.txt");
		regexMap.put(TokenType.WORD, "\\w+([\\-\\_]\\w+)*");
		regexMap.put(TokenType.PERIOD, "\\.");
		
		/*for (String regex : regexes) {
			patterns.add(Pattern.compile(regex));
		}*/
	}
	
	public List<Token> tokenizeDocument(String sequence) {
		System.out.println("sequence=" + sequence);
		tokens = new ArrayList<Token>();
		sequence = sequence.toLowerCase();
		String[] sequenceList = sequence.split(" ");
		for (int i=0; i<sequenceList.length; ++i) {
			addToken(sequenceList[i]);
		}
		System.out.println("tokens=" + tokens.toString());
		return tokens;
	}

	/**
	 * Create a list of tokens, gives an empty list if the sequence is empty
	 * @param sequence the sequence to tokenize
	 * @return a list of tokens
	 * @throws IllegalArgumentException if the format of the sequence is incorrect
	 */
	public List<Token> tokenizeQuery(String sequence) throws IllegalArgumentException {
		tokens = new ArrayList<Token>();
		sequence = sequence.toLowerCase();
		String[] sequenceList = sequence.split(" ");
		if (sequenceList.length > 0) {
			// Check the first token
			addFirstToken(sequenceList[0]);
			// Loop over each sub-sequence
			for (int i=1; i<sequenceList.length; ++i) {
				addToken(sequenceList[i]);
			}
		}
		return tokens;
	}
	
	/**
	 * Add the first token in a command to the list of tokens. 
	 * Legal sub-sequences are "add", "exit" or "get", other inputs generate IllegalArgumentException
	 * @param subSequence the word to match
	 * @return the token than matches the subSequence
	 * @throws IllegalArgumentException
	 */
	private void addFirstToken(String subSequence) throws IllegalArgumentException {
		Token token = null;
		System.out.println("subseq = " + subSequence);
		if (subSequence.equals("add")) {
			token = new Token(TokenType.ADD, subSequence);
		}
		else if (subSequence.equals("get")) {
			token = new Token(TokenType.GET, subSequence);
		}
		else if (subSequence.equals("exit")) {
			token = new Token(TokenType.EXIT, subSequence);
		} 
		else if (subSequence.equals("select")) { 
			token = new Token(TokenType.SELECT, subSequence);
		} else {
			throw new IllegalArgumentException("Please start a command with ADD, GET, SELECT or EXIT.");
		}
		tokens.add(token);
	}
	
	private void addToken(String subSequence) {
		// Use regex to select a token
		Token token;
		if (subSequence.matches(regexMap.get(TokenType.FILENAME))) {
			token = new Token(TokenType.FILENAME, subSequence);
		}
		else if (subSequence.matches(regexMap.get(TokenType.WORD))) {
			token = new Token(TokenType.WORD, subSequence);
		} else if (subSequence.matches(regexMap.get(TokenType.PERIOD))) {
			token = new Token(TokenType.PERIOD, subSequence);
		} else {
			throw new IllegalArgumentException("[" + subSequence + "] is not supported by any token.");
		}
		tokens.add(token);
	}
	
}
