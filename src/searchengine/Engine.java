/**
 * 
 */
package searchengine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Handles the logic between UI, database and inverted index.
 *
 */
public class Engine {
	
	private InvertedIndex invertedIndex = new InvertedIndex();
	private List<Token> filenames = new ArrayList<Token>();
	
	public Engine() {
		String db = findDataBase();
		this.loadIndex(db);
	}
	
	/**
	 * Load all documents in the database to this object's inverted index
	 * @param pathDB The absolute path of the data base folder in the local system
	 */
	private void loadIndex(String pathDB) {
		System.out.println("database absolute path: " + pathDB);
		
		// Loop over all txt files in db
		File db = new File(pathDB);
		File[] fileList = db.listFiles();
		if (fileList != null) {
			String docID;
			for (File file : fileList) {
				docID = extractDocName(file.getAbsolutePath());
				System.out.println(String.format("path: %s, id: %s", file.getAbsolutePath(), docID));
				loadFileToIndex(file, docID);
			}
		}
		System.out.println("index = " + invertedIndex.toString());
	}
	
	/**
	 * Extract the document name from the file path
	 * @param absolutePath For example "c:/path/to/file.txt"
	 * @return The file name "file.txt"
	 */
	private static String extractDocName(String path) {
		String[] s = path.split(String.format("\\%s", File.separator));
		System.out.println(s[s.length-1]);
		return s[s.length-1];
	}
	
	/**
	 * Add a document to the data base and the index.
	 * @param docID Document identifier and file name (including .txt)
	 * @param text The content of the document
	 */
	public void addDocument(String docID, String text) throws IllegalArgumentException {
		// Check if file already exists in the data base
		String docName = docID;
		String docPath = findDataBase() + File.separator + docName;
		if (new File(docPath).exists())
			throw new IllegalArgumentException(String.format("File %s already exists.", docID));
		// Store as a file in db
		try {
			writeFile(docPath, text);
		} catch (IOException e) {
			System.err.println(String.format("Failed to write the file with id <%d>, "
					+ "path <%s>, and content <%s>", docID, docPath, text));
			e.printStackTrace();
		}
		// Load to index
		File file = new File(docPath);
		loadFileToIndex(file, docID);
	}
	
	private static void writeFile(String absolutePath, String text) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(absolutePath));
		writer.write(text);
		writer.close();
	}

	/**
	 * Load a file to the inverted index
	 * @param file that is exists in the database
	 * @param docId the file's identifier
	 */
	private void loadFileToIndex(File file, String docId) {
		// Read the file on disk
		Scanner sc = null;
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println(String.format("File not found: %s", file.getAbsolutePath()));
			e.printStackTrace();
		}
		StringBuilder contents = new StringBuilder();
		while (sc != null && sc.hasNext()) {
			String word = sc.next();
			contents.append(word + " ");
		}
		// Tokenize the document
		Lexer lexer = new Lexer();
		List<Token> tokens = lexer.tokenizeDocument(contents.toString());
		// Create an ADD query
		Query query = new Query(QueryType.ADD, tokens);
		System.out.println("Add doc query = " + query.toString());
		// Add each term to index
		for (int i=0; i<query.size(); ++i) {
			Document docTerm = new Document(docId, 1.0);
			invertedIndex.insert(query.get(i), docTerm);
		}
	}

	/**
	 * Find the absolute path of the database
	 */
	private static String findDataBase() {
		String pathDB = new File("").getAbsolutePath() + File.separator + "db";
		return new File(pathDB).getAbsolutePath();
	}
	
	/**
	 * Search for documents that match a query
	 * @param query of type SEARCH
	 * @return The documents sorted by tf-idf
	 */
	public DocumentList search(Query query) throws IllegalArgumentException {
		if (!QueryType.GET.equals(query.getType()))
			throw new IllegalArgumentException("Wrong query format. Please use a GET query.");
		
		DocumentList documents;
		if (query.size() < 1) {
			// TODO dont create empty queries!
			documents = new TfDocumentList("");
			System.out.println("Empty: " + documents.toString());
		}
		else if (query.size() == 1) {
			// one word query
			String term = query.get(0);
			documents = invertedIndex.getDocuments(term);
			TfidfDocumentList sortedDocuments = sortResults(term, documents);
			System.out.println("One word: " + sortedDocuments.toString());
		} else {
			throw new IllegalArgumentException("Only one-word queries are supported.");
		}
		return documents;
	}
	
	private TfidfDocumentList sortResults(String term, DocumentList results) {
		Double idf = invertedIndex.calcIdf(term);
		System.out.println(String.format("n=%d, nTerm=%d, idf=%f", 
				invertedIndex.numberOfDocuments(), results.size(), idf));
		TfidfDocumentList sortedResults = new TfidfDocumentList(results, idf);
		sortedResults.sort();
		return sortedResults;
	}
	
}
