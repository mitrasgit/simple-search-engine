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
	
	public Query(QueryType queryType, List<Token> tokens) throws SyntaxException {
		this.queryType = queryType;	
		switch (queryType) {
		case SELECT:
			this.query = buildSelectQuery(tokens);
			break;
		case GET:
			this.query = buildGetQuery(tokens);
			break;
		default:
			throw new SyntaxException("QueryType <" + queryType + "> is not supported.");
		}
	}

	private List<String> buildSelectQuery(List<Token> tokens) throws SyntaxException {
		List<String> query = new ArrayList<String>();
		TokenType type;
		for (Token t : tokens) {
			type = t.getType();
			if (TokenType.FILENAME.equals(type)) {
				query.add(t.getValue());
			} else {
				throw new SyntaxException("Select queries can't contain token " + t.toString());
			}
		}
		return query;
	}
	
	private List<String> buildGetQuery(List<Token> tokens) throws SyntaxException {
		List<String> query = new ArrayList<String>();
		TokenType type;
		for (Token t : tokens) {
			type = t.getType();
			switch (type) {
			case WORD:
				query.add(t.getValue());
				break;
			case FILENAME:
				query.add(t.getValue());
				break;
			default:
				throw new SyntaxException("GET query can't contain token " + t.toString());
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
