/**
 * 
 */
package searchengine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;
import java.util.Scanner;

/**
 * Handles the logic between UI, database and inverted index.
 *
 */
public class SearchEngine {
	
	private InvertedIndex invertedIndex = new InvertedIndex();
	
	public SearchEngine() {
		String db = findDataBase();
		this.loadIndexAllFiles(db);
	}
	
	public SearchEngine(Query selectQuery) {
		String db = findDataBase();
		this.loadIndex(db, selectQuery);
	}
	
	/**
	 * Re-initialize the index so the search engine only considers 
	 * the files in the SELECT {@link Query}. The search engine will load all
	 * files if the query is empty.
	 * @param selectQuery a query that states the files to use
	 */
	public void reloadIndex(Query selectQuery) {
		String db = findDataBase();
		invertedIndex = new InvertedIndex();
		loadIndex(db, selectQuery);
		System.out.println("Reloaded the index: " + invertedIndex.toString());
	}
	
	/**
	 * Load the {@link SearchEngine}'s index according to a query with filename-tokens.
	 * Loads all data if the query is empty.
	 * @param dbPath the path to the data base
	 * @param selectQuery a query that selects the files to be loaded
	 */
	private void loadIndex(String dbPath, Query selectQuery) {
		if (!QueryType.SELECT.equals(selectQuery.getType())) {
			System.err.println(selectQuery.getType() + " IS NOT A SELECT QUERY!");
			return;
		}
		if (selectQuery.size() < 1) {
			// if query is empty --> load all files in db
			loadIndexAllFiles(dbPath);
		} else {
			// else --> loop over the files in the query and TRY to read/add
			String docId;
			for (int i=0; i<selectQuery.size(); ++i) {
				docId = selectQuery.get(i);
				if (!invertedIndex.hasDocId(docId)) {
					File file = new File(dbPath + File.separator + docId);
					try {
						loadFile(file, docId);
					} catch (FileNotFoundException e) {
						System.err.println("File " + file.getName() + " doesn't exist, skipped to load it to index.");
					}
				}
			}
		}
	}

	/**
	 * Load all documents in the database
	 * @param dbPath The absolute path of the local data base folder
	 */
	private void loadIndexAllFiles(String dbPath) {
		File db = new File(dbPath);
		File[] fileList = db.listFiles();
		if (fileList != null) {
			String docID;
			for (File file : fileList) {
				docID = file.getName();
				try {
					loadFile(file, docID);
				} catch (FileNotFoundException e) {
					System.err.println("File " + file.getName() + " doesn't exist, skipped to load it to index.");
				}
			}
		}
		System.out.println("index = " + invertedIndex.toString());
	}
	
	/**
	 * Load a file to the inverted index
	 * @param file that is exists in the database
	 * @param docId the file's identifier
	 * @throws FileNotFoundException if the file does not exist
	 */
	private void loadFile(File file, String docId) throws FileNotFoundException {
		List<Token> tokens = readFile(file);
		// Add each term to index
		for (int i=1; i<tokens.size(); ++i) {
			Document docTerm = new Document(docId, 1.0);
			invertedIndex.insert(tokens.get(i).getValue(), docTerm);
		}
	}
	
	/**
	 * Add a document to the data base and the index.
	 * @param docID Document identifier and file name (including .txt)
	 * @param text The content of the document
	 * @throws FileAlreadyExistsException 
	 */
	public void addDocument(String docID, String text) throws FileAlreadyExistsException {
		// Check if file already exists in the data base
		String docName = docID;
		String docPath = findDataBase() + File.separator + docName;
		if (new File(docPath).exists())
			throw new FileAlreadyExistsException(String.format("File %s already exists.", docID));
		// Store as a file in db
		try {
			writeFile(docPath, text);
		} catch (IOException e) {
			System.err.println(String.format("Failed to write the file with id <%d>, "
					+ "path <%s>", docID, docPath));
			e.printStackTrace();
		}
		// Load to index
		File file = new File(docPath);
		try {
			loadFile(file, docID);
		} catch (FileNotFoundException e) {
			System.err.println("Failed to read " + file.getAbsolutePath() + ", skipped loading to index.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Search for documents that match a query
	 * @param getQuery {@link Query} of type GET
	 * @return The documents sorted by tf-idf
	 */
	public DocumentList search(Query getQuery) throws IllegalArgumentException {
		if (!QueryType.GET.equals(getQuery.getType()))
			throw new IllegalArgumentException("Wrong query format. Please use a GET query.");
		
		DocumentList documents = new TfDocumentList("");
		if (getQuery.size() < 1) {
			System.out.println("Empty: " + documents.toString());
		}
		else if (getQuery.size() == 1) {
			// one word query
			String term = getQuery.get(0);
			documents = invertedIndex.getDocumentsTfidf(term);
			documents.sort();
		} else {
			System.out.println("Only one-word queries are supported.");
		}
		return documents;
	}
	
	////////////// CREATE A DATABASE OBJECT THAT HANDLES READ/WRITE //////////////
	
	/**
	 * Find the absolute path of the database
	 */
	private static String findDataBase() {
		String pathDB = new File("").getAbsolutePath() + File.separator + "db";
		return new File(pathDB).getAbsolutePath();
	}
	
	private static void writeFile(String absolutePath, String text) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(absolutePath));
		writer.write(text);
		writer.close();
	}
	
	/**
	 * Read a file from the local data base
	 * @param file the file to read
	 * @return the contents of the file as a list of {@link Token} 
	 * @throws FileNotFoundException 
	 */
	private List<Token> readFile(File file) throws FileNotFoundException {
		// Read the file on disk
		Scanner sc = new Scanner(file);
		StringBuilder contents = new StringBuilder();
		while (sc != null && sc.hasNext()) {
			String word = sc.next();
			contents.append(word + " ");
		}
		sc.close();
		// Tokenize the document
		Lexer lexer = new Lexer();
		List<Token> tokens = lexer.tokenizeDocument(contents.toString());
		return tokens;
	}
	
}
