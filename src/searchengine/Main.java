/**
 * 
 */
package searchengine;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Interface with the user
 * DONE sort results by tf-idf
 * DONE use Document class
 * DONE Use a lexer/tokenizer to create Token/Term objects
 * DONE Add a SELECT option to define the set of documents to consider, expand the lexer, 
 * add QueryType.SELECT, reload the index
 * DONE Handle all of the exceptions
 * TODO Limit the amount of exceptions, use more default behaviours etc
 * TODO Create a parser: lexer controls the tokens, parser controls the token flow by creating a tree, 
 * TODO Review the code structure (Query class (DONE), call new Query (structure), DataBase (create))
 * TODO Run tests with pre-calculated tf-idf scores
 *
 */
public class Main {

	private static final String MSG_COMMANDS = "\nType EXIT to terminate,\n"
			+ "ADD <filename.txt> <file content> to add a document,\n"
			+ "GET <query> to search for a word,\n"
			+ "SELECT <filename.txt> <filename.txt> ... to select a subset of the files.";
	private static final String MSG_WELCOME = "Welcome to Simple Search Engine!\n" + MSG_COMMANDS;
	
	private static void addDocument(SearchEngine engine, String input) {
		String[] inputList = input.split(" ", 3);
		if (inputList.length < 3 || !inputList[1].endsWith(".txt")) {
			System.out.println("Failed to add the file. Please use the command format: "
					+ "ADD <filename>.txt <text>");
		} else {
			String fileName = inputList[1];
			try {
				engine.addDocument(fileName, inputList[2]);
				System.out.println(String.format("Added %s.", fileName));
			} catch (FileAlreadyExistsException e) {
				String message = e.getMessage();
				System.out.println(String.format("%s Please use another file name.", message));
			}
		}
	}
	
	private static DocumentList search(SearchEngine engine, Query query) {
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
	private static List<Token> readInput(Lexer lexer, String input) {
		List<Token> tokens = new ArrayList<Token>();
		try {
		 tokens = lexer.tokenizeQuery(input);
		} catch (SyntaxException e) {
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
		TokenType action = null;
		SearchEngine searchEngine = new SearchEngine();
		Lexer lexer = new Lexer();
		Scanner scanner = new Scanner(System.in);
		
		System.out.println(MSG_WELCOME);
		// Read input queries from the user
		while (!TokenType.EXIT.equals(action)) {
			String input = scanner.nextLine();
			
			// Tokenize the input
			List<Token> tokens = readInput(lexer, input);
			System.out.println("query tokens = " + tokens.toString());
			
			// Choose action
			if (tokens.isEmpty()) {
				action = null;
			} else {
				action = tokens.get(0).getType();
			}
			
			if (TokenType.ADD.equals(action)) {
				System.out.println("-> Add a doc");
				if (input.length() > 0) {
					addDocument(searchEngine, input);
				}
			}
			else if (TokenType.GET.equals(action)) {
				System.out.println("-> Search");
				if (tokens.size() > 1) {
					try {
						Query query = new Query(QueryType.GET, tokens.subList(1, tokens.size()));
						System.out.println("Query = " + query.toString());
						DocumentList results = search(searchEngine, query);
						if (results!=null)
							printResults(results);
					} catch (SyntaxException e) {
						System.out.println(e.getMessage());
						System.out.println(MSG_COMMANDS);
					}
				}
			}
			else if (TokenType.SELECT.equals(action)) {
				System.out.println("-> Select");
				if (tokens.size() > 1) {
					try {
						Query query = new Query(QueryType.SELECT, tokens.subList(1, tokens.size()));
						System.out.println("Query = " + query.toString());
						searchEngine.reloadIndex(query);
					} catch (SyntaxException e) {
						System.out.println(e.getMessage());
						System.out.println(MSG_COMMANDS);
					}
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
