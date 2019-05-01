/**
 * 
 */
package searchengine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A query object
 */
public class Query {
	
	private List<String> query = new ArrayList<String>();
	private QueryType queryType;
	
	
	public Query(QueryType queryType, List<Token> tokens) {
		this.queryType = queryType;
		parseTokens(tokens);
	}
	
	/**
	 * Create a Query
	 * @param queryType The type of query
	 * @param query An array with the contents of the query (TODO turn this into a token array!)
	 */
	public Query(QueryType queryType, String[] query) {
		List<String> tokens = new ArrayList<String>(Arrays.asList(query));
		this.query = tokens;
		this.queryType = queryType;
	}
	
	/**
	 * Parse a list of tokens and store it as the query list
	 * (declutters the list of tokens from weird symbols etc)
	 * @param tokens the list of tokens
	 */
	private void parseTokens(List<Token> tokens) {
		for (int i=0; i<tokens.size(); ++i) {
			Token token = tokens.get(i);
			TokenType type = token.getType();
			if (type.equals(TokenType.WORD) || type.equals(TokenType.FILENAME)) {
				query.add(tokens.get(i).getValue());
			}
		}
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
