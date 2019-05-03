/**
 * 
 */
package searchengine;

import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Interface with the user.
 * Reads input from the user and chooses what to do.
 *
 */
public class Main {

	private static final String MSG_COMMANDS = "Type EXIT to terminate,\n"
			+ "ADD <filename.txt> <file content> to add a document,\n"
			+ "GET <query> to search for a word,\n"
			+ "SELECT <filename.txt> <filename.txt> ... to select a subset of the files.";
	private static final String MSG_WELCOME = "\nWelcome to Simple Search Engine!\n" + MSG_COMMANDS;
	
	/**
	 * Make the search engine store a new document in the data base and then load it to index
	 * @param engine the search engine
	 * @param input the line of input from the user
	 */
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
	
	/**
	 * Makes a lexer read the input to a list of tokens
	 * @param lexer converts a string to a list of tokens
	 * @param input from the user
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
						DocumentList results = searchEngine.search(query);
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
