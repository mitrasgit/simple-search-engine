/**
 * 
 */
package searchengine;

import java.util.ArrayList;
import java.util.List;

/**
 * A query object
 */
public class Query {
	
	private List<String> query;
	private QueryType queryType;
	
	public Query(QueryType queryType, List<Token> tokens) {
		this.queryType = queryType;	
		switch (queryType) {
		case SELECT:
			this.query = buildSelectQuery(tokens);
			break;
		case ADD:
			this.query = buildAddQuery(tokens);
			break;
		case GET:
			this.query = buildGetQuery(tokens);
			break;
		case EXIT:
			this.query = new ArrayList<String>();
			break;
		default:
			throw new IllegalArgumentException("QueryType <" + queryType + "> is not supported.");
		}
	}

	private List<String> buildSelectQuery(List<Token> tokens) {
		List<String> query = new ArrayList<String>();
		TokenType type;
		for (Token t : tokens) {
			type = t.getType();
			if (TokenType.FILENAME.equals(type)) {
				query.add(t.getValue());
			} else {
				throw new IllegalArgumentException("Select queries can't contain token " + t.toString());
			}
		}
		return query;
	}
	
	private List<String> buildAddQuery(List<Token> tokens) {
		List<String> query = new ArrayList<String>();
		Token t;
		if (tokens.size() < 2) {
			throw new IllegalArgumentException("Can't create ADD query from tokens " + tokens.toString());
		} else {
			// first token is a filename
			t = tokens.get(0);
			if (TokenType.FILENAME.equals(t.getType())) {
				query.add(t.getValue());
			} else {
				throw new IllegalArgumentException("ADD query can't start with token " + t.toString());
			}
			// words
			for (int i=1; i<tokens.size(); ++i) {
				t = tokens.get(i);
				switch (t.getType()) {
				case WORD:
					query.add(t.getValue());
					break;
				case PERIOD:
					break;
				default:
					throw new IllegalArgumentException("ADD query can't add text with token " + t.toString());
				}
			}
		}
		return query;
	}
	
	private List<String> buildGetQuery(List<Token> tokens) {
		List<String> query = new ArrayList<String>();
		TokenType type;
		for (Token t : tokens) {
			type = t.getType();
			switch (type) {
			case WORD:
				query.add(t.getValue());
				break;
			case PERIOD:
				break;
			default:
				throw new IllegalArgumentException("GET query can't contain token " + t.toString());
			}
		}
		return query;
	}
	
	public QueryType getType() {
		return queryType;
	}
	
	/**
	 * Get the size of the query (i.e. number of tokens)
	 */
	public int size() {
		return query.size();
	}
	
	/**
	 * Get the token at the given index
	 */
	public String get(int index) {
		return query.get(index);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{" + queryType.toString());
		sb.append(", [");
		for (int i=0; i < query.size(); ++i) {
			sb.append(query.get(i));
			if (i < query.size()-1)
				sb.append(",");
		}
		sb.append("]}");
		return sb.toString();
	}

}
