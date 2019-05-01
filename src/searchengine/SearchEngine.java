/**
 * 
 */
package searchengine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Interface with the user
 * DONE handle weirdly formatted files
 * DONE sort results by tf-idf
 * DONE use Document class
 * DONE Use a lexer/tokenizer to create Token/Term objects
 * TODO Add a SELECT option to define the set of documents to consider, expand the lexer, 
 * add QueryType.SELECT/create SelectQuery obj, reload the index
 * TODO Make the code consequent: variable names, store tokens in the index etc
 * TODO separate the classes into two packages? e.g. searchengine and (user input, tokenizer, tokens etc)
 * TODO Run tests with pre-calculated tf-idf scores
 *
 */
public class SearchEngine {
	
	private Engine engine = new Engine();
	private static final String MSG_COMMANDS = "Type EXIT to terminate,\n"
			+ "ADD <filename.txt> <file content> to add a document,\n"
			+ "GET <query> to search for a word.";
	private static final String MSG_WELCOME = "Welcome to Simple Search Engine!\n" + MSG_COMMANDS;
	
	public SearchEngine() {
		System.out.println(MSG_WELCOME);
	}
	
	private void addDocument(String input) {
		String[] inputList = input.split(" ", 3);
		if (inputList.length < 3 || !inputList[1].endsWith(".txt")) {
			System.out.println("Failed to add the file. Please use the command format: "
					+ "ADD <filename>.txt <text>");
		} else {
			String fileName = inputList[1];
			try {
				engine.addDocument(fileName, inputList[2]);
			} catch (IllegalArgumentException e) {
				String message = e.getMessage();
				System.out.println(String.format("%s Please use another file name.", message));
			}
		}
	}
	
	private DocumentList search(Query query) {
		DocumentList results = null;
		try {
			results = engine.search(query);
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
		return results;
	}
	
	/**
	 * 
	 * @param lexer
	 * @param input
	 * @return
	 */
	private List<Token> tokenizeInput(Lexer lexer, String input) {
		List<Token> tokens = new ArrayList<Token>();
		try {
		 tokens = lexer.tokenizeQuery(input);
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			System.out.println(MSG_COMMANDS);
		}
		return tokens;
	}

	/**
	 * @param args
	 * @throws IOException If the Search Engine fails to create an inverted index
	 */
	public static void main(String[] args) {
		SearchEngine se = new SearchEngine();
		TokenType action = null;
		Scanner scanner = new Scanner(System.in);
		Lexer lexer = new Lexer();
		
		// Read input queries from the user
		while (!TokenType.EXIT.equals(action)) {
			String input = scanner.nextLine();
			
			// Tokenize the input
			List<Token> tokens = se.tokenizeInput(lexer, input);
			System.out.println("query tokens = " + tokens.toString());
			
			// Choose action
			if (tokens.isEmpty()) {
				action = null;
			} else {
				action = tokens.get(0).getType();
			}
			
			if (TokenType.ADD.equals(action)) {
				System.out.println("- Add a doc");
				se.addDocument(input);
			}
			else if (TokenType.GET.equals(action)) {
				System.out.println("- Search");
				if (tokens.size() > 0) {
					Query query = new Query(QueryType.GET, tokens.subList(1, tokens.size()));
					System.out.println("Query = " + query.toString());
					DocumentList results = se.search(query);
					if (results!=null)
						printResults(results);
				}
			}
		}
		
		scanner.close();
		System.out.println("Closed the Search Engine.");
	}
	
	public static void printResults(DocumentList results) {
		System.out.println("Search results:");
		System.out.println(results.toString());
	}

}
